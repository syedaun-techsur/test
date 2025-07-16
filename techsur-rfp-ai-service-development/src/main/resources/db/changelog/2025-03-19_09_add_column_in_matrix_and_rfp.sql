-- Add section_no column to compliance_matrix table
ALTER TABLE rfpaiservice.compliance_matrix
ADD COLUMN "section_no" VARCHAR(30);

-- Add section_no column to compliance_matrix_aud (audit) table
ALTER TABLE rfpaiservice.compliance_matrix_aud
ADD COLUMN "section_no" VARCHAR(30);

-- Add is_archived flag to rfps table with default false to indicate active status
ALTER TABLE rfpaiservice.rfps
ADD COLUMN "is_archived" BOOLEAN NOT NULL DEFAULT false;

-- Add is_archived flag to rfps_aud (audit) table with default false for record status tracking
ALTER TABLE rfpaiservice.rfps_aud
ADD COLUMN "is_archived" BOOLEAN NOT NULL DEFAULT false;