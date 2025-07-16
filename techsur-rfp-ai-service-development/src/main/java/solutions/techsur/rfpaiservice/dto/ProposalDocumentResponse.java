package solutions.techsur.rfpaiservice.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.repository.NoRepositoryBean;
import solutions.techsur.rfpaiservice.entity.RequestForProposalDocument;

import java.util.List;

@Setter
@Getter
@SuperBuilder
@NoArgsConstructor
public class ProposalDocumentResponse {
    List<RequestForProposalDocument> uploadedDocument;
    List<RequestForProposalDocument> blueBookDocument;
}
