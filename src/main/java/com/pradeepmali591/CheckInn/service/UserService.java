package com.pradeepmali591.CheckInn.service;

import com.pradeepmali591.CheckInn.dto.profile.request.ProfileUpdateRequest;
import com.pradeepmali591.CheckInn.entity.User;

public interface UserService {

    User getUserById(Long id);

    void updateProfile(ProfileUpdateRequest request);
}
