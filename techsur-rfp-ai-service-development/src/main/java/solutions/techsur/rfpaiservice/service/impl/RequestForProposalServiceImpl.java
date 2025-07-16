package solutions.techsur.rfpaiservice.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import solutions.techsur.common.microservice.exceptions.AppException;
import solutions.techsur.rfpaiservice.document.DocumentHelper;
import solutions.techsur.rfpaiservice.dto.*;
import solutions.techsur.rfpaiservice.entity.RequestForProposal;
import solutions.techsur.rfpaiservice.entity.ResponseOutline;
import solutions.techsur.rfpaiservice.repository.RequestForProposalRepository;
import solutions.techsur.rfpaiservice.repository.ResponseOutlineRepository;
import solutions.techsur.rfpaiservice.service.RequestForProposalService;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static solutions.techsur.common.microservice.exceptions.AppReason.*;
import static solutions.techsur.rfpaiservice.repository.CommonSpecification.alwaysTrue;
import static solutions.techsur.rfpaiservice.repository.CommonSpecification.isEqualTo;
import static solutions.techsur.rfpaiservice.repository.RFPSpecification.multiFieldSearch;
import static solutions.techsur.rfpaiservice.repository.ResponseOutlineSpecification.childSpecification;
import static solutions.techsur.rfpaiservice.repository.ResponseOutlineSpecification.parentSectionSpecification;

@Slf4j
@Service
@Transactional
@AllArgsConstructor
public class RequestForProposalServiceImpl implements RequestForProposalService {

    private final RequestForProposalRepository repository;
    private final ResponseOutlineRepository responseOutlineRepository;
    private final DocumentHelper helper;

    private static final int maxDepth = 4;

    @Override
    public RequestForProposal createRequestForProposal(ProposalRequest request) {
        RequestForProposal proposal = RequestForProposal.builder().build();
        BeanUtils.copyProperties(request, proposal);
        return repository.save(proposal);
    }

    @Override
    public Page<RequestForProposal> getProposals(ProposalFilter filter, Pageable pageable) {
        Specification specification = alwaysTrue();
        if (filter.isArchived()) {
            specification = specification.and(isEqualTo(true, "isArchived"));
        } else {
            specification = specification.and(isEqualTo(false, "isArchived"));
        }

        if (StringUtils.isNoneBlank(filter.getSearch())) {
            specification = specification.and(multiFieldSearch(filter));
        }

        return repository.findAll(specification, pageable);
    }

    @Override
    public RequestForProposal createResponseOutline(OutlineResponse request, Integer proposalId) {
        RequestForProposal proposal = findProposalById(proposalId);

        // Clear outline if it has previous generated outline.
        if (proposal.getResponseOutlines() != null) {
            proposal.getResponseOutlines().clear();
        }

        List<ResponseOutline> outlines = new ArrayList<>();
        if (request.getSections() != null) {
            List<SectionResponse> sectionResponses = request.getSections();

            for (SectionResponse sectionResponse : sectionResponses) {
                List<ResponseOutline> responseSubsections = new ArrayList<>();
                ResponseOutline responseOutline = ResponseOutline.builder()
                        .proposal(proposal)
                        .sectionNo(sectionResponse.getSectionNumber())
                        .sectionTitle(sectionResponse.getSectionTitle())
                        .requirement(sectionResponse.getRequirement())
                        .context(sectionResponse.getContext())
                        .sectionPurpose(sectionResponse.getSectionPurpose())
                        .instructionsToWriter(sectionResponse.getInstructionsToWriter())
                        .sourceMapping(sectionResponse.getSourceMapping())
                        .winThemeAlignment(sectionResponse.getWinThemeAlignment())
                        .build();
                // Recursively process subsections
                if (sectionResponse.getSubsections() != null) {
                    responseOutline.getChildSection().addAll(processSubsections(sectionResponse.getSubsections(), proposal, responseSubsections, responseOutline));
                }
                outlines.add(responseOutline);
            }
        }

        proposal.getResponseOutlines().addAll(outlines);
        repository.save(proposal);
        return proposal;
    }

