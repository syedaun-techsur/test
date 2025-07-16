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

@RestController
@RequestMapping("/api/v1/responses")
@AllArgsConstructor
@Tag(name = "RFP Responses", description = "API for managing RFP Responses.")
public class RequestForProposalResponsesController {

    private final RequestForProposalResponsesService service;

    @PostMapping
    @PreAuthorize("hasAnyAuthority(@properties.getUploadDocument())")
    @Operation(summary = "Create a new responses", description = "Creates a new responses and returns its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Responses created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request payload"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<CreationDTO> createResponses(@Validated(Validation.CreateValidation.class) @RequestBody ResponsesRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(CreationDTO.builder()
                .id(service.createResponses(request)
                        .getId()).build());
    }

    @PutMapping("/{responseId}")
    @PreAuthorize("hasAnyAuthority(@properties.getUploadDocument())")
    @Operation(
            summary = "Update a Responses",
            description = "Updates an existing Response by its ID.",
            tags = { "Responses" }
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Responses updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "404", description = "Responses not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Void> updateResponses(
            @RequestBody @Parameter(description = "Updated responses details") ResponsesRequest request,
            @PathVariable @Parameter(description = "ID of the responses to update") Integer responseId) {
        service.updateResponses(request, responseId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{responseId}")
    @PreAuthorize("hasAnyAuthority(@properties.getUploadDocument())")
    @Operation(
            summary = "Get Responses",
            description = "Retrieves a Response based on the given response id parameters.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successful retrieval of response"),
                    @ApiResponse(responseCode = "404", description = "Responses Not found"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    public ResponseEntity<RequestForProposalResponses> getResponses(@PathVariable Integer responseId) {
        return ResponseEntity.ok(service.getResponses(responseId));
    }
}
