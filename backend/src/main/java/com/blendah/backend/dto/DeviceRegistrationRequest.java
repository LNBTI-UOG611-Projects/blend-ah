
package com.blendah.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record DeviceRegistrationRequest(

    @NotNull(message = "Installation ID is required")
    UUID installationId,

    @NotBlank(message = "FCM token is required")
    String fcmToken,

    @NotBlank(message = "Device platform is required")
    String devicePlatform

) {}
