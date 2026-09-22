package com.pradeepmali591.CheckInn.service.impl;

import com.pradeepmali591.CheckInn.dto.hotel.request.HotelRequest;
import com.pradeepmali591.CheckInn.dto.hotel.response.HotelResponse;
import com.pradeepmali591.CheckInn.entity.Hotel;
import com.pradeepmali591.CheckInn.entity.Room;
import com.pradeepmali591.CheckInn.exception.ResourceNotFoundException;
import com.pradeepmali591.CheckInn.repository.HotelRepository;
import com.pradeepmali591.CheckInn.repository.RoomRepository;
import com.pradeepmali591.CheckInn.service.HotelService;
import com.pradeepmali591.CheckInn.service.InventoryService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class HotelServiceImpl implements HotelService {

    HotelRepository hotelRepository;
    InventoryService inventoryService;
    RoomRepository roomRepository;
    ModelMapper modelMapper;


    @Override
    public HotelResponse createNewHotel(HotelRequest request) {

        log.info("Creating a new hotel with name: {}", request.getName());

        Hotel hotel = modelMapper.map(request, Hotel.class);
        hotel.setActive(false);
        hotel = hotelRepository.save(hotel);
        log.info("Created a new hotel with ID: {}", hotel.getId());

        return modelMapper.map(hotel, HotelResponse.class);
    }

    @Override
    public HotelResponse getHotelById(Long id) {

        log.info("Getting the hotel with ID: {}", id );

        Hotel hotel = hotelRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found with ID: "+id));
        return modelMapper.map(hotel, HotelResponse.class);
    }

    @Override
    public HotelResponse updateHotelById(Long id, HotelRequest request) {

        log.info("Updating the hotel with ID: {}", id);

        Hotel hotel = hotelRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found with ID: "+id));

        modelMapper.map(request, hotel);


        hotel = hotelRepository.save(hotel);

        return modelMapper.map(hotel, HotelResponse.class);
    }

    @Override
    public void deleteHotelById(Long id) {
        Hotel hotel = hotelRepository
                .findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException
                                ("Hotel not found with Id: "+id));

        //TODO: delete the future inventories for this hotel
        for(Room room : hotel.getRooms()){
            inventoryService.deleteAllInventories(room);
            roomRepository.deleteById(room.getId());
        }

        hotelRepository.deleteById(id);
    }

    @Override
    public void activateHotel(Long id) {

        log.info("Activating the hotel with ID: {}", id);

        Hotel hotel = hotelRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found with ID: "+id));

        hotel.setActive(true);
        //TODO: Create inventory for all the rooms for this hotel
        //assuming only do it once
        for(Room room: hotel.getRooms()) {
            inventoryService.initializeRoomForAYear(room);
        }

    }


}

