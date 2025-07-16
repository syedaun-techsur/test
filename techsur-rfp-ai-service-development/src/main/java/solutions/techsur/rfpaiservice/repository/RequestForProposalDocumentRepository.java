package solutions.techsur.rfpaiservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import solutions.techsur.rfpaiservice.entity.RequestForProposal;
import solutions.techsur.rfpaiservice.entity.RequestForProposalDocument;

import java.util.List;

/**
 * Repository interface for managing RequestForProposalDocument entities.
 */
@Repository
public interface RequestForProposalDocumentRepository extends JpaRepository<RequestForProposalDocument, Integer>, JpaSpecificationExecutor<RequestForProposalDocument> {

    /**
     * Finds all RequestForProposalDocument entities associated with a given RequestForProposal.
     * 
     * @param proposal the RequestForProposal entity
     * @return a list of matching RequestForProposalDocument entities
     */
    List<RequestForProposalDocument> findByRequestForProposal(RequestForProposal proposal);
}