
package com.blendah.backend.dto;

import java.time.OffsetDateTime;
import java.util.UUID;

public record AdminUserResponse(
    UUID userId,
    String fullName,
    String email,
    String role,
    String profileImageUrl,
    OffsetDateTime createdAt,
    boolean userStatus
) {}
