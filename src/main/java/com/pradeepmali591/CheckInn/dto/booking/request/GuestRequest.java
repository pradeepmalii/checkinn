package com.pradeepmali591.CheckInn.dto.booking.request;

import com.pradeepmali591.CheckInn.entity.User;
import com.pradeepmali591.CheckInn.entity.enums.Gender;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GuestRequest {

    Long id;
    User user;
    String name;
    Gender gender;
    Integer age;
}
