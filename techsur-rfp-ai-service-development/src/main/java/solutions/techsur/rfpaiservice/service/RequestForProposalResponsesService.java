package solutions.techsur.rfpaiservice.service;

import solutions.techsur.rfpaiservice.dto.ResponsesRequest;
import solutions.techsur.rfpaiservice.entity.RequestForProposalResponses;

/**
 * Service interface for managing Request For Proposal Responses.
 */
public interface RequestForProposalResponsesService {

    /**
     * Creates a new RequestForProposalResponses entity from the given request.
     *
     * @param request the data to create the response
     * @return the created RequestForProposalResponses entity
     */
    RequestForProposalResponses createResponses(final ResponsesRequest request);

    /**
     * Updates an existing RequestForProposalResponses entity identified by responseId using the provided request data.
     *
     * @param request the update data
     * @param responseId the identifier of the response to update
     */
    void updateResponses(final ResponsesRequest request, final Integer responseId);

    /**
     * Retrieves a RequestForProposalResponses entity by its identifier.
     *
     * @param responseId the identifier of the response to retrieve
     * @return the found RequestForProposalResponses entity
     */
    RequestForProposalResponses getResponses(final Integer responseId);
}