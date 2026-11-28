package com.blendah.backend.service;

import com.blendah.backend.dto.AuthResponse;
import com.blendah.backend.dto.LoginRequest;
import com.blendah.backend.dto.RegisterRequest;
import com.blendah.backend.exception.ApiException;
import com.blendah.backend.model.User;
import com.blendah.backend.repository.UserRepository;
import com.blendah.backend.security.AuthUser;
import com.blendah.backend.security.JwtService;
import com.blendah.backend.security.TokenBlocklist;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.regex.Pattern;

@Service
public class AuthService {

    private static final Logger log = LoggerFactory.getLogger(AuthService.class);
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$");

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final TokenBlocklist tokenBlocklist;

    // Used when the email doesn't exist, so a wrong email takes as long as a wrong password
    private final String dummyHash;

    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       JwtService jwtService,
                       TokenBlocklist tokenBlocklist) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.tokenBlocklist = tokenBlocklist;
        this.dummyHash = passwordEncoder.encode("blendah-timing-placeholder");
    }

    public User register(RegisterRequest request) {
        String fullName = clean(request.fullName());
        String email = clean(request.email()).toLowerCase();
        String password = request.password() == null ? "" : request.password();

        if (fullName.isEmpty() || email.isEmpty() || password.isEmpty()) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Full name, email and password are required");
        }
        if (fullName.length() > 255) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Full name is too long");
        }
        if (email.length() > 255 || !EMAIL_PATTERN.matcher(email).matches()) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Please enter a valid email address");
        }
        validateNewPassword(password);
        if (userRepository.existsByEmailIgnoreCase(email)) {
            throw new ApiException(HttpStatus.CONFLICT, "Email is already in use");
        }

        User user = new User();
        user.setFullName(fullName);
        user.setEmail(email);
        user.setPasswordHash(passwordEncoder.encode(password));
        user.setRole(User.UserRole.NORMAL_USER);   // public sign-up can never create an ADMIN

        try {
            return userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            String reason = e.getMostSpecificCause().getMessage();
            log.warn("Register failed: {}", reason);
            if (reason != null && reason.contains("users_email_key")) {
                throw new ApiException(HttpStatus.CONFLICT, "Email is already in use");
            }
            throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "Could not create account. Please try again.");
        }
    }

    public AuthResponse login(LoginRequest request) {
        String email = clean(request.email()).toLowerCase();
        String password = request.password() == null ? "" : request.password();

        if (email.isEmpty() || password.isEmpty()) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Email and password are required");
        }

        Optional<User> userOpt = userRepository.findByEmailIgnoreCase(email);
        if (userOpt.isEmpty()) {
            passwordEncoder.matches(password, dummyHash);     // keep timing similar
            throw new ApiException(HttpStatus.UNAUTHORIZED, "Invalid email or password");
        }

        User user = userOpt.get();
        if (!passwordEncoder.matches(password, user.getPasswordHash())) {
            throw new ApiException(HttpStatus.UNAUTHORIZED, "Invalid email or password");
        }

        // Only revealed after the correct password, so it can't be used to probe emails
        if (!Boolean.TRUE.equals(user.getUserStatus())) {
            throw new ApiException(HttpStatus.FORBIDDEN, "Your account has been deactivated. Please contact support.");
        }

        return new AuthResponse(
            "Login successful",
            user.getUserId(),
            user.getFullName(),
            user.getEmail(),
            user.getRole().name(),
            jwtService.createAccessToken(user),
            "Bearer",
            jwtService.getAccessTokenSeconds()
        );
    }

    /** Invalidates the token used for this request. */
    public void logout(AuthUser me) {
        tokenBlocklist.revokeToken(me.tokenId(), me.tokenExpiresAt());
    }

    /** Shared by registration and (later) change password. */
    public static void validateNewPassword(String password) {
        if (password.length() < 8) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Password must be at least 8 characters");
        }
        if (password.length() > 72) { // BCrypt limit
            throw new ApiException(HttpStatus.BAD_REQUEST, "Password must be 72 characters or less");
        }
    }

    private static String clean(String value) {
        return value == null ? "" : value.trim();
    }
}