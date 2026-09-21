package com.pradeepmali591.CheckInn.dto.room.response;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class RoomResponse {

    private Long id;
    private String type;
    private BigDecimal basePrice;
    private String[] photos;
    private String[] amenities;
    private Integer totalCount;
    private Integer capacity;
}
