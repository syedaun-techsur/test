package solutions.techsur.rfpaiservice.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import solutions.techsur.rfpaiservice.dto.OutlineResponse;
import solutions.techsur.rfpaiservice.dto.ProposalFilter;
import solutions.techsur.rfpaiservice.dto.ProposalRequest;
import solutions.techsur.rfpaiservice.dto.SingleOutlineRequest;
import solutions.techsur.rfpaiservice.entity.RequestForProposal;

public interface RequestForProposalService {

    /**
     * Creates a new Request for Proposal.
     *
     * @param proposalRequest the request data
     * @return the created RequestForProposal entity
     */
    RequestForProposal createRequestForProposal(ProposalRequest proposalRequest);

    /**
     * Retrieves paginated proposals based on filters.
     *
     * @param filter   filter criteria
     * @param pageable pagination information
     * @return paginated proposals page
     */
    Page<RequestForProposal> getProposals(ProposalFilter filter, Pageable pageable);

    /**
     * Creates a response outline linked to a proposal.
     *
     * @param outlineResponse the outline request data
     * @param proposalId      the proposal identifier
     * @return the updated RequestForProposal entity
     */
    RequestForProposal createResponseOutline(OutlineResponse outlineResponse, Integer proposalId);

    /**
     * Retrieves a proposal with its outlines.
     *
     * @param proposalId the proposal identifier
     * @return the proposal with outlines
     */
    OutlineResponse getProposalWithOutlines(Integer proposalId);

    /**
     * Updates a response outline for a specific proposal.
     *
     * @param outlineResponse the outline data to update
     * @param proposalId      the proposal identifier
     */
    void updateResponseOutline(OutlineResponse outlineResponse, Integer proposalId);

    /**
     * Deletes a request for proposal by its ID.
     *
     * @param proposalId the proposal identifier
     */
    void deleteRequestForProposal(Integer proposalId);

    /**
     * Deletes a response outline by its ID.
     *
     * @param outlineId the outline identifier
     */
    void deleteResponseOutline(Integer outlineId);

    /**
     * Retrieves a proposal by its ID.
     *
     * @param proposalId the proposal identifier
     * @return the RequestForProposal entity
     */
    RequestForProposal getProposalById(Integer proposalId);

    /**
     * Updates a request for proposal with new data.
     *
     * @param proposalRequest the updated request data
     * @param proposalId      the proposal identifier
     */
    void updateRequestForProposal(ProposalRequest proposalRequest, Integer proposalId);

    /**
     * Updates a single response outline.
     *
     * @param singleOutlineRequest the outline data to update
     * @param outlineId            the outline identifier
     */
    void updateSingleResponseOutline(SingleOutlineRequest singleOutlineRequest, Integer outlineId);
}