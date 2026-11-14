
package com.blendah.backend.service;

import com.blendah.backend.dto.AdminUserResponse;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import java.time.ZoneOffset;

import java.util.List;

@Service
public class AdminUserService {

    private final JdbcTemplate jdbcTemplate;

    public AdminUserService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<AdminUserResponse> getAllUsers() {

        String sql = """
            SELECT
                user_id,
                full_name,
                email,
                role,
                profile_image_url,
                created_at,
                user_status
            FROM public.users
            ORDER BY created_at DESC NULLS LAST
            """;

        return jdbcTemplate.query(sql, (rs, rowNum) ->
            new AdminUserResponse(
                rs.getObject("user_id", java.util.UUID.class),
                rs.getString("full_name"),
                rs.getString("email"),
                rs.getString("role"),
                rs.getString("profile_image_url"),
                rs.getTimestamp("created_at").toInstant().atOffset(ZoneOffset.UTC),
                rs.getBoolean("user_status")
            )
        );
    }
}
