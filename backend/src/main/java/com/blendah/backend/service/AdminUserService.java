
package com.blendah.backend.service;

import com.blendah.backend.dto.AdminUserResponse;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.time.ZoneOffset;
import java.util.List;
import java.util.UUID;

@Service
public class AdminUserService {

    private final JdbcTemplate jdbcTemplate;

    public AdminUserService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static final String USER_COLUMNS = """
        SELECT
            user_id,
            full_name,
            email,
            role,
            profile_image_url,
            created_at,
            user_status
        FROM public.users
        """;

    private final org.springframework.jdbc.core.RowMapper<AdminUserResponse>
        userMapper = (rs, rowNum) -> {

        var createdAt = rs.getTimestamp("created_at");

        return new AdminUserResponse(
            rs.getObject("user_id", UUID.class),
            rs.getString("full_name"),
            rs.getString("email"),
            rs.getString("role"),
            rs.getString("profile_image_url"),
            createdAt == null
                ? null
                : createdAt.toInstant().atOffset(ZoneOffset.UTC),
            rs.getBoolean("user_status")
        );
    };

    public List<AdminUserResponse> getAllUsers() {
        return jdbcTemplate.query(
            USER_COLUMNS + " ORDER BY created_at DESC NULLS LAST",
            userMapper
        );
    }

    public AdminUserResponse getUserById(UUID userId) {
        List<AdminUserResponse> users = jdbcTemplate.query(
            USER_COLUMNS + " WHERE user_id = ?",
            userMapper,
            userId
        );

        if (users.isEmpty()) {
            throw new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "User not found"
            );
        }

        return users.get(0);
    }
}
