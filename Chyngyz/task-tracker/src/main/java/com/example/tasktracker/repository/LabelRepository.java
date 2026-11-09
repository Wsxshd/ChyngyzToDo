package com.example.tasktracker.repository;

import com.example.tasktracker.model.Label;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LabelRepository extends JpaRepository<Label, Long> {
}
