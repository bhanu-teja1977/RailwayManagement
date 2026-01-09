-- Insert admin user (password: admin123)
INSERT INTO users (name, email, password, role) VALUES
('Admin User', 'admin@railway.com', '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVymGe07xd00DMxs.AQubh4a', 'ADMIN');

-- Insert regular user (password: user123)
INSERT INTO users (name, email, password, role) VALUES
('Test User', 'user@railway.com', '$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG', 'USER');

-- Insert demo trains
INSERT INTO trains (train_number, name, source, destination, departure_time, arrival_time, seats_available, fare, active) VALUES
('12345', 'Hyderabad Chennai Express', 'Hyderabad', 'Chennai', '2025-11-15 08:00:00', '2025-11-15 18:00:00', 100, 850.00, TRUE),
('67890', 'Chennai Bangalore Superfast', 'Chennai', 'Bangalore', '2025-11-16 10:30:00', '2025-11-16 16:30:00', 120, 650.00, TRUE),
('11111', 'Bangalore Mumbai Express', 'Bangalore', 'Mumbai', '2025-11-17 20:00:00', '2025-11-18 08:00:00', 150, 1200.00, TRUE),
('22222', 'Delhi Kolkata Rajdhani', 'Delhi', 'Kolkata', '2025-11-18 16:00:00', '2025-11-19 09:00:00', 80, 1500.00, TRUE),
('33333', 'Mumbai Pune Shatabdi', 'Mumbai', 'Pune', '2025-11-19 06:00:00', '2025-11-19 09:30:00', 200, 450.00, TRUE);
