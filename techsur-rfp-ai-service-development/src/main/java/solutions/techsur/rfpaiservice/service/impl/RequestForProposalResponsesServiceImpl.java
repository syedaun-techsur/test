package solutions.techsur.rfpaiservice.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import solutions.techsur.common.microservice.exceptions.AppException;
import solutions.techsur.rfpaiservice.dto.ResponsesRequest;
import solutions.techsur.rfpaiservice.entity.RequestForProposal;
import solutions.techsur.rfpaiservice.entity.RequestForProposalResponses;
import solutions.techsur.rfpaiservice.entity.ResponseOutline;
import solutions.techsur.rfpaiservice.repository.RequestForProposalRepository;
import solutions.techsur.rfpaiservice.repository.RequestForProposalResponsesRepository;
import solutions.techsur.rfpaiservice.repository.ResponseOutlineRepository;
import solutions.techsur.rfpaiservice.service.RequestForProposalResponsesService;

import java.util.Objects;

import static solutions.techsur.common.microservice.exceptions.AppReason.*;

@Service
@AllArgsConstructor
@Transactional
public class RequestForProposalResponsesServiceImpl implements RequestForProposalResponsesService {

    private final RequestForProposalResponsesRepository repository;
    private final RequestForProposalRepository proposalRepository;
    private final ResponseOutlineRepository outlineRepository;

    @Override
    public RequestForProposalResponses createResponses(ResponsesRequest request) {
        RequestForProposalResponses responses = new RequestForProposalResponses();
        mapProposalAndOutline(request, responses);
        BeanUtils.copyProperties(request, responses);
        return repository.save(responses);
    }

    @Override
    public void updateResponses(ResponsesRequest request, Integer responsesId) {
        RequestForProposalResponses responses = findResponsesById(responsesId);
        mapProposalAndOutline(request, responses);

        if (Objects.nonNull(request.getGeneratedText())) {
            responses.setGeneratedText(request.getGeneratedText());
        }

        if (request.getStatus() != null && request.getStatus() != RequestForProposalResponses.ResponsesStatus.GENERATED) {
            responses.setStatus(request.getStatus());
        }
        repository.save(responses);
    }

    @Override
    public RequestForProposalResponses getResponses(Integer responseId) {
        return findResponsesById(responseId);
    }

    // private method region start.
    private RequestForProposal findProposalById(Integer id) {
        return proposalRepository.findById(id).orElseThrow(() -> new AppException(RFP_NOT_FOUND));
    }

    private ResponseOutline findOutlineById(Integer id) {
        return outlineRepository.findById(id).orElseThrow(() -> new AppException(RESPONSE_OUTLINE_NOT_FOUND));
    }

    private RequestForProposalResponses findResponsesById(Integer id) {
        return repository.findById(id).orElseThrow(() -> new AppException(RESPONSES_NOT_FOUND));
    }

    private void mapProposalAndOutline(ResponsesRequest request, RequestForProposalResponses responses) {
        if (Objects.nonNull(request.getProposalId())) {
            responses.setProposal(findProposalById(request.getProposalId()));
        }

        if (Objects.nonNull(request.getOutlineId())) {
            responses.setOutline(findOutlineById(request.getOutlineId()));
        }
    }
    // private method region end.
}