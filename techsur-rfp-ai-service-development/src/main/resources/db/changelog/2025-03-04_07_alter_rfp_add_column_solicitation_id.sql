ALTER TABLE rfpaiservice.rfps
ADD COLUMN solicitation_id VARCHAR(50) NOT NULL DEFAULT 'Unknown';

ALTER TABLE rfpaiservice.rfps_aud
ADD COLUMN solicitation_id VARCHAR(50) NOT NULL DEFAULT 'Unknown';


