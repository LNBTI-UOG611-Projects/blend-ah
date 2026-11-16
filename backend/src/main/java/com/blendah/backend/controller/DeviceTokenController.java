
package com.blendah.backend.controller;

import com.blendah.backend.dto.DeviceRegistrationRequest;
import com.blendah.backend.dto.DeviceTokenResponse;
import com.blendah.backend.service.DeviceTokenService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/devices")
public class DeviceTokenController {

    private final DeviceTokenService deviceTokenService;

    public DeviceTokenController(
            DeviceTokenService deviceTokenService) {
        this.deviceTokenService = deviceTokenService;
    }

    @PostMapping("/register")
    public ResponseEntity<DeviceTokenResponse> registerDevice(
            @Valid @RequestBody DeviceRegistrationRequest request) {

        // Authentication integration pending.
        // Do not accept a userId from the request body.
        return ResponseEntity.status(
                HttpStatus.SERVICE_UNAVAILABLE
        ).build();
    }
}
