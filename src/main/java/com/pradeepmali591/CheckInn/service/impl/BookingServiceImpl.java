package com.pradeepmali591.CheckInn.service.impl;

import com.pradeepmali591.CheckInn.dto.booking.request.BookingRequest;
import com.pradeepmali591.CheckInn.dto.booking.response.BookingResponse;
import com.pradeepmali591.CheckInn.entity.*;
import com.pradeepmali591.CheckInn.entity.enums.BookingStatus;
import com.pradeepmali591.CheckInn.exception.ResourceNotFoundException;
import com.pradeepmali591.CheckInn.repository.BookingRepository;
import com.pradeepmali591.CheckInn.repository.HotelRepository;
import com.pradeepmali591.CheckInn.repository.InventoryRepository;
import com.pradeepmali591.CheckInn.repository.RoomRepository;
import com.pradeepmali591.CheckInn.service.BookingService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
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

        //Creating the booking
        User user = new User();
        user.setId(1L); //TODO: remove dummy user

        //TODO: calculate dynamic amount

        Booking booking = Booking.builder()
                .bookingStatus(BookingStatus.RESERVED)
                .hotel(hotel)
                .room(room)
                .checkInDate(request.getCheckOutDate())
                .checkOutDate(request.getCheckOutDate())
                .user(user)
                .roomCount(request.getRoomCount())
                .amount(BigDecimal.TEN)
                .build();

        booking = bookingRepository.save(booking);

        return modelMapper.map(booking, BookingResponse.class);
    }
}
