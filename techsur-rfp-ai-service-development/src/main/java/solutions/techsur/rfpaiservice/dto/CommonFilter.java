package solutions.techsur.rfpaiservice.dto;

import lombok.Getter;
import lombok.Setter;
import org.springdoc.core.annotations.ParameterObject;
import java.util.Objects;

/**
 * Common filter DTO used for generic search criteria across different APIs.
 */
@Getter
@Setter
@ParameterObject
public class CommonFilter {

    /**
     * Search criteria string used for filtering results.
     */
    private String search;

    public CommonFilter() {
        // No-args constructor for framework usage
    }

    @Override
    public String toString() {
        return "CommonFilter{" +
                "search='" + search + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CommonFilter)) return false;
        CommonFilter that = (CommonFilter) o;
        return Objects.equals(search, that.search);
    }

    @Override
    public int hashCode() {
        return Objects.hash(search);
    }
}