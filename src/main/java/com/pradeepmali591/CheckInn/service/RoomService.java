package com.pradeepmali591.CheckInn.service;

import com.pradeepmali591.CheckInn.dto.room.request.RoomRequest;
import com.pradeepmali591.CheckInn.dto.room.response.RoomResponse;
import org.jspecify.annotations.Nullable;

import java.util.List;

public interface RoomService {

    RoomResponse createNewRoom(Long hotelId, RoomRequest request);

    List<RoomResponse> getAllRoomsInHotel(Long hotelId);

    RoomResponse getRoomById(Long roomId);

    void deleteRoomById(Long roomId);

    RoomResponse updateRoomById(Long hotelId, Long roomId, RoomRequest request);
}
