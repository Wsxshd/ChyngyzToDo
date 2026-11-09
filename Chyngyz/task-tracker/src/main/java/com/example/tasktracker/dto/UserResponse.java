package com.example.tasktracker.dto;

import com.example.tasktracker.model.User;

public record UserResponse(Long id, String name, String email) {

    public static UserResponse from(User u) {
        return new UserResponse(u.getId(), u.getName(), u.getEmail());
    }
}
