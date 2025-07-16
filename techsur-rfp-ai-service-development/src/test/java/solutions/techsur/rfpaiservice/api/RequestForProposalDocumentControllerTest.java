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
        when(documentService.uploadRFPDocument(any(MockMultipartFile[].class), anyInt(), eq(false), eq(false))).thenReturn("File uploaded successfully.");

        ResponseEntity<String> response = documentController.uploadRFPDocument(new MockMultipartFile[]{mockFile}, 1);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("File uploaded successfully.", response.getBody());
    }

    @Test
    void testDeleteRFPDocument() {
        doNothing().when(documentService).deleteRFPDocument(1);

        ResponseEntity<Void> response = documentController.deleteRFPDocument(1);

        assertEquals(204, response.getStatusCodeValue());
    }

    @Test
    void testReplaceDocument() {
        doNothing().when(documentService).replaceRPFDocument(any(MockMultipartFile.class), anyInt(), eq(false), eq(false));

        ResponseEntity<Void> response = documentController.replaceDocument(1, mockFile, false);

        assertEquals(204, response.getStatusCodeValue());

        verify(documentService, times(1)).replaceRPFDocument(any(MockMultipartFile.class), anyInt(), eq(false), eq(false));
    }

    @Test
    void testGetDocumentByRequestProposalId() {
        ProposalDocumentResponse mockResponse = new ProposalDocumentResponse();
        when(documentService.getDocumentByRequestProposalId(1)).thenReturn(mockResponse);

        ResponseEntity<ProposalDocumentResponse> response = documentController.getDocumentByRequestProposalId(1);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
    }

    @Test
    void testUploadBlueBookRFPDocument() {
        when(documentService.uploadBlueBookRFPDocument(any(MockMultipartFile[].class), anyInt(), eq(true))).thenReturn("File uploaded successfully.");

        ResponseEntity<String> response = documentController.uploadBlueBookRFPDocument(new MockMultipartFile[]{mockFile}, 1);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("File uploaded successfully.", response.getBody());
    }

    @Test
    void testUploadAdminBlueBookRFPDocument() {
        when(documentService.uploadAdminBlueBookRFPDocument(any(MockMultipartFile[].class), eq(true), eq(true)))
                .thenReturn("File uploaded successfully.");

        ResponseEntity<String> response = documentController.uploadAdminBlueBookRFPDocument(new MockMultipartFile[]{mockFile});

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("File uploaded successfully.", response.getBody());
    }

    @Test
    void testGetAllBluePrintDocumentUploadedByAdmin() {
        List<RequestForProposalDocument> mockDocuments = List.of(new RequestForProposalDocument());
        when(documentService.getAllBluePrintDocumentUploadedByAdmin()).thenReturn(mockDocuments);

        ResponseEntity<List<RequestForProposalDocument>> response = documentController.getAllBluePrintDocumentUploadedByAdmin();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void testReplaceAdminBlueBookDocument() {
        doNothing().when(documentService).replaceAdminBlueBookDocument(anyInt(), any(MockMultipartFile.class));

        ResponseEntity<Void> response = documentController.replaceAdminBlueBookDocument(1, mockFile);

        assertEquals(204, response.getStatusCodeValue());
    }

    @Test
    void testCopyDocuments_Success() {
        String solicitationId = "12345";
        List<Integer> documentIds = Arrays.asList(101, 102, 103);

        ResponseEntity<Void> response = documentController.copyDocuments(solicitationId, documentIds);

        verify(documentService, times(1)).copyDocuments(solicitationId, documentIds);
        assertEquals(204, response.getStatusCodeValue());
        assertNull(response.getBody());
    }
}