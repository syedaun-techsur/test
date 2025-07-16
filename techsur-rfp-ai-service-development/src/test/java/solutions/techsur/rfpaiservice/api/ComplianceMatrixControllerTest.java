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
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ComplianceMatrixControllerTest {

    @Mock
    private ComplianceMatrixService service;

    @InjectMocks
    private ComplianceMatrixController controller;

    private ComplianceMatrix complianceMatrix;
    private List<ComplianceMatrixRequest> complianceMatrixRequests;

    @BeforeEach
    void setUp() {
        complianceMatrix = new ComplianceMatrix();
        complianceMatrix.setId(1);
        complianceMatrixRequests = List.of(new ComplianceMatrixRequest());
    }

    @Test
    void shouldCreateComplianceMatricesSuccessfully() {
        // Arrange
        when(service.createComplianceMatrix(anyInt(), anyList())).thenReturn(List.of(complianceMatrix));

        // Act - use explicit projectId rather than anyInt matcher as argument
        int projectId = 1;
        ResponseEntity<List<CreationDTO>> response = controller.createComplianceMatrices(projectId, complianceMatrixRequests);

        // Assert
        assertEquals(201, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isEmpty());
    }

    @Test
    void shouldReturnComplianceMatrixById() {
        // Arrange
        when(service.getComplianceMatrix(1)).thenReturn(complianceMatrix);

        // Act
        ResponseEntity<ComplianceMatrix> response = controller.getComplianceMatrix(1);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().getId());
    }

    @Test
    void shouldReturnPagedComplianceMatrix() {
        // Arrange
        Page<ComplianceMatrix> page = new PageImpl<>(List.of(complianceMatrix));
        when(service.getComplianceMatrixPage(any(CommonFilter.class), any(PageRequest.class), anyInt())).thenReturn(page);

        // Act
        CommonFilter filter = new CommonFilter();
        PageRequest pageRequest = PageRequest.of(0, 10);
        int projectId = 1;
        ResponseEntity<Page<ComplianceMatrix>> response = controller.getComplianceMatrixPage(filter, pageRequest, projectId);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isEmpty());
        assertEquals(1, response.getBody().getTotalElements());
    }

    @Test
    void shouldUpdateComplianceMatrixSuccessfully() {
        // Arrange
        doNothing().when(service).updateComplianceMatrix(any(), anyInt());

        // Act
        int projectId = 1;
        ResponseEntity<Void> response = controller.updateComplianceMatrix(projectId, complianceMatrixRequests);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
    }
}