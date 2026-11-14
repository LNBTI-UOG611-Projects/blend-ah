
package com.blendah.backend.controller;

import com.blendah.backend.model.MiniApp;
import com.blendah.backend.repository.MiniAppRepository;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/mini-apps")
public class MiniAppController {

    private final MiniAppRepository miniAppRepository;

    public MiniAppController(MiniAppRepository miniAppRepository) {
        this.miniAppRepository = miniAppRepository;
    }

    @GetMapping
    public List<MiniApp> viewMiniAppListings() {
        return miniAppRepository.findAllActive();
    }
}

