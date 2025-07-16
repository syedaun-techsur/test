package solutions.techsur.rfpaiservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import solutions.techsur.rfpaiservice.entity.RequestForProposal;
import solutions.techsur.rfpaiservice.entity.RequestForProposalDocument;

import java.util.List;

@Repository
public interface RequestForProposalDocumentRepository extends JpaRepository<RequestForProposalDocument, Integer> , JpaSpecificationExecutor<RequestForProposalDocument> {
    List<RequestForProposalDocument> findByRequestForProposal(RequestForProposal proposal);
}
