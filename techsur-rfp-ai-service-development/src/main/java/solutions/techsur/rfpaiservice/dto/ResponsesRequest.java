package solutions.techsur.rfpaiservice.dto;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import solutions.techsur.common.microservice.dto.Validation;
import solutions.techsur.rfpaiservice.entity.RequestForProposalResponses;

/**
 * Data Transfer Object for responses request.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Builder
public class ResponsesRequest {

    @NotNull(groups = Validation.CreateValidation.class, message = "Generated text is required")
    @Setter(AccessLevel.NONE)
    private String generatedText;

    @Builder.Default
    @Setter(AccessLevel.NONE)
    private RequestForProposalResponses.ResponsesStatus status = RequestForProposalResponses.ResponsesStatus.GENERATED;

    @Nullable
    private Integer proposalId;

    @Nullable
    private Integer outlineId;
}