-- Create users table
CREATE TABLE if not exists users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    phone_number VARCHAR(20),
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    last_login TIMESTAMP
);

-- Check if users table exists and has the required columns
DO $$
BEGIN
    -- Check if the users table exists with the new schema
    IF NOT EXISTS (
        SELECT 1 FROM information_schema.columns 
        WHERE table_name = 'users' AND column_name = 'first_name'
    ) THEN
        -- Add new columns to existing users table if they don't exist
        ALTER TABLE users ADD COLUMN IF NOT EXISTS first_name VARCHAR(100);
        ALTER TABLE users ADD COLUMN IF NOT EXISTS last_name VARCHAR(100);
        ALTER TABLE users ADD COLUMN IF NOT EXISTS phone_number VARCHAR(20);
        ALTER TABLE users ADD COLUMN IF NOT EXISTS status VARCHAR(20) DEFAULT 'ACTIVE';
        ALTER TABLE users ADD COLUMN IF NOT EXISTS last_login TIMESTAMP;
        
        -- Update existing users with default values
        UPDATE users SET 
            first_name = COALESCE(first_name, 'Default'),
            last_name = COALESCE(last_name, 'User'),
            status = COALESCE(status, 'ACTIVE')
        WHERE first_name IS NULL OR last_name IS NULL OR status IS NULL;
        
        -- Make required columns NOT NULL after setting default values
        ALTER TABLE users ALTER COLUMN first_name SET NOT NULL;
        ALTER TABLE users ALTER COLUMN last_name SET NOT NULL;
        ALTER TABLE users ALTER COLUMN status SET NOT NULL;
    END IF;
END $$;

-- Create user_roles table if it doesn't exist
CREATE TABLE user_roles (
    user_id UUID NOT NULL,
    role VARCHAR(50) NOT NULL,
    PRIMARY KEY (user_id, role),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Create indexes if they don't exist
CREATE INDEX IF NOT EXISTS idx_users_status ON users(status);
CREATE INDEX IF NOT EXISTS idx_users_created_at ON users(created_at);

-- Insert default admin user if not exists (password: admin123)
INSERT INTO users (id, username, email, password, first_name, last_name, status, created_at) 
SELECT 
    gen_random_uuid(),
    'admin',
    'admin@example.com',
    '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa',
    'Admin',
    'User',
    'ACTIVE',
    CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM users WHERE username = 'admin');

-- Insert admin role if not exists
INSERT INTO user_roles (user_id, role)
SELECT u.id, 'ADMIN'
FROM users u
WHERE u.username = 'admin'
AND NOT EXISTS (
    SELECT 1 FROM user_roles ur 
    WHERE ur.user_id = u.id AND ur.role = 'ADMIN'
);

-- Insert default user if not exists (password: user123)
INSERT INTO users (id, username, email, password, first_name, last_name, status, created_at) 
SELECT 
    gen_random_uuid(),
    'user',
    'user@example.com',
    '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVymGe07xd00DMxs.AQubh4a',
    'Regular',
    'User',
    'ACTIVE',
    CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM users WHERE username = 'user');

-- Insert user role if not exists
INSERT INTO user_roles (user_id, role)
SELECT u.id, 'USER'
FROM users u
WHERE u.username = 'user'
AND NOT EXISTS (
    SELECT 1 FROM user_roles ur 
    WHERE ur.user_id = u.id AND ur.role = 'USER'
);