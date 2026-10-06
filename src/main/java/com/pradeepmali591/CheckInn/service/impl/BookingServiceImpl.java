package com.pradeepmali591.CheckInn.service.impl;

import com.pradeepmali591.CheckInn.dto.booking.request.BookingRequest;
import com.pradeepmali591.CheckInn.dto.booking.request.GuestRequest;
import com.pradeepmali591.CheckInn.dto.booking.response.BookingResponse;
import com.pradeepmali591.CheckInn.entity.*;
import com.pradeepmali591.CheckInn.entity.enums.BookingStatus;
import com.pradeepmali591.CheckInn.exception.ResourceNotFoundException;
import com.pradeepmali591.CheckInn.exception.UnAuthorisedException;
import com.pradeepmali591.CheckInn.repository.*;
import com.pradeepmali591.CheckInn.service.BookingService;
import com.pradeepmali591.CheckInn.service.CheckoutService;
import com.pradeepmali591.CheckInn.strategy.PriceService;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.Refund;
import com.stripe.model.checkout.Session;
import com.stripe.param.RefundCreateParams;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.pradeepmali591.CheckInn.util.AppUtils.getCurrentUser;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final InventoryRepository inventoryRepository;

  private final RoomRepository roomRepository;
    private final HotelRepository hotelRepository;
    private final BookingRepository bookingRepository;
    private final ModelMapper modelMapper;
    private final GuestRepository guestRepository;
    private final CheckoutService checkoutService;
    private final PriceService priceService;

    @Value("${frontend.url}")
    private String frontendUrl;

    @Override
    @Transactional
    public BookingResponse initialiseBooking(BookingRequest request) {

        log.info("Initializing booking for hotel: {}, room: {}, date: {} - {}",
                request.getHotelId(), request.getRoomId(), request.getCheckInDate(),
                request.getCheckOutDate());

        log.info("Hotel count from Spring: {}", hotelRepository.count());
        log.info("Hotel 7 exists from Spring: {}", hotelRepository.existsById(7L));

        Hotel hotel = hotelRepository
                .findById(request.getHotelId())
                .orElseThrow(() -> new ResourceNotFoundException
                        ("Hotel not found with ID: "+ request.getHotelId()));

        Room room = roomRepository
                .findById(request.getRoomId())
                .orElseThrow(() -> new ResourceNotFoundException
                        ("Room not found with ID: "+request.getRoomId()));

        List<Inventory> inventoryList = inventoryRepository.findAndLockAvailableInventory(
                request.getRoomId(), request.getCheckInDate(), request.getCheckOutDate(),
                request.getRoomCount());

        long daysCount = ChronoUnit.DAYS.between(request.getCheckInDate(),
                request.getCheckOutDate())+1;

        if(inventoryList.size() != daysCount){
            throw new IllegalStateException("Room is not available anymore");
        }

        //Reserve the room / update the booked count of inventories
        inventoryRepository.initBooking(room.getId(), request.getCheckInDate(),
                request.getCheckOutDate(), request.getRoomCount());

        inventoryRepository.saveAll(inventoryList);

        //TODO: calculate dynamic amount
        BigDecimal priceForOneRoom = priceService.calculateTotalPrice(inventoryList);
        BigDecimal totalPrice = priceForOneRoom.multiply(BigDecimal.valueOf(request.getRoomCount()));



        Booking booking = Booking.builder()
                .bookingStatus(BookingStatus.RESERVED)
                .hotel(hotel)
                .room(room)
                .checkInDate(request.getCheckInDate())
                .checkOutDate(request.getCheckOutDate())
                .user(getCurrentUser())
                .roomCount(request.getRoomCount())
                .amount(totalPrice)
                .build();

        booking = bookingRepository.save(booking);

        return modelMapper.map(booking, BookingResponse.class);
    }


    @Override
    @Transactional
    public BookingResponse addGuests(Long bookingId, List<GuestRequest> requestList) {

        log.info("Adding guests for booking with id: {}", bookingId);

        Booking booking = bookingRepository
                .findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Booking not found with id: "+bookingId));
        User user = getCurrentUser();

        log.info("Authenticated user ID: {}", user.getId());
        log.info("Booking user ID: {}", booking.getUser().getId());
        log.info("Booking ID: {}", booking.getId());

        if (!Objects.equals(user.getId(), booking.getUser().getId())){
            throw new UnAuthorisedException("Booking does not belong to this user with id: "+user.getId());

        }

        if(hasBookingExpired(booking)) {
            throw  new IllegalStateException("Booking has already expired");
        }

        if(booking.getBookingStatus() != BookingStatus.RESERVED){
            throw new IllegalStateException("Booking is not under reserved state, cannot add guests");
        }

        for(GuestRequest guestRequest : requestList){
            Guest guest = modelMapper.map(guestRequest, Guest.class);
            guest.setUser(user);
            guest = guestRepository.save(guest);
            booking.getGuests().add(guest);
        }

        booking.setBookingStatus(BookingStatus.GUESTS_ADDED);
        booking = bookingRepository.save(booking);

        return modelMapper.map(booking, BookingResponse.class);
    }

    @Override
    @Transactional
    public String initiatePayments(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Booking not found with id: "+bookingId));

        User user = getCurrentUser();
        if (!Objects.equals(user.getId(), booking.getUser().getId())){
            throw new UnAuthorisedException(
                    "Booking does not belong to this user with id: "+user.getId());
        }

        if(hasBookingExpired(booking)) {
            throw  new IllegalStateException("Booking has already expired");
        }

        String sessionUrl = checkoutService.getCheckoutSession(
                booking,
                frontendUrl+"/payments/success",
                frontendUrl+"/payments/failure");

        booking.setBookingStatus(BookingStatus.PAYMENT_PENDING);
        bookingRepository.save(booking);

        return sessionUrl;
    }

    @Override
    @Transactional
    public void capturePayment(Event event) {

        if ("checkout.session.completed".equals(event.getType())) {
            Session session = (Session) event.getDataObjectDeserializer().getObject().orElse(null);
            if (session == null) return;

            String sessionId = session.getId();
            Booking booking =
                    bookingRepository.findByPaymentSessionId(sessionId).orElseThrow(() ->
                            new ResourceNotFoundException("Booking not found for session ID: "+sessionId));

            booking.setBookingStatus(BookingStatus.CONFIRMED);
            bookingRepository.save(booking);

            inventoryRepository.findAndLockReservedInventory(booking.getRoom().getId(), booking.getCheckInDate(),
                    booking.getCheckOutDate(), booking.getRoomCount());

            inventoryRepository.confirmBooking(booking.getRoom().getId(), booking.getCheckInDate(),
                    booking.getCheckOutDate(), booking.getRoomCount());

            log.info("Successfully confirmed the booking for Booking ID: {}", booking.getId());
        } else {
            log.warn("Unhandled event type: {}", event.getType());
        }
    }

    @Override
    @Transactional
    public void cancelBooking(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(
                () -> new ResourceNotFoundException("Booking not found with id: "+bookingId)
        );
        User user = getCurrentUser();
        if (!user.equals(booking.getUser())) {
            throw new UnAuthorisedException("Booking does not belong to this user with id: "+user.getId());
        }

        if(booking.getBookingStatus() != BookingStatus.CONFIRMED) {
            throw new IllegalStateException("Only confirmed bookings can be cancelled");
        }

        booking.setBookingStatus(BookingStatus.CANCELLED);
        bookingRepository.save(booking);

        inventoryRepository.findAndLockReservedInventory(booking.getRoom().getId(), booking.getCheckInDate(),
                booking.getCheckOutDate(), booking.getRoomCount());

        inventoryRepository.cancelBooking(booking.getRoom().getId(), booking.getCheckInDate(),
                booking.getCheckOutDate(), booking.getRoomCount());

        // handle the refund

        try {
            Session session = Session.retrieve(booking.getPaymentSessionId());
            RefundCreateParams refundParams = RefundCreateParams.builder()
                    .setPaymentIntent(session.getPaymentIntent())
                    .build();

            Refund.create(refundParams);
        } catch (StripeException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public BookingStatus getBookingStatus(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(
                () -> new ResourceNotFoundException("Booking not found with id: "+bookingId)
        );
        User user = getCurrentUser();
        if (!user.equals(booking.getUser())) {
            throw new UnAuthorisedException("Booking does not belong to this user with id: "+user.getId());
        }

        return booking.getBookingStatus();
    }




    public boolean hasBookingExpired(Booking booking){
        return booking.getCreatedAt().plusMinutes(10).isBefore(LocalDateTime.now());
    }

}
