package com.blendah.backend.service;

import com.blendah.backend.dto.UserResponse;
import com.blendah.backend.exception.ApiException;
import com.blendah.backend.model.User;
import com.blendah.backend.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.UUID;

/** The logged-in user's own profile (view now; edit + change password in later steps). */
@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserResponse getProfile(UUID userId) {
        return toResponse(findUser(userId));
    }

    User findUser(UUID userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "User not found"));
    }

    static UserResponse toResponse(User user) {
        return new UserResponse(
            user.getUserId(),
            user.getFullName(),
            user.getEmail(),
            user.getRole().name(),
            user.getProfileImageUrl(),
            user.getCreatedAt()
        );
    }
}