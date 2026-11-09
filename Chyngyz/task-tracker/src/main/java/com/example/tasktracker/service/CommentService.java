package com.example.tasktracker.service;

import com.example.tasktracker.dto.CommentRequest;
import com.example.tasktracker.dto.CommentResponse;
import com.example.tasktracker.exception.NotFoundException;
import com.example.tasktracker.model.Comment;
import com.example.tasktracker.model.Task;
import com.example.tasktracker.model.User;
import com.example.tasktracker.repository.CommentRepository;
import com.example.tasktracker.repository.TaskRepository;
import com.example.tasktracker.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class CommentService {

    private final CommentRepository commentRepository;
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    public CommentService(CommentRepository commentRepository,
                          TaskRepository taskRepository,
                          UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    public List<CommentResponse> findByTask(Long taskId) {
        if (!taskRepository.existsById(taskId)) {
            throw new NotFoundException("Task not found: " + taskId);
        }
        return commentRepository.findByTaskIdOrderByCreatedAtAsc(taskId)
                .stream()
                .map(CommentResponse::from)
                .toList();
    }

    @Transactional
    public CommentResponse create(Long taskId, CommentRequest request) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new NotFoundException("Task not found: " + taskId));
        User author = userRepository.findById(request.authorId())
                .orElseThrow(() -> new NotFoundException("User not found: " + request.authorId()));

        Comment comment = new Comment();
        comment.setText(request.text().trim());
        comment.setTask(task);
        comment.setAuthor(author);
        return CommentResponse.from(commentRepository.saveAndFlush(comment));
    }
}
