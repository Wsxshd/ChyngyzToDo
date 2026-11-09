package com.example.tasktracker.dto;

import com.example.tasktracker.model.Priority;
import com.example.tasktracker.model.TaskStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.Set;

public record TaskRequest(
        @NotBlank(message = "title must not be blank")
        @Size(max = 200, message = "title must be at most 200 characters")
        String title,

        @Size(max = 2000, message = "description must be at most 2000 characters")
        String description,

        @NotNull(message = "status is required (TODO, IN_PROGRESS, DONE)")
        TaskStatus status,

        @NotNull(message = "priority is required (LOW, MEDIUM, HIGH)")
        Priority priority,

        LocalDate dueDate,

        @NotNull(message = "projectId is required")
        Long projectId,

        Long assigneeId,

        Set<Long> labelIds
) {
}
