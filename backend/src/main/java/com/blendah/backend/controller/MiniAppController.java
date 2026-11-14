package com.blendah.backend.controller;

import com.blendah.backend.dto.MiniAppResponse;
import com.blendah.backend.service.MiniAppService;

import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/mini-apps")
public class MiniAppController {

    private final MiniAppService miniAppService;

    public MiniAppController(MiniAppService miniAppService) {
        this.miniAppService = miniAppService;
    }

    @GetMapping
    public List<MiniAppResponse> viewMiniAppListings(
            @RequestParam(required = false) String search) {

        return miniAppService.getMiniApps(search);
    }
}