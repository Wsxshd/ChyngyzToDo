package com.example.tasktracker.dto;

/** One row of the "workload per user" report. */
public record UserReport(
        Long userId,
        String userName,
        long totalTasks,
        long overdueTasks
) {
}
