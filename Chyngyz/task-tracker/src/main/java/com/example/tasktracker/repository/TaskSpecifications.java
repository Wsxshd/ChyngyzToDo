package com.example.tasktracker.repository;

import com.example.tasktracker.model.Priority;
import com.example.tasktracker.model.Task;
import com.example.tasktracker.model.TaskStatus;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

/** Builds the dynamic WHERE clause for task search. Every filter is optional. */
public final class TaskSpecifications {

    private TaskSpecifications() {
    }

    public static Specification<Task> search(String q,
                                             TaskStatus status,
                                             Priority priority,
                                             Long projectId,
                                             Long assigneeId) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (q != null && !q.isBlank()) {
                String like = "%" + q.trim().toLowerCase() + "%";
                predicates.add(cb.or(
                        cb.like(cb.lower(root.<String>get("title")), like),
                        cb.like(cb.lower(root.<String>get("description")), like)));
            }
            if (status != null) {
                predicates.add(cb.equal(root.get("status"), status));
            }
            if (priority != null) {
                predicates.add(cb.equal(root.get("priority"), priority));
            }
            if (projectId != null) {
                predicates.add(cb.equal(root.get("project").get("id"), projectId));
            }
            if (assigneeId != null) {
                predicates.add(cb.equal(root.get("assignee").get("id"), assigneeId));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
