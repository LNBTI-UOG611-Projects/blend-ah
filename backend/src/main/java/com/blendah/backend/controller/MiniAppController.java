
package com.blendah.backend.controller;

import com.blendah.backend.dto.MiniAppCreateRequest;
import com.blendah.backend.dto.MiniAppResponse;
import com.blendah.backend.service.MiniAppService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/mini-apps")
public class MiniAppController {

    private final MiniAppService miniAppService;

    public MiniAppController(MiniAppService miniAppService) {
        this.miniAppService = miniAppService;
    }

    // View Mini-App Listing
    @GetMapping
    public List<MiniAppResponse> viewMiniAppListings(
            @RequestParam(required = false) String search) {

        return miniAppService.getMiniApps(search);
    }

    // Add New Mini-App
    @PostMapping
    public ResponseEntity<MiniAppResponse> createMiniApp(
            @Valid @RequestBody MiniAppCreateRequest request) {

        MiniAppResponse created =
                miniAppService.createMiniApp(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(created);
    }
}
