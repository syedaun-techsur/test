package solutions.techsur.rfpaiservice.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.SuperBuilder;
import solutions.techsur.common.microservice.dto.Validation;
import solutions.techsur.rfpaiservice.entity.ComplianceMatrix;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class ComplianceMatrixRequest {
    @NotBlank(groups = Validation.CreateValidation.class, message = "Requirement is required")
    private String requirement;

    @Builder.Default
    private ComplianceMatrix.ComplianceStatus status = ComplianceMatrix.ComplianceStatus.FAIL;

    private String justification;

    private String sectionNo;
}
