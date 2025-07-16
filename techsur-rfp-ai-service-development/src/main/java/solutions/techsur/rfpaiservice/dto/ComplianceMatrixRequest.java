package solutions.techsur.rfpaiservice.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import solutions.techsur.common.microservice.dto.Validation;
import solutions.techsur.rfpaiservice.entity.ComplianceMatrix;

/**
 * DTO representing a request for Compliance Matrix operations.
 */
@Getter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class ComplianceMatrixRequest {

    /**
     * Requirement description, mandatory during creation.
     */
    @NotBlank(groups = Validation.CreateValidation.class, message = "Requirement is required")
    private String requirement;

    /**
     * Compliance status, defaults to FAIL.
     */
    @Builder.Default
    private ComplianceMatrix.ComplianceStatus status = ComplianceMatrix.ComplianceStatus.FAIL;

    /**
     * Justification for the compliance status.
     */
    private String justification;

    /**
     * Section number associated with the compliance item.
     */
    private String sectionNo;
}