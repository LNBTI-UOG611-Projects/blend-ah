
package com.blendah.backend.model;

import jakarta.persistence.*;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "devicetokens", schema = "public")
public class DeviceToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "device_id")
    private Long deviceId;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(name = "installation_id", nullable = false, unique = true)
    private UUID installationId;

    @Column(name = "fcm_token", nullable = false, unique = true)
    private String fcmToken;

    @Column(name = "device_platform", nullable = false)
    private String devicePlatform;

    @Column(name = "created_at", insertable = false, updatable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;

    public DeviceToken() {
    }

    @PrePersist
    @PreUpdate
    public void updateTimestamp() {
        updatedAt = OffsetDateTime.now();
    }

    public Long getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(Long deviceId) {
        this.deviceId = deviceId;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public UUID getInstallationId() {
        return installationId;
    }

    public void setInstallationId(UUID installationId) {
        this.installationId = installationId;
    }

    public String getFcmToken() {
        return fcmToken;
    }

    public void setFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }

    public String getDevicePlatform() {
        return devicePlatform;
    }

    public void setDevicePlatform(String devicePlatform) {
        this.devicePlatform = devicePlatform;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }
}
