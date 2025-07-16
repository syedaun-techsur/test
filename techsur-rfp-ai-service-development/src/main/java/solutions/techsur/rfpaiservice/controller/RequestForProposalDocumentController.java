package solutions.techsur.rfpaiservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import solutions.techsur.rfpaiservice.dto.ProposalDocumentResponse;
import solutions.techsur.rfpaiservice.entity.RequestForProposalDocument;
import solutions.techsur.rfpaiservice.service.RequestForProposalDocumentService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/document")
@AllArgsConstructor
@Tag(name = "Request Proposal Document")
public class RequestForProposalDocumentController {

    private final RequestForProposalDocumentService documentService;

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAnyAuthority(@properties.getUploadDocument())")
    @Operation(summary = "Upload multiple documents",
        description = "Uploads an array of files to an S3 bucket, Requires 'contributor' authority.",
        security = {@SecurityRequirement(name = "bearerToken")})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "File uploaded successfully."),
            @ApiResponse(responseCode = "400", description = "Unable to upload file"),
            @ApiResponse(responseCode = "415", description = "Unsupported Media Type"),
            @ApiResponse(responseCode = "401", description = "User is not authorized")
    })
    public ResponseEntity<?> uploadRFPDocument(
            @RequestParam("files") @Parameter(
                    description = "File to be uploaded", required = true,
                    content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE))
            MultipartFile[] files,
            @RequestParam("proposalId") @Parameter(
                    description = "Proposal ID to which the document will be attached.", required = true)
            Integer proposalId) {

        String response = documentService.uploadRFPDocument(files, proposalId, false, false);
        return ResponseEntity.status(201).body(response);
    }

    @DeleteMapping("/delete/{documentId}")
    @PreAuthorize("hasAnyAuthority(@properties.getUploadDocument())")
    @Operation(summary = "Delete an RFP document",
            description = "Deletes an existing RFP document based on the provided document ID.")
    @ApiResponse(responseCode = "204", description = "Document deleted successfully")
    @ApiResponse(responseCode = "404", description = "Document not found")
    public ResponseEntity<Void> deleteRFPDocument(
            @Parameter(description = "ID of the document to delete", required = true)
            @PathVariable Integer documentId) {
        documentService.deleteRFPDocument(documentId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping(value = "/replace", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Replace an RFP document",
            description = "Replaces an existing RFP document with a new file based on the provided document ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Document replaced successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid file format or missing file"),
            @ApiResponse(responseCode = "404", description = "Document not found")
    })
    @PreAuthorize("hasAnyAuthority(@properties.getUploadDocument())")
    public ResponseEntity<Void> replaceDocument(
            @RequestParam("documentId") @Parameter(
                    description = "ID of the document to replace.", required = true)
            Integer documentId,

            @RequestParam("file") @Parameter(
                    description = "File to be uploaded", required = true,
                    content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE))
            MultipartFile file,
            @RequestParam("isBlueBook") @Parameter(
                    description = "Which file to be replaced (Proposal document/Blue book)", required = true)
            boolean isBlueBook) {
        documentService.replaceRPFDocument(file, documentId, isBlueBook, false);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{proposalId}/proposal")
    @Operation(
            summary = "Get Proposal Document",
            description = "Fetches the document associated with a specific proposal ID."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the document"),
            @ApiResponse(responseCode = "404", description = "Proposal document not found")
    })
    @PreAuthorize("hasAnyAuthority(@properties.getUploadDocument())")
    public ResponseEntity<ProposalDocumentResponse> getDocumentByRequestProposalId(
            @Parameter(description = "ID of the proposal to fetch the document for", required = true, example = "123")
            @PathVariable Integer proposalId) {

        ProposalDocumentResponse response = documentService.getDocumentByRequestProposalId(proposalId);
        return ResponseEntity.ok(response);
    }

    @PostMapping(value = "/uploadbluebook", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAnyAuthority(@properties.getUploadDocument())")
    @Operation(summary = "Upload multiple Blue Book documents",
            description = "Uploads an array of files to an S3 bucket")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "File uploaded successfully."),
            @ApiResponse(responseCode = "400", description = "Unable to upload file"),
            @ApiResponse(responseCode = "415", description = "Unsupported Media Type"),
            @ApiResponse(responseCode = "401", description = "User is not authorized")
    })
    public ResponseEntity<?> uploadBlueBookRFPDocument(
            @RequestParam("files") @Parameter(
                    description = "Blue Book file to be uploaded", required = true,
                    content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE))
            MultipartFile[] files,
            @RequestParam("proposalId") @Parameter(
                    description = "Proposal ID to which the blue book document will be attached.", required = true)
            Integer proposalId) {

        String response = documentService.uploadBlueBookRFPDocument(files, proposalId, true);
        return ResponseEntity.status(201).body(response);
    }

    @PostMapping(value = "/admin/uploadblueprintdoc", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAnyAuthority(@properties.getDeleteProposal())")
    @Operation(summary = "Upload multiple blueprint documents by admin",
            description = "Uploads an array of files to an S3 bucket")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "File uploaded successfully."),
            @ApiResponse(responseCode = "400", description = "Unable to upload file"),
            @ApiResponse(responseCode = "415", description = "Unsupported Media Type"),
            @ApiResponse(responseCode = "401", description = "User is not authorized")
    })
    public ResponseEntity<?> uploadAdminBlueBookRFPDocument(
            @RequestParam("files") @Parameter(
                    description = "Blueprint files to be uploaded", required = true,
                    content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE))
            MultipartFile[] files) {

        String response = documentService.uploadAdminBlueBookRFPDocument(files, true, true);
        return ResponseEntity.status(201).body(response);
    }

    @GetMapping("/admin/getblueprintdocs")
    @Operation(
            summary = "Get Blueprint Documents uploaded by Admin",
            description = "Fetches the documents associated with admin."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the documents"),
            @ApiResponse(responseCode = "404", description = "Documents not found")
    })
    @PreAuthorize("hasAnyAuthority(@properties.getDeleteProposal())")
    public ResponseEntity<List<RequestForProposalDocument>> getAllBluePrintDocumentUploadedByAdmin() {
        List<RequestForProposalDocument> documents = documentService.getAllBluePrintDocumentUploadedByAdmin();
        return ResponseEntity.ok(documents);
    }

    @PutMapping(value = "/admin/replaceblueprintdocs", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Replace a Blueprint document uploaded by admin",
            description = "Replaces an existing blueprint document with a new file based on the provided document ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Document replaced successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid file format or missing file"),
            @ApiResponse(responseCode = "404", description = "Document not found")
    })
    @PreAuthorize("hasAnyAuthority(@properties.getUploadDocument())")
    public ResponseEntity<Void> replaceAdminBlueBookDocument(
            @RequestParam("documentId") @Parameter(
                    description = "ID of the document to replace.", required = true)
            Integer documentId,

            @RequestParam("file") @Parameter(
                    description = "File to be uploaded", required = true,
                    content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE))
            MultipartFile file) {
        documentService.replaceAdminBlueBookDocument(file, documentId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/copy-documents")
    @Operation(
            summary = "Copy specific documents to a solicitation folder",
            description = "Deletes all files in 'techsur/rfp/{solicitationId}/' and then copies the specified document IDs from 'techsur/documents/'.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Documents copied successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content)
            }
    )
    public ResponseEntity<Void> copyDocuments(
            @Parameter(description = "Solicitation ID for the target folder", required = true, example = "12345")
            @RequestParam String solicitationId,

            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "List of document IDs to be copied",
                    required = true,
                    content = @Content(schema = @Schema(implementation = Integer.class))
            )
            @RequestBody List<Integer> documentIds) {

        documentService.copyDocuments(solicitationId, documentIds);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{documentId}/view")
    @PreAuthorize("hasAnyAuthority(@properties.getUploadDocument())")
    @Operation(
            summary = "View a document by ID",
            description = "Streams the requested document from the S3 bucket for inline viewing. Requires 'viewer' authority.",
            security = {@SecurityRequirement(name = "bearerToken")}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Document streamed successfully.",
                    content = @Content(mediaType = MediaType.APPLICATION_OCTET_STREAM_VALUE)),
            @ApiResponse(responseCode = "404", description = "Document not found"),
            @ApiResponse(responseCode = "401", description = "User is not authorized")
    })
    public ResponseEntity<InputStreamResource> viewDocument(
            @PathVariable("documentId") @Parameter(description = "ID of the document to view", required = true)
            Integer documentId,
            @RequestParam(required = false, defaultValue = "false") boolean download)  {

        RequestForProposalDocument document = documentService.getDocumentById(documentId);
        ResponseInputStream<GetObjectResponse> s3Stream = documentService.getS3Document(document.getFilePath());
        String fileName = document.getFileName();
        String contentType = documentService.resolveContentType(fileName);

        HttpHeaders headers = new HttpHeaders();
        String dispositionType = download ? "attachment" : "inline";
        headers.add(HttpHeaders.CONTENT_DISPOSITION, dispositionType + "; filename=\"" + fileName + "\"");

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .headers(headers)
                .body(new InputStreamResource(s3Stream));
    }
}