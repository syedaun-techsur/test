package solutions.techsur.rfpaiservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import solutions.techsur.common.microservice.dto.Validation;
import solutions.techsur.rfpaiservice.entity.RequestForProposal;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProposalRequest {
    @NotBlank(groups = Validation.CreateValidation.class, message = "Title is required")
    private String title;

    @NotBlank(groups = Validation.CreateValidation.class, message = "Description is required")
    private String description;

    @NotNull(groups = Validation.CreateValidation.class, message = "Deadline cannot be null")
    private LocalDate deadline;

    @NotBlank(groups = Validation.CreateValidation.class, message = "Solicitation id is required")
    private String solicitationId;

    @NotNull(groups = Validation.CreateValidation.class, message = "Status cannot be null")
    @Builder.Default
    private final RequestForProposal.Status status = RequestForProposal.Status.UPLOADED;
}