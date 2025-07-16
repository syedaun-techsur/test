package solutions.techsur.rfpaiservice.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import solutions.techsur.rfpaiservice.dto.CommonFilter;
import solutions.techsur.rfpaiservice.dto.ComplianceMatrixRequest;
import solutions.techsur.rfpaiservice.entity.ComplianceMatrix;

import java.util.List;

public interface ComplianceMatrixService {

    List<ComplianceMatrix> createComplianceMatrix(Integer proposalId, List<ComplianceMatrixRequest> request);

    ComplianceMatrix getComplianceMatrix(Integer matrixId);

    Page<ComplianceMatrix> getComplianceMatrixPage(CommonFilter filter, Pageable pageable, Integer proposalId);

    void updateComplianceMatrix(List<ComplianceMatrixRequest> request, Integer proposalId);
}
