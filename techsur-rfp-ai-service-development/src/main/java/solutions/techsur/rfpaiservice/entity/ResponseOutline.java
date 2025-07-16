package solutions.techsur.rfpaiservice.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.envers.AuditTable;
import org.hibernate.envers.Audited;
import solutions.techsur.common.microservice.entity.BaseEntity;
import solutions.techsur.rfpaiservice.utils.StringListToJsonConverter;

import java.util.ArrayList;
import java.util.List;

@Table(name = "response_outline")
@Entity
@EqualsAndHashCode(callSuper = true, exclude = {"proposal", "childSection", "parentSection"})
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Data
@Audited
@AuditTable(value = "response_outline_aud")
@ToString(callSuper = true, exclude = {"proposal", "childSection", "parentSection"})
public class ResponseOutline extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "section_no", nullable = false)
    private String sectionNo;

    @Column(name = "section_title ", nullable = false)
    private String sectionTitle;

    @Column(name = "requirement", nullable = false)
    private String requirement;

    @Column(name = "context", nullable = false)
    private String context;

    @ManyToOne
    @JoinColumn(name = "parent_section_id", referencedColumnName = "id")
    @JsonBackReference
    @JsonProperty("section")
    private ResponseOutline parentSection;

    @OneToMany(mappedBy = "parentSection", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    @Builder.Default
    @JsonProperty("subsection")
    private List<ResponseOutline> childSection = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference
    @JoinColumn(name = "rfp_id", referencedColumnName = "id")
    private RequestForProposal proposal;

    @Column(name = "is_generated_content", nullable = false)
    @Builder.Default
    private boolean isGeneratedContent = false;

    @Column(name = "content")
    private String content;

    @Column(name = "section_purpose", columnDefinition = "TEXT")
    private String sectionPurpose;

    @Column(name = "instructions_to_writer", columnDefinition = "TEXT")
    private String instructionsToWriter;

    @Convert(converter = StringListToJsonConverter.class)
    @Column(name = "source_mapping", columnDefinition = "TEXT")
    @Builder.Default
    private List<String> sourceMapping = new ArrayList<>();

    @Convert(converter = StringListToJsonConverter.class)
    @Column(name = "win_theme_alignment", columnDefinition = "TEXT")
    @Builder.Default
    private List<String> winThemeAlignment = new ArrayList<>();

}
