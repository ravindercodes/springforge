-- Insert roles with dummy dates
INSERT INTO roles (name, created_at, updated_at)
VALUES
    ('ROLE_USER', '2025-02-01 10:00:00', '2023-01-01 10:00:00'),
    ('ROLE_ADMIN', '2025-02-01 10:05:00', '2023-01-01 10:05:00');

-- Insert users with dummy dates and password 'Springforge@123'
INSERT INTO users (username, email, password, deleted, is_enabled, created_at, updated_at)
VALUES
    ('springforge', 'springforge@test.com', '$2a$10$kJBqZQTEFQ.VmpwSUdO7p..l0lFbKXyTzTapxof5iqu.nxGbZtErO',
     false, true, '2025-02-01 10:00:00', '2025-02-01 10:00:00');

-- Insert user_roles with dummy dates
INSERT INTO user_roles (user_id, role_id)
VALUES
    (1, 2);
