package com.blendah.backend.dto;


/** Same JSON as before: {"email": "...", "password": "..."} */
public record LoginRequest(String email, String password) {}