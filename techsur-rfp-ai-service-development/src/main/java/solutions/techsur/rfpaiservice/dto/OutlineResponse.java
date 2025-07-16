package solutions.techsur.rfpaiservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.util.List;

/**
 * Response DTO representing an outline with a title and a list of sections.
 */
@Getter
@Setter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@ToString
@EqualsAndHashCode
@JsonPropertyOrder({"outlineTitle", "sections"})
public class OutlineResponse {

    @JsonProperty("outline_title")
    private String outlineTitle;

    @JsonProperty("sections")
    private List<SectionResponse> sections;
}