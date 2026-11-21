package main.java.com.blendah.backend.security;

import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/** Writes {"error": "..."} - the same error format AuthController already uses. */
final class SecurityErrorWriter {

    private SecurityErrorWriter() {}

    static void write(HttpServletResponse response, int status, String message) throws IOException {
        response.setStatus(status);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        String safe = message.replace("\\", "\\\\").replace("\"", "\\\"");
        response.getWriter().write("{\"error\":\"" + safe + "\"}");
    }
}