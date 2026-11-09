package com.example.tasktracker.controller;

import com.example.tasktracker.dto.LabelReport;
import com.example.tasktracker.dto.ProjectReport;
import com.example.tasktracker.dto.UserReport;
import com.example.tasktracker.service.ReportService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/projects")
    public List<ProjectReport> projects() {
        return reportService.tasksPerProject();
    }

    @GetMapping("/users")
    public List<UserReport> users() {
        return reportService.workloadPerUser();
    }

    @GetMapping("/labels")
    public List<LabelReport> labels() {
        return reportService.tasksPerLabel();
    }
}
