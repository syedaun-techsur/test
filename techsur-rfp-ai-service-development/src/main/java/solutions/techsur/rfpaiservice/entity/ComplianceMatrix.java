package solutions.techsur.rfpaiservice.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonValue;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.envers.AuditTable;
import org.hibernate.envers.Audited;
import solutions.techsur.common.microservice.entity.BaseEntity;

@Table(name = "compliance_matrix")
@Entity
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Audited
@AuditTable(value = "compliance_matrix_aud")
@ToString(callSuper = true)
public class ComplianceMatrix extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String requirement;

    @Column(name = "compliance_status")
    @Builder.Default
    @Enumerated(EnumType.STRING)
    private ComplianceStatus status = ComplianceStatus.PASS;

    private String justification;

    @ManyToOne
    @JoinColumn(name = "rfp_id", referencedColumnName = "id")
    @JsonBackReference
    private RequestForProposal proposal;

    @Column(name = "section_no")
    private String sectionNo;

    public enum ComplianceStatus {
        FAIL("Fail"), PASS("Pass");

        private final String value;

        ComplianceStatus(String value) {
            this.value = value;
        }

        @JsonValue  // Ensures this value is used in JSON responses
        public String getValue() {
            return value;
        }
    }
}
