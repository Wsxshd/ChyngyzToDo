package com.example.tasktracker.dto;

/** One row of the "tasks per label" report. */
public record LabelReport(Long labelId, String labelName, long taskCount) {
}
