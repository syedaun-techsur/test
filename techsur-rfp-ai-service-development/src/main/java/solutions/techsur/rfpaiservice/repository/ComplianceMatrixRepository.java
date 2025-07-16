package solutions.techsur.rfpaiservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import solutions.techsur.rfpaiservice.entity.ComplianceMatrix;

/**
 * Repository interface for ComplianceMatrix entity.
 * Provides CRUD operations and specification execution support.
 */
@Repository
public interface ComplianceMatrixRepository extends JpaRepository<ComplianceMatrix, Integer>, JpaSpecificationExecutor<ComplianceMatrix> {

    /**
     * Deletes ComplianceMatrix records by the given proposal ID.
     *
     * @param proposalId the proposal ID to match for deletion
     */
    @Modifying
    @Transactional
    void deleteByProposalId(Integer proposalId);
}