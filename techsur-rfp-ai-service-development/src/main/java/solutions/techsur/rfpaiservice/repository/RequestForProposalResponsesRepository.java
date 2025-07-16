package solutions.techsur.rfpaiservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import solutions.techsur.rfpaiservice.entity.RequestForProposalResponses;

@Repository
public interface RequestForProposalResponsesRepository extends JpaRepository<RequestForProposalResponses, Integer> {
}
