ALTER TABLE rfpaiservice.response_outline
DROP COLUMN IF EXISTS created_at;

ALTER TABLE rfpaiservice.response_outline_aud
DROP COLUMN IF EXISTS created_at;

ALTER TABLE rfpaiservice.response_outline
ADD COLUMN IF NOT EXISTS created_by_name VARCHAR(100) NOT NULL DEFAULT 'System' COMMENT 'User who created the record',
ADD COLUMN IF NOT EXISTS updated_by_name VARCHAR(100) NOT NULL DEFAULT 'System' COMMENT 'User who last updated the record',
ADD COLUMN IF NOT EXISTS created TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Record creation timestamp',
ADD COLUMN IF NOT EXISTS updated TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Record last update timestamp';

ALTER TABLE rfpaiservice.response_outline_aud
ADD COLUMN IF NOT EXISTS created_by_name VARCHAR(100) NOT NULL DEFAULT 'System' COMMENT 'User who created the record',
ADD COLUMN IF NOT EXISTS updated_by_name VARCHAR(100) NOT NULL DEFAULT 'System' COMMENT 'User who last updated the record',
ADD COLUMN IF NOT EXISTS created TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Record creation timestamp',
ADD COLUMN IF NOT EXISTS updated TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Record last update timestamp';