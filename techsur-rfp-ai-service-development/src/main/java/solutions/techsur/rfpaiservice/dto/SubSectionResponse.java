package solutions.techsur.rfpaiservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.List;

@Getter
@Setter
@Builder
@JsonPropertyOrder({"subSectionId", "subSectionNumber", "subSectionTitle", "requirement", "context", "parentId", "subsections"})
public class SubSectionResponse {

    private Integer outlineSubSectionId;

    @JsonProperty("subsection_id")
    private String subSectionNumber;

    @JsonProperty("subsection_title")
    private String subSectionTitle;

    private String requirement;

    private String context;

    private Integer parentId;

    List<SubSectionResponse> subsections;

    private boolean isGeneratedContent;

    private String content;

    private Instant lastUpdatedDate;

    @JsonProperty("section_purpose")
    private String sectionPurpose;

    @JsonProperty("instructions_to_writer")
    private String instructionsToWriter;

    @JsonProperty("source_mapping")
    private List<String> sourceMapping;

    @JsonProperty("win_theme_alignment")
    private List<String> winThemeAlignment;

}
