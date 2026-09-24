package com.pradeepmali591.CheckInn.controller;

import com.pradeepmali591.CheckInn.dto.hotel.response.HotelResponse;
import com.pradeepmali591.CheckInn.dto.hotelInfo.response.HotelInfoResponse;
import com.pradeepmali591.CheckInn.dto.hotelSearch.request.HotelSearchRequest;
import com.pradeepmali591.CheckInn.service.HotelService;
import com.pradeepmali591.CheckInn.service.InventoryService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/hotels")
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class HotelBrowserController {

    InventoryService inventoryService;
    HotelService hotelService;

    @GetMapping("/search")
    public ResponseEntity<Page<HotelResponse>> searchHotels(@RequestBody HotelSearchRequest request){

        log.info("Attempting to search hotel in city: "+request.getCity());

        return ResponseEntity.ok(inventoryService.searchHotels(request));
    }

    @GetMapping("/{hotelId}/info")
    public ResponseEntity<HotelInfoResponse> getHotelInfo(@PathVariable Long hotelId){

        log.info("Attempting to get hotel info with ID: "+hotelId);

        return ResponseEntity.ok(hotelService.getHotelInfoById(hotelId));


    }
}
