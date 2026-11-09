package com.example.tasktracker.service;

import com.example.tasktracker.dto.ProjectRequest;
import com.example.tasktracker.dto.ProjectResponse;
import com.example.tasktracker.exception.NotFoundException;
import com.example.tasktracker.model.Project;
import com.example.tasktracker.model.User;
import com.example.tasktracker.repository.ProjectRepository;
import com.example.tasktracker.repository.UserRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    public ProjectService(ProjectRepository projectRepository, UserRepository userRepository) {
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
    }

    public List<ProjectResponse> findAll() {
        return projectRepository.findAll(Sort.by("id")).stream().map(ProjectResponse::from).toList();
    }

    public ProjectResponse findById(Long id) {
        return ProjectResponse.from(getEntity(id));
    }

    /** Used by other services/controllers to check that a project exists. */
    public void requireExists(Long id) {
        if (!projectRepository.existsById(id)) {
            throw new NotFoundException("Project not found: " + id);
        }
    }

    @Transactional
    public ProjectResponse create(ProjectRequest request) {
        Project project = new Project();
        apply(project, request);
        return ProjectResponse.from(projectRepository.saveAndFlush(project));
    }

    @Transactional
    public ProjectResponse update(Long id, ProjectRequest request) {
        Project project = getEntity(id);
        apply(project, request);
        return ProjectResponse.from(projectRepository.saveAndFlush(project));
    }

    @Transactional
    public void delete(Long id) {
        projectRepository.delete(getEntity(id));
        projectRepository.flush();
    }

    private Project getEntity(Long id) {
        return projectRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Project not found: " + id));
    }

    private void apply(Project project, ProjectRequest request) {
        User owner = userRepository.findById(request.ownerId())
                .orElseThrow(() -> new NotFoundException("User not found: " + request.ownerId()));
        project.setName(request.name().trim());
        project.setDescription(request.description());
        project.setOwner(owner);
    }
}
