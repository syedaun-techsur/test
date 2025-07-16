package solutions.techsur.rfpaiservice.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import solutions.techsur.rfpaiservice.dto.*;
import solutions.techsur.rfpaiservice.entity.RequestForProposal;
import solutions.techsur.rfpaiservice.entity.ResponseOutline;

public interface RequestForProposalService {
    RequestForProposal createRequestForProposal(ProposalRequest request);

    Page<RequestForProposal> getProposals(ProposalFilter filter, Pageable pageable);

    RequestForProposal createResponseOutline(OutlineResponse request, Integer proposalId);

    OutlineResponse getProposalWithOutlines(Integer proposalId);

    void updateResponseOutline(OutlineResponse request, Integer proposalId);

    void deleteRequestForProposal(Integer proposalId);

    void deleteResponseOutline(Integer outlineId);

    RequestForProposal getProposalById(Integer proposalId);

    void updateRequestForProposal(ProposalRequest request, Integer proposalId);

    void updateSingleResponseOutline(SingleOutlineRequest request, Integer outlineId);

}
