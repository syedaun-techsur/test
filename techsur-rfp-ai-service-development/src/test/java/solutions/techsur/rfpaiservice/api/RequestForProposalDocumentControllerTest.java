package solutions.techsur.rfpaiservice.api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import solutions.techsur.rfpaiservice.controller.RequestForProposalDocumentController;
import solutions.techsur.rfpaiservice.dto.ProposalDocumentResponse;
import solutions.techsur.rfpaiservice.entity.RequestForProposalDocument;
import solutions.techsur.rfpaiservice.service.RequestForProposalDocumentService;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RequestForProposalDocumentControllerTest {

    @Mock
    private RequestForProposalDocumentService documentService;

    @InjectMocks
    private RequestForProposalDocumentController documentController;

    private MockMultipartFile mockFile;

    @BeforeEach
    void setUp() {
        mockFile = new MockMultipartFile("files", "test.pdf", "application/pdf", "dummy content".getBytes());
    }

    @Test
    void testUploadRFPDocument() {
        // Arrange
        when(documentService.uploadRFPDocument(any(MockMultipartFile[].class), anyInt(), eq(false), eq(false)))
                .thenReturn("File uploaded successfully.");

        // Act
        ResponseEntity<String> response = documentController.uploadRFPDocument(new MockMultipartFile[]{mockFile}, 1);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("File uploaded successfully.", response.getBody());

        verify(documentService).uploadRFPDocument(any(MockMultipartFile[].class), anyInt(), eq(false), eq(false));
        verifyNoMoreInteractions(documentService);
    }

    @Test
    void testDeleteRFPDocument() {
        // Arrange
        doNothing().when(documentService).deleteRFPDocument(1);

        // Act
        ResponseEntity<Void> response = documentController.deleteRFPDocument(1);

        // Assert
        assertEquals(204, response.getStatusCodeValue());

        verify(documentService).deleteRFPDocument(1);
        verifyNoMoreInteractions(documentService);
    }

    @Test
    void testReplaceDocument() {
        // Arrange
        doNothing().when(documentService).replaceRPFDocument(any(MockMultipartFile.class), anyInt(), eq(false), eq(false));

        // Act
        ResponseEntity<Void> response = documentController.replaceDocument(1, mockFile, false);

        // Assert
        assertEquals(204, response.getStatusCodeValue());
        verify(documentService).replaceRPFDocument(any(MockMultipartFile.class), anyInt(), eq(false), eq(false));
        verifyNoMoreInteractions(documentService);
    }

    @Test
    void testGetDocumentByRequestProposalId() {
        // Arrange
        ProposalDocumentResponse mockResponse = new ProposalDocumentResponse();
        when(documentService.getDocumentByRequestProposalId(1)).thenReturn(mockResponse);

        // Act
        ResponseEntity<ProposalDocumentResponse> response = documentController.getDocumentByRequestProposalId(1);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());

        verify(documentService).getDocumentByRequestProposalId(1);
        verifyNoMoreInteractions(documentService);
    }

    @Test
    void testUploadBlueBookRFPDocument() {
        // Arrange
        when(documentService.uploadBlueBookRFPDocument(any(MockMultipartFile[].class), anyInt(), eq(true)))
                .thenReturn("File uploaded successfully.");

        // Act
        ResponseEntity<String> response = documentController.uploadBlueBookRFPDocument(new MockMultipartFile[]{mockFile}, 1);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("File uploaded successfully.", response.getBody());

        verify(documentService).uploadBlueBookRFPDocument(any(MockMultipartFile[].class), anyInt(), eq(true));
        verifyNoMoreInteractions(documentService);
    }

    @Test
    void testUploadAdminBlueBookRFPDocument() {
        // Arrange
        when(documentService.uploadAdminBlueBookRFPDocument(any(MockMultipartFile[].class), eq(true), eq(true)))
                .thenReturn("File uploaded successfully.");

        // Act
        ResponseEntity<String> response = documentController.uploadAdminBlueBookRFPDocument(new MockMultipartFile[]{mockFile});

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("File uploaded successfully.", response.getBody());

        verify(documentService).uploadAdminBlueBookRFPDocument(any(MockMultipartFile[].class), eq(true), eq(true));
        verifyNoMoreInteractions(documentService);
    }

    @Test
    void testGetAllBluePrintDocumentUploadedByAdmin() {
        // Arrange
        List<RequestForProposalDocument> mockDocuments = List.of(new RequestForProposalDocument());
        when(documentService.getAllBluePrintDocumentUploadedByAdmin()).thenReturn(mockDocuments);

        // Act
        ResponseEntity<List<RequestForProposalDocument>> response = documentController.getAllBluePrintDocumentUploadedByAdmin();

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());

        verify(documentService).getAllBluePrintDocumentUploadedByAdmin();
        verifyNoMoreInteractions(documentService);
    }

    @Test
    void testReplaceAdminBlueBookDocument() {
        // Arrange
        doNothing().when(documentService).replaceAdminBlueBookDocument(anyInt(), any(MockMultipartFile.class));

        // Act
        ResponseEntity<Void> response = documentController.replaceAdminBlueBookDocument(1, mockFile);

        // Assert
        assertEquals(204, response.getStatusCodeValue());

        verify(documentService).replaceAdminBlueBookDocument(1, mockFile);
        verifyNoMoreInteractions(documentService);
    }

    @Test
    void testCopyDocuments_Success() {
        // Arrange
        String solicitationId = "12345";
        List<Integer> documentIds = Arrays.asList(101, 102, 103);

        // Act
        ResponseEntity<Void> response = documentController.copyDocuments(solicitationId, documentIds);

        // Assert
        verify(documentService).copyDocuments(solicitationId, documentIds);
        assertEquals(204, response.getStatusCodeValue());
        assertNull(response.getBody());

        verifyNoMoreInteractions(documentService);
    }
}