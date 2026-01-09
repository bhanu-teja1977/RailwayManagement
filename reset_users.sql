-- Disable foreign key checks temporarily
SET FOREIGN_KEY_CHECKS = 0;

-- Truncate all tables that might have dependencies
TRUNCATE TABLE audit_logs;
TRUNCATE TABLE payments;
TRUNCATE TABLE bookings;
TRUNCATE TABLE users;

-- Re-enable foreign key checks
SET FOREIGN_KEY_CHECKS = 1;

-- Insert admin user (password: admin123)
INSERT INTO users (name, email, password, role) VALUES
('Admin User', 'admin@railway.com', '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVymGe07xd00DMxs.AQubh4a', 'ADMIN');

-- Insert regular user (password: user123)
INSERT INTO users (name, email, password, role) VALUES
('Test User', 'user@railway.com', '$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG', 'USER');

-- Verify users were created
SELECT id, name, email, role, created_at FROM users;
