package com.pradeepmali591.CheckInn.repository;

import com.pradeepmali591.CheckInn.entity.Guest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GuestRepository extends JpaRepository<Guest, Long> {
}
