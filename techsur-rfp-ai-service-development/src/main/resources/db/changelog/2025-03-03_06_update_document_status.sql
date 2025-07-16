-- Update status in the main rfps table from 'PENDING' to 'UPLOADED'
UPDATE rfpaiservice.rfps
SET status = 'UPLOADED'
WHERE status = 'PENDING';

-- Update status in the audit table rfps_aud from 'PENDING' to 'UPLOADED'
UPDATE rfpaiservice.rfps_aud
SET status = 'UPLOADED'
WHERE status = 'PENDING';