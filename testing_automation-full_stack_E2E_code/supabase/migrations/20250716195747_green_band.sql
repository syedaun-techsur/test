-- Note: Database creation and connection to 'auth_db' should be done outside migration scripts.

-- Create users table if not exists
CREATE TABLE IF NOT EXISTS users (
    id BIGSERIAL PRIMARY KEY,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(60) NOT NULL, -- bcrypt hashes are typically 60 chars
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Create an index on email for faster lookups, if not exists
CREATE INDEX IF NOT EXISTS idx_users_email ON users(email);

-- Insert demo user (password is bcrypt hash of "password123")
INSERT INTO users (email, password, first_name, last_name)
VALUES (
    'admin@example.com',
    '$2a$10$4lQcmKu7zJ8VuNz4jxmXJOmFhDUUqVkYfYuYTJYhHtKBkAXkYjVOi',
    'John',
    'Doe'
) ON CONFLICT (email) DO NOTHING;

-- Function to automatically update updated_at timestamp
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Trigger to automatically update updated_at before every update on users
DROP TRIGGER IF EXISTS update_users_updated_at ON users;
CREATE TRIGGER update_users_updated_at
BEFORE UPDATE ON users
FOR EACH ROW
EXECUTE PROCEDURE update_updated_at_column();