package solutions.techsur.rfpaiservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO class representing a request for Response Outline.
 */
@Setter
@Getter
public class ResponseOutlineRequest {

    /**
     * Section number of the response outline.
     */
    @NotBlank(message = "Section number is required")
    private String sectionNo;

    /**
     * Section title of the response outline.
     */
    @NotBlank(message = "Section title is required")
    private String sectionTitle;

    /**
     * Requirement related to the response outline.
     */
    @NotBlank(message = "Requirement is required")
    private String requirement;

    /**
     * Context title providing additional information.
     */
    @NotBlank(message = "Context title is required")
    private String context;

    /**
     * Optional parent section ID to establish hierarchy.
     */
    private Integer parentSectionId;
}