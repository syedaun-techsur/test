package solutions.techsur.rfpaiservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Data transfer object representing a single outline request.
 */
@Getter
@Setter
public class SingleOutlineRequest {

    /**
     * Title of the subsection.
     */
    @JsonProperty("subsection_title")
    private String sectionTitle;

    /**
     * Identifier of the subsection.
     */
    @JsonProperty("subsection_id")
    private String sectionNo;

    /**
     * Requirement detail for this outline request.
     */
    private String requirement;

    /**
     * Context information related to this outline request.
     */
    private String context;

    /**
     * Flag indicating if the content is generated.
     */
    @JsonProperty("is_generated_content")
    private boolean generatedContent;

    /**
     * Content related to the outline request.
     */
    private String content;

    /**
     * Purpose of the section.
     */
    @JsonProperty("section_purpose")
    private String sectionPurpose;

    /**
     * Instructions directed to the writer.
     */
    @JsonProperty("instructions_to_writer")
    private String instructionsToWriter;

    /**
     * Source mapping strings.
     */
    @JsonProperty("source_mapping")
    private List<String> sourceMapping;

    /**
     * Win theme alignment strings.
     */
    @JsonProperty("win_theme_alignment")
    private List<String> winThemeAlignment;
}