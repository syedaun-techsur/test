package solutions.techsur.rfpaiservice.entity;

import com.fasterxml.jackson.annotation.JsonValue;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.envers.AuditTable;
import org.hibernate.envers.Audited;
import solutions.techsur.common.microservice.entity.BaseEntity;

/**
 * Entity representing a response to a Request For Proposal.
 */
@Entity
@Table(name = "rfp_responses")
@Audited
@AuditTable(value = "rfp_responses_aud")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true, exclude = {"outline", "proposal"})
@ToString(callSuper = true, exclude = {"outline", "proposal"})
public class RequestForProposalResponse extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NonNull
    @Column(name = "generated_text", nullable = false)
    private String generatedText;

    @NonNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private ResponseStatus status = ResponseStatus.GENERATED;

    @ManyToOne
    @JoinColumn(name = "rfp_id", referencedColumnName = "id")
    private RequestForProposal proposal;

    @ManyToOne
    @JoinColumn(name = "outline_id", referencedColumnName = "id")
    private ResponseOutline outline;

    /**
     * Status of the response.
     */
    public enum ResponseStatus {
        GENERATED("Generated"),
        REVIEWED("Reviewed"),
        FINALIZED("Finalized");

        private final String value;

        ResponseStatus(String value) {
            this.value = value;
        }

        @JsonValue
        public String getValue() {
            return value;
        }
    }
}