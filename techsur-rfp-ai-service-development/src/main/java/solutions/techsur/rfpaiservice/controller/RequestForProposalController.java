package solutions.techsur.rfpaiservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import solutions.techsur.common.microservice.dto.Validation;
import solutions.techsur.rfpaiservice.dto.*;
import solutions.techsur.rfpaiservice.entity.RequestForProposal;
import solutions.techsur.rfpaiservice.service.impl.RequestForProposalServiceImpl;

@RestController
@RequestMapping("/api/v1/proposal")
@AllArgsConstructor
@Tag(name = "Proposals & Outline", description = "API for managing Request for Proposals and outline")
public class RequestForProposalController {

    private final RequestForProposalServiceImpl proposalService;

    @PostMapping
    @PreAuthorize("hasAnyAuthority(@properties.getUploadDocument())")
    @Operation(summary = "Create a new proposal", description = "Creates a new proposal and returns its ID.")
    @ApiResponse(responseCode = "201", description = "Proposal created successfully")
    @ApiResponse(responseCode = "400", description = "Invalid request payload")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    public ResponseEntity<CreationDTO> createProposal(@Validated(Validation.CreateValidation.class) @RequestBody ProposalRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(CreationDTO.builder()
                .id(proposalService.createRequestForProposal(request)
                        .getId()).build());
    }

