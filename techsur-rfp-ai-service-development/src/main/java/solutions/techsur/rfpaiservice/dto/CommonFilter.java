package solutions.techsur.rfpaiservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springdoc.core.annotations.ParameterObject;

/**
 * Represents common filtering parameters for API requests.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ParameterObject
public class CommonFilter {

    /**
     * The search term to filter results.
     */
    private String search;
}