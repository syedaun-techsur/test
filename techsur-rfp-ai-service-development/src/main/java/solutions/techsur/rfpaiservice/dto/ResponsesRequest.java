package solutions.techsur.rfpaiservice.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import solutions.techsur.common.microservice.dto.Validation;
import solutions.techsur.rfpaiservice.entity.RequestForProposalResponses;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class ResponsesRequest {
    @NotNull(groups = Validation.CreateValidation.class, message = "Generated text is required")
    private String generatedText;

    @Builder.Default
    private RequestForProposalResponses.ResponsesStatus status = RequestForProposalResponses.ResponsesStatus.GENERATED;

    private Integer proposalId;

    private Integer outlineId;
}
