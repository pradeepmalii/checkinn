package com.pradeepmali591.CheckInn.service;

import com.pradeepmali591.CheckInn.dto.hotel.request.HotelRequest;
import com.pradeepmali591.CheckInn.dto.hotel.response.HotelResponse;
import com.pradeepmali591.CheckInn.entity.Hotel;

public interface HotelService {

    HotelResponse createNewHotel(HotelRequest request);

    HotelResponse getHotelById(Long id);

    
}
