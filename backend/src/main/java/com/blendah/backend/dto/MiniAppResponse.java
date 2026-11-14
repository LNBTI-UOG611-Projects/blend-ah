package com.blendah.backend.dto;

import java.time.OffsetDateTime;

public record MiniAppResponse(
    Long appId,
    String appName,
    String description,
    String iconUrl,
    String appUrl,
    String status,
    OffsetDateTime createdAt
) {}