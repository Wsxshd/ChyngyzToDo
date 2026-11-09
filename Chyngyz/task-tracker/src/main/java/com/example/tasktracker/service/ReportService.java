package com.example.tasktracker.service;

import com.example.tasktracker.dto.LabelReport;
import com.example.tasktracker.dto.ProjectReport;
import com.example.tasktracker.dto.UserReport;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Reports are written as plain SQL (JOIN + GROUP BY + aggregate functions)
 * and executed through JdbcTemplate, so the SQL is easy to show to the teacher.
 */
@Service
public class ReportService {

    private final JdbcTemplate jdbc;

    public ReportService(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    /** Tasks per project: JOIN projects + users + tasks, COUNT and SUM. */
    public List<ProjectReport> tasksPerProject() {
        String sql = """
                SELECT p.id,
                       p.name,
                       u.name AS owner_name,
                       COUNT(t.id) AS total_tasks,
                       COALESCE(SUM(CASE WHEN t.status = 'DONE' THEN 1 ELSE 0 END), 0) AS done_tasks
                FROM projects p
                JOIN users u ON u.id = p.owner_id
                LEFT JOIN tasks t ON t.project_id = p.id
                GROUP BY p.id, p.name, u.name
                ORDER BY p.id
                """;
        return jdbc.query(sql, (rs, i) -> {
            long total = rs.getLong("total_tasks");
            long done = rs.getLong("done_tasks");
            double percent = total == 0 ? 0.0 : Math.round(done * 1000.0 / total) / 10.0;
            return new ProjectReport(
                    rs.getLong("id"),
                    rs.getString("name"),
                    rs.getString("owner_name"),
                    total,
                    done,
                    percent);
        });
    }

    /** Workload per user: LEFT JOIN users + tasks, COUNT and conditional SUM (overdue tasks). */
    public List<UserReport> workloadPerUser() {
        String sql = """
                SELECT u.id,
                       u.name,
                       COUNT(t.id) AS total_tasks,
                       COALESCE(SUM(CASE WHEN t.status <> 'DONE' AND t.due_date < CURRENT_DATE
                                         THEN 1 ELSE 0 END), 0) AS overdue_tasks
                FROM users u
                LEFT JOIN tasks t ON t.assignee_id = u.id
                GROUP BY u.id, u.name
                ORDER BY total_tasks DESC, u.id
                """;
        return jdbc.query(sql, (rs, i) -> new UserReport(
                rs.getLong("id"),
                rs.getString("name"),
                rs.getLong("total_tasks"),
                rs.getLong("overdue_tasks")));
    }

    /** Tasks per label: goes through the many-to-many table task_labels. */
    public List<LabelReport> tasksPerLabel() {
        String sql = """
                SELECT l.id,
                       l.name,
                       COUNT(tl.task_id) AS task_count
                FROM labels l
                LEFT JOIN task_labels tl ON tl.label_id = l.id
                GROUP BY l.id, l.name
                ORDER BY task_count DESC, l.name
                """;
        return jdbc.query(sql, (rs, i) -> new LabelReport(
                rs.getLong("id"),
                rs.getString("name"),
                rs.getLong("task_count")));
    }
}
