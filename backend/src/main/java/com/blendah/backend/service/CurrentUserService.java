package com.blendah.backend.service;

import com.blendah.backend.model.User;
import com.blendah.backend.repository.UserRepository;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

/**
 * Resolves "who is calling" for Member 2 endpoints.
 *
 * !!! TEMPORARY !!!  Member 1's authentication is not merged yet, so this reads
 * the user id from an "X-User-Id" header. That header can be forged, so this is
 * NOT secure. When Member 1's token/security work lands, replace the body of
 * requireActiveUserId() so it returns the authenticated principal's UUID.
 * Controllers call only this method, so nothing else has to change.
 */
@Service
public class CurrentUserService {

    public static final String TEMP_HEADER = "X-User-Id";

    private final UserRepository userRepository;

    public CurrentUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UUID requireActiveUserId(HttpServletRequest request) {
        String raw = request.getHeader(TEMP_HEADER);
        if (raw == null || raw.isBlank()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Login required");
        }

        UUID userId;
        try {
            userId = UUID.fromString(raw.trim());
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Login required");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED, "Login required"));

        if (!Boolean.TRUE.equals(user.getUserStatus())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Account is deactivated");
        }
        return userId;
    }
}
