-- Create users table
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Create trains table
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
    version BIGINT DEFAULT 0
);

-- Create bookings table
CREATE TABLE bookings (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    train_id BIGINT NOT NULL,
    booking_date TIMESTAMP NOT NULL,
    seats_booked INT NOT NULL,
    total_fare DECIMAL(10, 2) NOT NULL,
    status VARCHAR(20) NOT NULL,
    version BIGINT DEFAULT 0,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (train_id) REFERENCES trains(id)
);

-- Create payments table
CREATE TABLE payments (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    booking_id BIGINT NOT NULL UNIQUE,
    payment_mode VARCHAR(20) NOT NULL,
    amount DECIMAL(10, 2) NOT NULL,
    payment_date TIMESTAMP NOT NULL,
    transaction_id VARCHAR(50) NOT NULL UNIQUE,
    version BIGINT DEFAULT 0,
    FOREIGN KEY (booking_id) REFERENCES bookings(id)
);

-- Create audit_logs table
CREATE TABLE audit_logs (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT,
    action VARCHAR(100) NOT NULL,
    details TEXT,
    timestamp TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    ip_address VARCHAR(50) NOT NULL,
    user_agent VARCHAR(255),
    FOREIGN KEY (user_id) REFERENCES users(id)
);

-- Create indexes for better performance
CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_trains_route ON trains(source, destination);
CREATE INDEX idx_trains_departure ON trains(departure_time);
CREATE INDEX idx_bookings_user ON bookings(user_id);
CREATE INDEX idx_bookings_train ON bookings(train_id);
CREATE INDEX idx_bookings_status ON bookings(status);
CREATE INDEX idx_payments_transaction ON payments(transaction_id);
CREATE INDEX idx_audit_logs_user ON audit_logs(user_id);
CREATE INDEX idx_audit_logs_timestamp ON audit_logs(timestamp);