    private List<ResponseOutline> processSubsections(List<SubSectionResponse> subSections, RequestForProposal proposal, List<ResponseOutline> responseSubsections, ResponseOutline parentOutline) {

        for (SubSectionResponse subSectionResponse : subSections) {
            ResponseOutline subSectionOutline = ResponseOutline.builder()
                    .proposal(proposal)
                    .parentSection(parentOutline)
                    .sectionNo(subSectionResponse.getSubSectionNumber())
                    .sectionTitle(subSectionResponse.getSubSectionTitle())
                    .requirement(subSectionResponse.getRequirement())
                    .context(subSectionResponse.getContext())
                    .sectionPurpose(subSectionResponse.getSectionPurpose())
                    .instructionsToWriter(subSectionResponse.getInstructionsToWriter())
                    .sourceMapping(subSectionResponse.getSourceMapping())
                    .winThemeAlignment(subSectionResponse.getWinThemeAlignment())
                    .build();

            // Recursively process nested subsections
            if (subSectionResponse.getSubsections() != null) {
                subSectionOutline.getChildSection().addAll(processSubsections(subSectionResponse.getSubsections(), proposal, responseSubsections, subSectionOutline));
            }
            responseSubsections.add(subSectionOutline);
        }

        return responseSubsections;
    }


    @Override
    public OutlineResponse getProposalWithOutlines(Integer proposalId) {
        // Fetch the proposal by ID
        RequestForProposal proposal = findProposalById(proposalId);

        // Fetch all top-level (parent) sections
        List<ResponseOutline> parentSections = responseOutlineRepository.findAll(parentSectionSpecification(proposalId));
        parentSections = sortSubSection(parentSections);

        // Convert parent sections to DTOs
        List<SectionResponse> sectionResponses = parentSections.stream()
                .map(this::mapToSectionResponse).toList();
        return  OutlineResponse.builder().outlineTitle(proposal.getTitle()).sections(sectionResponses).build();
    }

    @Override
    public void updateResponseOutline(OutlineResponse request, Integer proposalId) {
        // Fetch the proposal by ID
        RequestForProposal proposal = findProposalById(proposalId);

        // Fetch all top-level (parent) sections
        List<ResponseOutline> parentSections = responseOutlineRepository.findAll(parentSectionSpecification(proposalId));


        List<ResponseOutline> outlines = new ArrayList<>();
        outlines.addAll(parentSections);
        //Fetch all low level outline child.
        for (ResponseOutline outline : parentSections) {
            outlines.addAll(getAllSubSectionFromDB(outline).toList());
        }

        //Remove all existing relationship.
        removeExistingRelationship(outlines);

        //Remove all existing relationship.
        proposal.getResponseOutlines().clear();

        if (request.getSections() != null) {
            List<SectionResponse> sections = request.getSections();
            List<SubSectionResponse> subSectionList = new ArrayList<>();
            for (SectionResponse response : sections) {
                if (response.getSubsections() != null) {
                    List<SubSectionResponse> subSectionResponses = getAllSubsection(response.getSubsections()).toList();
                    subSectionList.addAll(subSectionResponses);
                }
            }
            Set<Integer> outlineIdsFromRequest = Stream.concat(subSectionList.stream().filter(subSectionResponse -> subSectionResponse.getOutlineSubSectionId() != null).map(SubSectionResponse::getOutlineSubSectionId), sections.stream().filter(sectionResponse -> sectionResponse.getOutlineSectionId() != null).map(SectionResponse::getOutlineSectionId)).collect(Collectors.toSet());

            //Remove from DB.
            removeExistingOutlineFromDB(outlineIdsFromRequest, outlines);

            //Update or create outline and assign to its parent recursively.
            proposal.getResponseOutlines().addAll(updateSections(request, proposal, outlines));
        }

        repository.save(proposal);
    }

    @Override
    public void deleteRequestForProposal(Integer proposalId) {
        RequestForProposal proposal = getProposalById(proposalId);
        proposal.setArchived(true);
        repository.save(proposal);
    }

    @Override
    public void deleteResponseOutline(Integer outlineId) {
        responseOutlineRepository.deleteById(outlineId);
    }

    @Override
    public RequestForProposal getProposalById(Integer proposalId) {
        return findProposalById(proposalId);
    }

    @Override
    public void updateRequestForProposal(ProposalRequest request, Integer proposalId) {
        RequestForProposal proposal = findProposalById(proposalId);
        if (request.getStatus() != RequestForProposal.Status.UPLOADED) {
            proposal.setStatus(request.getStatus());
        }
        if (request.getTitle() != null) {
            proposal.setTitle(request.getTitle());
        }
        if (request.getDescription() != null) {
            proposal.setDescription(request.getDescription());
        }
        if (request.getDeadline() != null) {
            proposal.setDeadline(request.getDeadline());
        }

        if (request.getSolicitationId() != null) {
            proposal.setSolicitationId(request.getSolicitationId());
        }
    }

