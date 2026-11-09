package com.example.tasktracker.controller;

import com.example.tasktracker.dto.CommentRequest;
import com.example.tasktracker.dto.CommentResponse;
import com.example.tasktracker.dto.TaskRequest;
import com.example.tasktracker.dto.TaskResponse;
import com.example.tasktracker.model.Priority;
import com.example.tasktracker.model.TaskStatus;
import com.example.tasktracker.service.CommentService;
import com.example.tasktracker.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;
    private final CommentService commentService;

    public TaskController(TaskService taskService, CommentService commentService) {
        this.taskService = taskService;
        this.commentService = commentService;
    }

    // "" and "/" are both mapped, so /api/tasks and /api/tasks/ work in the browser

    @GetMapping({"", "/"})
    public List<TaskResponse> getAll() {
        return taskService.findAll();
    }

    @GetMapping("/search")
    public List<TaskResponse> search(@RequestParam(required = false) String q,
                                     @RequestParam(required = false) TaskStatus status,
                                     @RequestParam(required = false) Priority priority,
                                     @RequestParam(required = false) Long projectId,
                                     @RequestParam(required = false) Long assigneeId) {
        return taskService.search(q, status, priority, projectId, assigneeId);
    }

    @GetMapping("/{id}")
    public TaskResponse getById(@PathVariable Long id) {
        return taskService.findById(id);
    }

    @PostMapping({"", "/"})
    @ResponseStatus(HttpStatus.CREATED)
    public TaskResponse create(@Valid @RequestBody TaskRequest request) {
        return taskService.create(request);
    }

    @PutMapping("/{id}")
    public TaskResponse update(@PathVariable Long id, @Valid @RequestBody TaskRequest request) {
        return taskService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        taskService.delete(id);
    }

    // --- comments of a task ---

    @GetMapping("/{id}/comments")
    public List<CommentResponse> getComments(@PathVariable Long id) {
        return commentService.findByTask(id);
    }

    @PostMapping("/{id}/comments")
    @ResponseStatus(HttpStatus.CREATED)
    public CommentResponse addComment(@PathVariable Long id, @Valid @RequestBody CommentRequest request) {
        return commentService.create(id, request);
    }
}
