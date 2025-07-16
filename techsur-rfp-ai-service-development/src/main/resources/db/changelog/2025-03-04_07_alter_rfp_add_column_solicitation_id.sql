-- Add solicitation_id column to rfpaiservice.rfps table if not exists
ALTER TABLE rfpaiservice.rfps
ADD COLUMN IF NOT EXISTS solicitation_id VARCHAR(50) NOT NULL DEFAULT 'Unknown';

-- Add solicitation_id column to rfpaiservice.rfps_aud audit table if not exists
ALTER TABLE rfpaiservice.rfps_aud
ADD COLUMN IF NOT EXISTS solicitation_id VARCHAR(50) NOT NULL DEFAULT 'Unknown';