    @Override
    public void updateSingleResponseOutline(SingleOutlineRequest request, Integer outlineId) {
        ResponseOutline outline = findResponseOutlineById(outlineId);
        if (StringUtils.isNotBlank(request.getContext())) {
            outline.setContext(request.getContext());
        }
        if (StringUtils.isNotBlank(request.getRequirement())) {
            outline.setRequirement(request.getRequirement());
        }
        if (StringUtils.isNotBlank(request.getSectionNo())) {
            outline.setSectionNo(request.getSectionNo());
        }
        if (StringUtils.isNotBlank(request.getSectionTitle())) {
            outline.setSectionTitle(request.getSectionTitle());
        }
        if (request.isGeneratedContent()) {
            outline.setGeneratedContent(request.isGeneratedContent());
        }
        if(StringUtils.isNotBlank(request.getInstructionsToWriter())) {
            outline.setInstructionsToWriter(request.getInstructionsToWriter());
        }
        if(ObjectUtils.isNotEmpty(request.getSourceMapping())) {
            outline.setSourceMapping(request.getSourceMapping());
        }
        if(StringUtils.isNotBlank(request.getSectionPurpose())) {
            outline.setSectionPurpose(request.getSectionPurpose());
        }
        if(ObjectUtils.isNotEmpty(request.getWinThemeAlignment())) {
            outline.setWinThemeAlignment(request.getWinThemeAlignment());
        }
        outline.setContent(request.getContent());
        responseOutlineRepository.save(outline);
    }

