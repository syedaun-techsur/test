-- Create database (commented out because this command is usually executed separately)
-- CREATE DATABASE auth_db;

-- Connect to the database (commented out because connection commands depend on the SQL client)
-- \c auth_db;

-- Create users table
CREATE TABLE IF NOT EXISTS users (
    id BIGSERIAL PRIMARY KEY,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT now(),
    updated_at TIMESTAMP NOT NULL DEFAULT now()
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

-- Function to automatically update updated_at timestamp before update
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = now();
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Trigger to automatically update updated_at before updating a user record
DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM pg_trigger WHERE tgname = 'update_users_updated_at'
    ) THEN
        CREATE TRIGGER update_users_updated_at
        BEFORE UPDATE ON users
        FOR EACH ROW
        EXECUTE FUNCTION update_updated_at_column();
    END IF;
END;
$$;