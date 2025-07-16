package solutions.techsur.rfpaiservice.repository;

import jakarta.persistence.criteria.Predicate;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import solutions.techsur.rfpaiservice.dto.CommonFilter;
import solutions.techsur.rfpaiservice.entity.RequestForProposal;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Slf4j
public class RFPSpecification {

    public static Specification<RequestForProposal> multiFieldSearch(CommonFilter filter) {
        return (root, query, criteriaBuilder) -> {
            String search = filter.getSearch();
            if (search == null || search.trim().isEmpty()) {
                return criteriaBuilder.conjunction();
            }

            String searchLower = search.toLowerCase();
            List<Predicate> predicates = new ArrayList<>();

            // Use criteriaBuilder.lower for case-insensitive matching on fields
            Predicate titlePredicate = criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("title")), "%" + searchLower + "%");
            Predicate descriptionPredicate = criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("description")), "%" + searchLower + "%");
            Predicate solicitationIdPredicate = criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("solicitationId")), "%" + searchLower + "%");

            predicates.add(criteriaBuilder.or(titlePredicate, descriptionPredicate, solicitationIdPredicate));

            try {
                LocalDate date = LocalDate.parse(search);
                predicates.add(criteriaBuilder.equal(root.get("deadline"), date));
            } catch (DateTimeParseException ex) {
                log.warn("Unable to parse search string to date: {}", ex.getMessage());
            }

            return criteriaBuilder.or(predicates.toArray(new Predicate[0]));
        };
    }

}