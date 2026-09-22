package com.pradeepmali591.CheckInn.repository;

import com.pradeepmali591.CheckInn.entity.Inventory;
import com.pradeepmali591.CheckInn.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {

    void deleteByRoom(Room room);
}
