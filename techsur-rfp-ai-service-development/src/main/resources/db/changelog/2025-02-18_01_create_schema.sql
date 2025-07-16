CREATE SCHEMA IF NOT EXISTS rfpaiservice;

CREATE TABLE IF NOT EXISTS rfpaiservice.user_roles
(
    id        SERIAL PRIMARY KEY,
    role_name VARCHAR(50) UNIQUE NOT NULL
);

CREATE TABLE IF NOT EXISTS rfpaiservice.users
(
    id         SERIAL PRIMARY KEY,
    name       VARCHAR(255) NOT NULL,
    email      VARCHAR(255) UNIQUE NOT NULL,
    role_id    INTEGER NOT NULL REFERENCES rfpaiservice.user_roles(id),
    created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT email_format CHECK (email ~* '^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$')
);

CREATE TABLE IF NOT EXISTS rfpaiservice.rfps
(
    id          SERIAL PRIMARY KEY,
    title       VARCHAR(255) NOT NULL,
    description TEXT NOT NULL,
    status      VARCHAR(50) DEFAULT 'Uploaded' NOT NULL,
    created_by  INTEGER NOT NULL REFERENCES rfpaiservice.users(id),
    created_at  TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS rfpaiservice.rfp_documents
(
    id          SERIAL PRIMARY KEY,
    rfp_id      INTEGER NOT NULL REFERENCES rfpaiservice.rfps(id) ON DELETE CASCADE,
    file_name   VARCHAR(255) NOT NULL,
    file_path   VARCHAR(255) NOT NULL,
    uploaded_by INTEGER NOT NULL REFERENCES rfpaiservice.users(id),
    uploaded_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS rfpaiservice.response_outline
(
    id              SERIAL PRIMARY KEY,
    rfp_id          INTEGER NOT NULL REFERENCES rfpaiservice.rfps(id) ON DELETE CASCADE,
    section_no      VARCHAR(50) NOT NULL,
    section_title   VARCHAR(255) NOT NULL,
    parent_section_id INTEGER REFERENCES rfpaiservice.response_outline(id) ON DELETE SET NULL,
    created_at      TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS rfpaiservice.compliance_matrix
(
    id                SERIAL PRIMARY KEY,
    rfp_id            INTEGER NOT NULL REFERENCES rfpaiservice.rfps(id) ON DELETE CASCADE,
    requirement       TEXT NOT NULL,
    compliance_status VARCHAR(50) DEFAULT 'Compliant' NOT NULL,
    justification     TEXT,
    created_at        TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS rfpaiservice.rfp_responses
(
    id           SERIAL PRIMARY KEY,
    rfp_id       INTEGER NOT NULL REFERENCES rfpaiservice.rfps(id) ON DELETE CASCADE,
    outline_id   INTEGER NOT NULL REFERENCES rfpaiservice.response_outline(id) ON DELETE CASCADE,
    generated_text TEXT NOT NULL,
    status       VARCHAR(50) DEFAULT 'Generated' NOT NULL,
    created_at   TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS rfpaiservice.user_adjustments
(
    id              SERIAL PRIMARY KEY,
    response_id     INTEGER NOT NULL REFERENCES rfpaiservice.rfp_responses(id) ON DELETE CASCADE,
    user_id         INTEGER NOT NULL REFERENCES rfpaiservice.users(id),
    old_text        TEXT NOT NULL,
    new_text        TEXT NOT NULL,
    adjustment_time TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS rfpaiservice.audit_log
(
    id        SERIAL PRIMARY KEY,
    user_id   INTEGER NOT NULL REFERENCES rfpaiservice.users(id),
    action    VARCHAR(255) NOT NULL,
    details   TEXT,
    timestamp TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP
);