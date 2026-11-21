package main.java.com.blendah.backend.dto;

/** Same JSON as before: {"fullName": "...", "email": "...", "password": "..."} */
public record RegisterRequest(String fullName, String email, String password) {}