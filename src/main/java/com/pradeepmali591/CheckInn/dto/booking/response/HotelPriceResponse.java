package com.pradeepmali591.CheckInn.dto.booking.response;

import com.pradeepmali591.CheckInn.entity.Hotel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HotelPriceResponse {
    private Hotel hotel;
    private Double price;
}
