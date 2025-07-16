package solutions.techsur.rfpaiservice.controller;

import io.swagger.v3.oas.annotations.Operation;
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
    //@PreAuthorize("hasAnyAuthority(@properties.getUploadDocument())")
    @Operation(summary = "Create new compliance matrices", description = "Creates new compliance matrices and returns their IDs.")
    @ApiResponse(responseCode = "201", description = "Compliance matrices created successfully")
    @ApiResponse(responseCode = "400", description = "Invalid request payload")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    public ResponseEntity<List<CreationDTO>> createComplianceMatrices(
            @PathVariable final Integer proposalId,
            @Validated(Validation.CreateValidation.class) @RequestBody final List<ComplianceMatrixRequest> requests) {

        List<CreationDTO> creationDTOS = service.createComplianceMatrix(proposalId, requests)
                .stream()
                .map(complianceMatrix -> CreationDTO.builder().id(complianceMatrix.getId()).build())
                .toList();

        return ResponseEntity.status(HttpStatus.CREATED).body(creationDTOS);
    }

    @GetMapping("/{matrixId}")
    //@PreAuthorize("hasAnyAuthority(@properties.getUploadDocument())")
    @Operation(
            summary = "Get Compliance Matrix by ID",
            description = "Retrieves a Compliance Matrix based on the given matrix ID.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successful retrieval of Compliance Matrix"),
                    @ApiResponse(responseCode = "400", description = "Invalid request parameters"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    public ResponseEntity<ComplianceMatrix> getComplianceMatrix(@PathVariable final Integer matrixId) {
        ComplianceMatrix complianceMatrix = service.getComplianceMatrix(matrixId);
        return ResponseEntity.ok(complianceMatrix);
    }

    @GetMapping("/list")
    //@PreAuthorize("hasAnyAuthority(@properties.getUploadDocument())")
    @Operation(
            summary = "Get filtered Compliance Matrix",
            description = "Retrieves a list of compliance matrices based on the given filter and pagination parameters.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successful retrieval of Compliance Matrix"),
                    @ApiResponse(responseCode = "400", description = "Invalid request parameters"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    public ResponseEntity<Page<ComplianceMatrix>> getComplianceMatrixPage(
            @ParameterObject
            @RequestParam(required = false) final CommonFilter filter,
            @ParameterObject
            @SortDefault(sort = "status", direction = Sort.Direction.ASC)
            final Pageable pageable,
            @RequestParam("proposalId") final Integer proposalId) {

        Page<ComplianceMatrix> page = service.getComplianceMatrixPage(filter, pageable, proposalId);
        return ResponseEntity.ok(page);
    }

    @PutMapping("/{proposalId}")
    //@PreAuthorize("hasAnyAuthority(@properties.getUploadDocument())")
    @Operation(summary = "Update an existing compliance matrix", description = "Updates a compliance matrix based on the given proposal ID.")
    @ApiResponse(responseCode = "200", description = "Compliance matrix updated successfully")
    @ApiResponse(responseCode = "400", description = "Invalid request payload")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "404", description = "Compliance matrix not found")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    public ResponseEntity<Void> updateComplianceMatrix(
            @PathVariable final Integer proposalId,
            @RequestBody final List<ComplianceMatrixRequest> requests) {

        service.updateComplianceMatrix(requests, proposalId);
        return ResponseEntity.ok().build();
    }

}