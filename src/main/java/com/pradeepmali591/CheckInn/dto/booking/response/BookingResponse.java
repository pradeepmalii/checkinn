package com.pradeepmali591.CheckInn.dto.booking.response;

import com.pradeepmali591.CheckInn.dto.booking.request.GuestRequest;
import com.pradeepmali591.CheckInn.entity.Hotel;
import com.pradeepmali591.CheckInn.entity.Room;
import com.pradeepmali591.CheckInn.entity.User;
import com.pradeepmali591.CheckInn.entity.enums.BookingStatus;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookingResponse {




    Long id;
    Integer roomCount;
    LocalDate checkInDate;
    LocalDate checkOutDate;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    BookingStatus bookingStatus;
    Set<GuestRequest> guests;
}
