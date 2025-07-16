package solutions.techsur.rfpaiservice.repository;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import solutions.techsur.rfpaiservice.entity.ResponseOutline;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ResponseOutlineSpecification {

    public static Specification<ResponseOutline> proposalSpecification(Integer proposalId) {
        return (Root<ResponseOutline> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) ->
                criteriaBuilder.equal(root.get("proposal").get("id"), proposalId);
    }

    public static Specification<ResponseOutline> parentSectionSpecification(Integer proposalId) {
        return (Root<ResponseOutline> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) ->
                criteriaBuilder.and(
                        criteriaBuilder.equal(root.get("proposal").get("id"), proposalId),
                        criteriaBuilder.isNull(root.get("parentSection"))
                );
    }

    public static Specification<ResponseOutline> childSpecification(ResponseOutline parent) {
        return (Root<ResponseOutline> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) ->
                criteriaBuilder.equal(root.get("parentSection"), parent);
    }
}