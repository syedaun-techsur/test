package solutions.techsur.rfpaiservice.repository;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import solutions.techsur.rfpaiservice.entity.ResponseOutline;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ResponseOutlineSpecification {

    public static Specification<ResponseOutline> proposalSpecification(Integer proposalId) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("proposals").get("id"), proposalId);
    }

    public static Specification<ResponseOutline> parentSectionSpecification(Integer proposalId) {
        return (root, query, criteriaBuilder) -> {
            return criteriaBuilder.and(criteriaBuilder.equal(root.get("proposal").get("id"), proposalId), criteriaBuilder.isNull(root.get("parentSection")));
        };
    }

    public static Specification<ResponseOutline> childSpecification(ResponseOutline parent){
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("parentSection"), parent);
    }
}
