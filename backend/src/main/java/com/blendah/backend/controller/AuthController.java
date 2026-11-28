package com.blendah.backend.controller;

import com.blendah.backend.dto.AuthResponse;
import com.blendah.backend.dto.LoginRequest;
import com.blendah.backend.dto.RegisterRequest;
import com.blendah.backend.model.User;
import com.blendah.backend.security.AuthUser;
import com.blendah.backend.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        User saved = authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
            "message", "User registered successfully",
            "userId", saved.getUserId()
        ));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@AuthenticationPrincipal AuthUser me) {
        authService.logout(me);
        return ResponseEntity.ok(Map.of("message", "Logged out successfully"));
    }
}