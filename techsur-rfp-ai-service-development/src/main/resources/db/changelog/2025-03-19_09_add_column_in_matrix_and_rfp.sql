
ALTER TABLE rfpaiservice.compliance_matrix
ADD COLUMN section_no VARCHAR(30);

ALTER TABLE rfpaiservice.compliance_matrix_aud
ADD COLUMN section_no VARCHAR(30);

ALTER TABLE rfpaiservice.rfps
ADD COLUMN is_archived BOOLEAN NOT NULL DEFAULT false;

ALTER TABLE rfpaiservice.rfps_aud
ADD COLUMN is_archived BOOLEAN NOT NULL DEFAULT false;