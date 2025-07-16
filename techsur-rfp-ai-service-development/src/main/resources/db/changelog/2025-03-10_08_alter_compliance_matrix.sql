-- Drop 'created_at' columns if they exist to avoid errors on repeated runs
ALTER TABLE rfpaiservice.compliance_matrix
DROP COLUMN IF EXISTS created_at;

ALTER TABLE rfpaiservice.compliance_matrix_aud
DROP COLUMN IF EXISTS created_at;

-- Add audit and timestamp columns to compliance_matrix table if not already present
ALTER TABLE rfpaiservice.compliance_matrix
ADD COLUMN IF NOT EXISTS created_by_name VARCHAR(100) NOT NULL DEFAULT 'System';

ALTER TABLE rfpaiservice.compliance_matrix
ADD COLUMN IF NOT EXISTS updated_by_name VARCHAR(100) NOT NULL DEFAULT 'System';

ALTER TABLE rfpaiservice.compliance_matrix
ADD COLUMN IF NOT EXISTS created TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP;

ALTER TABLE rfpaiservice.compliance_matrix
ADD COLUMN IF NOT EXISTS updated TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP;

-- Add audit and timestamp columns to compliance_matrix_aud table if not already present
ALTER TABLE rfpaiservice.compliance_matrix_aud
ADD COLUMN IF NOT EXISTS created_by_name VARCHAR(100) NOT NULL DEFAULT 'System';

ALTER TABLE rfpaiservice.compliance_matrix_aud
ADD COLUMN IF NOT EXISTS updated_by_name VARCHAR(100) NOT NULL DEFAULT 'System';

ALTER TABLE rfpaiservice.compliance_matrix_aud
ADD COLUMN IF NOT EXISTS created TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP;

ALTER TABLE rfpaiservice.compliance_matrix_aud
ADD COLUMN IF NOT EXISTS updated TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP;