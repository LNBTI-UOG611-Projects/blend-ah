package com.blendah.backend.repository;

import com.blendah.backend.model.MiniApp;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MiniAppRepository
        extends JpaRepository<MiniApp, Long> {

    List<MiniApp> findByStatusIgnoreCase(String status);
}