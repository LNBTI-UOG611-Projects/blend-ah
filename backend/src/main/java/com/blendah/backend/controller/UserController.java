package com.blendah.backend.controller;

import com.blendah.backend.dto.UserResponse;
import com.blendah.backend.security.AuthUser;
import com.blendah.backend.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** Endpoints for the logged-in user's own account. All require a token. */
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    public UserResponse getMyProfile(@AuthenticationPrincipal AuthUser me) {
        return userService.getProfile(me.userId());
    }
}