package com.example.tasktracker.exception;

import java.time.LocalDateTime;
import java.util.Map;

/** Unified JSON body for all error responses. */
public record ApiError(
        LocalDateTime timestamp,
        int status,
        String message,
        Map<String, String> errors
) {

    public static ApiError of(int status, String message) {
        return new ApiError(LocalDateTime.now(), status, message, Map.of());
    }

    public static ApiError of(int status, String message, Map<String, String> errors) {
        return new ApiError(LocalDateTime.now(), status, message, errors);
    }
}
