package com.pradeepmali591.CheckInn.service;

import com.pradeepmali591.CheckInn.entity.Room;

public interface InventoryService {

    void initializeRoomForAYear(Room room);

    void deleteAllInventories(Room room);


}
