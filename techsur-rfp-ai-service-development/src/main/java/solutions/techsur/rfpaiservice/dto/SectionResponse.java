package solutions.techsur.rfpaiservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.Singular;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@JsonPropertyOrder({"sectionTitle", "sectionId", "sectionNumber", "subsections", "requirement", "context", "parentId"}) // Ensures order
public class SectionResponse {
    @JsonProperty("section_title")
    private String sectionTitle;

    @JsonProperty("section_id")
    private String sectionNumber;

    private Integer outlineSectionId;

    private String requirement;

    private String context;

    private Integer parentId;

    @Singular
    private List<SubSectionResponse> subsections;

    private String content;

    private Instant lastUpdatedDate;

    @JsonProperty("section_purpose")
    private String sectionPurpose;

    @JsonProperty("instructions_to_writer")
    private String instructionsToWriter;

    @Singular
    @JsonProperty("source_mapping")
    private List<String> sourceMapping;

    @Singular
    @JsonProperty("win_theme_alignment")
    private List<String> winThemeAlignment;
}