-- =============================================
-- Complete Database Reset Script
-- Railway Management System
-- =============================================

USE railway_management;

-- Drop all tables in correct order (reverse of dependencies)
DROP TABLE IF EXISTS audit_logs;
DROP TABLE IF EXISTS payments;
DROP TABLE IF EXISTS bookings;
DROP TABLE IF EXISTS trains;
DROP TABLE IF EXISTS users;

-- =============================================
-- Create users table
-- =============================================
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_users_email (email)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- =============================================
-- Create trains table
-- =============================================
CREATE TABLE trains (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    train_number VARCHAR(20) NOT NULL UNIQUE,
    name VARCHAR(100) NOT NULL,
    source VARCHAR(100) NOT NULL,
    destination VARCHAR(100) NOT NULL,
    departure_time TIMESTAMP NOT NULL,
    arrival_time TIMESTAMP NOT NULL,
    seats_available INT NOT NULL,
    fare DECIMAL(10, 2) NOT NULL,
    active BOOLEAN DEFAULT TRUE,
    version BIGINT DEFAULT 0,
    INDEX idx_trains_route (source, destination),
    INDEX idx_trains_departure (departure_time),
    INDEX idx_trains_number (train_number)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- =============================================
-- Create bookings table
-- =============================================
CREATE TABLE bookings (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    train_id BIGINT NOT NULL,
    booking_date TIMESTAMP NOT NULL,
    seats_booked INT NOT NULL,
    total_fare DECIMAL(10, 2) NOT NULL,
    status VARCHAR(20) NOT NULL,
    version BIGINT DEFAULT 0,
    INDEX idx_bookings_user (user_id),
    INDEX idx_bookings_train (train_id),
    INDEX idx_bookings_status (status),
    INDEX idx_bookings_date (booking_date),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (train_id) REFERENCES trains(id) ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- =============================================
-- Create payments table
-- =============================================
CREATE TABLE payments (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    booking_id BIGINT NOT NULL UNIQUE,
    payment_mode VARCHAR(20) NOT NULL,
    amount DECIMAL(10, 2) NOT NULL,
    payment_date TIMESTAMP NOT NULL,
    transaction_id VARCHAR(50) NOT NULL UNIQUE,
    version BIGINT DEFAULT 0,
    INDEX idx_payments_transaction (transaction_id),
    INDEX idx_payments_booking (booking_id),
    INDEX idx_payments_date (payment_date),
    FOREIGN KEY (booking_id) REFERENCES bookings(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- =============================================
-- Create audit_logs table
-- =============================================
CREATE TABLE audit_logs (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT,
    action VARCHAR(100) NOT NULL,
    details TEXT,
    timestamp TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    ip_address VARCHAR(50) NOT NULL,
    user_agent VARCHAR(255),
    INDEX idx_audit_logs_user (user_id),
    INDEX idx_audit_logs_timestamp (timestamp),
    INDEX idx_audit_logs_action (action),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- =============================================
-- Insert Seed Data
-- =============================================

-- Insert admin user (password: admin123)
INSERT INTO users (name, email, password, role) VALUES
('Admin User', 'admin@railway.com', '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVymGe07xd00DMxs.AQubh4a', 'ADMIN');

-- Insert regular user (password: user123)
INSERT INTO users (name, email, password, role) VALUES
('Test User', 'user@railway.com', '$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG', 'USER');

-- Insert sample trains
INSERT INTO trains (train_number, name, source, destination, departure_time, arrival_time, seats_available, fare, active) VALUES
('12345', 'Hyderabad Chennai Express', 'Hyderabad', 'Chennai', '2025-11-15 08:00:00', '2025-11-15 18:00:00', 100, 850.00, TRUE),
('67890', 'Chennai Bangalore Superfast', 'Chennai', 'Bangalore', '2025-11-16 10:30:00', '2025-11-16 16:30:00', 120, 650.00, TRUE),
('11111', 'Bangalore Mumbai Express', 'Bangalore', 'Mumbai', '2025-11-17 20:00:00', '2025-11-18 08:00:00', 150, 1200.00, TRUE),
('22222', 'Delhi Kolkata Rajdhani', 'Delhi', 'Kolkata', '2025-11-18 16:00:00', '2025-11-19 09:00:00', 80, 1500.00, TRUE),
('33333', 'Mumbai Pune Shatabdi', 'Mumbai', 'Pune', '2025-11-19 06:00:00', '2025-11-19 09:30:00', 200, 450.00, TRUE);

-- =============================================
-- Verify Data
-- =============================================

-- Check users
SELECT 'USERS:' as Table_Name;
SELECT id, name, email, role, created_at FROM users;

-- Check trains
SELECT 'TRAINS:' as Table_Name;
SELECT id, train_number, name, source, destination, seats_available, fare FROM trains;

-- Summary
SELECT 
    'Database Reset Complete' as Status,
    (SELECT COUNT(*) FROM users) as Total_Users,
    (SELECT COUNT(*) FROM trains) as Total_Trains;
