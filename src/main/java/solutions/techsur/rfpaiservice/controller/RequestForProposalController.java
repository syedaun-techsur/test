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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import solutions.techsur.common.microservice.dto.Validation;
import solutions.techsur.rfpaiservice.dto.*;
import solutions.techsur.rfpaiservice.entity.RequestForProposal;
import solutions.techsur.rfpaiservice.service.impl.RequestForProposalServiceImpl;

@RestController
@RequestMapping("/api/v1/proposal")
@AllArgsConstructor
@Tag(name = "Proposals & Outline", description = "API for managing Request for Proposals and Outline")
@Validated
public class RequestForProposalController {

    private final RequestForProposalServiceImpl proposalService;

    @PostMapping
    @Operation(
        summary = "Create a New Proposal",
        description = "Creates a new proposal and returns its ID."
    )
    @ApiResponse(responseCode = "201", description = "Proposal created successfully")
    @ApiResponse(responseCode = "400", description = "Invalid request payload")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    public ResponseEntity<CreationDTO> createProposal(
            @Validated(Validation.CreateValidation.class) @RequestBody ProposalRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(CreationDTO.builder()
                        .id(proposalService.createRequestForProposal(request).getId())
                        .build());
    }

    @GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Get Filtered Proposals",
            description = "Retrieves a list of proposals based on the given filter and pagination parameters.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successful retrieval of proposals"),
                    @ApiResponse(responseCode = "400", description = "Invalid request parameters"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    public ResponseEntity<Page<RequestForProposal>> getProposals(
            ProposalFilter filter,
            @ParameterObject
            @SortDefault(sort = "created", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(proposalService.getProposals(filter, pageable));
    }

    @PostMapping("/{proposalId}/outline")
    @Operation(
            summary = "Create a New Response Outline",
            description = "Creates a new response outline and returns its ID.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Outline response request payload",
                    required = true,
                    content = @Content(schema = @Schema(implementation = OutlineResponse.class))
            )
    )
    @ApiResponse(responseCode = "201", description = "Response outline created successfully")
    @ApiResponse(responseCode = "400", description = "Invalid request payload")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    public ResponseEntity<CreationDTO> createResponseOutline(
            @PathVariable Integer proposalId,
            @Valid @RequestBody OutlineResponse request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(CreationDTO.builder()
                        .id(proposalService.createResponseOutline(request, proposalId).getId())
                        .build());
    }

    @GetMapping(value = "/{proposalId}/outline", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Get Response Outline",
            description = "Retrieves a response outline of proposals based on the given proposal ID.",
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
    @Operation(
            summary = "Update a Response Outline",
            description = "Updates an existing Response Outline section and subsection by proposal ID."
    )
    @ApiResponse(responseCode = "204", description = "Response outline updated successfully")
    @ApiResponse(responseCode = "400", description = "Invalid request data")
    @ApiResponse(responseCode = "404", description = "Response outline not found")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    public ResponseEntity<Void> updateResponseOutline(
            @RequestBody OutlineResponse request,
            @PathVariable Integer proposalId) {
        proposalService.updateResponseOutline(request, proposalId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{proposalId}")
    @Operation(
            summary = "Delete a Proposal",
            description = "Deletes a request for a proposal based on its ID.",
            tags = {"Proposal"}
    )
    @ApiResponse(responseCode = "204", description = "Proposal deleted successfully")
    @ApiResponse(responseCode = "400", description = "Invalid proposal ID")
    @ApiResponse(responseCode = "404", description = "Proposal not found")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    public ResponseEntity<Void> deleteRequestForProposal(@PathVariable Integer proposalId) {
        proposalService.deleteRequestForProposal(proposalId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{outlineId}/outline")
    @Operation(
            summary = "Delete an Outline",
            description = "Deletes a response outline based on its ID.",
            tags = {"Proposal"}
    )
    @ApiResponse(responseCode = "204", description = "Outline deleted successfully")
    @ApiResponse(responseCode = "400", description = "Invalid Outline ID")
    @ApiResponse(responseCode = "404", description = "Outline not found")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    public ResponseEntity<Void> deleteResponseOutline(@PathVariable Integer outlineId) {
        proposalService.deleteResponseOutline(outlineId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{proposalId}")
    @Operation(
            summary = "Get Request For Proposal by ID",
            description = "Retrieves a proposal based on the given RFP ID.",
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
            @RequestBody ProposalRequest request,
            @PathVariable Integer proposalId) {
        proposalService.updateRequestForProposal(request, proposalId);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Generate an Outline DOCX File",
            description = "Creates a structured .docx document for a given proposal ID and returns it as a downloadable file.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully generated the outlines DOCX file",
                            content = @Content(mediaType = MediaType.APPLICATION_OCTET_STREAM_VALUE)),
                    @ApiResponse(responseCode = "404", description = "Proposal not found"),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error")
            }
    )
    @GetMapping(value = "/{proposalId}/download", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<byte[]> generateDoc(@PathVariable Integer proposalId) {
        byte[] fileContent = proposalService.generateOutlineDoc(proposalId);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        // Quote filename to support spaces or special characters safely
        headers.setContentDispositionFormData("attachment", "\"Generated_Document.docx\"");

        return ResponseEntity.ok()
                .headers(headers)
                .body(fileContent);
    }

    @PutMapping("/{outlineId}/single/outline")
    @Operation(
            summary = "Update a Response Outline",
            description = "Updates an existing Response Outline by its ID."
    )
    @ApiResponse(responseCode = "204", description = "Response outline updated successfully")
    @ApiResponse(responseCode = "400", description = "Invalid request data")
    @ApiResponse(responseCode = "404", description = "Response outline not found")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    public ResponseEntity<Void> updateSingleResponseOutline(
            @RequestBody SingleOutlineRequest request,
            @PathVariable Integer outlineId) {
        proposalService.updateSingleResponseOutline(request, outlineId);
        return ResponseEntity.noContent().build();
    }

}