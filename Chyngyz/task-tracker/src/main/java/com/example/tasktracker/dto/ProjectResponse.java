package com.example.tasktracker.dto;

import com.example.tasktracker.model.Project;

public record ProjectResponse(Long id, String name, String description, Long ownerId, String ownerName) {

    public static ProjectResponse from(Project p) {
        return new ProjectResponse(
                p.getId(),
                p.getName(),
                p.getDescription(),
                p.getOwner().getId(),
                p.getOwner().getName());
    }
}
