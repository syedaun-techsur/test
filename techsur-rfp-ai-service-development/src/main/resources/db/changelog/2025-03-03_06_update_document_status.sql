-- Update document status from 'PENDING' to 'UPLOADED'
-- Executed on 2025-03-03

BEGIN;

UPDATE rfpaiservice.rfps
SET status = 'UPLOADED'
WHERE status = 'PENDING';

UPDATE rfpaiservice.rfps_aud
SET status = 'UPLOADED'
WHERE status = 'PENDING';

COMMIT;