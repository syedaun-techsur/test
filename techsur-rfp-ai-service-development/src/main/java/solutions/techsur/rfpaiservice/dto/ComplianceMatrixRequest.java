package solutions.techsur.rfpaiservice.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import solutions.techsur.common.microservice.dto.Validation;
import solutions.techsur.rfpaiservice.entity.ComplianceMatrix;

/**
 * DTO representing a request to create or update a compliance matrix entry.
 */
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class ComplianceMatrixRequest {

    /**
     * The requirement description.
     */
    @NonNull
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
     * Corresponding section number.
     */
    private String sectionNo;
}