package solutions.techsur.rfpaiservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.Collections;
import java.util.List;

@Getter
@Setter
@Builder
@JsonPropertyOrder({
        "sub_section_id",
        "sub_section_number",
        "sub_section_title",
        "requirement",
        "context",
        "parent_id",
        "subsections",
        "generated_content",
        "content",
        "last_updated_date",
        "section_purpose",
        "instructions_to_writer",
        "source_mapping",
        "win_theme_alignment"
})
public class SubSectionResponse {

    @JsonProperty("sub_section_id")
    private Integer outlineSubSectionId;

    @JsonProperty("sub_section_number")
    private String subSectionNumber;

    @JsonProperty("sub_section_title")
    private String subSectionTitle;

    private String requirement;

    private String context;

    @JsonProperty("parent_id")
    private Integer parentId;

    @Default
    private List<SubSectionResponse> subsections = Collections.emptyList();

    @JsonProperty("generated_content")
    private boolean generatedContent;

    private String content;

    @JsonProperty("last_updated_date")
    private Instant lastUpdatedDate;

    @JsonProperty("section_purpose")
    private String sectionPurpose;

    @JsonProperty("instructions_to_writer")
    private String instructionsToWriter;

    @JsonProperty("source_mapping")
    @Default
    private List<String> sourceMapping = Collections.emptyList();

    @JsonProperty("win_theme_alignment")
    @Default
    private List<String> winThemeAlignment = Collections.emptyList();
}