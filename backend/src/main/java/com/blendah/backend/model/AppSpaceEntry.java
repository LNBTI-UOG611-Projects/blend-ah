package com.blendah.backend.model;

import jakarta.persistence.*;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "app_space", schema = "public")
public class AppSpaceEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "app_space_id")
    private Long appSpaceId;

    @Column(name = "user_id", nullable = false, updatable = false)
    private UUID userId;

    @Column(name = "app_id", nullable = false, updatable = false)
    private Long appId;

    @Column(name = "added_at", updatable = false)
    private OffsetDateTime addedAt;

    @PrePersist
    void onCreate() {
        if (addedAt == null) {
            addedAt = OffsetDateTime.now();
        }
    }

    public Long getAppSpaceId() { return appSpaceId; }
    public UUID getUserId() { return userId; }
    public void setUserId(UUID userId) { this.userId = userId; }
    public Long getAppId() { return appId; }
    public void setAppId(Long appId) { this.appId = appId; }
    public OffsetDateTime getAddedAt() { return addedAt; }
}
