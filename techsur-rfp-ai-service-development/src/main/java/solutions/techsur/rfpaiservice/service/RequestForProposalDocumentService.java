package solutions.techsur.rfpaiservice.service;

import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import solutions.techsur.rfpaiservice.dto.ProposalDocumentResponse;
import solutions.techsur.rfpaiservice.entity.RequestForProposalDocument;

import java.util.List;

/**
 * Service interface for managing Request For Proposal (RFP) documents.
 * Defines operations for uploading, deleting, replacing, retrieving,
 * copying RFP and Blue Book documents.
 */
public interface RequestForProposalDocumentService {

    /**
     * Upload multiple RFP documents.
     *
     * @param files      files to upload
     * @param proposalId associated proposal ID
     * @param isBlueBook flag indicating if the document is a Blue Book
     * @param isAdmin    flag indicating if admin privileges apply
     * @return status or location information as String
     */
    String uploadRFPDocument(MultipartFile[] files, Integer proposalId, boolean isBlueBook, boolean isAdmin);

    /**
     * Upload multiple Blue Book RFP documents.
     *
     * @param files      files to upload
     * @param proposalId associated proposal ID
     * @param isBlueBook flag for Blue Book identification
     * @return status or location information as String
     */
    String uploadBlueBookRFPDocument(MultipartFile[] files, Integer proposalId, boolean isBlueBook);

    /**
     * Upload Blue Book RFP documents by admin users.
     *
     * @param files      files to upload
     * @param isBlueBook flag for Blue Book identification
     * @param isAdmin    administrative flag
     * @return status or location information as String
     */
    String uploadAdminBlueBookRFPDocument(MultipartFile[] files, boolean isBlueBook, boolean isAdmin);

    /**
     * Delete an RFP document by its ID.
     *
     * @param documentId identifier of the document to delete
     */
    void deleteRFPDocument(Integer documentId);

    /**
     * Replace an existing RFP document.
     *
     * @param file       new file to replace with
     * @param documentId document to replace
     * @param isBlueBook flag for Blue Book identification
     * @param isAdmin    flag indicating admin privileges
     */
    void replaceRFPDocument(MultipartFile file, Integer documentId, boolean isBlueBook, boolean isAdmin);

    /**
     * Retrieve ProposalDocumentResponse by proposal ID.
     *
     * @param proposalId proposal identifier
     * @return ProposalDocumentResponse details
     */
    ProposalDocumentResponse getDocumentByRequestProposalId(Integer proposalId);

    /**
     * Retrieve all Blue Print documents uploaded by admins.
     *
     * @return list of RequestForProposalDocument entities
     */
    List<RequestForProposalDocument> getAllBluePrintDocumentUploadedByAdmin();

    /**
     * Replace an admin Blue Book document.
     *
     * @param file       new file to replace with
     * @param documentId document to replace
     */
    void replaceAdminBlueBookDocument(MultipartFile file, Integer documentId);

    /**
     * Copy documents for a given solicitation ID and document IDs list.
     *
     * @param solicitationId solicitation identifier
     * @param documentIds    list of document identifiers
     */
    void copyDocuments(String solicitationId, List<Integer> documentIds);

    /**
     * Retrieve a document by its ID.
     *
     * @param documentId identifier of the document
     * @return RequestForProposalDocument entity
     */
    RequestForProposalDocument getDocumentById(Integer documentId);

    /**
     * Retrieve a document from S3 given a file path.
     *
     * @param filePath path to the file in S3
     * @return ResponseInputStream of GetObjectResponse from S3
     */
    ResponseInputStream<GetObjectResponse> getS3Document(String filePath);

    /**
     * Resolve the content type for a given file name.
     *
     * @param fileName name of the file
     * @return resolved MIME content type String
     */
    String resolveContentType(String fileName);
}