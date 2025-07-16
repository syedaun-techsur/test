package solutions.techsur.rfpaiservice.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;
import solutions.techsur.rfpaiservice.dto.CommonFilter;
import solutions.techsur.rfpaiservice.dto.ComplianceMatrixRequest;
import solutions.techsur.rfpaiservice.entity.ComplianceMatrix;

import java.util.List;

public interface ComplianceMatrixService {

    /**
     * Creates compliance matrix entries for a given proposal.
     *
     * @param proposalId the ID of the proposal
     * @param requests the list of compliance matrix requests
     * @return list of created ComplianceMatrix entities
     */
    @NonNull
    List<ComplianceMatrix> createComplianceMatrix(@NonNull Integer proposalId, @NonNull List<ComplianceMatrixRequest> requests);

    /**
     * Retrieves a compliance matrix by its ID.
     *
     * @param matrixId the ID of the compliance matrix
     * @return the ComplianceMatrix entity
     */
    @NonNull
    ComplianceMatrix getComplianceMatrix(@NonNull Integer matrixId);

    /**
     * Retrieves a paginated list of compliance matrices for a proposal filtered by given criteria.
     *
     * @param filter filtering criteria
     * @param pageable pagination information
     * @param proposalId the ID of the proposal
     * @return paginated list of ComplianceMatrix entities
     */
    @NonNull
    Page<ComplianceMatrix> getComplianceMatrixPage(@NonNull CommonFilter filter, @NonNull Pageable pageable, @NonNull Integer proposalId);

    /**
     * Updates compliance matrix entries for a given proposal.
     *
     * @param requests the list of compliance matrix requests to update
     * @param proposalId the ID of the proposal
     */
    void updateComplianceMatrix(@NonNull List<ComplianceMatrixRequest> requests, @NonNull Integer proposalId);
}