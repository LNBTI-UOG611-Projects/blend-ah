
package com.blendah.backend.service;

import com.blendah.backend.dto.DeviceRegistrationRequest;
import com.blendah.backend.dto.DeviceTokenResponse;
import com.blendah.backend.model.DeviceToken;
import com.blendah.backend.repository.DeviceTokenRepository;
import com.blendah.backend.repository.UserRepository;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Service
public class DeviceTokenService {

    private final DeviceTokenRepository deviceTokenRepository;
    private final UserRepository userRepository;

    public DeviceTokenService(
            DeviceTokenRepository deviceTokenRepository,
            UserRepository userRepository) {

        this.deviceTokenRepository = deviceTokenRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public DeviceTokenResponse registerDevice(
            UUID authenticatedUserId,
            DeviceRegistrationRequest request) {

        if (authenticatedUserId == null ||
                !userRepository.existsById(authenticatedUserId)) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Authenticated user not found"
            );
        }

        String platform = request.devicePlatform()
                .trim()
                .toUpperCase();

        if (!platform.equals("ANDROID") &&
                !platform.equals("IOS")) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Device platform must be ANDROID or IOS"
            );
        }

        String fcmToken = request.fcmToken().trim();

        // Prevent an FCM token from being assigned
        // to multiple installations.
        var tokenOwner = deviceTokenRepository
                .findByFcmToken(fcmToken);

        if (tokenOwner.isPresent() &&
                !tokenOwner.get().getInstallationId()
                        .equals(request.installationId())) {

            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "FCM token is already registered"
            );
        }

        DeviceToken device = deviceTokenRepository
                .findByInstallationId(request.installationId())
                .orElseGet(DeviceToken::new);

        // An existing installation must belong to
        // the authenticated user.
        if (device.getDeviceId() != null &&
                !device.getUserId().equals(authenticatedUserId)) {

            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Device belongs to another user"
            );
        }

        device.setUserId(authenticatedUserId);
        device.setInstallationId(request.installationId());
        device.setFcmToken(fcmToken);
        device.setDevicePlatform(platform);

        DeviceToken saved = deviceTokenRepository.save(device);

        return new DeviceTokenResponse(
                saved.getDeviceId(),
                saved.getUserId(),
                saved.getInstallationId(),
                saved.getDevicePlatform(),
                saved.getCreatedAt(),
                saved.getUpdatedAt()
        );
    }
}
