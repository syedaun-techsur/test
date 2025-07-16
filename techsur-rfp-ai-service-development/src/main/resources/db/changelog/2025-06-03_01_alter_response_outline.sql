ALTER TABLE rfpaiservice.response_outline ADD COLUMN source_mapping TEXT;
ALTER TABLE rfpaiservice.response_outline ADD COLUMN win_theme_alignment TEXT;
ALTER TABLE rfpaiservice.response_outline ADD COLUMN section_purpose TEXT;
ALTER TABLE rfpaiservice.response_outline ADD COLUMN instructions_to_writer TEXT;


ALTER TABLE rfpaiservice.response_outline_aud ADD COLUMN source_mapping TEXT;
ALTER TABLE rfpaiservice.response_outline_aud ADD COLUMN win_theme_alignment TEXT;
ALTER TABLE rfpaiservice.response_outline_aud ADD COLUMN section_purpose TEXT;
ALTER TABLE rfpaiservice.response_outline_aud ADD COLUMN instructions_to_writer TEXT;

-- Step 1: Drop the existing foreign key constraint
ALTER TABLE rfpaiservice.rfp_responses
DROP CONSTRAINT rfp_responses_outline_id_fkey;

-- Step 2: Recreate it with ON DELETE CASCADE
ALTER TABLE rfpaiservice.rfp_responses
ADD CONSTRAINT rfp_responses_outline_id_fkey
FOREIGN KEY (outline_id)
REFERENCES rfpaiservice.response_outline(id)
ON DELETE CASCADE;
