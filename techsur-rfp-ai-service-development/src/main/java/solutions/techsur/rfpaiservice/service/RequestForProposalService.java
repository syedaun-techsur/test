package solutions.techsur.rfpaiservice.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import solutions.techsur.rfpaiservice.dto.*;
import solutions.techsur.rfpaiservice.entity.RequestForProposal;
import solutions.techsur.rfpaiservice.entity.ResponseOutline;

/**
 * Service interface for handling RequestForProposal related operations.
 */
public interface RequestForProposalService {

    /**
     * Creates a new RequestForProposal.
     *
     * @param request the proposal request data
     * @return the created RequestForProposal entity
     */
    RequestForProposal createRequestForProposal(ProposalRequest request);

    /**
     * Retrieves paged RequestForProposal entities based on filter criteria.
     *
     * @param filter   the proposal filter criteria
     * @param pageable the pagination information
     * @return a paged list of RequestForProposal entities
     */
    Page<RequestForProposal> getProposals(ProposalFilter filter, Pageable pageable);

    /**
     * Creates a new ResponseOutline linked to a specific proposal.
     *
     * @param request    the outline response data
     * @param proposalId the ID of the related proposal
     * @return the created RequestForProposal entity
     */
    RequestForProposal createResponseOutline(OutlineResponse request, int proposalId);

    /**
     * Retrieves a proposal along with its associated outlines.
     *
     * @param proposalId the ID of the proposal
     * @return the OutlineResponse containing proposal and outlines
     */
    OutlineResponse getProposalWithOutlines(int proposalId);

    /**
     * Updates an existing ResponseOutline for a given proposal.
     *
     * @param request    the updated outline response data
     * @param proposalId the ID of the related proposal
     */
    void updateResponseOutline(OutlineResponse request, int proposalId);

    /**
     * Deletes a RequestForProposal by its ID.
     *
     * @param proposalId the ID of the proposal to delete
     */
    void deleteRequestForProposal(int proposalId);

    /**
     * Deletes a ResponseOutline by its ID.
     *
     * @param outlineId the ID of the response outline to delete
     */
    void deleteResponseOutline(int outlineId);

    /**
     * Retrieves a RequestForProposal by its ID.
     *
     * @param proposalId the ID of the proposal
     * @return the found RequestForProposal entity
     */
    RequestForProposal getProposalById(int proposalId);

    /**
     * Updates an existing RequestForProposal.
     *
     * @param request    the updated proposal request data
     * @param proposalId the ID of the proposal to update
     */
    void updateRequestForProposal(ProposalRequest request, int proposalId);

    /**
     * Updates a single ResponseOutline by its outline ID.
     *
     * @param request   the updated single outline request data
     * @param outlineId the ID of the response outline to update
     */
    void updateSingleResponseOutline(SingleOutlineRequest request, int outlineId);
}