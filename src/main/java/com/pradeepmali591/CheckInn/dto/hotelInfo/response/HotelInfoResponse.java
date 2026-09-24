package com.pradeepmali591.CheckInn.dto.hotelInfo.response;

import com.pradeepmali591.CheckInn.dto.hotel.response.HotelResponse;
import com.pradeepmali591.CheckInn.dto.room.response.RoomResponse;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class HotelInfoResponse {

    private HotelResponse hotelResponse;
    private List<RoomResponse> roomResponseList;
}
