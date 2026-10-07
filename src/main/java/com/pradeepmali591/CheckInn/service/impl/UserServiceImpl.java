package com.pradeepmali591.CheckInn.service.impl;

import com.pradeepmali591.CheckInn.dto.profile.request.ProfileUpdateRequest;
import com.pradeepmali591.CheckInn.entity.User;
import com.pradeepmali591.CheckInn.exception.ResourceNotFoundException;
import com.pradeepmali591.CheckInn.repository.UserRepository;
import com.pradeepmali591.CheckInn.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import static com.pradeepmali591.CheckInn.util.AppUtils.getCurrentUser;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService, UserDetailsService{

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found with id: "+id));
    }

    @Override
    public void updateProfile(ProfileUpdateRequest request) {
        User user = getCurrentUser();

        if(request.getDateOfBirth() != null) user.setDateOfBirth(request.getDateOfBirth());
        if(request.getGender() != null) user.setGender(request.getGender());
        if (request.getName() != null) user.setName(request.getName());

        userRepository.save(user);

    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username).orElse(null);
    }
}

