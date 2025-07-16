UPDATE rfpaiservice.rfps
SET status = 'UPLOADED'
WHERE status = 'PENDING';

UPDATE rfpaiservice.rfps_aud
SET status = 'UPLOADED'
WHERE status = 'PENDING';