    public byte[] generateOutlineDoc(Integer proposalId) {
        OutlineResponse outlineResponse = getProposalWithOutlines(proposalId);
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
             XWPFDocument document = helper.createDocument(outlineResponse)) {
            document.write(outputStream);
            document.close();
            return outputStream.toByteArray();
        } catch (IOException e) {
            throw new AppException(INTERNAL_ERROR);
        }
    }

    // private method region start.

    private RequestForProposal findProposalById(Integer proposalId) {
        return repository.findById(proposalId)
                .orElseThrow(() -> new AppException(RFP_NOT_FOUND));
    }

    private ResponseOutline findResponseOutlineById(Integer outlineId) {
        return responseOutlineRepository.findById(outlineId)
                .orElseThrow(() -> new AppException(RESPONSE_OUTLINE_NOT_FOUND));
    }

    private SectionResponse mapToSectionResponse(ResponseOutline parent) {
        // Fetch and map subsections (child sections) recursively
        List<ResponseOutline> firstLevelChild = responseOutlineRepository.findAll(childSpecification(parent));
        firstLevelChild = sortSubSection(firstLevelChild);
        List<SubSectionResponse> listOfSubsection = new ArrayList<>();
        for (ResponseOutline outline : firstLevelChild) {
            listOfSubsection.add(SubSectionResponse.builder()
                    .outlineSubSectionId(outline.getId())
                    .isGeneratedContent(outline.isGeneratedContent())
                    .parentId(parent.getId())
                    .subSectionTitle(outline.getSectionTitle())
                    .subSectionNumber(outline.getSectionNo())
                    .requirement(outline.getRequirement())
                    .context(outline.getContext())
                    .content(outline.getContent())
                    .lastUpdatedDate(outline.getUpdated())
                    .sourceMapping(outline.getSourceMapping())
                    .sectionPurpose(outline.getSectionPurpose())
                    .winThemeAlignment(outline.getWinThemeAlignment())
                    .instructionsToWriter(outline.getInstructionsToWriter())
                    .subsections(getSubSectionsRecursively(outline, new AtomicInteger(0))) // Recursive call
                    .build());
        }
        return SectionResponse.builder()
                .subsections(listOfSubsection)
                .outlineSectionId(parent.getId())
                .sectionTitle(parent.getSectionTitle())
                .content(parent.getContent())
                .lastUpdatedDate(parent.getUpdated())
                .sourceMapping(parent.getSourceMapping())
                .sectionPurpose(parent.getSectionPurpose())
                .winThemeAlignment(parent.getWinThemeAlignment())
                .instructionsToWriter(parent.getInstructionsToWriter())
                .sectionNumber(parent.getSectionNo()).build();
    }

    private List<SubSectionResponse> getSubSectionsRecursively(ResponseOutline parent, AtomicInteger depth) {
        if (depth.incrementAndGet() >= maxDepth) { // Stop recursion after maxDepth levels
            return Collections.emptyList();
        }

        List<ResponseOutline> childSections = responseOutlineRepository.findAll(childSpecification(parent));

        // Sort before mapping
       // childSections.sort(Comparator.comparing(ResponseOutline::getSectionNo));
        childSections = sortSubSection(childSections);

        return childSections.stream()
                .map(child -> SubSectionResponse.builder()
                        .outlineSubSectionId(child.getId())
                        .subSectionTitle(child.getSectionTitle())
                        .subSectionNumber(child.getSectionNo())
                        .parentId(parent.getId())
                        .requirement(child.getRequirement())
                        .context(child.getContext())
                        .isGeneratedContent(child.isGeneratedContent())
                        .content(child.getContent())
                        .lastUpdatedDate(child.getUpdated())
                        .sourceMapping(child.getSourceMapping())
                        .sectionPurpose(child.getSectionPurpose())
                        .winThemeAlignment(child.getWinThemeAlignment())
                        .instructionsToWriter(child.getInstructionsToWriter())
                        .subsections(getSubSectionsRecursively(child, new AtomicInteger(depth.get()))) // Recursive call
                        .build())
                .toList();
    }

    private void removeExistingRelationship(List<ResponseOutline> parentSections) {
        for (ResponseOutline outline : parentSections) {
            if (ObjectUtils.isNotEmpty(outline.getChildSection())) {
                outline.getChildSection().clear();
            }
        }
    }

    private void removeExistingOutlineFromDB(Set<Integer> ids, List<ResponseOutline> outlinesFromDB) {
        for (ResponseOutline outline : outlinesFromDB) {
            if (!ids.contains(outline.getId())) {
                deleteResponseOutline(outline.getId());
            }
        }
    }

    private List<ResponseOutline> updateSections(OutlineResponse request, RequestForProposal proposal, List<ResponseOutline> allOutlines) {
        List<SectionResponse> sections = request.getSections();
        List<ResponseOutline> outlines = new ArrayList<>();
        ResponseOutline parentOutline = null;
        Map<Integer, ResponseOutline> allExistingOutline = allOutlines.stream().collect(Collectors.toMap(ResponseOutline::getId, Function.identity()));
        for (SectionResponse section : sections) {
            List<ResponseOutline> responseSubsections = new ArrayList<>();
            if (section.getOutlineSectionId() == null) {
                parentOutline = createOutline(section, proposal);
            } else if (allExistingOutline.containsKey(section.getOutlineSectionId())) {
                parentOutline = updateSection(section, allExistingOutline.get(section.getOutlineSectionId()));
            }
            parentOutline = allExistingOutline.get(section.getOutlineSectionId()) == null ? parentOutline : allExistingOutline.get(section.getOutlineSectionId());
            if (section.getSubsections() != null) {
                parentOutline.getChildSection().addAll(processSubSection(section.getSubsections(), parentOutline, allExistingOutline, responseSubsections, proposal));
            }
            outlines.add(parentOutline);
        }
        return outlines;
    }

    private List<ResponseOutline> processSubSection(List<SubSectionResponse> subSections, ResponseOutline parentOutline, Map<Integer, ResponseOutline> allExistingOutline, List<ResponseOutline> responseSubsections, RequestForProposal proposal) {
        ResponseOutline outline;
        for (SubSectionResponse subSectionResponse : subSections) {

            if (subSectionResponse.getOutlineSubSectionId() == null) {
                outline = createOutline(subSectionResponse, parentOutline, proposal);
            } else {
                outline = updateSubSection(subSectionResponse, allExistingOutline.get(subSectionResponse.getOutlineSubSectionId()), parentOutline);
            }

            if (subSectionResponse.getSubsections() != null) {
                outline.getChildSection().addAll(processSubSection(subSectionResponse.getSubsections(), outline, allExistingOutline, responseSubsections, proposal));
            }
            responseSubsections.add(outline);
        }
        return responseSubsections;
    }

    private ResponseOutline updateSubSection(SubSectionResponse section, ResponseOutline toBeUpdatedOutline, ResponseOutline parentOutline) {
        toBeUpdatedOutline.setSectionNo(section.getSubSectionNumber());
        toBeUpdatedOutline.setRequirement(section.getRequirement());
        toBeUpdatedOutline.setContext(section.getContext());
        toBeUpdatedOutline.setSectionTitle(section.getSubSectionTitle());
        toBeUpdatedOutline.setParentSection(parentOutline);
        toBeUpdatedOutline.setSectionPurpose(section.getSectionPurpose());
        toBeUpdatedOutline.setInstructionsToWriter(section.getInstructionsToWriter());
        toBeUpdatedOutline.setSourceMapping(section.getSourceMapping());
        toBeUpdatedOutline.setWinThemeAlignment(section.getWinThemeAlignment());
        return toBeUpdatedOutline;
    }

    private Stream<SubSectionResponse> getAllSubsection(List<SubSectionResponse> subSectionResponses) {

        return Stream.concat(subSectionResponses != null ? subSectionResponses.stream() : Stream.empty(), subSectionResponses.stream().flatMap(this::collectSubsection));
    }

    private Stream<SubSectionResponse> collectSubsection(SubSectionResponse subSectionResponse) {
        if (subSectionResponse.getSubsections() == null) {
            return Stream.of(subSectionResponse);
        }
        return getAllSubsection(subSectionResponse.getSubsections());
    }

    private Stream<ResponseOutline> getAllSubSectionFromDB(ResponseOutline parent) {

        List<ResponseOutline> child = responseOutlineRepository.findAll(childSpecification(parent));

        // Sort children based on sectionNumber before processing
        return Stream.concat(
                child.stream().sorted(Comparator.comparing(ResponseOutline::getSectionNo)), // Sort at this level
                child.stream()
                        .flatMap(responseOutline -> getAllSubSectionFromDB(responseOutline))
        );
    }

    private ResponseOutline updateSection(SectionResponse section, ResponseOutline toBeUpdatedOutline) {
        toBeUpdatedOutline.setSectionNo(section.getSectionNumber());
        toBeUpdatedOutline.setRequirement(section.getRequirement());
        toBeUpdatedOutline.setContext(section.getContext());
        toBeUpdatedOutline.setSectionTitle(section.getSectionTitle());
        toBeUpdatedOutline.setSectionPurpose(section.getSectionPurpose());
        toBeUpdatedOutline.setInstructionsToWriter(section.getInstructionsToWriter());
        toBeUpdatedOutline.setSourceMapping(section.getSourceMapping());
        toBeUpdatedOutline.setWinThemeAlignment(section.getWinThemeAlignment());
        return toBeUpdatedOutline;
    }

    private ResponseOutline createOutline(SectionResponse section, RequestForProposal proposal) {
        return ResponseOutline.builder()
                .sectionNo(section.getSectionNumber())
                .proposal(proposal)
                .sectionTitle(section.getSectionTitle())
                .requirement(section.getRequirement())
                .context(section.getContext())
                .sectionPurpose(section.getSectionPurpose())
                .instructionsToWriter(section.getInstructionsToWriter())
                .sourceMapping(section.getSourceMapping())
                .winThemeAlignment(section.getWinThemeAlignment())
                .build();
    }

    private ResponseOutline createOutline(SubSectionResponse subSectionResponse, ResponseOutline parentOutline, RequestForProposal proposal) {
        return ResponseOutline.builder()
                .sectionNo(subSectionResponse.getSubSectionNumber())
                .sectionTitle(subSectionResponse.getSubSectionTitle())
                .requirement(subSectionResponse.getRequirement())
                .context(subSectionResponse.getContext())
                .proposal(proposal)
                .sectionPurpose(subSectionResponse.getSectionPurpose())
                .instructionsToWriter(subSectionResponse.getInstructionsToWriter())
                .sourceMapping(subSectionResponse.getSourceMapping())
                .winThemeAlignment(subSectionResponse.getWinThemeAlignment())
                .parentSection(parentOutline)
                .build();
    }

    private List<ResponseOutline> sortSubSection(List<ResponseOutline> responseOutlines) {
        return responseOutlines.stream().sorted((s1, s2) -> {
            String[] parts1 = s1.getSectionNo().split("\\.");
            String[] parts2 = s2.getSectionNo().split("\\.");

            int length = Math.max(parts1.length, parts2.length);
            for (int i = 0; i < length; i++) {
                int num1 = (i < parts1.length) ? Integer.parseInt(parts1[i]) : 0;
                int num2 = (i < parts2.length) ? Integer.parseInt(parts2[i]) : 0;
                if (num1 != num2) {
                    return Integer.compare(num1, num2);
                }
            }
            return 0;
        }).toList();
    }

    //private method region end.
}
