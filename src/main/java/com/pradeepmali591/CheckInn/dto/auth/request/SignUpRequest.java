package com.pradeepmali591.CheckInn.dto.auth.request;

import lombok.Data;

@Data
public class SignUpRequest {

    private String email;
    private String password;
    private String name;
}
