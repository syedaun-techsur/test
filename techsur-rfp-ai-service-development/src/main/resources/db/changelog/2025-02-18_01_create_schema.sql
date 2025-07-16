create schema if not exists rfpaiservice;

CREATE TABLE rfpaiservice.user_roles
(
    id        SERIAL PRIMARY KEY,
    role_name VARCHAR(50) UNIQUE NOT NULL
);

CREATE TABLE rfpaiservice.users
(
    id         SERIAL PRIMARY KEY,
    name       VARCHAR(255)        NOT NULL,
    email      VARCHAR(255) UNIQUE NOT NULL,
    role_id    INT REFERENCES user_roles (id),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE rfpaiservice.rfps
(
    id          SERIAL PRIMARY KEY,
    title       VARCHAR(255) NOT NULL,
    description TEXT         NOT NULL,
    status      VARCHAR(50) DEFAULT 'Uploaded',
    created_by  INT REFERENCES users (id),
    created_at  TIMESTAMP   DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE rfpaiservice.rfp_documents
(
    id          SERIAL PRIMARY KEY,
    rfp_id      INT REFERENCES rfps (id),
    file_name   VARCHAR(255) NOT NULL,
    file_path   VARCHAR(255) NOT NULL,
    uploaded_by INT REFERENCES users (id),
    uploaded_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE rfpaiservice.response_outline
(
    id                SERIAL PRIMARY KEY,
    rfp_id            INT REFERENCES rfps (id),
    section_no        VARCHAR(50)  NOT NULL,
    section_title     VARCHAR(255) NOT NULL,
    parent_section_id INT REFERENCES response_outline (id),
    created_at        TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE rfpaiservice.compliance_matrix
(
    id                SERIAL PRIMARY KEY,
    rfp_id            INT REFERENCES rfps (id),
    requirement       TEXT NOT NULL,
    compliance_status VARCHAR(50) DEFAULT 'Compliant',
    justification     TEXT NULL,
    created_at        TIMESTAMP   DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE rfpaiservice.rfp_responses
(
    id             SERIAL PRIMARY KEY,
    rfp_id         INT REFERENCES rfps (id),
    outline_id     INT REFERENCES response_outline (id),
    generated_text TEXT NOT NULL,
    status         VARCHAR(50) DEFAULT 'Generated',
    created_at     TIMESTAMP   DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE rfpaiservice.user_adjustments
(
    id              SERIAL PRIMARY KEY,
    response_id     INT REFERENCES rfp_responses (id),
    user_id         INT REFERENCES users (id),
    old_text        TEXT NOT NULL,
    new_text        TEXT NOT NULL,
    adjustment_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE rfpaiservice.audit_log
(
    id        SERIAL PRIMARY KEY,
    user_id   INT REFERENCES users (id),
    action    VARCHAR(255) NOT NULL,
    details   TEXT NULL,
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
