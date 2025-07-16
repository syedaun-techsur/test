package solutions.techsur.rfpaiservice.service;

import solutions.techsur.rfpaiservice.dto.ResponsesRequest;
import solutions.techsur.rfpaiservice.entity.RequestForProposalResponses;

public interface RequestForProposalResponsesService {
    RequestForProposalResponses createResponses(ResponsesRequest request);

    void updateResponses(ResponsesRequest request, Integer responsesId);

    RequestForProposalResponses getResponses(Integer responseId);
}
