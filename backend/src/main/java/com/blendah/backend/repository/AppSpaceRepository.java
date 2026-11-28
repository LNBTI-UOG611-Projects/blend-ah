package com.blendah.backend.repository;

import com.blendah.backend.model.AppSpaceEntry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface AppSpaceRepository extends JpaRepository<AppSpaceEntry, Long> {
    List<AppSpaceEntry> findByUserIdOrderByAddedAtDesc(UUID userId);
    boolean existsByUserIdAndAppId(UUID userId, Long appId);
    long deleteByUserIdAndAppId(UUID userId, Long appId);
}
