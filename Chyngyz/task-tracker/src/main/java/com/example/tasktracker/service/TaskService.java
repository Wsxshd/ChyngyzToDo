package com.example.tasktracker.service;

import com.example.tasktracker.dto.TaskRequest;
import com.example.tasktracker.dto.TaskResponse;
import com.example.tasktracker.exception.NotFoundException;
import com.example.tasktracker.model.Label;
import com.example.tasktracker.model.Priority;
import com.example.tasktracker.model.Project;
import com.example.tasktracker.model.Task;
import com.example.tasktracker.model.TaskStatus;
import com.example.tasktracker.model.User;
import com.example.tasktracker.repository.LabelRepository;
import com.example.tasktracker.repository.ProjectRepository;
import com.example.tasktracker.repository.TaskRepository;
import com.example.tasktracker.repository.TaskSpecifications;
import com.example.tasktracker.repository.UserRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Transactional(readOnly = true)
public class TaskService {

    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final LabelRepository labelRepository;

    public TaskService(TaskRepository taskRepository,
                       ProjectRepository projectRepository,
                       UserRepository userRepository,
                       LabelRepository labelRepository) {
        this.taskRepository = taskRepository;
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
        this.labelRepository = labelRepository;
    }

    public List<TaskResponse> findAll() {
        return taskRepository.findAll(Sort.by("id")).stream().map(TaskResponse::from).toList();
    }

    public TaskResponse findById(Long id) {
        return TaskResponse.from(getEntity(id));
    }

    /** Search by text (title/description) and optional filters. All parameters are optional. */
    public List<TaskResponse> search(String q,
                                     TaskStatus status,
                                     Priority priority,
                                     Long projectId,
                                     Long assigneeId) {
        return taskRepository
                .findAll(TaskSpecifications.search(q, status, priority, projectId, assigneeId), Sort.by("id"))
                .stream()
                .map(TaskResponse::from)
                .toList();
    }

    @Transactional
    public TaskResponse create(TaskRequest request) {
        Task task = new Task();
        apply(task, request);
        return TaskResponse.from(taskRepository.saveAndFlush(task));
    }

    @Transactional
    public TaskResponse update(Long id, TaskRequest request) {
        Task task = getEntity(id);
        apply(task, request);
        return TaskResponse.from(taskRepository.saveAndFlush(task));
    }

    @Transactional
    public void delete(Long id) {
        taskRepository.delete(getEntity(id));
        taskRepository.flush();
    }

    private Task getEntity(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Task not found: " + id));
    }

    private void apply(Task task, TaskRequest request) {
        Project project = projectRepository.findById(request.projectId())
                .orElseThrow(() -> new NotFoundException("Project not found: " + request.projectId()));

        User assignee = null;
        if (request.assigneeId() != null) {
            assignee = userRepository.findById(request.assigneeId())
                    .orElseThrow(() -> new NotFoundException("User not found: " + request.assigneeId()));
        }

        Set<Label> labels = new HashSet<>();
        if (request.labelIds() != null && !request.labelIds().isEmpty()) {
            List<Label> found = labelRepository.findAllById(request.labelIds());
            if (found.size() != request.labelIds().size()) {
                throw new NotFoundException("One or more labels were not found: " + request.labelIds());
            }
            labels.addAll(found);
        }

        task.setTitle(request.title().trim());
        task.setDescription(request.description());
        task.setStatus(request.status());
        task.setPriority(request.priority());
        task.setDueDate(request.dueDate());
        task.setProject(project);
        task.setAssignee(assignee);
        task.setLabels(labels);
    }
}
