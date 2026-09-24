package com.pradeepmali591.CheckInn.service;

import com.pradeepmali591.CheckInn.dto.hotel.request.HotelRequest;
import com.pradeepmali591.CheckInn.dto.hotel.response.HotelResponse;
import com.pradeepmali591.CheckInn.dto.hotelInfo.response.HotelInfoResponse;
import org.jspecify.annotations.Nullable;

public interface HotelService {

    HotelResponse createNewHotel(HotelRequest request);

    HotelResponse getHotelById(Long id);

    HotelResponse updateHotelById(Long id, HotelRequest request);

    void deleteHotelById(Long id);

    void activateHotel(Long id);

    HotelInfoResponse getHotelInfoById(Long hotelId);
}
