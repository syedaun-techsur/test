package solutions.techsur.rfpaiservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@JsonPropertyOrder({"outlineTitle", "sections"}) // Ensures outlineTitle appears first
public class OutlineResponse {
    @JsonProperty("outline_title")
    private String outlineTitle;
    private List<SectionResponse> sections;
}
