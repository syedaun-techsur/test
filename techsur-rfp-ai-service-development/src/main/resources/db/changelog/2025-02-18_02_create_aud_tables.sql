-- Envers requirements
CREATE SEQUENCE rfpaiservice.revinfo_seq START 1 INCREMENT 50;

CREATE TABLE rfpaiservice.revinfo (
    rev BIGINT NOT NULL DEFAULT nextval('rfpaiservice.revinfo_seq') PRIMARY KEY,
    revtstmp BIGINT NOT NULL
);

-- Audit table for rfps
CREATE TABLE rfpaiservice.rfps_aud (
    id BIGINT NOT NULL,
    rev BIGINT NOT NULL,
    revtype SMALLINT NOT NULL,
    title VARCHAR(255) NOT NULL,
    description TEXT NOT NULL,
    status VARCHAR(50),
    created_by BIGINT,
    created_at TIMESTAMP NOT NULL,
    PRIMARY KEY (id, rev),
    FOREIGN KEY (rev) REFERENCES rfpaiservice.revinfo(rev) ON DELETE CASCADE
);

-- Audit table for rfp_documents
CREATE TABLE rfpaiservice.rfp_documents_aud (
    id BIGINT NOT NULL,
    rev BIGINT NOT NULL,
    revtype SMALLINT NOT NULL,
    rfp_id BIGINT,
    file_name VARCHAR(255) NOT NULL,
    file_path VARCHAR(255) NOT NULL,
    uploaded_by BIGINT,
    uploaded_at TIMESTAMP NOT NULL,
    PRIMARY KEY (id, rev),
    FOREIGN KEY (rev) REFERENCES rfpaiservice.revinfo(rev) ON DELETE CASCADE
);

-- Audit table for response_outline
CREATE TABLE rfpaiservice.response_outline_aud (
    id BIGINT NOT NULL,
    rev BIGINT NOT NULL,
    revtype SMALLINT NOT NULL,
    rfp_id BIGINT,
    section_no VARCHAR(50) NOT NULL,
    section_title VARCHAR(255) NOT NULL,
    parent_section_id BIGINT,
    created_at TIMESTAMP NOT NULL,
    PRIMARY KEY (id, rev),
    FOREIGN KEY (rev) REFERENCES rfpaiservice.revinfo(rev) ON DELETE CASCADE
);

-- Audit table for compliance_matrix
CREATE TABLE rfpaiservice.compliance_matrix_aud (
    id BIGINT NOT NULL,
    rev BIGINT NOT NULL,
    revtype SMALLINT NOT NULL,
    rfp_id BIGINT,
    requirement TEXT NOT NULL,
    compliance_status VARCHAR(50),
    justification TEXT,
    created_at TIMESTAMP NOT NULL,
    PRIMARY KEY (id, rev),
    FOREIGN KEY (rev) REFERENCES rfpaiservice.revinfo(rev) ON DELETE CASCADE
);

-- Audit table for rfp_responses
CREATE TABLE rfpaiservice.rfp_responses_aud (
    id BIGINT NOT NULL,
    rev BIGINT NOT NULL,
    revtype SMALLINT NOT NULL,
    rfp_id BIGINT,
    outline_id BIGINT,
    generated_text TEXT NOT NULL,
    status VARCHAR(50),
    created_at TIMESTAMP NOT NULL,
    PRIMARY KEY (id, rev),
    FOREIGN KEY (rev) REFERENCES rfpaiservice.revinfo(rev) ON DELETE CASCADE
);