package solutions.techsur.rfpaiservice.repository;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import solutions.techsur.rfpaiservice.entity.RequestForProposalDocument;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RFPDocumentSpecification {

    /**
     * Specification to find RequestForProposalDocument by a specific proposal ID.
     *
     * @param proposal the ID of the proposal
     * @return Specification for filtering RequestForProposalDocument by proposal ID
     */
    public static Specification<RequestForProposalDocument> findRFPByProposalSpecification(Integer proposal) {
        return (root, query, criteriaBuilder) -> 
                criteriaBuilder.equal(root.get("requestForProposal").get("id"), proposal);
    }

    /**
     * Specification to find documents uploaded by admin (where requestForProposal is null).
     *
     * @return Specification for filtering admin-uploaded documents
     */
    public static Specification<RequestForProposalDocument> adminUploadedDocumentSpecification() {
        return (root, query, criteriaBuilder) -> criteriaBuilder.isNull(root.get("requestForProposal"));
    }
}