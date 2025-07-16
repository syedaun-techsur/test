package solutions.techsur.rfpaiservice.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ResponseOutlineRequest {
    @NotNull(message = "Section number is required")
    private String sectionNo;

    @NotNull(message = "Section title is required")
    private String sectionTitle;

    @NotNull(message = "Requirement is required")
    private String requirement;

    @NotNull(message = "Context title is required")
    private String context;

    private Integer parentSectionId;
}
