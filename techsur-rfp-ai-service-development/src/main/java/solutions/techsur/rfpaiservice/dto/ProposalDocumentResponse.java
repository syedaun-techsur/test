package solutions.techsur.rfpaiservice.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Singular;
import java.util.List;
import solutions.techsur.rfpaiservice.entity.RequestForProposalDocument;

@Setter
@Getter
@Builder
@NoArgsConstructor
public final class ProposalDocumentResponse {

    @Singular
    private final List<RequestForProposalDocument> uploadedDocument;

    @Singular
    private final List<RequestForProposalDocument> blueBookDocument;
}