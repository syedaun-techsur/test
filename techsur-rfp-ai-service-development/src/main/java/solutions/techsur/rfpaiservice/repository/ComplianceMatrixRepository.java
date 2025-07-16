package solutions.techsur.rfpaiservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import solutions.techsur.rfpaiservice.entity.ComplianceMatrix;

@Repository
public interface ComplianceMatrixRepository extends JpaRepository<ComplianceMatrix, Integer>, JpaSpecificationExecutor<ComplianceMatrix> {

    /**
     * Deletes all ComplianceMatrix records associated with the given proposal ID.
     *
     * @param proposalId the ID of the proposal whose compliance matrix records should be deleted
     */
    @Modifying
    @Transactional
    void deleteByProposalId(Integer proposalId);
}