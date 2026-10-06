package com.pradeepmali591.CheckInn.dto.hotel.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HotelReportResponse {
    private Long bookingCount;
    private BigDecimal totalRevenue;
    private BigDecimal avgRevenue;
}
