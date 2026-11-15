package com.blendah.backend.model;

import jakarta.persistence.*;
import org.hibernate.annotations.ColumnTransformer;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "users")
public class User {

    public enum UserRole { NORMAL_USER, ADMIN }

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "user_id", updatable = false, nullable = false)
    private UUID userId;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    // The database column is the PostgreSQL enum "user_role".
    // "?::user_role" converts the text to that enum when saving.
    @Enumerated(EnumType.STRING)
    @ColumnTransformer(write = "?::user_role")
    @Column(name = "role", columnDefinition = "user_role")
    private UserRole role = UserRole.NORMAL_USER;

    @Column(name = "profile_image_url")
    private String profileImageUrl;

    // New accounts are active by default (used by the admin portal)
    @Column(name = "user_status", nullable = false)
    private Boolean userStatus = true;

    @Column(name = "created_at", updatable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;

    @PrePersist
    void onCreate() {
        createdAt = OffsetDateTime.now();
        updatedAt = createdAt;
    }

    @PreUpdate
    void onUpdate() {
        updatedAt = OffsetDateTime.now();
    }

    // Getters and Setters
    public UUID getUserId() { return userId; }
    public void setUserId(UUID userId) { this.userId = userId; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
    public UserRole getRole() { return role; }
    public void setRole(UserRole role) { this.role = role; }
    public String getProfileImageUrl() { return profileImageUrl; }
    public void setProfileImageUrl(String profileImageUrl) { this.profileImageUrl = profileImageUrl; }
    public OffsetDateTime getCreatedAt() { return createdAt; }
    public OffsetDateTime getUpdatedAt() { return updatedAt; }
        public Boolean getUserStatus() { return userStatus; }
    public void setUserStatus(Boolean userStatus) { this.userStatus = userStatus; }
}
