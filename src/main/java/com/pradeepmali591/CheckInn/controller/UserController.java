package com.pradeepmali591.CheckInn.controller;

import com.pradeepmali591.CheckInn.dto.booking.response.BookingResponse;
import com.pradeepmali591.CheckInn.dto.profile.request.ProfileUpdateRequest;
import com.pradeepmali591.CheckInn.service.BookingService;
import com.pradeepmali591.CheckInn.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final BookingService bookingService;

    @PatchMapping("/profile")
    public ResponseEntity<Void> updateProfile(@RequestBody ProfileUpdateRequest request) {
        userService.updateProfile(request);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/myBookings")
    public ResponseEntity<List<BookingResponse>> getMyBookings() {
        return ResponseEntity.ok(bookingService.getMyBookings());
    }


}
