package solutions.techsur.rfpaiservice.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import solutions.techsur.rfpaiservice.entity.RequestForProposalDocument;

import java.util.List;

/**
 * Response DTO for Proposal Document containing uploaded and blue book documents.
 */
@Setter
@Getter
@SuperBuilder
@Builder
@NoArgsConstructor
public class ProposalDocumentResponse {

    /**
     * List of uploaded proposal documents.
     */
    private List<RequestForProposalDocument> uploadedDocuments;

    /**
     * List of blue book documents related to the proposal.
     */
    private List<RequestForProposalDocument> blueBookDocuments;
}