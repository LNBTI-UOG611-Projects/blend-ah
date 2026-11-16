
package com.blendah.backend.dto;

import java.time.OffsetDateTime;
import java.util.UUID;

public record DeviceTokenResponse(

    Long deviceId,
    UUID userId,
    UUID installationId,
    String devicePlatform,
    OffsetDateTime createdAt,
    OffsetDateTime updatedAt

) {}