    @GetMapping("/list")
    @PreAuthorize("hasAnyAuthority(@properties.getUploadDocument())")
    @Operation(
            summary = "Get filtered proposals",
            description = "Retrieves a list of proposals based on the given filter and pagination parameters.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successful retrieval of proposals"),
                    @ApiResponse(responseCode = "400", description = "Invalid request parameters"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    public ResponseEntity<Page<RequestForProposal>> getProposals(ProposalFilter filter, @ParameterObject  @SortDefault(sort = "created", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(proposalService.getProposals(filter, pageable));
    }

    @PostMapping("/{proposalId}/outline")
    @Operation(
            summary = "Create a new response outline",
            description = "Creates a new response outline and returns its ID.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Outline response request payload",
                    required = true,
                    content = @Content(schema = @Schema(implementation = OutlineResponse.class))
            )
    )
    @PreAuthorize("hasAnyAuthority(@properties.getUploadDocument())")
    @ApiResponse(responseCode = "201", description = "Response outline created successfully")
    @ApiResponse(responseCode = "400", description = "Invalid request payload")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    public ResponseEntity<CreationDTO> createResponseOutline(@PathVariable Integer proposalId, @Valid @RequestBody OutlineResponse request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(CreationDTO.builder()
                .id(proposalService.createResponseOutline(request, proposalId).getId()).build());
    }

    @GetMapping(value = "/{proposalId}/outline", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyAuthority(@properties.getUploadDocument())")
    @Operation(
            summary = "Get Response outline",
            description = "Retrieves a Response outline of proposals based on the given proposal id parameters.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successful retrieval of response outline"),
                    @ApiResponse(responseCode = "400", description = "Invalid request parameters"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    public ResponseEntity<OutlineResponse> getResponseOutline(@PathVariable Integer proposalId) {
        return ResponseEntity.ok(proposalService.getProposalWithOutlines(proposalId));
    }

    @PutMapping("/{proposalId}/outline")
    @PreAuthorize("hasAnyAuthority(@properties.getUploadDocument())")
    @Operation(
            summary = "Update a Response Outline",
            description = "Updates an existing Response Outline section , subsection by proposal ID."
    )
    @ApiResponse(responseCode = "204", description = "Response outline updated successfully")
    @ApiResponse(responseCode = "400", description = "Invalid request data")
    @ApiResponse(responseCode = "404", description = "Response outline not found")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    public ResponseEntity<Void> updateResponseOutline(
            @RequestBody @Parameter(description = "Updated response outline details")  OutlineResponse request,
            @PathVariable @Parameter(description = "ID of the response outline to update") Integer proposalId) {
        proposalService.updateResponseOutline(request, proposalId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{proposalId}")
    @PreAuthorize("hasAnyAuthority(@properties.getDeleteProposal())")
    @Operation(
            summary = "Delete a Proposal",
            description = "Deletes a request for a proposal based on its ID.",
            tags = {"Proposal"}
    )
    @ApiResponse(responseCode = "204", description = "Proposal deleted successfully")
    @ApiResponse(responseCode = "400", description = "Invalid proposal ID")
    @ApiResponse(responseCode = "404", description = "Proposal not found")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    public ResponseEntity<Void> deleteRequestForProposal(
            @PathVariable
            @Parameter(description = "ID of the proposal to delete", example = "123")
            Integer proposalId) {
        proposalService.deleteRequestForProposal(proposalId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{outlineId}/outline")
    @PreAuthorize("hasAnyAuthority(@properties.getUploadDocument())")
    @Operation(
            summary = "Delete a outline",
            description = "Deletes a request for a proposal based on its ID.",
            tags = {"Proposal"}
    )
    @ApiResponse(responseCode = "204", description = "Outline deleted successfully")
    @ApiResponse(responseCode = "400", description = "Invalid Outline ID")
    @ApiResponse(responseCode = "404", description = "Outline not found")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    public ResponseEntity<Void> deleteResponseOutline(
            @PathVariable
            @Parameter(description = "ID of the outline to delete", example = "123")
            Integer outlineId) {
        proposalService.deleteResponseOutline(outlineId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{proposalId}")
    @PreAuthorize("hasAnyAuthority(@properties.getUploadDocument())")
    @Operation(
            summary = "Get Request for proposal by ID",
            description = "Retrieves a proposal based on the given rfp ID.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successful retrieval of proposal"),
                    @ApiResponse(responseCode = "400", description = "Invalid request parameters"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    public ResponseEntity<RequestForProposal> getRequestForProposalById(@PathVariable Integer proposalId) {
        return ResponseEntity.ok(proposalService.getProposalById(proposalId));
    }

    @PutMapping("/{proposalId}")
    @PreAuthorize("hasAnyAuthority(@properties.getUploadDocument())")
    @Operation(
            summary = "Update a Request For Proposal",
            description = "Updates an existing Request For Proposal by its ID.",
            tags = {"Request For Proposal"}
    )
    @ApiResponse(responseCode = "204", description = "Request For Proposal updated successfully")
    @ApiResponse(responseCode = "400", description = "Invalid request data")
    @ApiResponse(responseCode = "404", description = "Response outline not found")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    public ResponseEntity<Void> updateRequestForProposal(
            @RequestBody @Parameter(description = "Updated Request For Proposal details") ProposalRequest request,
            @PathVariable @Parameter(description = "ID of the Request For Proposal to update") Integer proposalId) {
        proposalService.updateRequestForProposal(request, proposalId);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Generate an outline DOCX file",
            description = "Creates a structured .docx document for a given proposal ID and returns it as a downloadable file.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully generated the outlines DOCX file",
                            content = @Content(mediaType = "application/octet-stream")),
                    @ApiResponse(responseCode = "404", description = "Proposal not found"),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error")
            }
    )
    @GetMapping(value = "/{proposalId}/download", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<byte[]> generateDoc(@PathVariable Integer proposalId) {
        byte[] fileContent = proposalService.generateOutlineDoc(proposalId);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", "Generated_Document.docx");

        return ResponseEntity.ok()
                .headers(headers)
                .body(fileContent);
    }

    @PutMapping("/{outlineId}/single/outline")
    @PreAuthorize("hasAnyAuthority(@properties.getUploadDocument())")
    @Operation(
            summary = "Update a Response Outline",
            description = "Updates an existing Response Outline by its ID."
    )
    @ApiResponse(responseCode = "204", description = "Response outline updated successfully")
    @ApiResponse(responseCode = "400", description = "Invalid request data")
    @ApiResponse(responseCode = "404", description = "Response outline not found")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    public ResponseEntity<Void> updateSingleResponseOutline(
            @RequestBody @Parameter(description = "Updated response outline details") SingleOutlineRequest request,
            @PathVariable @Parameter(description = "ID of the response outline to update") Integer outlineId) {
        proposalService.updateSingleResponseOutline(request, outlineId);
        return ResponseEntity.noContent().build();
    }

}
