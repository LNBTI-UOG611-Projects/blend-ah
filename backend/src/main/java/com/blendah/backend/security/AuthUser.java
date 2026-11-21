package main.java.com.blendah.backend.security;

import com.blendah.backend.model.User;

import java.time.Instant;
import java.util.UUID;

/**
 * The logged-in user for the current request.
 *
 * Any controller can get it with:
 *   public ResponseEntity<?> something(@AuthenticationPrincipal AuthUser me) { ... me.userId() ... }
 *
 * Use me.userId() instead of accepting a userId from the request body.
 */
public record AuthUser(
    UUID userId,
    String email,
    String fullName,
    User.UserRole role,
    String tokenId,          // "jti" of the access token (used by logout)
    Instant tokenExpiresAt
) {
    public boolean isAdmin() {
        return role == User.UserRole.ADMIN;
    }
}