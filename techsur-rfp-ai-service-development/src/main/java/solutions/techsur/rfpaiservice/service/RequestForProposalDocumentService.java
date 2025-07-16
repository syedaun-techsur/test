package solutions.techsur.rfpaiservice.service;

import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import solutions.techsur.rfpaiservice.dto.ProposalDocumentResponse;
import solutions.techsur.rfpaiservice.entity.RequestForProposalDocument;

import java.util.List;

public interface RequestForProposalDocumentService {
    String uploadRFPDocument(MultipartFile[] files, Integer proposalId, boolean isBlueBook, boolean isAdmin);

    String uploadBlueBookRFPDocument(MultipartFile[] files, Integer proposalId, boolean isBlueBook);

    String uploadAdminBlueBookRFPDocument(MultipartFile[] files, boolean isBlueBook, boolean isAdmin);

    void deleteRFPDocument(Integer documentId);

    void replaceRPFDocument(MultipartFile file, Integer documentId, boolean isBlueBook, boolean isAdmin);

    ProposalDocumentResponse getDocumentByRequestProposalId(Integer proposalId);

    List<RequestForProposalDocument> getAllBluePrintDocumentUploadedByAdmin();

    void replaceAdminBlueBookDocument(MultipartFile file, Integer documentId);

    void copyDocuments(String solicitationId, List<Integer> documentIds);

    RequestForProposalDocument getDocumentById(Integer documentId);

    ResponseInputStream<GetObjectResponse> getS3Document(String filePath);

    String resolveContentType(String fileName);
}
