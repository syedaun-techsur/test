package solutions.techsur.rfpaiservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import solutions.techsur.common.microservice.dto.Validation;
import solutions.techsur.rfpaiservice.dto.ComplianceMatrixRequest;
import solutions.techsur.rfpaiservice.dto.CreationDTO;
import solutions.techsur.rfpaiservice.dto.CommonFilter;
import solutions.techsur.rfpaiservice.entity.ComplianceMatrix;
import solutions.techsur.rfpaiservice.service.ComplianceMatrixService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/compliance")
@AllArgsConstructor
@Tag(name = "Compliance Matrix", description = "API for managing Request for Compliance Matrix")
public class ComplianceMatrixController {

    private final ComplianceMatrixService service;

    @PostMapping("/{proposalId}")
    @PreAuthorize("hasAnyAuthority(@properties.getUploadDocument())")
    @Operation(summary = "Create new compliance matrices", description = "Creates new compliance matrices and returns their IDs.")
    @ApiResponse(responseCode = "201", description = "Compliance matrices created successfully")
    @ApiResponse(responseCode = "400", description = "Invalid request payload")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    public ResponseEntity<List<CreationDTO>> createComplianceMatrices(@PathVariable Integer proposalId,
            @Validated(Validation.CreateValidation.class) @RequestBody List<ComplianceMatrixRequest> requests) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createComplianceMatrix(proposalId, requests)
                .stream().map(complianceMatrix -> CreationDTO
                        .builder().id(complianceMatrix.getId()).build()).toList());
    }


    @GetMapping("/{matrixId}")
    @PreAuthorize("hasAnyAuthority(@properties.getUploadDocument())")
    @Operation(
            summary = "Get Compliance Matrix by ID",
            description = "Retrieves a Compliance Matrix based on the given rfp ID.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successful retrieval of Compliance Matrix"),
                    @ApiResponse(responseCode = "400", description = "Invalid request parameters"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    public ResponseEntity<ComplianceMatrix> getComplianceMatrix(@PathVariable Integer matrixId) {
        return ResponseEntity.ok(service.getComplianceMatrix(matrixId));
    }

    @GetMapping("/list")
    @PreAuthorize("hasAnyAuthority(@properties.getUploadDocument())")
    @Operation(
            summary = "Get filtered Compliance Matrix",
            description = "Retrieves a list of proposals based on the given filter and pagination parameters.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successful retrieval of Compliance Matrix"),
                    @ApiResponse(responseCode = "400", description = "Invalid request parameters"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    public ResponseEntity<Page<ComplianceMatrix>> getComplianceMatrixPage(CommonFilter filter, @ParameterObject @SortDefault(sort = "status", direction = Sort.Direction.ASC) Pageable pageable, @RequestParam("proposalId") @Parameter(
            description = "Proposal ID to which the document will be attached.", required = true)
    Integer proposalId) {
        return ResponseEntity.ok(service.getComplianceMatrixPage(filter, pageable, proposalId));
    }

    @PutMapping("/{proposalId}")
    @PreAuthorize("hasAnyAuthority(@properties.getUploadDocument())")
    @Operation(summary = "Update an existing compliance matrix", description = "Updates a compliance matrix based on the given ID.")
    @ApiResponse(responseCode = "200", description = "Compliance matrix updated successfully")
    @ApiResponse(responseCode = "400", description = "Invalid request payload")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "404", description = "Compliance matrix not found")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    public ResponseEntity<Void> updateComplianceMatrix(@PathVariable Integer proposalId, @RequestBody List<ComplianceMatrixRequest> request) {
        service.updateComplianceMatrix(request, proposalId);
        return ResponseEntity.ok().build();
    }

}
