
package com.blendah.backend.repository;

import com.blendah.backend.model.DeviceToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DeviceTokenRepository
        extends JpaRepository<DeviceToken, Long> {

    Optional<DeviceToken> findByInstallationId(UUID installationId);

    Optional<DeviceToken> findByFcmToken(String fcmToken);

    List<DeviceToken> findByUserId(UUID userId);

    boolean existsByInstallationId(UUID installationId);
}
