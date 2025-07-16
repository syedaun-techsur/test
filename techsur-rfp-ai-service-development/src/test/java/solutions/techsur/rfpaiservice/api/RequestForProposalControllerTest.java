package solutions.techsur.rfpaiservice.api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

public class RequestForProposalControllerTest {

    @InjectMocks
    private RequestForProposalController proposalController;

    @Mock
    private RequestForProposalServiceImpl proposalService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // If environment requires, consider initMocks(this)
    }

    @Test
    void testCreateProposal() {
        ProposalRequest request = new ProposalRequest();
        RequestForProposal savedProposal = new RequestForProposal();
        savedProposal.setId(1);

        when(proposalService.createRequestForProposal(request)).thenReturn(savedProposal);

        ResponseEntity<CreationDTO> response = proposalController.createProposal(request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody(), "Response body should not be null");
        assertEquals(1, response.getBody().getId());
    }

    @Test
    void testGetProposals() {
        ProposalFilter filter = new ProposalFilter();
        Pageable pageable = Pageable.unpaged(); // Better practice for tests here
        Page<RequestForProposal> page = new PageImpl<>(Collections.emptyList());

        when(proposalService.getProposals(filter, pageable)).thenReturn(page);

        ResponseEntity<Page<RequestForProposal>> response = proposalController.getProposals(filter, pageable);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody(), "Response body should not be null");
    }

    @Test
    void testCreateResponseOutline() {
        Integer proposalId = 1;
        OutlineResponse request = new OutlineResponse();
        RequestForProposal responseOutline = new RequestForProposal();
        responseOutline.setId(2);

        when(proposalService.createResponseOutline(request, proposalId)).thenReturn(responseOutline);

        ResponseEntity<CreationDTO> response = proposalController.createResponseOutline(proposalId, request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody(), "Response body should not be null");
        assertEquals(2, response.getBody().getId());
    }

    @Test
    void testGetResponseOutline() {
        Integer proposalId = 1;
        OutlineResponse outlineResponse = new OutlineResponse();

        when(proposalService.getProposalWithOutlines(proposalId)).thenReturn(outlineResponse);

        ResponseEntity<OutlineResponse> response = proposalController.getResponseOutline(proposalId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody(), "Response body should not be null");
    }

    @Test
    void testDeleteProposal() {
        Integer proposalId = 1;
        doNothing().when(proposalService).deleteRequestForProposal(proposalId);

        ResponseEntity<Void> response = proposalController.deleteRequestForProposal(proposalId);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }
}