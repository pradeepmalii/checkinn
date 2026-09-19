package com.pradeepmali591.CheckInn.service.impl;

import com.pradeepmali591.CheckInn.dto.hotel.request.HotelRequest;
import com.pradeepmali591.CheckInn.dto.hotel.response.HotelResponse;
import com.pradeepmali591.CheckInn.entity.Hotel;
import com.pradeepmali591.CheckInn.exception.ResourceNotFoundException;
import com.pradeepmali591.CheckInn.repository.HotelRepository;
import com.pradeepmali591.CheckInn.service.HotelService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class HotelServiceImpl implements HotelService {

    HotelRepository hotelRepository;
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
}

