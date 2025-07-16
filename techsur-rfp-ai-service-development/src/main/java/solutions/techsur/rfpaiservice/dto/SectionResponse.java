package solutions.techsur.rfpaiservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@JsonPropertyOrder({
    "sectionTitle", "sectionId", "sectionNumber", "subsections", "requirement",
    "context", "parentId", "content", "lastUpdatedDate",
    "sectionPurpose", "instructionsToWriter", "sourceMapping", "winThemeAlignment"
}) // Ensures order
public class SectionResponse {

    @JsonProperty("section_title")
    private String sectionTitle;

    @JsonProperty("section_id")
    private Integer sectionId;

    @JsonProperty("section_number")
    private String sectionNumber;

    private String requirement;

    private String context;

    private Integer parentId;

    private List<SubSectionResponse> subsections = new ArrayList<>();

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