package com.pradeepmali591.CheckInn.dto.auth.request;

import lombok.Data;

@Data
public class LoginRequest {

    private String email;
    private String password;
}
