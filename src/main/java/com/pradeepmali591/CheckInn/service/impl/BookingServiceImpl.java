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
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class BookingServiceImpl implements BookingService {
    private final InventoryRepository inventoryRepository;

    RoomRepository roomRepository;
    HotelRepository hotelRepository;
    BookingRepository bookingRepository;
    ModelMapper modelMapper;
    GuestRepository guestRepository;

    @Override
    @Transactional
    public BookingResponse initialiseBooking(BookingRequest request) {

        log.info("Initializing booking for hotel: {}, room: {}, date: {} - {}",
                request.getHotelId(), request.getRoomId(), request.getCheckInDate(),
                request.getCheckOutDate());

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
        for(Inventory inventory: inventoryList){
            inventory.setReservedCount(inventory.getReservedCount() + request.getRoomCount());
        }

        inventoryRepository.saveAll(inventoryList);

        //TODO: calculate dynamic amount

        Booking booking = Booking.builder()
                .bookingStatus(BookingStatus.RESERVED)
                .hotel(hotel)
                .room(room)
                .checkInDate(request.getCheckInDate())
                .checkOutDate(request.getCheckOutDate())
                .user(getCurrentUser())
                .roomCount(request.getRoomCount())
                .amount(BigDecimal.TEN)
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

        if(!user.equals(booking.getUser())){
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



    public boolean hasBookingExpired(Booking booking){
        return booking.getCreatedAt().plusMinutes(10).isBefore(LocalDateTime.now());
    }

    public User getCurrentUser(){
        return (User) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
    }
}
