package solutions.techsur.rfpaiservice.service;

import solutions.techsur.rfpaiservice.dto.ResponsesRequest;
import solutions.techsur.rfpaiservice.entity.RequestForProposalResponses;

/**
 * Service interface for managing Request For Proposal Responses.
 */
@FunctionalInterface
public interface RequestForProposalResponsesService {

    /**
     * Creates new RequestForProposalResponses based on the provided request data.
     *
     * @param request the data to create the responses
     * @return the created RequestForProposalResponses entity
     */
    RequestForProposalResponses createResponses(ResponsesRequest request);

    /**
     * Updates existing RequestForProposalResponses with updated data.
     *
     * @param request the updated response data
     * @param responseId the ID of the response to update
     */
    void updateResponses(ResponsesRequest request, Integer responseId);

    /**
     * Retrieves a RequestForProposalResponses entity by its ID.
     *
     * @param responseId the ID of the response to retrieve
     * @return the corresponding RequestForProposalResponses entity
     */
    RequestForProposalResponses getResponses(Integer responseId);
}