package com.pradeepmali591.CheckInn.controller;

import com.pradeepmali591.CheckInn.dto.booking.response.BookingResponse;
import com.pradeepmali591.CheckInn.dto.hotel.request.HotelRequest;
import com.pradeepmali591.CheckInn.dto.hotel.response.HotelReportResponse;
import com.pradeepmali591.CheckInn.dto.hotel.response.HotelResponse;
import com.pradeepmali591.CheckInn.service.BookingService;
import com.pradeepmali591.CheckInn.service.HotelService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/admin/hotels")
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class HotelController {

    HotelService hotelService;
    BookingService bookingService;

    @PostMapping
    public ResponseEntity<HotelResponse> createNewHotel(@RequestBody HotelRequest request){
        log.info("Attempting to create new hotel with name: "+ request.getName());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(hotelService.createNewHotel(request));
    }

    @GetMapping("/{hotelId}")
    public ResponseEntity<HotelResponse> getHotelById(@PathVariable Long hotelId){
        log.info("Attempting to get hotel with ID: "+ hotelId);

        return ResponseEntity.ok(hotelService.getHotelById(hotelId));
    }

    @PutMapping("/{hotelId}")
    public ResponseEntity<HotelResponse> updateHotelById(@PathVariable Long hotelId
                                                        ,@RequestBody HotelRequest request){
        log.info("Attempting to update hotel with ID: "+ hotelId);

        return ResponseEntity.ok(hotelService.updateHotelById(hotelId, request));
    }

    @DeleteMapping("/{hotelId}")
    public ResponseEntity<Void> deleteHotelById(@PathVariable Long hotelId){
        log.info("Attempting to delete hotel with ID: "+ hotelId);

        hotelService.deleteHotelById(hotelId);

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{hotelId}/activate")
    public ResponseEntity<Void> activateHotelById(@PathVariable Long hotelId){
        log.info("Attempting to activate hotel with ID: "+hotelId);

        hotelService.activateHotel(hotelId);

        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<HotelResponse>> getAllHotels() {
        return ResponseEntity.ok(hotelService.getAllHotels());
    }

    @GetMapping("/{hotelId}/bookings")
    public ResponseEntity<List<BookingResponse>> getAllBookingsByHotelId(@PathVariable Long hotelId) {
        return ResponseEntity.ok(bookingService.getAllBookingsByHotelId(hotelId));
    }

    @GetMapping("/{hotelId}/reports")
    public ResponseEntity<HotelReportResponse> getHotelReport(@PathVariable Long hotelId,
                                                              @RequestParam(required = false) LocalDate startDate,
                                                              @RequestParam(required = false) LocalDate endDate) {

        if (startDate == null) startDate = LocalDate.now().minusMonths(1);
        if (endDate == null) endDate = LocalDate.now();

        return ResponseEntity.ok(bookingService.getHotelReport(hotelId, startDate, endDate));
    }



}
