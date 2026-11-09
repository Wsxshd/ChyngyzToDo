package com.example.tasktracker.service;

import com.example.tasktracker.dto.UserRequest;
import com.example.tasktracker.dto.UserResponse;
import com.example.tasktracker.exception.NotFoundException;
import com.example.tasktracker.model.User;
import com.example.tasktracker.repository.UserRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<UserResponse> findAll() {
        return userRepository.findAll(Sort.by("id")).stream().map(UserResponse::from).toList();
    }

    public UserResponse findById(Long id) {
        return UserResponse.from(getEntity(id));
    }

    @Transactional
    public UserResponse create(UserRequest request) {
        User user = new User();
        apply(user, request);
        return UserResponse.from(userRepository.saveAndFlush(user));
    }

    @Transactional
    public UserResponse update(Long id, UserRequest request) {
        User user = getEntity(id);
        apply(user, request);
        return UserResponse.from(userRepository.saveAndFlush(user));
    }

    @Transactional
    public void delete(Long id) {
        userRepository.delete(getEntity(id));
        userRepository.flush();
    }

    private User getEntity(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found: " + id));
    }

    private void apply(User user, UserRequest request) {
        user.setName(request.name().trim());
        user.setEmail(request.email().trim().toLowerCase());
    }
}
