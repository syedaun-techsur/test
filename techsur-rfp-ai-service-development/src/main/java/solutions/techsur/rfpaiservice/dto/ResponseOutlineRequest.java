package solutions.techsur.rfpaiservice.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO representing a request for response outline sections.
 * Validates that key fields are not null to ensure integrity of request.
 */
@Getter
@Setter
public class ResponseOutlineRequest {

    @NotNull(message = "Section number must not be null")
    private String sectionNo;

    @NotNull(message = "Section title must not be null")
    private String sectionTitle;

    @NotNull(message = "Requirement must not be null")
    private String requirement;

    @NotNull(message = "Context must not be null")
    private String context;

    private Integer parentSectionId;
}