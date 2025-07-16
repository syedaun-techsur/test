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

    private static final int MAX_DEPTH = 4;

    @Override
    public RequestForProposal createRequestForProposal(final ProposalRequest request) {
        RequestForProposal proposal = RequestForProposal.builder().build();
        BeanUtils.copyProperties(request, proposal);
        return repository.save(proposal);
    }

    @Override
    @SuppressWarnings("rawtypes")
    public Page<RequestForProposal> getProposals(final ProposalFilter filter, final Pageable pageable) {
        Specification<RequestForProposal> specification = alwaysTrue();
        specification = specification.and(isEqualTo(filter.isArchived(), "isArchived"));

        if (StringUtils.isNoneBlank(filter.getSearch())) {
            specification = specification.and(multiFieldSearch(filter));
        }
        return repository.findAll(specification, pageable);
    }

    @Override
    public RequestForProposal createResponseOutline(final OutlineResponse request, final Integer proposalId) {
        final RequestForProposal proposal = findProposalById(proposalId);

        // Clear outline if it has previous generated outline.
        if (proposal.getResponseOutlines() != null) {
            proposal.getResponseOutlines().clear();
        }

        final List<ResponseOutline> outlines = new ArrayList<>();
        if (request.getSections() != null) {
            final List<SectionResponse> sectionResponses = request.getSections();

            for (final SectionResponse sectionResponse : sectionResponses) {
                final List<ResponseOutline> responseSubsections = new ArrayList<>();
                final ResponseOutline responseOutline = ResponseOutline.builder()
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
                    responseOutline.getChildSection().addAll(processSubSections(sectionResponse.getSubsections(), proposal, responseSubsections, responseOutline));
                }
                outlines.add(responseOutline);
            }
        }

        proposal.getResponseOutlines().addAll(outlines);
        repository.save(proposal);
        return proposal;
    }

    private List<ResponseOutline> processSubSections(final List<SubSectionResponse> subSections,
                                                    final RequestForProposal proposal,
                                                    final List<ResponseOutline> responseSubSections,
                                                    final ResponseOutline parentOutline) {
        for (final SubSectionResponse subSectionResponse : subSections) {
            final ResponseOutline subSectionOutline = ResponseOutline.builder()
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
                subSectionOutline.getChildSection().addAll(processSubSections(subSectionResponse.getSubsections(), proposal, responseSubSections, subSectionOutline));
            }
            responseSubSections.add(subSectionOutline);
        }
        return responseSubSections;
    }

    @Override
    public OutlineResponse getProposalWithOutlines(final Integer proposalId) {
        // Fetch the proposal by ID
        final RequestForProposal proposal = findProposalById(proposalId);

        // Fetch all top-level (parent) sections
        List<ResponseOutline> parentSections = responseOutlineRepository.findAll(parentSectionSpecification(proposalId));
        parentSections = sortSubSections(parentSections);

        // Convert parent sections to DTOs
        final List<SectionResponse> sectionResponses = parentSections.stream()
                .map(this::mapToSectionResponse)
                .collect(Collectors.toList());
        return OutlineResponse.builder().outlineTitle(proposal.getTitle()).sections(sectionResponses).build();
    }

    @Override
    public void updateResponseOutline(final OutlineResponse request, final Integer proposalId) {
        // Fetch the proposal by ID
        final RequestForProposal proposal = findProposalById(proposalId);

        // Fetch all top-level (parent) sections
        final List<ResponseOutline> parentSections = responseOutlineRepository.findAll(parentSectionSpecification(proposalId));

        final List<ResponseOutline> outlines = new ArrayList<>(parentSections);
        // Fetch all lower level outline children
        for (final ResponseOutline outline : parentSections) {
            outlines.addAll(getAllSubSectionsFromDB(outline).collect(Collectors.toList()));
        }

        // Remove all existing relationships
        removeExistingRelationships(outlines);

        // Clear all existing relationship from proposal
        if (proposal.getResponseOutlines() != null) {
            proposal.getResponseOutlines().clear();
        }

        if (request.getSections() != null) {
            final List<SectionResponse> sections = request.getSections();
            final List<SubSectionResponse> subSectionList = new ArrayList<>();
            for (final SectionResponse response : sections) {
                if (response.getSubsections() != null) {
                    final List<SubSectionResponse> subSectionResponses = getAllSubsections(response.getSubsections()).collect(Collectors.toList());
                    subSectionList.addAll(subSectionResponses);
                }
            }
            final Set<Integer> outlineIdsFromRequest = Stream.concat(
                            subSectionList.stream()
                                    .filter(sub -> sub.getOutlineSubSectionId() != null)
                                    .map(SubSectionResponse::getOutlineSubSectionId),
                            sections.stream()
                                    .filter(sec -> sec.getOutlineSectionId() != null)
                                    .map(SectionResponse::getOutlineSectionId))
                    .collect(Collectors.toSet());

            // Remove outlines not present in request from DB
            removeExistingOutlinesFromDB(outlineIdsFromRequest, outlines);

            // Update or create outlines and assign to parents recursively
            proposal.getResponseOutlines().addAll(updateSections(request, proposal, outlines));
        }

        repository.save(proposal);
    }

    @Override
    public void deleteRequestForProposal(final Integer proposalId) {
        final RequestForProposal proposal = getProposalById(proposalId);
        proposal.setArchived(true);
        repository.save(proposal);
    }

    @Override
    public void deleteResponseOutline(final Integer outlineId) {
        responseOutlineRepository.deleteById(outlineId);
    }

    @Override
    public RequestForProposal getProposalById(final Integer proposalId) {
        return findProposalById(proposalId);
    }

    @Override
    public void updateRequestForProposal(final ProposalRequest request, final Integer proposalId) {
        final RequestForProposal proposal = findProposalById(proposalId);
        if (request.getStatus() != null && request.getStatus() != RequestForProposal.Status.UPLOADED) {
            proposal.setStatus(request.getStatus());
        }
        if (StringUtils.isNotBlank(request.getTitle())) {
            proposal.setTitle(request.getTitle());
        }
        if (StringUtils.isNotBlank(request.getDescription())) {
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
    public void updateSingleResponseOutline(final SingleOutlineRequest request, final Integer outlineId) {
        final ResponseOutline outline = findResponseOutlineById(outlineId);
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
            outline.setGeneratedContent(true);
        }
        if (StringUtils.isNotBlank(request.getInstructionsToWriter())) {
            outline.setInstructionsToWriter(request.getInstructionsToWriter());
        }
        if (ObjectUtils.isNotEmpty(request.getSourceMapping())) {
            outline.setSourceMapping(request.getSourceMapping());
        }
        if (StringUtils.isNotBlank(request.getSectionPurpose())) {
            outline.setSectionPurpose(request.getSectionPurpose());
        }
        if (ObjectUtils.isNotEmpty(request.getWinThemeAlignment())) {
            outline.setWinThemeAlignment(request.getWinThemeAlignment());
        }
        outline.setContent(request.getContent());
        responseOutlineRepository.save(outline);
    }

    public byte[] generateOutlineDoc(final Integer proposalId) {
        final OutlineResponse outlineResponse = getProposalWithOutlines(proposalId);
        try (final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
             final XWPFDocument document = helper.createDocument(outlineResponse)) {
            document.write(outputStream);
            return outputStream.toByteArray();
        } catch (final IOException e) {
            log.error("Error generating outline document", e);
            throw new AppException(INTERNAL_ERROR);
        }
    }

    // private method region start.

    private RequestForProposal findProposalById(final Integer proposalId) {
        return repository.findById(proposalId)
                .orElseThrow(() -> new AppException(RFP_NOT_FOUND));
    }

    private ResponseOutline findResponseOutlineById(final Integer outlineId) {
        return responseOutlineRepository.findById(outlineId)
                .orElseThrow(() -> new AppException(RESPONSE_OUTLINE_NOT_FOUND));
    }

    private SectionResponse mapToSectionResponse(final ResponseOutline parent) {
        // Fetch and map subsections (child sections) recursively
        List<ResponseOutline> firstLevelChild = responseOutlineRepository.findAll(childSpecification(parent));
        firstLevelChild = sortSubSections(firstLevelChild);

        final List<SubSectionResponse> subSectionResponses = new ArrayList<>();
        for (final ResponseOutline outline : firstLevelChild) {
            subSectionResponses.add(SubSectionResponse.builder()
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
                    .subsections(getSubSectionsRecursively(outline, new AtomicInteger(1))) // Start depth at 1
                    .build());
        }
        return SectionResponse.builder()
                .subsections(subSectionResponses)
                .outlineSectionId(parent.getId())
                .sectionTitle(parent.getSectionTitle())
                .content(parent.getContent())
                .lastUpdatedDate(parent.getUpdated())
                .sourceMapping(parent.getSourceMapping())
                .sectionPurpose(parent.getSectionPurpose())
                .winThemeAlignment(parent.getWinThemeAlignment())
                .instructionsToWriter(parent.getInstructionsToWriter())
                .sectionNumber(parent.getSectionNo())
                .build();
    }

    private List<SubSectionResponse> getSubSectionsRecursively(final ResponseOutline parent, final AtomicInteger depth) {
        if (depth.get() >= MAX_DEPTH) { // Stop recursion after maxDepth levels
            return Collections.emptyList();
        }
        depth.incrementAndGet();

        List<ResponseOutline> childSections = responseOutlineRepository.findAll(childSpecification(parent));
        childSections = sortSubSections(childSections);

        final int currentDepth = depth.get();
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
                        .subsections(getSubSectionsRecursively(child, new AtomicInteger(currentDepth))) // Incremented depth
                        .build())
                .collect(Collectors.toList());
    }

    private void removeExistingRelationships(final List<ResponseOutline> outlines) {
        for (final ResponseOutline outline : outlines) {
            if (outline.getChildSection() != null && !outline.getChildSection().isEmpty()) {
                outline.getChildSection().clear();
            }
        }
    }

    private void removeExistingOutlinesFromDB(final Set<Integer> ids, final List<ResponseOutline> outlinesFromDB) {
        for (final ResponseOutline outline : outlinesFromDB) {
            if (!ids.contains(outline.getId())) {
                deleteResponseOutline(outline.getId());
            }
        }
    }

    private List<ResponseOutline> updateSections(final OutlineResponse request,
                                                 final RequestForProposal proposal,
                                                 final List<ResponseOutline> allOutlines) {
        final List<SectionResponse> sections = request.getSections();
        final List<ResponseOutline> updatedOutlines = new ArrayList<>();
        final Map<Integer, ResponseOutline> existingOutlineMap = allOutlines.stream()
                .collect(Collectors.toMap(ResponseOutline::getId, Function.identity()));

        for (final SectionResponse section : sections) {
            ResponseOutline parentOutline;
            if (section.getOutlineSectionId() == null) {
                parentOutline = createOutline(section, proposal);
            } else if (existingOutlineMap.containsKey(section.getOutlineSectionId())) {
                parentOutline = updateSection(section, existingOutlineMap.get(section.getOutlineSectionId()));
            } else {
                parentOutline = createOutline(section, proposal);
            }

            if (section.getSubsections() != null) {
                parentOutline.getChildSection().addAll(processSubSection(section.getSubsections(), parentOutline, existingOutlineMap, new ArrayList<>(), proposal));
            }
            updatedOutlines.add(parentOutline);
        }
        return updatedOutlines;
    }

    private List<ResponseOutline> processSubSection(final List<SubSectionResponse> subSections,
                                                    final ResponseOutline parentOutline,
                                                    final Map<Integer, ResponseOutline> existingOutlineMap,
                                                    final List<ResponseOutline> responseSubsections,
                                                    final RequestForProposal proposal) {
        for (final SubSectionResponse subSectionResponse : subSections) {
            final ResponseOutline outline;
            if (subSectionResponse.getOutlineSubSectionId() == null) {
                outline = createOutline(subSectionResponse, parentOutline, proposal);
            } else {
                outline = updateSubSection(subSectionResponse, existingOutlineMap.get(subSectionResponse.getOutlineSubSectionId()), parentOutline);
            }
            if (subSectionResponse.getSubsections() != null) {
                outline.getChildSection().addAll(processSubSection(subSectionResponse.getSubsections(), outline, existingOutlineMap, responseSubsections, proposal));
            }
            responseSubsections.add(outline);
        }
        return responseSubsections;
    }

    private ResponseOutline updateSubSection(final SubSectionResponse section, final ResponseOutline toBeUpdatedOutline, final ResponseOutline parentOutline) {
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

    private Stream<SubSectionResponse> getAllSubsections(final List<SubSectionResponse> subSectionResponses) {
        if (subSectionResponses == null) {
            return Stream.empty();
        }
        return Stream.concat(
                subSectionResponses.stream(),
                subSectionResponses.stream().flatMap(this::collectSubsections)
        );
    }

    private Stream<SubSectionResponse> collectSubsections(final SubSectionResponse subSectionResponse) {
        if (subSectionResponse.getSubsections() == null) {
            return Stream.of(subSectionResponse);
        }
        return getAllSubsections(subSectionResponse.getSubsections());
    }

    private Stream<ResponseOutline> getAllSubSectionsFromDB(final ResponseOutline parent) {
        final List<ResponseOutline> children = responseOutlineRepository.findAll(childSpecification(parent));
        // Sort children based on sectionNo before processing
        final Stream<ResponseOutline> sortedChildren = children.stream()
                .sorted(Comparator.comparing(ResponseOutline::getSectionNo));

        return Stream.concat(
                sortedChildren,
                children.stream()
                        .flatMap(this::getAllSubSectionsFromDB)
        );
    }

    private ResponseOutline updateSection(final SectionResponse section, final ResponseOutline toBeUpdatedOutline) {
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

    private ResponseOutline createOutline(final SectionResponse section, final RequestForProposal proposal) {
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

    private ResponseOutline createOutline(final SubSectionResponse subSectionResponse,
                                          final ResponseOutline parentOutline,
                                          final RequestForProposal proposal) {
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

    private List<ResponseOutline> sortSubSections(final List<ResponseOutline> responseOutlines) {
        return responseOutlines.stream().sorted((s1, s2) -> {
            final String[] parts1 = s1.getSectionNo().split("\\.");
            final String[] parts2 = s2.getSectionNo().split("\\.");
            final int maxLength = Math.max(parts1.length, parts2.length);
            for (int i = 0; i < maxLength; i++) {
                final int num1 = (i < parts1.length) ? parseSectionNumber(parts1[i]) : 0;
                final int num2 = (i < parts2.length) ? parseSectionNumber(parts2[i]) : 0;
                if (num1 != num2) {
                    return Integer.compare(num1, num2);
                }
            }
            return 0;
        }).collect(Collectors.toList());
    }

    /**
     * Parses a section number part safely, defaults to 0 on failure.
     */
    private int parseSectionNumber(final String numberPart) {
        try {
            return Integer.parseInt(numberPart);
        } catch (final NumberFormatException e) {
            log.warn("Failed to parse section number part '{}', defaulting to 0", numberPart);
            return 0;
        }
    }

    // private method region end.
}