package com.pradeepmali591.CheckInn.controller;

import com.pradeepmali591.CheckInn.dto.room.request.RoomRequest;
import com.pradeepmali591.CheckInn.dto.room.response.RoomResponse;
import com.pradeepmali591.CheckInn.service.RoomService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/hotels/{hotelId}/rooms")
@Slf4j
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class RoomAdminController {

    RoomService roomService;

    @PostMapping
    public ResponseEntity<RoomResponse> createNewRoom(@PathVariable Long hotelId
                                                      ,@RequestBody RoomRequest request){

        log.info("Attempting to create new room in hotel with ID: {}",hotelId);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(roomService.createNewRoom(hotelId, request));
    }



}
