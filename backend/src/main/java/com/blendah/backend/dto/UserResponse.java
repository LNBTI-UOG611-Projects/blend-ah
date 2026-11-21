package main.java.com.blendah.backend.dto;

import java.time.OffsetDateTime;
import java.util.UUID;

/** The logged-in user's own profile. Never includes the password hash. */
public record UserResponse(
    UUID userId,
    String fullName,
    String email,
    String role,
    String profileImageUrl,
    OffsetDateTime createdAt
) {}