package solutions.techsur.rfpaiservice.repository;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import solutions.techsur.rfpaiservice.dto.CommonFilter;
import solutions.techsur.rfpaiservice.entity.RequestForProposal;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Slf4j
public class RFPSpecification {

    public static Specification<RequestForProposal> multiFieldSearch(CommonFilter filter) {
        return (root, query, criteriaBuilder) -> {
            String searchTerm = filter.getSearch();
            if (searchTerm == null || searchTerm.trim().isEmpty()) {
                return criteriaBuilder.conjunction(); // Return always true predicate if no search term
            }
            String loweredSearchTerm = searchTerm.toLowerCase();

            List<Predicate> predicates = new ArrayList<>();
            
            // Case-insensitive like predicates for string fields
            predicates.add(criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("title")), "%" + loweredSearchTerm + "%"));
            predicates.add(criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("description")), "%" + loweredSearchTerm + "%"));
            predicates.add(criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("solicitationId")), "%" + loweredSearchTerm + "%"));

            // Try parsing the search term as date and add equality predicate if valid
            try {
                LocalDate date = LocalDate.parse(searchTerm);
                predicates.add(criteriaBuilder.equal(root.get("deadline"), date));
            } catch (Exception ex) {
                log.warn("Error while parsing string to date: {}", ex.getMessage());
            }

            // Combine predicates with OR operator
            return criteriaBuilder.or(predicates.toArray(new Predicate[0]));
        };
    }

}