-- Add new columns to response_outline table only if they don't already exist
ALTER TABLE rfpaiservice.response_outline 
ADD COLUMN IF NOT EXISTS source_mapping TEXT;

ALTER TABLE rfpaiservice.response_outline 
ADD COLUMN IF NOT EXISTS win_theme_alignment TEXT;

ALTER TABLE rfpaiservice.response_outline 
ADD COLUMN IF NOT EXISTS section_purpose TEXT;

ALTER TABLE rfpaiservice.response_outline 
ADD COLUMN IF NOT EXISTS instructions_to_writer TEXT;


-- Add new columns to response_outline_aud table only if they don't already exist
ALTER TABLE rfpaiservice.response_outline_aud 
ADD COLUMN IF NOT EXISTS source_mapping TEXT;

ALTER TABLE rfpaiservice.response_outline_aud 
ADD COLUMN IF NOT EXISTS win_theme_alignment TEXT;

ALTER TABLE rfpaiservice.response_outline_aud 
ADD COLUMN IF NOT EXISTS section_purpose TEXT;

ALTER TABLE rfpaiservice.response_outline_aud 
ADD COLUMN IF NOT EXISTS instructions_to_writer TEXT;


-- Step 1: Drop the existing foreign key constraint if it exists
ALTER TABLE rfpaiservice.rfp_responses
DROP CONSTRAINT IF EXISTS rfp_responses_outline_id_fkey;

-- Step 2: Recreate it with ON DELETE CASCADE
ALTER TABLE rfpaiservice.rfp_responses
ADD CONSTRAINT rfp_responses_outline_id_fkey
FOREIGN KEY (outline_id)
REFERENCES rfpaiservice.response_outline(id)
ON DELETE CASCADE;