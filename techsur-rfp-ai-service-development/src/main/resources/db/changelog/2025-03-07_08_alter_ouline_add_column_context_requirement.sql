ALTER TABLE rfpaiservice.response_outline
ADD COLUMN requirement TEXT NOT NULL DEFAULT 'Unknown',
ADD COLUMN context TEXT NOT NULL DEFAULT 'Unknown';

ALTER TABLE rfpaiservice.response_outline_aud
ADD COLUMN requirement TEXT NOT NULL DEFAULT 'Unknown',
ADD COLUMN context TEXT NOT NULL DEFAULT 'Unknown';





