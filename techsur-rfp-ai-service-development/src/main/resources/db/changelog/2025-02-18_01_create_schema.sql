CREATE SCHEMA IF NOT EXISTS rfpaiservice;

CREATE TABLE rfpaiservice.user_roles
(
    id         SERIAL PRIMARY KEY,
    role_name  VARCHAR(50) UNIQUE NOT NULL
);

CREATE TABLE rfpaiservice.users
(
    id         SERIAL PRIMARY KEY,
    name       VARCHAR(255) NOT NULL,
    email      VARCHAR(255) UNIQUE NOT NULL,
    role_id    INT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_users_user_roles FOREIGN KEY (role_id) REFERENCES rfpaiservice.user_roles (id) ON DELETE SET NULL
);

CREATE TABLE rfpaiservice.rfps
(
    id          SERIAL PRIMARY KEY,
    title       VARCHAR(255) NOT NULL,
    description TEXT NOT NULL,
    status      VARCHAR(50) DEFAULT 'Uploaded',
    created_by  INT NOT NULL,
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_rfps_users FOREIGN KEY (created_by) REFERENCES rfpaiservice.users (id) ON DELETE SET NULL
);

CREATE TABLE rfpaiservice.rfp_documents
(
    id          SERIAL PRIMARY KEY,
    rfp_id      INT NOT NULL,
    file_name   VARCHAR(255) NOT NULL,
    file_path   VARCHAR(255) NOT NULL,
    uploaded_by INT NOT NULL,
    uploaded_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_rfp_documents_rfps FOREIGN KEY (rfp_id) REFERENCES rfpaiservice.rfps (id) ON DELETE SET NULL,
    CONSTRAINT fk_rfp_documents_users FOREIGN KEY (uploaded_by) REFERENCES rfpaiservice.users (id) ON DELETE SET NULL
);

CREATE TABLE rfpaiservice.response_outline
(
    id                 SERIAL PRIMARY KEY,
    rfp_id             INT NOT NULL,
    section_no         VARCHAR(50) NOT NULL,
    section_title      VARCHAR(255) NOT NULL,
    parent_section_id  INT,
    created_at         TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_response_outline_rfps FOREIGN KEY (rfp_id) REFERENCES rfpaiservice.rfps (id) ON DELETE CASCADE,
    CONSTRAINT fk_response_outline_parent FOREIGN KEY (parent_section_id) REFERENCES rfpaiservice.response_outline (id) ON DELETE SET NULL
);

CREATE TABLE rfpaiservice.compliance_matrix
(
    id                SERIAL PRIMARY KEY,
    rfp_id            INT NOT NULL,
    requirement       TEXT NOT NULL,
    compliance_status VARCHAR(50) DEFAULT 'Compliant' NOT NULL,
    justification     TEXT,
    created_at        TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_compliance_matrix_rfps FOREIGN KEY (rfp_id) REFERENCES rfpaiservice.rfps (id) ON DELETE CASCADE
);

CREATE TABLE rfpaiservice.rfp_responses
(
    id             SERIAL PRIMARY KEY,
    rfp_id         INT NOT NULL,
    outline_id     INT NOT NULL,
    generated_text TEXT NOT NULL,
    status         VARCHAR(50) DEFAULT 'Generated' NOT NULL,
    created_at     TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_rfp_responses_rfps FOREIGN KEY (rfp_id) REFERENCES rfpaiservice.rfps (id) ON DELETE CASCADE,
    CONSTRAINT fk_rfp_responses_outline FOREIGN KEY (outline_id) REFERENCES rfpaiservice.response_outline (id) ON DELETE CASCADE
);

CREATE TABLE rfpaiservice.user_adjustments
(
    id              SERIAL PRIMARY KEY,
    response_id     INT NOT NULL,
    user_id         INT NOT NULL,
    old_text        TEXT NOT NULL,
    new_text        TEXT NOT NULL,
    adjustment_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_user_adjustments_responses FOREIGN KEY (response_id) REFERENCES rfpaiservice.rfp_responses (id) ON DELETE CASCADE,
    CONSTRAINT fk_user_adjustments_users FOREIGN KEY (user_id) REFERENCES rfpaiservice.users (id) ON DELETE CASCADE
);

CREATE TABLE rfpaiservice.audit_log
(
    id        SERIAL PRIMARY KEY,
    user_id   INT NOT NULL,
    action    VARCHAR(255) NOT NULL,
    details   TEXT,
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_audit_log_users FOREIGN KEY (user_id) REFERENCES rfpaiservice.users (id) ON DELETE SET NULL
);