
package com.blendah.backend.dto;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;

public record MiniAppUpdateRequest(

    @NotBlank(message = "Mini-App name is required")
    String appName,

    String description,

    String iconUrl,

    @NotBlank(message = "App URL is required")
    @URL(message = "Enter a valid App URL")
    String appUrl

) {}
