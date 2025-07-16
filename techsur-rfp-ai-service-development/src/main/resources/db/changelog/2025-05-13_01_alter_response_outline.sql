-- Add content column to response_outline table if it doesn't already exist
ALTER TABLE rfpaiservice.response_outline
ADD COLUMN IF NOT EXISTS content TEXT;

-- Add content column to response_outline_aud table if it doesn't already exist
ALTER TABLE rfpaiservice.response_outline_aud
ADD COLUMN IF NOT EXISTS content TEXT;