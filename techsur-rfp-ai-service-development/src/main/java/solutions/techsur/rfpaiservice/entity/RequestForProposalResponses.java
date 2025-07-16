package solutions.techsur.rfpaiservice.entity;


import com.fasterxml.jackson.annotation.JsonValue;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.envers.AuditTable;
import org.hibernate.envers.Audited;
import solutions.techsur.common.microservice.entity.BaseEntity;

@Table(name = "rfp_responses")
@Entity
@EqualsAndHashCode(callSuper = true, exclude = {"outline", "proposal"})
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Audited
@AuditTable(value = "rfp_responses_aud")
@ToString(callSuper = true, exclude = {"outline", "proposal"})
public class RequestForProposalResponses extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "generated_text", nullable = false)
    private String generatedText;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private ResponsesStatus status = ResponsesStatus.GENERATED;

    @ManyToOne
    @JoinColumn(name = "rfp_id", referencedColumnName = "id")
    private RequestForProposal proposal;

    @ManyToOne
    @JoinColumn(name = "outline_id", referencedColumnName = "id")
    private ResponseOutline outline;


    public enum ResponsesStatus {
        GENERATED("Generated"),
        REVIEWED("Reviewed"),
        FINALIZED("Finalized");

        private final String value;

        ResponsesStatus(String value) {
            this.value = value;
        }

        @JsonValue  // Ensures this value is used in JSON responses
        public String getValue() {
            return value;
        }
    }
}
