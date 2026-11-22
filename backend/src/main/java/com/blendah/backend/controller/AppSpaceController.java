package com.blendah.backend.controller;

import com.blendah.backend.dto.AppSpaceItemResponse;
import com.blendah.backend.dto.DashboardResponse;
import com.blendah.backend.service.AppSpaceService;
import com.blendah.backend.service.CurrentUserService;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/** Personal App Space + dashboard. The user always comes from CurrentUserService. */
@RestController
@RequestMapping("/api/app-space")
@CrossOrigin(origins = "*")
public class AppSpaceController {

    private final AppSpaceService appSpaceService;
    private final CurrentUserService currentUser;

    public AppSpaceController(AppSpaceService appSpaceService,
                              CurrentUserService currentUser) {
        this.appSpaceService = appSpaceService;
        this.currentUser = currentUser;
    }

    @GetMapping
    public List<AppSpaceItemResponse> myApps(HttpServletRequest request) {
        return appSpaceService.getAppSpace(currentUser.requireActiveUserId(request));
    }

    @GetMapping("/dashboard")
    public DashboardResponse dashboard(HttpServletRequest request) {
        return appSpaceService.getDashboard(currentUser.requireActiveUserId(request));
    }

    @PostMapping("/{appId}")
    public ResponseEntity<AppSpaceItemResponse> add(
            @PathVariable Long appId, HttpServletRequest request) {
        AppSpaceItemResponse added = appSpaceService.addApp(
                currentUser.requireActiveUserId(request), appId);
        return ResponseEntity.status(HttpStatus.CREATED).body(added);
    }

    @DeleteMapping("/{appId}")
    public ResponseEntity<Void> remove(
            @PathVariable Long appId, HttpServletRequest request) {
        appSpaceService.removeApp(currentUser.requireActiveUserId(request), appId);
        return ResponseEntity.noContent().build();
    }
}
