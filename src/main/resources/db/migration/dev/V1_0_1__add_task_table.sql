CREATE TABLE tasks (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    created_at TIMESTAMP(6),
    title VARCHAR(255),
    description TEXT,
    is_completed BOOLEAN,
    duration BIGINT,

    CONSTRAINT fk_tasks_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE tasks_time (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    task_id BIGINT NOT NULL,
    time_type VARCHAR(255) NOT NULL,
    time TIMESTAMP(6),

    CONSTRAINT fk_tasks_time_task FOREIGN KEY (task_id) REFERENCES tasks(id) ON DELETE CASCADE
);