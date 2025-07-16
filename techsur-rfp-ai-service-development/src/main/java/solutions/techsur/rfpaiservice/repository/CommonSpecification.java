package solutions.techsur.rfpaiservice.repository;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.CriteriaQuery;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CommonSpecification {

    /**
     * Creates a specification that always evaluates to true.
     *
     * @param <T> the entity type
     * @return a Specification that always evaluates to true
     */
    public static <T> Specification<T> alwaysTrue() {
        return (root, query, criteriaBuilder) -> criteriaBuilder.isTrue(criteriaBuilder.literal(true));
    }

    /**
     * Creates a specification that checks if a given field equals the provided value.
     *
     * @param <T> the entity type
     * @param field the entity field to compare
     * @param value the value to compare against
     * @return a Specification checking for equality on the field
     */
    public static <T> Specification<T> isEqualTo(String field, Object value) {
        if (value == null) {
            return (root, query, criteriaBuilder) -> criteriaBuilder.isNull(root.get(field));
        }
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(field), value);
    }
}