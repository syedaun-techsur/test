package solutions.techsur.rfpaiservice.service;

import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import solutions.techsur.rfpaiservice.dto.ProposalDocumentResponse;
import solutions.techsur.rfpaiservice.entity.RequestForProposalDocument;

import java.util.List;

/**
 * Service interface for handling Request For Proposal (RFP) documents.
 */
public interface RequestForProposalDocumentService {

    /**
     * Upload multiple RFP documents.
     *
     * @param files      array of files to upload
     * @param proposalId the proposal ID associated with documents
     * @param isBlueBook flag indicating if the document is a Blue Book
     * @param isAdmin    flag indicating if upload is done by admin
     * @return a result message or document location key
     */
    String uploadRFPDocument(MultipartFile[] files, Integer proposalId, boolean isBlueBook, boolean isAdmin);

    /**
     * Upload Blue Book RFP documents.
     *
     * @param files      array of files to upload
     * @param proposalId the proposal ID associated with documents
     * @param isBlueBook flag indicating if the document is a Blue Book
     * @return a result message or document location key
     */
    String uploadBlueBookRFPDocument(MultipartFile[] files, Integer proposalId, boolean isBlueBook);

    /**
     * Upload Blue Book RFP documents by admin.
     *
     * @param files      array of files to upload
     * @param isBlueBook flag indicating if the document is a Blue Book
     * @param isAdmin    flag indicating admin upload
     * @return a result message or document location key
     */
    String uploadAdminBlueBookRFPDocument(MultipartFile[] files, boolean isBlueBook, boolean isAdmin);

    /**
     * Delete an RFP document by its ID.
     *
     * @param documentId ID of the document to delete
     */
    void deleteRFPDocument(Integer documentId);

    /**
     * Replace an existing RFP document with a new one.
     *
     * @param file       new file to replace the existing document
     * @param documentId ID of the document to replace
     * @param isBlueBook flag indicating if the document is a Blue Book
     * @param isAdmin    flag indicating if operation is by admin
     */
    void replaceRFPDocument(MultipartFile file, Integer documentId, boolean isBlueBook, boolean isAdmin);

    /**
     * Retrieve document response by proposal ID.
     *
     * @param proposalId proposal ID
     * @return ProposalDocumentResponse containing document details
     */
    ProposalDocumentResponse getDocumentByRequestProposalId(Integer proposalId);

    /**
     * Get all blueprint documents uploaded by admin.
     *
     * @return list of RequestForProposalDocument entities
     */
    List<RequestForProposalDocument> getAllBlueprintDocumentsUploadedByAdmin();

    /**
     * Replace an admin Blue Book document.
     *
     * @param file       new file to replace the existing document
     * @param documentId ID of the document to replace
     */
    void replaceAdminBlueBookDocument(MultipartFile file, Integer documentId);

    /**
     * Copy documents from one solicitation to another.
     *
     * @param solicitationId solicitation identifier
     * @param documentIds    list of document IDs to copy
     */
    void copyDocuments(String solicitationId, List<Integer> documentIds);

    /**
     * Get a document by its ID.
     *
     * @param documentId ID of the document
     * @return RequestForProposalDocument entity
     */
    RequestForProposalDocument getDocumentById(Integer documentId);

    /**
     * Retrieve a document from S3 by its file path.
     *
     * @param filePath the path of the file in S3
     * @return ResponseInputStream of GetObjectResponse for the file
     */
    ResponseInputStream<GetObjectResponse> getS3Document(String filePath);

    /**
     * Resolve the content type of a file given its name.
     *
     * @param fileName the name of the file
     * @return the content MIME type string
     */
    String resolveContentType(String fileName);
}