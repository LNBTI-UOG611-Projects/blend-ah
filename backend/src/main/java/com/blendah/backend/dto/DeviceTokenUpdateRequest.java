
package com.blendah.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record DeviceTokenUpdateRequest(

    @NotNull(message = "Installation ID is required")
    UUID installationId,

    @NotBlank(message = "New FCM token is required")
    String fcmToken

) {}
