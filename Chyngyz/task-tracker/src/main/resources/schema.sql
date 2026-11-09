-- Task tracker schema (PostgreSQL), normalized to 3NF.
-- IF NOT EXISTS makes the script safe to run on every application start.

CREATE TABLE IF NOT EXISTS users (
    id    BIGSERIAL    PRIMARY KEY,
    name  VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS projects (
    id          BIGSERIAL    PRIMARY KEY,
    name        VARCHAR(150) NOT NULL,
    description VARCHAR(1000),
    owner_id    BIGINT       NOT NULL REFERENCES users (id)
);

CREATE TABLE IF NOT EXISTS labels (
    id   BIGSERIAL   PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS tasks (
    id          BIGSERIAL     PRIMARY KEY,
    title       VARCHAR(200)  NOT NULL,
    description VARCHAR(2000),
    status      VARCHAR(20)   NOT NULL DEFAULT 'TODO'
                CHECK (status IN ('TODO', 'IN_PROGRESS', 'DONE')),
    priority    VARCHAR(20)   NOT NULL DEFAULT 'MEDIUM'
                CHECK (priority IN ('LOW', 'MEDIUM', 'HIGH')),
    due_date    DATE,
    created_at  TIMESTAMP     NOT NULL DEFAULT NOW(),
    project_id  BIGINT        NOT NULL REFERENCES projects (id) ON DELETE CASCADE,
    assignee_id BIGINT        REFERENCES users (id) ON DELETE SET NULL
);

-- many-to-many: a task can have several labels, a label belongs to many tasks
CREATE TABLE IF NOT EXISTS task_labels (
    task_id  BIGINT NOT NULL REFERENCES tasks (id)  ON DELETE CASCADE,
    label_id BIGINT NOT NULL REFERENCES labels (id) ON DELETE CASCADE,
    PRIMARY KEY (task_id, label_id)
);

CREATE TABLE IF NOT EXISTS comments (
    id         BIGSERIAL     PRIMARY KEY,
    text       VARCHAR(1000) NOT NULL,
    created_at TIMESTAMP     NOT NULL DEFAULT NOW(),
    task_id    BIGINT        NOT NULL REFERENCES tasks (id) ON DELETE CASCADE,
    author_id  BIGINT        NOT NULL REFERENCES users (id)
);

CREATE INDEX IF NOT EXISTS idx_tasks_project  ON tasks (project_id);
CREATE INDEX IF NOT EXISTS idx_tasks_assignee ON tasks (assignee_id);
CREATE INDEX IF NOT EXISTS idx_tasks_status   ON tasks (status);
CREATE INDEX IF NOT EXISTS idx_comments_task  ON comments (task_id);
