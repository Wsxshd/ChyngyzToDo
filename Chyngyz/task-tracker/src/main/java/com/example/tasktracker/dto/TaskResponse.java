package com.example.tasktracker.dto;

import com.example.tasktracker.model.Label;
import com.example.tasktracker.model.Priority;
import com.example.tasktracker.model.Task;
import com.example.tasktracker.model.TaskStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record TaskResponse(
        Long id,
        String title,
        String description,
        TaskStatus status,
        Priority priority,
        LocalDate dueDate,
        LocalDateTime createdAt,
        Long projectId,
        String projectName,
        Long assigneeId,
        String assigneeName,
        List<String> labels
) {

    public static TaskResponse from(Task t) {
        return new TaskResponse(
                t.getId(),
                t.getTitle(),
                t.getDescription(),
                t.getStatus(),
                t.getPriority(),
                t.getDueDate(),
                t.getCreatedAt(),
                t.getProject().getId(),
                t.getProject().getName(),
                t.getAssignee() == null ? null : t.getAssignee().getId(),
                t.getAssignee() == null ? null : t.getAssignee().getName(),
                t.getLabels().stream().map(Label::getName).sorted().toList());
    }
}
