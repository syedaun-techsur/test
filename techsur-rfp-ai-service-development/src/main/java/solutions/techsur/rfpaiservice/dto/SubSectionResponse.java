package solutions.techsur.rfpaiservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;
import lombok.Getter;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@JsonPropertyOrder({
        "subSectionId",
        "subSectionNumber",
        "subSectionTitle",
        "requirement",
        "context",
        "parentId",
        "subsections",
        "isGeneratedContent",
        "content",
        "lastUpdatedDate",
        "sectionPurpose",
        "instructionsToWriter",
        "sourceMapping",
        "winThemeAlignment"
})
public class SubSectionResponse {

    @JsonProperty("subSectionId")
    private Integer outlineSubSectionId;

    @JsonProperty("subSectionNumber")
    private String subSectionNumber;

    @JsonProperty("subSectionTitle")
    private String subSectionTitle;

    @JsonProperty("requirement")
    private String requirement;

    @JsonProperty("context")
    private String context;

    @JsonProperty("parentId")
    private Integer parentId;

    @Builder.Default
    @JsonProperty("subsections")
    private List<SubSectionResponse> subsections = new ArrayList<>();

    @JsonProperty("isGeneratedContent")
    private boolean isGeneratedContent;

    @JsonProperty("content")
    private String content;

    @JsonProperty("lastUpdatedDate")
    private Instant lastUpdatedDate;

    @JsonProperty("sectionPurpose")
    private String sectionPurpose;

    @JsonProperty("instructionsToWriter")
    private String instructionsToWriter;

    @Builder.Default
    @JsonProperty("sourceMapping")
    private List<String> sourceMapping = new ArrayList<>();

    @Builder.Default
    @JsonProperty("winThemeAlignment")
    private List<String> winThemeAlignment = new ArrayList<>();

}