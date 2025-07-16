package solutions.techsur.rfpaiservice.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import solutions.techsur.common.microservice.exceptions.AppException;
import solutions.techsur.rfpaiservice.document.DocumentHelper;
import solutions.techsur.rfpaiservice.dto.ProposalDocumentResponse;
import solutions.techsur.rfpaiservice.entity.RequestForProposal;
import solutions.techsur.rfpaiservice.entity.RequestForProposalDocument;
import solutions.techsur.rfpaiservice.repository.RequestForProposalDocumentRepository;
import solutions.techsur.rfpaiservice.repository.RequestForProposalRepository;
import solutions.techsur.rfpaiservice.service.RequestForProposalDocumentService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static solutions.techsur.common.microservice.exceptions.AppReason.*;
import static solutions.techsur.rfpaiservice.repository.RFPDocumentSpecification.adminUploadedDocumenteSpecification;
import static solutions.techsur.rfpaiservice.repository.RFPDocumentSpecification.findRFPByProposalSpecification;

@Slf4j
@Service
@Transactional
public class RequestForProposalDocumentServiceImpl implements RequestForProposalDocumentService {

    private final DocumentHelper documentHelper;
    private final RequestForProposalRepository requestForProposalRepository;
    private final RequestForProposalDocumentRepository documentRepository;
    private final org.keycloak.admin.client.Keycloak keycloak;

    private static final List<String> ALLOWED_EXTENSIONS = Arrays.asList("pdf", "docx", "txt", "xls", "xlsx", "doc");

    public RequestForProposalDocumentServiceImpl(DocumentHelper documentHelper, RequestForProposalRepository requestForProposalRepository, RequestForProposalDocumentRepository documentRepository, org.keycloak.admin.client.Keycloak keycloak) {
        this.documentHelper = documentHelper;
        this.requestForProposalRepository = requestForProposalRepository;
        this.documentRepository = documentRepository;
        this.keycloak = keycloak;
    }

    @Override
    public String uploadRFPDocument(MultipartFile[] files, Integer proposalId, boolean isBlueBook, boolean isAdmin) {
        // Validate all files upfront
        for (MultipartFile file : files) {
            String[] fileValidationMessage = validateMultipartFile(file);
            if (ObjectUtils.isNotEmpty(fileValidationMessage)) {
                throw new AppException(INVALID_FILE, fileValidationMessage);
            }
        }

        RequestForProposal proposal = null;
        if (proposalId != null) {
            proposal = findProposalById(proposalId);
        }

        List<RequestForProposalDocument> documents = new ArrayList<>();

        final RequestForProposal finalProposal = proposal;
        List<String> uploadedFiles = Arrays.stream(files)
                .map(file -> {
                    String fileName = StringUtils.cleanPath(file.getOriginalFilename());
                    String fileKey;
                    if (isAdmin && isBlueBook) {
                        fileKey = documentHelper.getFilePathForAdminBlueBook(fileName);
                    } else {
                        if (finalProposal == null) {
                            throw new AppException(RFP_NOT_FOUND, "Proposal ID is required for non-admin uploads");
                        }
                        fileKey = documentHelper.getFilePath(isBlueBook, fileName, finalProposal.getId());
                    }

                    RequestForProposalDocument document = RequestForProposalDocument.builder()
                            .fileName(fileName)
                            .requestForProposal(finalProposal)
                            .filePath(fileKey)
                            .build();
                    documents.add(document);

                    try {
                        documentHelper.uploadRFPDocument(file, fileKey);
                        return fileName;
                    } catch (IOException e) {
                        throw new AppException(INTERNAL_ERROR, "Facing issue while uploading the file " + fileName, e);
                    }
                })
                .collect(Collectors.toList());

        documentRepository.saveAll(documents);
        return String.join(", ", uploadedFiles);
    }

    @Override
    public void deleteRFPDocument(Integer documentId) {
        RequestForProposalDocument document = findDocumentById(documentId);
        try {
            documentRepository.delete(document);
            documentHelper.deleteRFPDocument(document.getFilePath());
        } catch (Exception e) {
            log.error("Error deleting file: {}", document.getFileName(), e);
            throw new AppException(INTERNAL_ERROR, "Failed to delete file: " + document.getFileName(), e);
        }
    }

    @Override
    public void replaceRPFDocument(MultipartFile file, Integer documentId, boolean isBlueBook, boolean isAdmin) {
        String[] fileValidationMessage = validateMultipartFile(file);
        if (ObjectUtils.isNotEmpty(fileValidationMessage)) {
            throw new AppException(INVALID_FILE, fileValidationMessage);
        }
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        RequestForProposalDocument document = findDocumentById(documentId);
        String fileKey;
        if (isBlueBook && isAdmin) {
            fileKey = documentHelper.getFilePathForAdminBlueBook(fileName);
        } else {
            if (document.getRequestForProposal() == null) {
                throw new AppException(RFP_NOT_FOUND, "Associated Proposal not found for document id " + documentId);
            }
            fileKey = documentHelper.getFilePath(isBlueBook, fileName, document.getRequestForProposal().getId());
        }

        try {
            documentHelper.deleteRFPDocument(document.getFilePath());
            documentHelper.uploadRFPDocument(file, fileKey);
            document.setFilePath(fileKey);
            document.setFileName(fileName);
            documentRepository.save(document);
        } catch (IOException e) {
            throw new AppException(INTERNAL_ERROR, "Facing issue while replacing the file " + document.getFileName(), e);
        }
    }

