package com.blendah.backend.controller;

import com.blendah.backend.model.User;
import com.blendah.backend.repository.UserRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$");

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public AuthController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String, String> payload) {
        String fullName = clean(payload.get("fullName"));
        String email = clean(payload.get("email")).toLowerCase();
        String password = payload.get("password") == null ? "" : payload.get("password");

        if (fullName.isEmpty() || email.isEmpty() || password.isEmpty()) {
            return error(HttpStatus.BAD_REQUEST, "Full name, email and password are required");
        }
        if (fullName.length() > 255) {
            return error(HttpStatus.BAD_REQUEST, "Full name is too long");
        }
        if (email.length() > 255 || !EMAIL_PATTERN.matcher(email).matches()) {
            return error(HttpStatus.BAD_REQUEST, "Please enter a valid email address");
        }
        if (password.length() < 8) {
            return error(HttpStatus.BAD_REQUEST, "Password must be at least 8 characters");
        }
        if (password.length() > 72) { // BCrypt limit
            return error(HttpStatus.BAD_REQUEST, "Password must be 72 characters or less");
        }
        if (userRepository.existsByEmailIgnoreCase(email)) {
            return error(HttpStatus.CONFLICT, "Email is already in use");
        }

        User user = new User();
        user.setFullName(fullName);
        user.setEmail(email);
        user.setPasswordHash(passwordEncoder.encode(password));
        user.setRole(User.UserRole.NORMAL_USER);

        User saved;
        try {
            saved = userRepository.save(user);
                } catch (DataIntegrityViolationException e) {
            String reason = e.getMostSpecificCause().getMessage();
            System.err.println("Register failed: " + reason);
            if (reason != null && reason.contains("users_email_key")) {
                return error(HttpStatus.CONFLICT, "Email is already in use");
            }
            return error(HttpStatus.INTERNAL_SERVER_ERROR, "Could not create account. Please try again.");
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
            "message", "User registered successfully",
            "userId", saved.getUserId()
        ));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> payload) {
        String email = clean(payload.get("email")).toLowerCase();
        String password = payload.get("password") == null ? "" : payload.get("password");

        if (email.isEmpty() || password.isEmpty()) {
            return error(HttpStatus.BAD_REQUEST, "Email and password are required");
        }

        Optional<User> userOpt = userRepository.findByEmailIgnoreCase(email);
        if (userOpt.isPresent() && passwordEncoder.matches(password, userOpt.get().getPasswordHash())) {
            User user = userOpt.get();
            return ResponseEntity.ok(Map.of(
                "message", "Login successful",
                "userId", user.getUserId(),
                "fullName", user.getFullName(),
                "email", user.getEmail(),
                "role", user.getRole().name()
            ));
        }
        return error(HttpStatus.UNAUTHORIZED, "Invalid email or password");
    }

    private static String clean(String value) {
        return value == null ? "" : value.trim();
    }

    private static ResponseEntity<Map<String, String>> error(HttpStatus status, String message) {
        return ResponseEntity.status(status).body(Map.of("error", message));
    }
}
