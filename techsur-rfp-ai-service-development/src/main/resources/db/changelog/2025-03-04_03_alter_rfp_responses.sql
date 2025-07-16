-- Drop the 'created_at' column if it exists to avoid errors if not present
ALTER TABLE rfpaiservice.rfp_responses
DROP COLUMN IF EXISTS created_at;

ALTER TABLE rfpaiservice.rfp_responses_aud
DROP COLUMN IF EXISTS created_at;

-- Add audit columns to rfp_responses table
ALTER TABLE rfpaiservice.rfp_responses
ADD COLUMN created_by_name VARCHAR(100) NOT NULL DEFAULT 'System',
ADD COLUMN updated_by_name VARCHAR(100) NOT NULL DEFAULT 'System',
ADD COLUMN created TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
ADD COLUMN updated TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP;

-- Add audit columns to rfp_responses_aud table
ALTER TABLE rfpaiservice.rfp_responses_aud
ADD COLUMN created_by_name VARCHAR(100) NOT NULL DEFAULT 'System',
ADD COLUMN updated_by_name VARCHAR(100) NOT NULL DEFAULT 'System',
ADD COLUMN created TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
ADD COLUMN updated TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP;