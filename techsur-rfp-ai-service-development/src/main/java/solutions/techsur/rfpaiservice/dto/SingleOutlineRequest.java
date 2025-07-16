package solutions.techsur.rfpaiservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SingleOutlineRequest {

    @JsonProperty("subsection_title")
    private String subsectionTitle;

    @JsonProperty("subsection_id")
    private String subsectionId;

    private String requirement;

    private String context;

    @JsonProperty("is_generated_content")
    private boolean generatedContent;

    private String content;

    @JsonProperty("section_purpose")
    private String sectionPurpose;

    @JsonProperty("instructions_to_writer")
    private String instructionsToWriter;

    @JsonProperty("source_mapping")
    private List<String> sourceMapping;

    @JsonProperty("win_theme_alignment")
    private List<String> winThemeAlignment;
}