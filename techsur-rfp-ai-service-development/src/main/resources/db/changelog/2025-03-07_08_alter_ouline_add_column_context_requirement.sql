-- Add 'requirement' and 'context' columns to response_outline table
ALTER TABLE rfpaiservice.response_outline
ADD COLUMN requirement TEXT NOT NULL DEFAULT 'Unknown';

ALTER TABLE rfpaiservice.response_outline
ADD COLUMN context TEXT NOT NULL DEFAULT 'Unknown';

-- Add 'requirement' and 'context' columns to response_outline_aud table
ALTER TABLE rfpaiservice.response_outline_aud
ADD COLUMN requirement TEXT NOT NULL DEFAULT 'Unknown';

ALTER TABLE rfpaiservice.response_outline_aud
ADD COLUMN context TEXT NOT NULL DEFAULT 'Unknown';