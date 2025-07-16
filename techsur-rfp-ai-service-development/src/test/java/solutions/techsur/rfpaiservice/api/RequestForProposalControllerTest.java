package solutions.techsur.rfpaiservice.api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import solutions.techsur.rfpaiservice.controller.RequestForProposalController;
import solutions.techsur.rfpaiservice.dto.CreationDTO;
import solutions.techsur.rfpaiservice.dto.OutlineResponse;
import solutions.techsur.rfpaiservice.dto.ProposalFilter;
import solutions.techsur.rfpaiservice.dto.ProposalRequest;
import solutions.techsur.rfpaiservice.entity.RequestForProposal;
import solutions.techsur.rfpaiservice.service.impl.RequestForProposalServiceImpl;

import java.util.Collections;

import static org.mockito.Mockito.*;

public class RequestForProposalControllerTest {

    @InjectMocks
    private RequestForProposalController proposalController;

    @Mock
    private RequestForProposalServiceImpl proposalService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createProposal_ShouldReturnCreatedProposal() {
        // Arrange
        ProposalRequest request = new ProposalRequest();
        RequestForProposal savedProposal = new RequestForProposal();
        savedProposal.setId(1);

        when(proposalService.createRequestForProposal(request)).thenReturn(savedProposal);

        // Act
        ResponseEntity<CreationDTO> response = proposalController.createProposal(request);

        // Assert
        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());
        Assertions.assertNotNull(response.getBody(), "Response body should not be null");
        Assertions.assertEquals(1, response.getBody().getId());
    }

    @Test
    void getProposals_ShouldReturnProposalPage() {
        // Arrange
        ProposalFilter filter = new ProposalFilter();
        Pageable pageable = mock(Pageable.class);
        Page<RequestForProposal> page = new PageImpl<>(Collections.emptyList());

        when(proposalService.getProposals(filter, pageable)).thenReturn(page);

        // Act
        ResponseEntity<Page<RequestForProposal>> response = proposalController.getProposals(filter, pageable);

        // Assert
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(response.getBody(), "Response body should not be null");
    }

    @Test
    void createResponseOutline_ShouldReturnCreatedOutline() {
        // Arrange
        Integer proposalId = 1;
        OutlineResponse request = new OutlineResponse();
        RequestForProposal responseOutline = new RequestForProposal();
        responseOutline.setId(2);

        when(proposalService.createResponseOutline(request, proposalId)).thenReturn(responseOutline);

        // Act
        ResponseEntity<CreationDTO> response = proposalController.createResponseOutline(proposalId, request);

        // Assert
        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());
        Assertions.assertNotNull(response.getBody(), "Response body should not be null");
        Assertions.assertEquals(2, response.getBody().getId());
    }

    @Test
    void getResponseOutline_ShouldReturnOutlineResponse() {
        // Arrange
        Integer proposalId = 1;
        OutlineResponse outlineResponse = new OutlineResponse();

        when(proposalService.getProposalWithOutlines(proposalId)).thenReturn(outlineResponse);

        // Act
        ResponseEntity<OutlineResponse> response = proposalController.getResponseOutline(proposalId);

        // Assert
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(response.getBody(), "Response body should not be null");
    }

    @Test
    void deleteProposal_ShouldReturnNoContent() {
        // Arrange
        Integer proposalId = 1;
        doNothing().when(proposalService).deleteRequestForProposal(proposalId);

        // Act
        ResponseEntity<Void> response = proposalController.deleteRequestForProposal(proposalId);

        // Assert
        Assertions.assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }
}