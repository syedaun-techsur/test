-- Add section_no column to compliance_matrix if it doesn't already exist
ALTER TABLE rfpaiservice.compliance_matrix
ADD COLUMN IF NOT EXISTS section_no VARCHAR(30);

-- Add section_no column to compliance_matrix_aud if it doesn't already exist
ALTER TABLE rfpaiservice.compliance_matrix_aud
ADD COLUMN IF NOT EXISTS section_no VARCHAR(30);

-- Add is_archived column with default FALSE to rfps if it doesn't already exist
ALTER TABLE rfpaiservice.rfps
ADD COLUMN IF NOT EXISTS is_archived BOOLEAN NOT NULL DEFAULT FALSE;

-- Add is_archived column with default FALSE to rfps_aud if it doesn't already exist
ALTER TABLE rfpaiservice.rfps_aud
ADD COLUMN IF NOT EXISTS is_archived BOOLEAN NOT NULL DEFAULT FALSE;