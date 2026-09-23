package com.pradeepmali591.CheckInn.dto.hotelSearch.request;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class HotelSearchRequest {

    String city;
    LocalDate startDate;
    LocalDate endDate;
    Integer roomsCount;

    Integer page=0;
    Integer size=10;


}
