-- Add is_generated_content column to response_outline table
ALTER TABLE rfpaiservice.response_outline
ADD COLUMN IF NOT EXISTS is_generated_content BOOLEAN DEFAULT FALSE NOT NULL;

-- Add is_generated_content column to response_outline_aud table
ALTER TABLE rfpaiservice.response_outline_aud
ADD COLUMN IF NOT EXISTS is_generated_content BOOLEAN DEFAULT FALSE NOT NULL;