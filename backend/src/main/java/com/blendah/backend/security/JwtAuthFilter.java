package com.blendah.backend.security;

import com.blendah.backend.model.User;
import com.blendah.backend.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Reads "Authorization: Bearer <token>" on every request.
 * If the token is valid, the user is loaded from the database so that
 * deactivation and role changes take effect immediately.
 *
 * Not a @Component on purpose: SecurityConfig creates it, so it only runs
 * inside the security filter chain (not twice).
 */
public class JwtAuthFilter extends OncePerRequestFilter {

    /** Request attribute read by the 401 handler to give a clearer message. */
    public static final String AUTH_ERROR_ATTRIBUTE = "blendah.authError";

    private static final String SESSION_EXPIRED = "Your session has expired. Please log in again.";

    private final JwtService jwtService;
    private final TokenBlocklist tokenBlocklist;
    private final UserRepository userRepository;

    public JwtAuthFilter(JwtService jwtService, TokenBlocklist tokenBlocklist, UserRepository userRepository) {
        this.jwtService = jwtService;
        this.tokenBlocklist = tokenBlocklist;
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {

        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (header == null || !header.startsWith("Bearer ")) {
            chain.doFilter(request, response);           // no token: public endpoints still work
            return;
        }

        String token = header.substring(7).trim();
        Claims claims;
        UUID userId;
        try {
            claims = jwtService.parse(token);
            userId = UUID.fromString(claims.getSubject());
        } catch (JwtException | IllegalArgumentException e) {
            request.setAttribute(AUTH_ERROR_ATTRIBUTE, SESSION_EXPIRED);
            chain.doFilter(request, response);
            return;
        }

        if (tokenBlocklist.isRevoked(claims.getId(), userId, claims.getIssuedAt().toInstant())) {
            request.setAttribute(AUTH_ERROR_ATTRIBUTE, SESSION_EXPIRED);
            chain.doFilter(request, response);
            return;
        }

        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            request.setAttribute(AUTH_ERROR_ATTRIBUTE, SESSION_EXPIRED);
            chain.doFilter(request, response);
            return;
        }

        User user = userOpt.get();
        if (!Boolean.TRUE.equals(user.getUserStatus())) {
            SecurityErrorWriter.write(response, HttpServletResponse.SC_FORBIDDEN,
                    "Your account has been deactivated. Please contact support.");
            return;
        }

        AuthUser principal = new AuthUser(
                user.getUserId(),
                user.getEmail(),
                user.getFullName(),
                user.getRole(),
                claims.getId(),
                claims.getExpiration().toInstant());

        // Spring's hasRole("ADMIN") checks for the authority "ROLE_ADMIN"
        var authorities = List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()));
        var authentication = UsernamePasswordAuthenticationToken.authenticated(principal, null, authorities);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        chain.doFilter(request, response);
    }
}