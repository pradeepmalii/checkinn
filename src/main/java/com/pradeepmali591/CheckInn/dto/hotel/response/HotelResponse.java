package com.pradeepmali591.CheckInn.dto.hotel.response;

import com.pradeepmali591.CheckInn.entity.HotelContactInfo;
import lombok.Data;

@Data
public class HotelResponse {

    private Long id;
    private String name;
    private String city;
    private String[] photos;
    private String[] amenities;
    private HotelContactInfo hotelContactInfo;
    private Boolean active;
}
