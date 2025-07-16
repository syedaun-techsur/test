package solutions.techsur.rfpaiservice.repository;

import org.springframework.data.jpa.domain.Specification;
import solutions.techsur.rfpaiservice.entity.RequestForProposalDocument;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

public class RFPDocumentSpecification {

    private RFPDocumentSpecification() {
        // Private constructor to prevent instantiation
    }

    public static Specification<RequestForProposalDocument> findByProposalIdSpecification(Integer proposalId) {
        return new Specification<RequestForProposalDocument>() {
            @Override
            public javax.persistence.criteria.Predicate toPredicate(Root<RequestForProposalDocument> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                return criteriaBuilder.equal(root.get("requestForProposal").get("id"), proposalId);
            }
        };
    }

    public static Specification<RequestForProposalDocument> adminUploadedDocumentSpecification() {
        return new Specification<RequestForProposalDocument>() {
            @Override
            public javax.persistence.criteria.Predicate toPredicate(Root<RequestForProposalDocument> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                return criteriaBuilder.isNull(root.get("requestForProposal"));
            }
        };
    }
}