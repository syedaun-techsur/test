package solutions.techsur.rfpaiservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.envers.AuditTable;
import org.hibernate.envers.Audited;
import solutions.techsur.common.microservice.entity.BaseEntity;

/**
 * Entity representing a document associated with a Request For Proposal (RFP).
 */
@Table(name = "rfp_documents")
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true, exclude = {"requestForProposal"})
@Setter
@Getter
@Audited
@AuditTable("rfp_documents_aud")
@ToString(callSuper = true, exclude = {"requestForProposal"})
public class RequestForProposalDocument extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "rfp_id")
    private RequestForProposal requestForProposal;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "file_path")
    private String filePath;

    @Transient
    private String role;
}