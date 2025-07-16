package solutions.techsur.rfpaiservice.service.impl;

import lombok.AllArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import solutions.techsur.common.microservice.exceptions.AppException;
import solutions.techsur.rfpaiservice.dto.CommonFilter;
import solutions.techsur.rfpaiservice.dto.ComplianceMatrixRequest;
import solutions.techsur.rfpaiservice.entity.ComplianceMatrix;
import solutions.techsur.rfpaiservice.entity.RequestForProposal;
import solutions.techsur.rfpaiservice.repository.ComplianceMatrixRepository;
import solutions.techsur.rfpaiservice.repository.RequestForProposalRepository;
import solutions.techsur.rfpaiservice.service.ComplianceMatrixService;

import java.util.ArrayList;
import java.util.List;

import static solutions.techsur.common.microservice.exceptions.AppReason.COMPLIANCE_MATRIX_NOT_FOUND;
import static solutions.techsur.common.microservice.exceptions.AppReason.RFP_NOT_FOUND;
import static solutions.techsur.rfpaiservice.repository.ComplianceMatrixSpecification.multiFieldSearch;
import static solutions.techsur.rfpaiservice.repository.ComplianceMatrixSpecification.proposalSpecification;

@Service
@Transactional
@AllArgsConstructor
public class ComplianceMatrixServiceImpl implements ComplianceMatrixService {

    private final ComplianceMatrixRepository repository;
    private final RequestForProposalRepository proposalRepository;

    @Override
    public List<ComplianceMatrix> createComplianceMatrix(Integer proposalId, List<ComplianceMatrixRequest> request) {
        RequestForProposal proposal = findProposalById(proposalId);
        repository.deleteByProposalId(proposal.getId());

        List<ComplianceMatrix> matrices = new ArrayList<>();
        for (ComplianceMatrixRequest matrixRequest : request) {
            ComplianceMatrix matrix = ComplianceMatrix.builder().build();
            matrix.setProposal(proposal);
            BeanUtils.copyProperties(matrixRequest, matrix);
            matrices.add(matrix);
        }
        return repository.saveAll(matrices);
    }

    @Override
    public ComplianceMatrix getComplianceMatrix(Integer matrixId) {
        return findComplianceMatrix(matrixId);
    }

    @Override
    public Page<ComplianceMatrix> getComplianceMatrixPage(CommonFilter filter, Pageable pageable, Integer proposalId) {
        Specification<ComplianceMatrix> specification = proposalSpecification(proposalId);
        if (StringUtils.isNotBlank(filter.getSearch())) {
            specification = specification.and(multiFieldSearch(filter));
        }
        return repository.findAll(specification, pageable);
    }

    @Override
    public void updateComplianceMatrix(List<ComplianceMatrixRequest> request, Integer proposalId) {
        RequestForProposal proposal = findProposalById(proposalId);
        if (ObjectUtils.isNotEmpty(proposal.getComplianceMatrices())) {
            proposal.getComplianceMatrices().clear();
        }
        List<ComplianceMatrix> matrices = new ArrayList<>();
        for (ComplianceMatrixRequest complianceMatrixRequest : request) {
            ComplianceMatrix matrix = ComplianceMatrix.builder().build();
            if (StringUtils.isNotBlank(complianceMatrixRequest.getRequirement())) {
                matrix.setRequirement(complianceMatrixRequest.getRequirement());
            }
            matrix.setStatus(complianceMatrixRequest.getStatus());

            if (StringUtils.isNotBlank(complianceMatrixRequest.getJustification())) {
                matrix.setJustification(complianceMatrixRequest.getJustification());
            }

            if (complianceMatrixRequest.getSectionNo() != null) {
                matrix.setSectionNo(complianceMatrixRequest.getSectionNo());
            }
            matrix.setProposal(proposal);
            matrices.add(matrix);
        }
        repository.saveAll(matrices);
    }

    // private method region start.
    private RequestForProposal findProposalById(Integer id) {
        return proposalRepository.findById(id).orElseThrow(() -> new AppException(RFP_NOT_FOUND));
    }

    private ComplianceMatrix findComplianceMatrix(Integer matrixId) {
        return repository.findById(matrixId).orElseThrow(() -> new AppException(COMPLIANCE_MATRIX_NOT_FOUND));
    }

    //private method region end.
}