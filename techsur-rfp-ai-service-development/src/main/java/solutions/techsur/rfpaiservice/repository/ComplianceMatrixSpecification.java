package solutions.techsur.rfpaiservice.repository;

import jakarta.persistence.criteria.Predicate;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import solutions.techsur.rfpaiservice.dto.CommonFilter;
import solutions.techsur.rfpaiservice.entity.ComplianceMatrix;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ComplianceMatrixSpecification {

    public static Specification<ComplianceMatrix> multiFieldSearch(CommonFilter filter) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(criteriaBuilder.or(criteriaBuilder.like(root.get("requirement"), "%" + filter.getSearch().toLowerCase() + "%")));
            predicates.add(criteriaBuilder.like(root.get("justification"), "%" + filter.getSearch().toLowerCase() + "%"));
            return criteriaBuilder.or(predicates.toArray(new Predicate[0]));
        };
    }

    public static Specification<ComplianceMatrix> proposalSpecification(Integer proposalId) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("proposal").get("id"), proposalId);
    }
}
