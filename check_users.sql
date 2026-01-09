-- Check if users exist
SELECT id, name, email, role FROM users;

-- If you need to reset/recreate the users, run this:
-- DELETE FROM users WHERE email IN ('admin@railway.com', 'user@railway.com');

-- Then insert fresh users:
-- INSERT INTO users (name, email, password, role) VALUES
-- ('Admin User', 'admin@railway.com', '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVymGe07xd00DMxs.AQubh4a', 'ADMIN'),
-- ('Test User', 'user@railway.com', '$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG', 'USER');
