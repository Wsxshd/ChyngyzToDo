package com.example.tasktracker.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CommentRequest(
        @NotBlank(message = "text must not be blank")
        @Size(max = 1000, message = "text must be at most 1000 characters")
        String text,

        @NotNull(message = "authorId is required")
        Long authorId
) {
}
