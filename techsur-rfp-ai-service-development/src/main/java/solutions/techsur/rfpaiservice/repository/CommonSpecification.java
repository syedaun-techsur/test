package solutions.techsur.rfpaiservice.repository;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

/**
 * Utility class providing common JPA Specifications for querying.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CommonSpecification {

    /**
     * Returns a Specification that always evaluates to true.
     *
     * @param <T> the entity type
     * @return a Specification that is always true
     */
    public static <T> Specification<T> alwaysTrue() {
        return (root, query, cb) -> cb.isTrue(cb.literal(true));
    }

    /**
     * Returns a Specification that checks if the given field's value is equal to the provided value.
     *
     * @param <T>   the entity type
     * @param field the entity field name to compare
     * @param value the value to compare the field against
     * @return a Specification that checks field equality
     */
    @SuppressWarnings("unchecked")
    public static <T> Specification<T> isEqualTo(String field, Object value) {
        return (root, query, cb) -> cb.equal(root.get(field), value);
    }
}