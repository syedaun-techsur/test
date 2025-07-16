-- Envers revision info sequence and table
CREATE SEQUENCE rfpaiservice.revinfo_seq
    START WITH 1
    INCREMENT BY 50
    MINVALUE 1
    NO CYCLE;

CREATE TABLE rfpaiservice.revinfo (
    rev BIGINT NOT NULL DEFAULT nextval('rfpaiservice.revinfo_seq') PRIMARY KEY,
    revtstmp BIGINT NOT NULL
);

-- Auditing table for rfps
CREATE TABLE rfpaiservice.rfps_aud (
    id BIGINT NOT NULL,
    rev BIGINT NOT NULL,
    revtype SMALLINT NOT NULL,
    title VARCHAR(255) NOT NULL,
    description TEXT NOT NULL,
    status VARCHAR(50),
    created_by INT,
    created_at TIMESTAMP,
    PRIMARY KEY (id, rev),
    CONSTRAINT fk_rfps_aud_rev FOREIGN KEY (rev) REFERENCES rfpaiservice.revinfo (rev) NOT NULL
);

-- Auditing table for rfp documents
CREATE TABLE rfpaiservice.rfp_documents_aud (
    id BIGINT NOT NULL,
    rev BIGINT NOT NULL,
    revtype SMALLINT NOT NULL,
    rfp_id INT,
    file_name VARCHAR(255) NOT NULL,
    file_path VARCHAR(255) NOT NULL,
    uploaded_by INT,
    uploaded_at TIMESTAMP,
    PRIMARY KEY (id, rev),
    CONSTRAINT fk_rfp_documents_aud_rev FOREIGN KEY (rev) REFERENCES rfpaiservice.revinfo (rev) NOT NULL
);

-- Auditing table for response outline
CREATE TABLE rfpaiservice.response_outline_aud (
    id BIGINT NOT NULL,
    rev BIGINT NOT NULL,
    revtype SMALLINT NOT NULL,
    rfp_id INT,
    section_no VARCHAR(50) NOT NULL,
    section_title VARCHAR(255) NOT NULL,
    parent_section_id INT,
    created_at TIMESTAMP,
    PRIMARY KEY (id, rev),
    CONSTRAINT fk_response_outline_aud_rev FOREIGN KEY (rev) REFERENCES rfpaiservice.revinfo (rev) NOT NULL
);

-- Auditing table for compliance matrix
CREATE TABLE rfpaiservice.compliance_matrix_aud (
    id BIGINT NOT NULL,
    rev BIGINT NOT NULL,
    revtype SMALLINT NOT NULL,
    rfp_id INT,
    requirement TEXT NOT NULL,
    compliance_status VARCHAR(50),
    justification TEXT,
    created_at TIMESTAMP,
    PRIMARY KEY (id, rev),
    CONSTRAINT fk_compliance_matrix_aud_rev FOREIGN KEY (rev) REFERENCES rfpaiservice.revinfo (rev) NOT NULL
);

-- Auditing table for rfp responses
CREATE TABLE rfpaiservice.rfp_responses_aud (
    id BIGINT NOT NULL,
    rev BIGINT NOT NULL,
    revtype SMALLINT NOT NULL,
    rfp_id INT,
    outline_id INT,
    generated_text TEXT NOT NULL,
    status VARCHAR(50),
    created_at TIMESTAMP,
    PRIMARY KEY (id, rev),
    CONSTRAINT fk_rfp_responses_aud_rev FOREIGN KEY (rev) REFERENCES rfpaiservice.revinfo (rev) NOT NULL
);