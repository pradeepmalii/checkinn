package com.pradeepmali591.CheckInn.repository;

import com.pradeepmali591.CheckInn.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);
}
