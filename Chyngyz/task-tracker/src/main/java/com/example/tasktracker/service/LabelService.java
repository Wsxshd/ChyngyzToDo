package com.example.tasktracker.service;

import com.example.tasktracker.dto.LabelRequest;
import com.example.tasktracker.dto.LabelResponse;
import com.example.tasktracker.exception.NotFoundException;
import com.example.tasktracker.model.Label;
import com.example.tasktracker.repository.LabelRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class LabelService {

    private final LabelRepository labelRepository;

    public LabelService(LabelRepository labelRepository) {
        this.labelRepository = labelRepository;
    }

    public List<LabelResponse> findAll() {
        return labelRepository.findAll(Sort.by("id")).stream().map(LabelResponse::from).toList();
    }

    @Transactional
    public LabelResponse create(LabelRequest request) {
        Label label = new Label();
        label.setName(request.name().trim().toLowerCase());
        return LabelResponse.from(labelRepository.saveAndFlush(label));
    }

    @Transactional
    public LabelResponse update(Long id, LabelRequest request) {
        Label label = getEntity(id);
        label.setName(request.name().trim().toLowerCase());
        return LabelResponse.from(labelRepository.saveAndFlush(label));
    }

    @Transactional
    public void delete(Long id) {
        labelRepository.delete(getEntity(id));
        labelRepository.flush();
    }

    private Label getEntity(Long id) {
        return labelRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Label not found: " + id));
    }
}
