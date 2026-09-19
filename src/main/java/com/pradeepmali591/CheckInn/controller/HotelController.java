package com.pradeepmali591.CheckInn.controller;

import com.pradeepmali591.CheckInn.dto.hotel.request.HotelRequest;
import com.pradeepmali591.CheckInn.dto.hotel.response.HotelResponse;
import com.pradeepmali591.CheckInn.service.HotelService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/hotels")
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class HotelController {

    HotelService hotelService;

    @PostMapping
    public ResponseEntity<HotelResponse> createNewHotel(@RequestBody HotelRequest request){
        log.info("Attempting to create new hotel with name: "+ request.getName());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(hotelService.createNewHotel(request));
    }

    @GetMapping("/{hotelId}")
    public ResponseEntity<HotelResponse> getHotelById(@PathVariable Long hotelId){
        return ResponseEntity.ok(hotelService.getHotelById(hotelId));
    }
}
