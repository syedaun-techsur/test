package solutions.techsur.rfpaiservice.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonValue;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.envers.AuditTable;
import org.hibernate.envers.Audited;
import solutions.techsur.common.microservice.entity.BaseEntity;

@Entity
@Table(name = "compliance_matrix")
@Audited
@AuditTable(value = "compliance_matrix_aud")
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ComplianceMatrix extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 255, nullable = false)
    @NonNull
    private String requirement;

    @Column(name = "compliance_status", length = 10, nullable = false)
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private ComplianceStatus status = ComplianceStatus.PASS;

    @Column(length = 255)
    private String justification;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rfp_id", referencedColumnName = "id", nullable = false)
    @JsonBackReference
    @NonNull
    private RequestForProposal proposal;

    @Column(name = "section_no", length = 50)
    private String sectionNo;

    /**
     * Enum representing compliance status with JSON serialization support.
     */
    public enum ComplianceStatus {
        FAIL("Fail"),
        PASS("Pass");

        private final String value;

        ComplianceStatus(String value) {
            this.value = value;
        }

        @JsonValue
        public String getValue() {
            return value;
        }

        @Override
        public String toString() {
            return value;
        }
    }
}