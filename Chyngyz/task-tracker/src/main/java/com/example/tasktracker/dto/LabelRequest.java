package com.example.tasktracker.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LabelRequest(
        @NotBlank(message = "name must not be blank")
        @Size(max = 50, message = "name must be at most 50 characters")
        String name
) {
}
