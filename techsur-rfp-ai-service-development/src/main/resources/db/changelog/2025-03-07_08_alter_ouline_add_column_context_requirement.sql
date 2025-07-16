-- Add 'requirement' column with default value to response_outline table
ALTER TABLE rfpaiservice.response_outline
ADD COLUMN IF NOT EXISTS requirement TEXT NOT NULL DEFAULT 'Unknown';

-- Add 'context' column with default value to response_outline table
ALTER TABLE rfpaiservice.response_outline
ADD COLUMN IF NOT EXISTS context TEXT NOT NULL DEFAULT 'Unknown';

-- Add 'requirement' column with default value to response_outline_aud table
ALTER TABLE rfpaiservice.response_outline_aud
ADD COLUMN IF NOT EXISTS requirement TEXT NOT NULL DEFAULT 'Unknown';

-- Add 'context' column with default value to response_outline_aud table
ALTER TABLE rfpaiservice.response_outline_aud
ADD COLUMN IF NOT EXISTS context TEXT NOT NULL DEFAULT 'Unknown';