BEGIN;

DECLARE @BatchSize INT = 1000;
DECLARE @RowCount INT;

-- Loop to process updates in batches
WHILE 1 = 1
BEGIN
    -- Update a batch of records
    UPDATE TOP (@BatchSize) rfpaiservice.compliance_matrix
    SET compliance_status = 'PASS'
    WHERE compliance_status <> 'PASS';

    -- Check if any rows were updated
    SET @RowCount = @@ROWCOUNT;
    IF @RowCount < @BatchSize
        BREAK; -- Exit loop if fewer rows were updated than the batch size
END;

COMMIT;