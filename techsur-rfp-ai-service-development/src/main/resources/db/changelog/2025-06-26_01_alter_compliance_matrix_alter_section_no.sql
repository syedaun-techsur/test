ALTER TABLE "rfpaiservice"."compliance_matrix"
    ALTER COLUMN "section_no" TYPE character varying(1024)
    USING "section_no"::character varying(1024);

ALTER TABLE "rfpaiservice"."compliance_matrix_aud"
    ALTER COLUMN "section_no" TYPE character varying(1024)
    USING "section_no"::character varying(1024);