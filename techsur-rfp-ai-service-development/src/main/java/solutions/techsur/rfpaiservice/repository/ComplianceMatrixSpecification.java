package solutions.techsur.rfpaiservice.repository;

import jakarta.persistence.criteria.Predicate;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import solutions.techsur.rfpaiservice.dto.CommonFilter;
import solutions.techsur.rfpaiservice.entity.ComplianceMatrix;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ComplianceMatrixSpecification {

    public static Specification<ComplianceMatrix> multiFieldSearch(CommonFilter filter) {
        return (root, query, criteriaBuilder) -> {
            String search = filter.getSearch();
            if (search == null || search.trim().isEmpty()) {
                // No search term provided, return empty predicate (always true)
                return criteriaBuilder.conjunction();
            }

            String searchPattern = "%" + search.toLowerCase() + "%";

            List<Predicate> predicates = new ArrayList<>();

            // Case-insensitive search on "requirement"
            predicates.add(criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("requirement")),
                    searchPattern));

            // Case-insensitive search on "justification"
            predicates.add(criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("justification")),
                    searchPattern));

            // Combine predicates with OR
            return criteriaBuilder.or(predicates.toArray(new Predicate[0]));
        };
    }

    public static Specification<ComplianceMatrix> proposalSpecification(Integer proposalId) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("proposal").get("id"), proposalId);
    }
}