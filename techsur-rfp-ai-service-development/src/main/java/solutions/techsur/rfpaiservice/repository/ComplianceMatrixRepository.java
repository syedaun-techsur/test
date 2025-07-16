package solutions.techsur.rfpaiservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import solutions.techsur.rfpaiservice.entity.ComplianceMatrix;

@Repository
public interface ComplianceMatrixRepository extends JpaRepository<ComplianceMatrix, Integer>, JpaSpecificationExecutor<ComplianceMatrix> {
    void deleteByProposalId(Integer proposalId);
}
