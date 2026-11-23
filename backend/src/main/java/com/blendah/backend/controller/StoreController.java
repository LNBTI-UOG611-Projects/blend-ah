package com.blendah.backend.controller;

import com.blendah.backend.dto.MiniAppResponse;
import com.blendah.backend.service.StoreService;

import org.springframework.web.bind.annotation.*;

import java.util.List;

/** Public Mini-App Store (published apps only). */
@RestController
@RequestMapping("/api/store/apps")
@CrossOrigin(origins = "*")
public class StoreController {

    private final StoreService storeService;

    public StoreController(StoreService storeService) {
        this.storeService = storeService;
    }

    @GetMapping
    public List<MiniAppResponse> viewApps(
            @RequestParam(required = false) String search) {
        return storeService.getPublishedApps(search);
    }

    @GetMapping("/{id}")
    public MiniAppResponse viewAppDetails(@PathVariable Long id) {
        return storeService.getPublishedApp(id);
    }
}
