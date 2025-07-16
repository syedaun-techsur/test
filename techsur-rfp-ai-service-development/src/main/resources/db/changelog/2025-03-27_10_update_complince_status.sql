BEGIN;

UPDATE rfpaiservice.compliance_matrix
SET compliance_status = 'PASS'
WHERE compliance_status IS NULL;

COMMIT;