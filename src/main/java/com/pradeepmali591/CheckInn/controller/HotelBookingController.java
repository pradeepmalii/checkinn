package com.pradeepmali591.CheckInn.controller;

import com.pradeepmali591.CheckInn.dto.booking.request.BookingRequest;
import com.pradeepmali591.CheckInn.dto.booking.request.GuestRequest;
import com.pradeepmali591.CheckInn.dto.booking.response.BookingResponse;
import com.pradeepmali591.CheckInn.service.BookingService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/bookings")
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class HotelBookingController {

    BookingService bookingService;

    @PostMapping("/init")
    public ResponseEntity<BookingResponse> initialiseBooking(@RequestBody BookingRequest request){
        log.info("Attempting to initialize booking in hotel with ID: "+request.getHotelId());

        return ResponseEntity.ok(bookingService.initialiseBooking(request));
    }

    @PostMapping("/{bookingId}/addGuests")
    public ResponseEntity<BookingResponse> addGuests(@PathVariable Long bookingId,
                                                     @RequestBody List<GuestRequest> request){
        return ResponseEntity.ok(bookingService.addGuests(bookingId, request));
    }

    @PostMapping("/{bookingId}/payments")
    public ResponseEntity<Map<String, String>> initiatePayment(@PathVariable Long bookingId){
        String sessionUrl = bookingService.initiatePayments(bookingId);
        return ResponseEntity.ok(Map.of("sessionUrl", sessionUrl));
    }

    @PostMapping("/{bookingId}/cancel")
    public ResponseEntity<Void> cancelBooking(@PathVariable Long bookingId) {
        bookingService.cancelBooking(bookingId);
        return ResponseEntity.noContent().build();
    }


}
