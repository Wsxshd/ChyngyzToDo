package com.example.tasktracker.dto;

/** One row of the "tasks per project" report. */
public record ProjectReport(
        Long projectId,
        String projectName,
        String ownerName,
        long totalTasks,
        long doneTasks,
        double completionPercent
) {
}
