package solutions.techsur.rfpaiservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SingleOutlineRequest {

    @JsonProperty("subsection_title")
    private String sectionTitle;

    @JsonProperty("subsection_id")
    private String sectionNo;

    private String requirement;

    private String context;

    private boolean isGeneratedContent;

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
