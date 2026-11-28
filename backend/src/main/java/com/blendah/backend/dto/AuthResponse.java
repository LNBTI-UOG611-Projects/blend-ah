package com.blendah.backend.dto;

import java.util.UUID;

/**
 * Login response. The first five fields are unchanged from the original
 * login response; accessToken, tokenType and expiresIn are new.
 */
public record AuthResponse(
    String message,
    UUID userId,
    String fullName,
    String email,
    String role,
    String accessToken,
    String tokenType,
    long expiresIn          // seconds until the access token expires
) {}