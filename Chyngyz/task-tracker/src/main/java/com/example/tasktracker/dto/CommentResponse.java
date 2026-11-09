package com.example.tasktracker.dto;

import com.example.tasktracker.model.Comment;

import java.time.LocalDateTime;

public record CommentResponse(Long id, String text, LocalDateTime createdAt, Long authorId, String authorName) {

    public static CommentResponse from(Comment c) {
        return new CommentResponse(
                c.getId(),
                c.getText(),
                c.getCreatedAt(),
                c.getAuthor().getId(),
                c.getAuthor().getName());
    }
}
