package com.pradeepmali591.CheckInn.controller;

import com.pradeepmali591.CheckInn.dto.profile.request.ProfileUpdateRequest;
import com.pradeepmali591.CheckInn.service.BookingService;
import com.pradeepmali591.CheckInn.service.GuestService;
import com.pradeepmali591.CheckInn.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final BookingService bookingService;
    private final GuestService guestService;

    @PatchMapping("/profile")
    public ResponseEntity<Void> updateProfile(@RequestBody ProfileUpdateRequest request) {
        userService.updateProfile(request);

        return ResponseEntity.noContent().build();
    }


}
