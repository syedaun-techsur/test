package solutions.techsur.rfpaiservice.api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import solutions.techsur.rfpaiservice.controller.RequestForProposalResponsesController;
import solutions.techsur.rfpaiservice.dto.CreationDTO;
import solutions.techsur.rfpaiservice.dto.ResponsesRequest;
import solutions.techsur.rfpaiservice.entity.RequestForProposalResponses;
import solutions.techsur.rfpaiservice.service.RequestForProposalResponsesService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RequestForProposalResponsesControllerTest {

    @Mock
    private RequestForProposalResponsesService service;

    @InjectMocks
    private RequestForProposalResponsesController controller;

    private ResponsesRequest request;
    private RequestForProposalResponses responseEntity;

    @BeforeEach
    void setUp() {
        request = new ResponsesRequest();
        responseEntity = new RequestForProposalResponses();
        responseEntity.setId(1);
    }

    @Test
    void testCreateResponses() {
        when(service.createResponses(any(ResponsesRequest.class))).thenReturn(responseEntity);

        ResponseEntity<CreationDTO> response = controller.createResponses(request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody(), "Response body should not be null");
        assertEquals(1, response.getBody().getId());
    }

    @Test
    void testUpdateResponses() {
        doNothing().when(service).updateResponses(any(ResponsesRequest.class), eq(1));

        ResponseEntity<Void> response = controller.updateResponses(request, 1);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(service, times(1)).updateResponses(any(ResponsesRequest.class), eq(1));
    }

    @Test
    void testGetResponses() {
        when(service.getResponses(eq(1))).thenReturn(responseEntity);

        ResponseEntity<RequestForProposalResponses> response = controller.getResponses(1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody(), "Response body should not be null");
        assertEquals(1, response.getBody().getId());
    }
}