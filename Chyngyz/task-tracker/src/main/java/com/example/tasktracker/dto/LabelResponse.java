package com.example.tasktracker.dto;

import com.example.tasktracker.model.Label;

public record LabelResponse(Long id, String name) {

    public static LabelResponse from(Label l) {
        return new LabelResponse(l.getId(), l.getName());
    }
}
