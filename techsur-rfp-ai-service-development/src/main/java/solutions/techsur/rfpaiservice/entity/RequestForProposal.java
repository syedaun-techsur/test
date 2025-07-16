package solutions.techsur.rfpaiservice.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonValue;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.envers.AuditTable;
import org.hibernate.envers.Audited;
import solutions.techsur.common.microservice.entity.BaseEntity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Table(name = "rfps")
@Entity
@EqualsAndHashCode(callSuper = true, exclude = {"responseOutlines", "complianceMatrices"})
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Setter
@Getter
@Audited
@AuditTable(value = "rfps_aud")
@ToString(callSuper = true, exclude = {"responseOutlines", "complianceMatrices"})
public class RequestForProposal extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "title")
    private String title;
    private String description;
    @Column(name = "dead_line", nullable = false)
    private LocalDate deadline;

    @Enumerated(EnumType.STRING)  // Store enum as String
    @Column(nullable = false)
    @Builder.Default
    private Status status = Status.UPLOADED;

    @Column(name = "solicitation_id", nullable = false)
    private String solicitationId;

    @Column(name = "is_archived", columnDefinition = "boolean default false", nullable = false)
    @Builder.Default
    private boolean isArchived = false;

    @OneToMany(mappedBy = "proposal", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    @JsonManagedReference
    private List<ResponseOutline> responseOutlines = new ArrayList<>();

    @OneToMany(mappedBy = "proposal", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<ComplianceMatrix> complianceMatrices = new ArrayList<>();

    public enum Status {
        UPLOADED("Uploaded"),
        PROCESSING("Processing"),
        UNDER_REVIEW("Under Review"),
        FINALIZED("Finalized");

        private final String dbValue;

        Status(String dbValue) {
            this.dbValue = dbValue;
        }

        @JsonValue  // Store string representation in JSON responses
        public String getDbValue() {
            return dbValue;
        }

        @JsonCreator  // Convert string back to Enum when reading JSON
        public static Status fromDbValue(String dbValue) {
            for (Status status : values()) {
                if (status.dbValue.equalsIgnoreCase(dbValue)) {
                    return status;
                }
            }
            throw new IllegalArgumentException("Unknown status: " + dbValue);
        }
    }
}
