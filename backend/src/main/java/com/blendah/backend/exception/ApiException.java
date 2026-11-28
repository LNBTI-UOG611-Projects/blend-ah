package com.blendah.backend.exception;

import org.springframework.http.HttpStatus;

/** Thrown by Member 1 services; turned into {"error": "..."} by AuthExceptionHandler. */
public class ApiException extends RuntimeException {

    private final HttpStatus status;

    public ApiException(HttpStatus status, String message) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}