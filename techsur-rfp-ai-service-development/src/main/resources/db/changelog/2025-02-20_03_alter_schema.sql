ALTER TABLE rfpaiservice.rfps
DROP COLUMN created_by;

ALTER TABLE rfpaiservice.rfps
DROP COLUMN created_at;

ALTER TABLE rfpaiservice.rfp_documents
DROP COLUMN uploaded_by;

ALTER TABLE rfpaiservice.rfp_documents
DROP COLUMN uploaded_at;

ALTER TABLE rfpaiservice.rfp_documents_aud
DROP COLUMN uploaded_by;

ALTER TABLE rfpaiservice.rfp_documents_aud
DROP COLUMN uploaded_at;

ALTER TABLE rfpaiservice.rfps_aud
DROP COLUMN created_by;

ALTER TABLE rfpaiservice.rfps_aud
DROP COLUMN created_at;

ALTER TABLE rfpaiservice.rfps 
ADD COLUMN created_by_name VARCHAR(100) NOT NULL DEFAULT 'System';

ALTER TABLE rfpaiservice.rfps 
ADD COLUMN updated_by_name VARCHAR(100) NOT NULL DEFAULT 'System';

ALTER TABLE rfpaiservice.rfps 
ADD COLUMN created TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP;

ALTER TABLE rfpaiservice.rfps 
ADD COLUMN updated TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP;

ALTER TABLE rfpaiservice.rfp_documents 
ADD COLUMN created_by_name VARCHAR(100) NOT NULL DEFAULT 'System';

ALTER TABLE rfpaiservice.rfp_documents 
ADD COLUMN updated_by_name VARCHAR(100) NOT NULL DEFAULT 'System';

ALTER TABLE rfpaiservice.rfp_documents 
ADD COLUMN created TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP;

ALTER TABLE rfpaiservice.rfp_documents 
ADD COLUMN updated TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP;

ALTER TABLE rfpaiservice.rfps_aud 
ADD COLUMN created_by_name VARCHAR(100) NOT NULL DEFAULT 'System';

ALTER TABLE rfpaiservice.rfps_aud 
ADD COLUMN updated_by_name VARCHAR(100) NOT NULL DEFAULT 'System';

ALTER TABLE rfpaiservice.rfps_aud 
ADD COLUMN created TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP;

ALTER TABLE rfpaiservice.rfps_aud 
ADD COLUMN updated TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP;

ALTER TABLE rfpaiservice.rfp_documents_aud
ADD COLUMN created_by_name VARCHAR(100) NOT NULL DEFAULT 'System';

ALTER TABLE rfpaiservice.rfp_documents_aud
ADD COLUMN updated_by_name VARCHAR(100) NOT NULL DEFAULT 'System';

ALTER TABLE rfpaiservice.rfp_documents_aud
ADD COLUMN created TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP;

ALTER TABLE rfpaiservice.rfp_documents_aud
ADD COLUMN updated TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP;