package solutions.techsur.rfpaiservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.util.List;

/**
 * Response DTO representing an outline with a title and a list of sections.
 */
@Getter
@SuperBuilder
@AllArgsConstructor
@JsonPropertyOrder({"outline_title", "sections"}) // Ensures outline_title appears first in JSON
public class OutlineResponse {

    @JsonProperty("outline_title")
    private final String outlineTitle;

    @JsonProperty("sections")
    private final List<SectionResponse> sections;

}