    @Override
    public ProposalDocumentResponse getDocumentByRequestProposalId(Integer proposalId) {
        List<RequestForProposalDocument> proposalDocuments = documentRepository.findAll(
                findRFPByProposalSpecification(proposalId),
                Sort.by(Sort.Direction.DESC, "created")
        );

        List<RequestForProposalDocument> uploadedByAdmin = getAllBluePrintDocumentUploadedByAdmin();

        List<RequestForProposalDocument> blueBooks = Stream.concat(
                proposalDocuments.stream()
                        .filter(document -> document.getFilePath().contains("bluebook"))
                        .peek(document -> document.setRole("Contributor")),
                uploadedByAdmin.stream()
                        .peek(document -> document.setRole("Admin"))
        ).collect(Collectors.toList());

        return ProposalDocumentResponse.builder()
                .uploadedDocument(proposalDocuments.stream()
                        .filter(document -> !document.getFilePath().contains("bluebook"))
                        .peek(document -> document.setRole("Contributor"))
                        .collect(Collectors.toList()))
                .blueBookDocument(blueBooks)
                .build();
    }

    @Override
    public List<RequestForProposalDocument> getAllBluePrintDocumentUploadedByAdmin() {
        return documentRepository.findAll(
                adminUploadedDocumenteSpecification(),
                Sort.by(Sort.Direction.DESC, "created")
        );
    }

    @Override
    public void replaceAdminBlueBookDocument(MultipartFile file, Integer documentId) {
        replaceRPFDocument(file, documentId, true, true);
    }

    @Override
    public String uploadBlueBookRFPDocument(MultipartFile[] files, Integer proposalId, boolean isBlueBook) {
        return uploadRFPDocument(files, proposalId, isBlueBook, false);
    }

    @Override
    public String uploadAdminBlueBookRFPDocument(MultipartFile[] files, boolean isBlueBook, boolean isAdmin) {
        return uploadRFPDocument(files, null, isBlueBook, true);
    }

    @Override
    public void copyDocuments(String solicitationId, List<Integer> documentIds) {
        List<RequestForProposalDocument> documents = documentRepository.findAllById(documentIds);
        Map<String, String> fileNameAndFilePathMap = documents.stream()
                .collect(Collectors.toMap(RequestForProposalDocument::getFileName, RequestForProposalDocument::getFilePath));
        documentHelper.deleteFilesInFolder(solicitationId);
        documentHelper.copySpecificDocuments(fileNameAndFilePathMap, solicitationId);
    }

    @Override
    public RequestForProposalDocument getDocumentById(Integer documentId) {
        return findDocumentById(documentId);
    }

    @Override
    public ResponseInputStream<GetObjectResponse> getS3Document(String filePath) {
        return documentHelper.getDocumentFromS3(filePath);
    }

    @Override
    public String resolveContentType(String fileName) {
        if (fileName == null) {
            return "application/octet-stream";
        }

        String lowerFileName = fileName.toLowerCase();

        if (lowerFileName.endsWith(".pdf")) {
            return "application/pdf";
        } else if (lowerFileName.endsWith(".docx")) {
            return "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
        } else if (lowerFileName.endsWith(".doc")) {
            return "application/msword";
        } else if (lowerFileName.endsWith(".txt")) {
            return "text/plain";
        } else if (lowerFileName.endsWith(".xls")) {
            return "application/vnd.ms-excel";
        } else if (lowerFileName.endsWith(".xlsx")) {
            return "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
        } else {
            return "application/octet-stream"; // fallback
        }
    }

    // Private method region start

    private String[] validateMultipartFile(MultipartFile file) {
        List<String> errors = new ArrayList<>();
        if (file == null || file.isEmpty()) {
            log.warn("File is empty or null.");
            errors.add("File is empty. Please upload a valid file.");
            return errors.toArray(new String[0]);
        }

        String filename = StringUtils.cleanPath(file.getOriginalFilename());

        if (filename.contains("..")) {
            log.warn("Invalid filename: {}", filename);
            errors.add("Invalid filename.");
        }

        String fileExtension = getFileExtension(filename);
        if (!ALLOWED_EXTENSIONS.contains(fileExtension.toLowerCase())) {
            log.warn("Invalid file format: {}", fileExtension);
            errors.add("Invalid file format. Only PDF, DOCX, DOC, XLS, XLSX, and TXT are allowed.");
        }
        return errors.toArray(new String[0]);
    }

    private String getFileExtension(String filename) {
        if (StringUtils.isEmpty(filename)) {
            return "";
        }
        int lastDotIndex = filename.lastIndexOf('.');
        return (lastDotIndex == -1) ? "" : filename.substring(lastDotIndex + 1).toLowerCase();
    }

    private RequestForProposalDocument findDocumentById(Integer documentId) {
        return documentRepository.findById(documentId)
                .orElseThrow(() -> new AppException(RFP_DOCUMENT_NOT_FOUND));
    }

    private RequestForProposal findProposalById(Integer proposalId) {
        return requestForProposalRepository.findById(proposalId)
                .orElseThrow(() -> new AppException(RFP_NOT_FOUND));
    }

    // Private method region end
}