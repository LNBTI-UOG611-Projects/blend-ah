package com.blendah.backend.dto;

import java.time.OffsetDateTime;

public record AppSpaceItemResponse(
    Long appId,
    String appName,
    String description,
    String iconUrl,
    String appUrl,
    String status,
    boolean available,
    OffsetDateTime addedAt
) {}
