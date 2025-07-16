package solutions.techsur.rfpaiservice.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.keycloak.admin.client.Keycloak;
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

    private final Keycloak keycloak;

    private static final List<String> ALLOWED_EXTENSIONS = Arrays.asList("pdf", "docx", "txt", "xls", "xlsx", "doc");

    public RequestForProposalDocumentServiceImpl(final DocumentHelper documentHelper,
                                                 final RequestForProposalRepository requestForProposalRepository,
                                                 final RequestForProposalDocumentRepository documentRepository,
                                                 final Keycloak keycloak) {
        this.documentHelper = documentHelper;
        this.requestForProposalRepository = requestForProposalRepository;
        this.documentRepository = documentRepository;
        this.keycloak = keycloak;
    }

    @Override
    public String uploadRFPDocument(final MultipartFile[] files, final Integer proposalId, final boolean isBlueBook, final boolean isAdmin) {
        // Validate each file before processing
        for (final MultipartFile file : files) {
            final String[] fileValidationMessage = validateMultipartFile(file);
            if (fileValidationMessage.length > 0) {
                throw new AppException(INVALID_FILE, fileValidationMessage);
            }
        }

        final RequestForProposal proposal = (proposalId != null) ? findProposalById(proposalId) : null;

        final List<RequestForProposalDocument> documents = new ArrayList<>();

        final RequestForProposal finalProposal = proposal;
        final List<String> uploadedFiles = Arrays.stream(files)
                .map(file -> {
                    final String fileName = StringUtils.cleanPath(file.getOriginalFilename());
                    final String fileKey;
                    if (isAdmin && isBlueBook) {
                        fileKey = documentHelper.getFilePathForAdminBlueBook(fileName);
                    } else {
                        if (finalProposal == null) {
                            throw new AppException(INVALID_FILE, "Proposal ID must be provided for non-admin uploads");
                        }
                        fileKey = documentHelper.getFilePath(isBlueBook, fileName, finalProposal.getId());
                    }

                    final RequestForProposalDocument document = RequestForProposalDocument.builder()
                            .fileName(fileName)
                            .requestForProposal(finalProposal)
                            .filePath(fileKey)
                            .build();
                    documents.add(document);

                    try {
                        documentHelper.uploadRFPDocument(file, fileKey);
                        return fileName;
                    } catch (IOException e) {
                        log.error("Error uploading file {}", fileName, e);
                        throw new AppException(INTERNAL_ERROR, "Facing issue while uploading the file " + fileName, e);
                    }
                })
                .collect(Collectors.toList());

        documentRepository.saveAll(documents);
        return String.join(", ", uploadedFiles);
    }

    @Override
    public void deleteRFPDocument(final Integer documentId) {
        final RequestForProposalDocument document = findDocumentById(documentId);
        try {
            documentRepository.delete(document);
            documentHelper.deleteRFPDocument(document.getFilePath());
        } catch (Exception e) {
            log.error("Error deleting file: {}", document.getFileName(), e);
            throw new AppException(INTERNAL_ERROR, "Failed to delete file: " + document.getFileName(), e);
        }
    }

    @Override
    public void replaceRPFDocument(final MultipartFile file, final Integer documentId, final boolean isBlueBook, final boolean isAdmin) {
        final String[] fileValidationMessage = validateMultipartFile(file);
        if (fileValidationMessage.length > 0) {
            throw new AppException(INVALID_FILE, fileValidationMessage);
        }
        final String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        final RequestForProposalDocument document = findDocumentById(documentId);
        final String fileKey;
        if (isBlueBook && isAdmin) {
            fileKey = documentHelper.getFilePathForAdminBlueBook(fileName);
        } else {
            fileKey = documentHelper.getFilePath(isBlueBook, fileName, document.getRequestForProposal().getId());
        }

        try {
            documentHelper.deleteRFPDocument(document.getFilePath());
            documentHelper.uploadRFPDocument(file, fileKey);
            document.setFilePath(fileKey);
            document.setFileName(fileName);
            documentRepository.save(document);
        } catch (IOException e) {
            log.error("Error replacing file {}", document.getFileName(), e);
            throw new AppException(INTERNAL_ERROR, "Facing issue while replacing the file " + document.getFileName(), e);
        }
    }

    @Override
    public ProposalDocumentResponse getDocumentByRequestProposalId(final Integer proposalId) {
        final List<RequestForProposalDocument> proposalDocuments = documentRepository.findAll(
                findRFPByProposalSpecification(proposalId),
                Sort.by(Sort.Direction.DESC, "created")
        );

        final List<RequestForProposalDocument> uploadedByAdmin = getAllBluePrintDocumentUploadedByAdmin();

        final List<RequestForProposalDocument> blueBooks = Stream.concat(
                proposalDocuments.stream()
                        .filter(document -> document.getFilePath() != null && document.getFilePath().toLowerCase().contains("bluebook"))
                        .peek(requestForProposalDocument -> requestForProposalDocument.setRole("Contributor")),
                uploadedByAdmin.stream()
                        .peek(requestForProposalDocument -> requestForProposalDocument.setRole("Admin"))
        ).collect(Collectors.toList());

        final List<RequestForProposalDocument> nonBlueBookDocuments = proposalDocuments.stream()
                .filter(document -> document.getFilePath() == null || !document.getFilePath().toLowerCase().contains("bluebook"))
                .peek(requestForProposalDocument -> requestForProposalDocument.setRole("Contributor"))
                .collect(Collectors.toList());

        return ProposalDocumentResponse.builder()
                .uploadedDocument(nonBlueBookDocuments)
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
    public void replaceAdminBlueBookDocument(final MultipartFile file, final Integer documentId) {
        replaceRPFDocument(file, documentId, true, true);
    }

    @Override
    public String uploadBlueBookRFPDocument(final MultipartFile[] files, final Integer proposalId, final boolean isBlueBook) {
        return uploadRFPDocument(files, proposalId, isBlueBook, false);
    }

    @Override
    public String uploadAdminBlueBookRFPDocument(final MultipartFile[] files, final boolean isBlueBook, final boolean isAdmin) {
        return uploadRFPDocument(files, null, isBlueBook, true);
    }

    @Override
    public void copyDocuments(final String solicitationId, final List<Integer> documentIds) {
        final List<RequestForProposalDocument> documents = documentRepository.findAllById(documentIds);
        final Map<String, String> fileNameAndFilePathMap = documents.stream()
                .collect(Collectors.toMap(RequestForProposalDocument::getFileName, RequestForProposalDocument::getFilePath));
        documentHelper.deleteFilesInFolder(solicitationId);
        documentHelper.copySpecificDocuments(fileNameAndFilePathMap, solicitationId);
    }

    @Override
    public RequestForProposalDocument getDocumentById(final Integer documentId) {
        return findDocumentById(documentId);
    }

    public ResponseInputStream<GetObjectResponse> getS3Document(final String filePath) {
        if (filePath == null) {
            throw new AppException(INTERNAL_ERROR, "File path must not be null");
        }
        return documentHelper.getDocumentFromS3(filePath);
    }

    public String resolveContentType(final String fileName) {
        if (fileName == null) {
            return "application/octet-stream";
        }
        final String lowerFileName = fileName.toLowerCase();
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

    // Private helper methods

    private String[] validateMultipartFile(final MultipartFile file) {
        final List<String> errors = new ArrayList<>();
        if (file == null || file.isEmpty()) {
            log.warn("File is empty or null.");
            errors.add("File is empty. Please upload a valid file.");
            return errors.toArray(new String[0]);
        }

        final String filename = StringUtils.cleanPath(file.getOriginalFilename());
        if (filename.contains("..")) {
            log.warn("Invalid filename: {}", filename);
            errors.add("Invalid filename.");
        }

        final String fileExtension = getFileExtension(filename);
        if (!ALLOWED_EXTENSIONS.contains(fileExtension.toLowerCase())) {
            log.warn("Invalid file format: {}", fileExtension);
            errors.add("Invalid file format. Only PDF, DOCX, DOC, XLS, XLSX, and TXT are allowed.");
        }
        return errors.toArray(new String[0]);
    }

    private String getFileExtension(final String filename) {
        if (filename == null || !filename.contains(".")) {
            return "";
        }
        return filename.substring(filename.lastIndexOf('.') + 1).toLowerCase();
    }

    private RequestForProposalDocument findDocumentById(final Integer documentId) {
        return documentRepository.findById(documentId)
                .orElseThrow(() -> new AppException(RFP_DOCUMENT_NOT_FOUND));
    }

    private RequestForProposal findProposalById(final Integer proposalId) {
        return requestForProposalRepository.findById(proposalId)
                .orElseThrow(() -> new AppException(RFP_NOT_FOUND));
    }
}