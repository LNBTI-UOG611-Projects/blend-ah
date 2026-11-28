package com.blendah.backend.security;

import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Keeps track of access tokens that were logged out before they expired.
 *
 * Stored in memory: a backend restart clears it. That is acceptable because
 * access tokens are short-lived; a database-backed refresh-token table can
 * replace this later without changing the controllers.
 */
@Component
public class TokenBlocklist {

    // jti -> when that token would have expired anyway
    private final Map<String, Instant> revokedTokens = new ConcurrentHashMap<>();

    // userId -> every token issued before this moment is invalid (used after a password change)
    private final Map<UUID, Instant> revokedBefore = new ConcurrentHashMap<>();

    /** Logout: invalidate one token. */
    public void revokeToken(String tokenId, Instant expiresAt) {
        removeExpired();
        if (tokenId != null && expiresAt != null) {
            revokedTokens.put(tokenId, expiresAt);
        }
    }

    /** Invalidate every token the user currently has (all devices). */
    public void revokeAllTokensForUser(UUID userId) {
        // JWT "issued at" has second precision, so compare at second precision too
        revokedBefore.put(userId, Instant.now().truncatedTo(ChronoUnit.SECONDS));
    }

    public boolean isRevoked(String tokenId, UUID userId, Instant issuedAt) {
        if (tokenId != null && revokedTokens.containsKey(tokenId)) {
            return true;
        }
        Instant cutoff = revokedBefore.get(userId);
        return cutoff != null && issuedAt != null && issuedAt.isBefore(cutoff);
    }

    private void removeExpired() {
        Instant now = Instant.now();
        revokedTokens.entrySet().removeIf(e -> e.getValue().isBefore(now));
    }
}