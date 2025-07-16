-- Step 0: Add new columns to response_outline if they don't exist
DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM information_schema.columns 
        WHERE table_schema = 'rfpaiservice' 
          AND table_name = 'response_outline' 
          AND column_name = 'source_mapping'
    ) THEN
        ALTER TABLE rfpaiservice.response_outline ADD COLUMN source_mapping TEXT;
    END IF;
    IF NOT EXISTS (
        SELECT 1 FROM information_schema.columns 
        WHERE table_schema = 'rfpaiservice' 
          AND table_name = 'response_outline' 
          AND column_name = 'win_theme_alignment'
    ) THEN
        ALTER TABLE rfpaiservice.response_outline ADD COLUMN win_theme_alignment TEXT;
    END IF;
    IF NOT EXISTS (
        SELECT 1 FROM information_schema.columns 
        WHERE table_schema = 'rfpaiservice' 
          AND table_name = 'response_outline' 
          AND column_name = 'section_purpose'
    ) THEN
        ALTER TABLE rfpaiservice.response_outline ADD COLUMN section_purpose TEXT;
    END IF;
    IF NOT EXISTS (
        SELECT 1 FROM information_schema.columns 
        WHERE table_schema = 'rfpaiservice' 
          AND table_name = 'response_outline' 
          AND column_name = 'instructions_to_writer'
    ) THEN
        ALTER TABLE rfpaiservice.response_outline ADD COLUMN instructions_to_writer TEXT;
    END IF;
END$$;

-- Step 1: Add new columns to response_outline_aud if they don't exist
DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM information_schema.columns 
        WHERE table_schema = 'rfpaiservice' 
          AND table_name = 'response_outline_aud' 
          AND column_name = 'source_mapping'
    ) THEN
        ALTER TABLE rfpaiservice.response_outline_aud ADD COLUMN source_mapping TEXT;
    END IF;
    IF NOT EXISTS (
        SELECT 1 FROM information_schema.columns 
        WHERE table_schema = 'rfpaiservice' 
          AND table_name = 'response_outline_aud' 
          AND column_name = 'win_theme_alignment'
    ) THEN
        ALTER TABLE rfpaiservice.response_outline_aud ADD COLUMN win_theme_alignment TEXT;
    END IF;
    IF NOT EXISTS (
        SELECT 1 FROM information_schema.columns 
        WHERE table_schema = 'rfpaiservice' 
          AND table_name = 'response_outline_aud' 
          AND column_name = 'section_purpose'
    ) THEN
        ALTER TABLE rfpaiservice.response_outline_aud ADD COLUMN section_purpose TEXT;
    END IF;
    IF NOT EXISTS (
        SELECT 1 FROM information_schema.columns 
        WHERE table_schema = 'rfpaiservice' 
          AND table_name = 'response_outline_aud' 
          AND column_name = 'instructions_to_writer'
    ) THEN
        ALTER TABLE rfpaiservice.response_outline_aud ADD COLUMN instructions_to_writer TEXT;
    END IF;
END$$;

-- Step 2: Drop the existing foreign key constraint on rfp_responses
ALTER TABLE rfpaiservice.rfp_responses
DROP CONSTRAINT IF EXISTS rfp_responses_outline_id_fkey;

-- Step 3: Recreate the foreign key constraint with ON DELETE CASCADE
ALTER TABLE rfpaiservice.rfp_responses
ADD CONSTRAINT rfp_responses_outline_id_fkey
FOREIGN KEY (outline_id)
REFERENCES rfpaiservice.response_outline(id)
ON DELETE CASCADE;