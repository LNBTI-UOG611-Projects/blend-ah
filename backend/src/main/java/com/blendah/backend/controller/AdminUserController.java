
package com.blendah.backend.controller;

import com.blendah.backend.dto.AdminUserResponse;
import com.blendah.backend.service.AdminUserService;

import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin/users")
public class AdminUserController {

    private final AdminUserService adminUserService;

    public AdminUserController(AdminUserService adminUserService) {
        this.adminUserService = adminUserService;
    }

    @GetMapping
    public List<AdminUserResponse> getAllUsers() {
        return adminUserService.getAllUsers();
    }

    @GetMapping("/{userId}")
    public AdminUserResponse getUserById(@PathVariable UUID userId) {
        return adminUserService.getUserById(userId);
    }

    @PatchMapping("/{userId}/deactivate")
    public AdminUserResponse deactivateUser(@PathVariable UUID userId) {
        return adminUserService.deactivateUser(userId);
    }

    @PatchMapping("/{userId}/activate")
    public AdminUserResponse activateUser(@PathVariable UUID userId) {
        return adminUserService.activateUser(userId);
    }
}
