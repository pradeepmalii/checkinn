package com.pradeepmali591.CheckInn.dto.profile.request;

import com.pradeepmali591.CheckInn.entity.enums.Gender;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ProfileUpdateRequest {

    private String name;
    private LocalDate dateOfBirth;
    private Gender gender;

}
