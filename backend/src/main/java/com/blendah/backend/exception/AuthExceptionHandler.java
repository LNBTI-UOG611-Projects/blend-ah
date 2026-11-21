package main.java.com.blendah.backend.exception;

import com.blendah.backend.controller.AuthController;
import com.blendah.backend.controller.UserController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

/**
 * Error format for Member 1 endpoints only: {"error": "..."}.
 * Limited to AuthController and UserController so other members' error
 * responses are not changed.
 */
@RestControllerAdvice(assignableTypes = {AuthController.class, UserController.class})
public class AuthExceptionHandler {

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<Map<String, String>> handleApi(ApiException e) {
        return ResponseEntity.status(e.getStatus()).body(Map.of("error", e.getMessage()));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, String>> handleBadBody(HttpMessageNotReadableException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "Invalid request body"));
    }
}