package solutions.techsur.rfpaiservice.api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import solutions.techsur.rfpaiservice.controller.ComplianceMatrixController;
import solutions.techsur.rfpaiservice.dto.CommonFilter;
import solutions.techsur.rfpaiservice.dto.ComplianceMatrixRequest;
import solutions.techsur.rfpaiservice.dto.CreationDTO;
import solutions.techsur.rfpaiservice.entity.ComplianceMatrix;
import solutions.techsur.rfpaiservice.service.ComplianceMatrixService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ComplianceMatrixControllerTest {

    @Mock
    private ComplianceMatrixService service;

    @InjectMocks
    private ComplianceMatrixController controller;

    private ComplianceMatrix complianceMatrix;
    private List<ComplianceMatrixRequest> complianceMatrixRequest;

    @BeforeEach
    void setUp() {
        complianceMatrix = new ComplianceMatrix();
        complianceMatrix.setId(1);
        complianceMatrixRequest = List.of(new ComplianceMatrixRequest());
    }

    @Test
    void testCreateComplianceMatrices() {
        when(service.createComplianceMatrix(anyInt(), anyList())).thenReturn(List.of(complianceMatrix));

        ResponseEntity<List<CreationDTO>> response = controller.createComplianceMatrices(anyInt(), complianceMatrixRequest);

        assertEquals(201, response.getStatusCodeValue());
        assertFalse(response.getBody().isEmpty());
    }

    @Test
    void testGetComplianceMatrix() {
        when(service.getComplianceMatrix(1)).thenReturn(complianceMatrix);

        ResponseEntity<ComplianceMatrix> response = controller.getComplianceMatrix(1);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().getId());
    }

    @Test
    void testGetComplianceMatrixPage() {
        Page<ComplianceMatrix> page = new PageImpl<>(List.of(complianceMatrix));
        when(service.getComplianceMatrixPage(any(), any(), anyInt())).thenReturn(page);

        ResponseEntity<Page<ComplianceMatrix>> response = controller.getComplianceMatrixPage(new CommonFilter(), PageRequest.of(0, 10), anyInt());

        assertEquals(200, response.getStatusCodeValue());
        assertFalse(response.getBody().isEmpty());
    }

    @Test
    void testUpdateComplianceMatrix() {
        doNothing().when(service).updateComplianceMatrix(any(), anyInt());

        ResponseEntity<Void> response = controller.updateComplianceMatrix(1, complianceMatrixRequest);

        assertEquals(200, response.getStatusCodeValue());
    }
}
