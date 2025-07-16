package solutions.techsur.rfpaiservice.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.envers.AuditTable;
import org.hibernate.envers.Audited;
import solutions.techsur.common.microservice.entity.BaseEntity;

import java.util.List;

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
    String role;
}
