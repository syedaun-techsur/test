-- Create database
CREATE DATABASE auth_db;

-- Connect to the database
\connect auth_db;

-- Create users table
CREATE TABLE IF NOT EXISTS users (
    id BIGSERIAL PRIMARY KEY,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Create an index on email for faster lookups
CREATE INDEX IF NOT EXISTS idx_users_email ON users(email);

-- Insert demo user (password is bcrypt hash of "password123")
INSERT INTO users (email, password, first_name, last_name) 
VALUES (
    'admin@example.com', 
    '$2a$10$4lQcmKu7zJ8VuNz4jxmXJOmFhDUUqVkYfYuYTJYhHtKBkAXkYjVOi', 
    'John', 
    'Doe'
) ON CONFLICT (email) DO NOTHING;

-- Function to automatically update updated_at timestamp on row modification
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Drop existing trigger if exists to avoid conflicts
DROP TRIGGER IF EXISTS update_users_updated_at ON users;

-- Trigger to automatically update updated_at before row update
CREATE TRIGGER update_users_updated_at 
BEFORE UPDATE ON users 
FOR EACH ROW 
EXECUTE FUNCTION update_updated_at_column();