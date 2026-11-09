-- Seed data. ON CONFLICT DO NOTHING keeps the script idempotent (safe on every start).

INSERT INTO users (id, name, email) VALUES
    (1, 'Aidar Zhumabekov', 'aidar@example.com'),
    (2, 'Aigerim Sadykova', 'aigerim@example.com'),
    (3, 'Daniyar Omurov',   'daniyar@example.com'),
    (4, 'Elena Petrova',    'elena@example.com')
ON CONFLICT (id) DO NOTHING;

INSERT INTO projects (id, name, description, owner_id) VALUES
    (1, 'Website Redesign', 'New landing page and company website', 1),
    (2, 'Mobile App',       'Android and iOS client for customers', 2),
    (3, 'Course Project',   'University course work: task tracker backend', 3)
ON CONFLICT (id) DO NOTHING;

INSERT INTO labels (id, name) VALUES
    (1, 'backend'),
    (2, 'frontend'),
    (3, 'bug'),
    (4, 'urgent'),
    (5, 'documentation')
ON CONFLICT (id) DO NOTHING;

INSERT INTO tasks (id, title, description, status, priority, due_date, project_id, assignee_id) VALUES
    (1,  'Design main page mockup',     'Prepare Figma mockup of the landing page',        'DONE',        'HIGH',   CURRENT_DATE - 10, 1, 4),
    (2,  'Implement responsive layout', 'HTML/CSS for desktop, tablet and phone',          'IN_PROGRESS', 'HIGH',   CURRENT_DATE + 3,  1, 1),
    (3,  'Fix broken contact form',     'Form does not send emails in production',         'TODO',        'HIGH',   CURRENT_DATE - 2,  1, 2),
    (4,  'Write SEO texts',             'Texts for the main sections of the website',      'TODO',        'LOW',    CURRENT_DATE + 14, 1, NULL),
    (5,  'Set up REST API',             'Authentication and basic endpoints',              'IN_PROGRESS', 'HIGH',   CURRENT_DATE + 5,  2, 2),
    (6,  'Push notifications',          'Integrate push notifications for both platforms', 'TODO',        'MEDIUM', CURRENT_DATE + 20, 2, 3),
    (7,  'Crash on startup (Android 9)','App crashes on old Android versions',             'IN_PROGRESS', 'HIGH',   CURRENT_DATE - 1,  2, 3),
    (8,  'Design database schema',      'ER diagram, 3NF, constraints',                    'DONE',        'HIGH',   CURRENT_DATE - 7,  3, 3),
    (9,  'Implement CRUD endpoints',    'Tasks, projects and users',                       'IN_PROGRESS', 'MEDIUM', CURRENT_DATE + 2,  3, 3),
    (10, 'Write README and report',     'Documentation for the teacher',                   'TODO',        'MEDIUM', CURRENT_DATE + 6,  3, 1)
ON CONFLICT (id) DO NOTHING;

INSERT INTO task_labels (task_id, label_id) VALUES
    (1, 2), (2, 2), (3, 1), (3, 3), (3, 4), (4, 5),
    (5, 1), (6, 1), (7, 3), (7, 4), (8, 1), (8, 5),
    (9, 1), (10, 5)
ON CONFLICT DO NOTHING;

INSERT INTO comments (id, text, task_id, author_id) VALUES
    (1, 'Mockup approved by the client.',               1, 1),
    (2, 'Please check the SMTP settings first.',        3, 1),
    (3, 'Reproduced on Android 9, working on a fix.',   7, 3),
    (4, 'ER diagram is ready, see docs folder.',        8, 3)
ON CONFLICT (id) DO NOTHING;

-- Move sequences forward so that new rows created through the API get fresh ids.
SELECT setval(pg_get_serial_sequence('users',    'id'), (SELECT MAX(id) FROM users));
SELECT setval(pg_get_serial_sequence('projects', 'id'), (SELECT MAX(id) FROM projects));
SELECT setval(pg_get_serial_sequence('labels',   'id'), (SELECT MAX(id) FROM labels));
SELECT setval(pg_get_serial_sequence('tasks',    'id'), (SELECT MAX(id) FROM tasks));
SELECT setval(pg_get_serial_sequence('comments', 'id'), (SELECT MAX(id) FROM comments));
