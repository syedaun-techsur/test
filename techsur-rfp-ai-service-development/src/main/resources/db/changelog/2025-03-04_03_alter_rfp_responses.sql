-- Drop the old created_at columns
ALTER TABLE rfpaiservice.rfp_responses
DROP COLUMN created_at;

ALTER TABLE rfpaiservice.rfp_responses_aud
DROP COLUMN created_at;

-- Add new columns with appropriate types and defaults
ALTER TABLE rfpaiservice.rfp_responses
ADD COLUMN created_by_name VARCHAR(100) NOT NULL DEFAULT 'System',
ADD COLUMN updated_by_name VARCHAR(100) NOT NULL DEFAULT 'System',
ADD COLUMN created TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
ADD COLUMN updated TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP;

ALTER TABLE rfpaiservice.rfp_responses_aud
ADD COLUMN created_by_name VARCHAR(100) NOT NULL DEFAULT 'System',
ADD COLUMN updated_by_name VARCHAR(100) NOT NULL DEFAULT 'System',
ADD COLUMN created TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
ADD COLUMN updated TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP;

-- Note:
-- To ensure 'updated' column is automatically updated on row modification,
-- consider creating a trigger and trigger function like below (PostgreSQL example):
--
-- CREATE OR REPLACE FUNCTION update_updated_at_column()
-- RETURNS TRIGGER AS $$
-- BEGIN
--   NEW.updated = NOW();
--   RETURN NEW;
-- END;
-- $$ language 'plpgsql';
--
-- CREATE TRIGGER update_rfp_responses_updated_at
-- BEFORE UPDATE ON rfpaiservice.rfp_responses
-- FOR EACH ROW EXECUTE PROCEDURE update_updated_at_column();
--
-- Similar triggers can be created for rfp_responses_aud table.