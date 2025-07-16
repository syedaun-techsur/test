package solutions.techsur.rfpaiservice.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import solutions.techsur.rfpaiservice.dto.CommonFilter;
import solutions.techsur.rfpaiservice.dto.ComplianceMatrixRequest;
import solutions.techsur.rfpaiservice.entity.ComplianceMatrix;

import java.util.List;

/**
 * Service interface for handling Compliance Matrix operations.
 */
public interface ComplianceMatrixService {

    /**
     * Creates Compliance Matrix entries for a given proposal.
     *
     * @param proposalId the ID of the proposal
     * @param requests   the list of ComplianceMatrixRequest DTOs
     * @return the list of created ComplianceMatrix entities
     */
    List<ComplianceMatrix> createComplianceMatrix(Integer proposalId, List<ComplianceMatrixRequest> requests);

    /**
     * Retrieves a ComplianceMatrix entity by its ID.
     *
     * @param matrixId the ID of the ComplianceMatrix
     * @return the ComplianceMatrix entity
     */
    ComplianceMatrix getComplianceMatrix(Integer matrixId);

    /**
     * Retrieves a paginated list of ComplianceMatrix entities filtered by given criteria.
     *
     * @param filter      common filtering criteria
     * @param pageable    pagination information
     * @param proposalId  the proposal ID to filter by
     * @return a paginated list of ComplianceMatrix entities
     */
    Page<ComplianceMatrix> getComplianceMatrixPage(CommonFilter filter, Pageable pageable, Integer proposalId);

    /**
     * Updates Compliance Matrix entries for a given proposal.
     *
     * @param requests   the list of ComplianceMatrixRequest DTOs to update
     * @param proposalId the ID of the proposal
     */
    @Transactional
    void updateComplianceMatrix(List<ComplianceMatrixRequest> requests, Integer proposalId);
}