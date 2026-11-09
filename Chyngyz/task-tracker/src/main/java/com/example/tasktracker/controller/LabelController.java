package com.example.tasktracker.controller;

import com.example.tasktracker.dto.LabelRequest;
import com.example.tasktracker.dto.LabelResponse;
import com.example.tasktracker.service.LabelService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/labels")
public class LabelController {

    private final LabelService labelService;

    public LabelController(LabelService labelService) {
        this.labelService = labelService;
    }

    @GetMapping({"", "/"})
    public List<LabelResponse> getAll() {
        return labelService.findAll();
    }

    @PostMapping({"", "/"})
    @ResponseStatus(HttpStatus.CREATED)
    public LabelResponse create(@Valid @RequestBody LabelRequest request) {
        return labelService.create(request);
    }

    @PutMapping("/{id}")
    public LabelResponse update(@PathVariable Long id, @Valid @RequestBody LabelRequest request) {
        return labelService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        labelService.delete(id);
    }
}
