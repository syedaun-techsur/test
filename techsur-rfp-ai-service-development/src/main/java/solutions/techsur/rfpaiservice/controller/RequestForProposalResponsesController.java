package solutions.techsur.rfpaiservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import solutions.techsur.common.microservice.dto.Validation;
import solutions.techsur.rfpaiservice.dto.CreationDTO;
import solutions.techsur.rfpaiservice.dto.ResponsesRequest;
import solutions.techsur.rfpaiservice.entity.RequestForProposalResponses;
import solutions.techsur.rfpaiservice.service.RequestForProposalResponsesService;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/responses")
@AllArgsConstructor
@Tag(name = "RFP Responses", description = "API for managing RFP responses.")
@Validated
public class RequestForProposalResponsesController {

    private final RequestForProposalResponsesService service;

    @PostMapping
    @PreAuthorize("hasAuthority(@properties.getUploadDocument())")
    @Operation(summary = "Create a new response", description = "Creates a new response and returns its ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Response created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request payload"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<CreationDTO> createResponse(
            @Validated(Validation.CreateValidation.class) @Valid @RequestBody ResponsesRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(CreationDTO.builder()
                        .id(service.createResponses(request).getId())
                        .build());
    }

    @PutMapping("/{responseId}")
    @PreAuthorize("hasAuthority(@properties.getUploadDocument())")
    @Operation(
            summary = "Update a response",
            description = "Updates an existing response by its ID.",
            tags = {"Responses"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Response updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "404", description = "Response not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Void> updateResponse(
            @Parameter(description = "Updated response details") @Valid @RequestBody ResponsesRequest request,
            @Parameter(description = "ID of the response to update") @PathVariable Integer responseId) {
        service.updateResponses(request, responseId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{responseId}")
    @PreAuthorize("hasAuthority(@properties.getUploadDocument())")
    @Operation(
            summary = "Get response",
            description = "Retrieves a response based on the given response ID.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successful retrieval of response"),
                    @ApiResponse(responseCode = "404", description = "Response not found"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    public ResponseEntity<RequestForProposalResponses> getResponse(@PathVariable Integer responseId) {
        return ResponseEntity.ok(service.getResponses(responseId));
    }
}