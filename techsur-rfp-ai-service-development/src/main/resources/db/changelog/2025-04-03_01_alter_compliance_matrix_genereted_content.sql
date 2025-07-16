
ALTER TABLE rfpaiservice.response_outline
ADD COLUMN is_generated_content BOOLEAN DEFAULT FALSE NOT NULL;

ALTER TABLE rfpaiservice.response_outline_aud
ADD COLUMN is_generated_content BOOLEAN DEFAULT FALSE NOT NULL;