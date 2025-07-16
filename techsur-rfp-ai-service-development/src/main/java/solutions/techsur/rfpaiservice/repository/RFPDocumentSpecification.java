package solutions.techsur.rfpaiservice.repository;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import solutions.techsur.rfpaiservice.entity.RequestForProposalDocument;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RFPDocumentSpecification {

    public static Specification<RequestForProposalDocument> findRFPByProposalSpecification(Integer proposal) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("requestForProposal").get("id"), proposal);
    }

    public static Specification<RequestForProposalDocument> adminUploadedDocumenteSpecification() {
        return (root, query, criteriaBuilder) -> criteriaBuilder.isNull(root.get("requestForProposal"));
    }
}
