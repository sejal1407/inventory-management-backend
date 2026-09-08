package com.inventory.service;

import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.stereotype.Service;

import com.inventory.dto.RegisterRequest;
import com.inventory.entity.Users;
import com.inventory.repository.UserRepository;
import com.inventory.exception.DuplicateResourceException;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Users register(RegisterRequest request) {

        // Check if email already exists
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Email already registered");
        }

        Users user = new Users();

        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());

        // Encrypt password before saving
        user.setPassword(
                passwordEncoder.encode(request.getPassword())
        );

        // Default role
        user.setRole("STAFF");

        // Activate user
        user.setActive(true);

        // Audit information
        user.setCreatedBy("SYSTEM");
        user.setUpdatedBy("SYSTEM");

        return userRepository.save(user);
    }
}