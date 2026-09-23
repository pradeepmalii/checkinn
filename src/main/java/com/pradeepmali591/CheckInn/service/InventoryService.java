package com.pradeepmali591.CheckInn.service;

import com.pradeepmali591.CheckInn.dto.hotel.response.HotelResponse;
import com.pradeepmali591.CheckInn.dto.hotelSearch.request.HotelSearchRequest;
import com.pradeepmali591.CheckInn.entity.Room;
import org.jspecify.annotations.Nullable;
import org.springframework.data.domain.Page;

public interface InventoryService {

    void initializeRoomForAYear(Room room);

    void deleteAllInventories(Room room);

    Page<HotelResponse> searchHotels(HotelSearchRequest request);
}
