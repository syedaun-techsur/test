package solutions.techsur.rfpaiservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import solutions.techsur.rfpaiservice.entity.RequestForProposal;

@Repository
public interface RequestForProposalRepository extends JpaRepository<RequestForProposal, Integer>, JpaSpecificationExecutor<RequestForProposal> {
}
