package solutions.techsur.rfpaiservice.repository;

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
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(criteriaBuilder.or(criteriaBuilder.like(root.get("title"), "%" + filter.getSearch().toLowerCase() + "%")));
            predicates.add(criteriaBuilder.like(root.get("description"), "%" + filter.getSearch().toLowerCase() + "%"));
            predicates.add(criteriaBuilder.like(root.get("solicitationId"), "%" + filter.getSearch().toLowerCase() + "%"));

            try {
                LocalDate date = LocalDate.parse(filter.getSearch());
                predicates.add(criteriaBuilder.equal(root.get("deadline"), date));
            } catch (Exception ex) {
                log.warn("Error while parsing string to date");
            }
            return criteriaBuilder.or(predicates.toArray(new Predicate[0]));
        };
    }

}
