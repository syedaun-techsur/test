package solutions.techsur.rfpaiservice.document;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xwpf.usermodel.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import solutions.techsur.rfpaiservice.dto.OutlineResponse;
import solutions.techsur.rfpaiservice.dto.SectionResponse;
import solutions.techsur.rfpaiservice.dto.SubSectionResponse;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Slf4j
public class DocumentHelper {

    @Value("${s3.region}")
    private String stRegion;

    @Value("${s3.bucketName}")
    private String stBucketName;

    @Value("${s3.accessKey}")
    private String accessKey;

    @Value("${s3.secretKey}")
    private String secretKey;

    @Value("${s3.baseFolder}")
    private String baseFolder;

    @Value("${s3.blueBookBaseFolder}")
    private String blueBookBaseFolder;

    private static final SimpleDateFormat TIMESTAMP_FORMAT = new SimpleDateFormat("yyyyMMdd_HHmmss");

    private S3Client createS3Client() {
        Region region = Region.of(stRegion);
        AwsCredentials credentials = AwsBasicCredentials.create(accessKey, secretKey);
        return S3Client.builder()
                .region(region)
                .credentialsProvider(StaticCredentialsProvider.create(credentials))
                .build();
    }

    public String getFilePath(boolean isBlueBook, String fileName, Integer proposalId) {
        if (proposalId == null || fileName == null) {
            throw new IllegalArgumentException("Proposal ID and file name must not be null");
        }
        String timestamp = TIMESTAMP_FORMAT.format(new Date());
        StringBuilder pathBuilder = new StringBuilder();
        if (isBlueBook) {
            pathBuilder.append(blueBookBaseFolder);
        } else {
            pathBuilder.append(baseFolder);
        }
        pathBuilder.append('/')
                .append(proposalId)
                .append('_')
                .append(timestamp)
                .append('/')
                .append(fileName);
        return pathBuilder.toString();
    }

    public String getFilePathForAdminBlueBook(String fileName) {
        if (fileName == null) {
            throw new IllegalArgumentException("File name must not be null");
        }
        String timestamp = TIMESTAMP_FORMAT.format(new Date());
        StringBuilder pathBuilder = new StringBuilder(blueBookBaseFolder.length() + 20 + fileName.length());
        pathBuilder.append(blueBookBaseFolder)
                .append("/admin/")
                .append(timestamp)
                .append('/')
                .append(fileName);
        return pathBuilder.toString();
    }

    public void uploadRFPDocument(MultipartFile file, String fileKey) throws IOException {
        if (file == null || fileKey == null || fileKey.isEmpty()) {
            throw new IllegalArgumentException("File and fileKey must not be null or empty");
        }
        try (S3Client s3Client = createS3Client()) {
            s3Client.putObject(PutObjectRequest.builder()
                            .bucket(stBucketName)
                            .key(fileKey)
                            .build(),
                    RequestBody.fromBytes(file.getBytes()));
            log.info("Uploaded file to S3 with key: {}", fileKey);
        }
    }

    public void deleteRFPDocument(String fileKey) {
        if (fileKey == null || fileKey.isEmpty()) {
            throw new IllegalArgumentException("fileKey must not be null or empty");
        }
        try (S3Client s3Client = createS3Client()) {
            s3Client.deleteObject(DeleteObjectRequest.builder()
                    .bucket(stBucketName)
                    .key(fileKey)
                    .build());
            log.info("Deleted S3 object with key: {}", fileKey);
        }
    }

    public XWPFDocument createDocument(OutlineResponse outlineResponse) {
        XWPFDocument document = new XWPFDocument();
        if (outlineResponse == null) {
            return document;
        }
        if (outlineResponse.getOutlineTitle() != null && !outlineResponse.getOutlineTitle().isEmpty()) {
            addTitle(document, outlineResponse.getOutlineTitle());
        }
        if (outlineResponse.getSections() != null && !outlineResponse.getSections().isEmpty()) {
            outlineResponse.getSections().forEach(section -> addSection(document, section));
        }
        return document;
    }

    private void addTitle(XWPFDocument document, String titleText) {
        if (titleText == null || titleText.isEmpty()) {
            return;
        }
        XWPFParagraph title = document.createParagraph();
        title.setAlignment(ParagraphAlignment.CENTER);
        XWPFRun titleRun = title.createRun();
        titleRun.setText(titleText);
        titleRun.setBold(true);
        titleRun.setFontSize(16);
    }

    private void addSection(XWPFDocument document, SectionResponse section) {
        if (section == null) {
            return;
        }
        XWPFParagraph sectionTitle = document.createParagraph();
        sectionTitle.setSpacingBefore(200);
        XWPFRun sectionRun = sectionTitle.createRun();
        String sectionNumber = section.getSectionNumber() == null ? "" : section.getSectionNumber();
        String sectionTitleText = section.getSectionTitle() == null ? "" : section.getSectionTitle();
        sectionRun.setText(sectionNumber + ". " + sectionTitleText);
        sectionRun.setBold(true);
        sectionRun.setFontSize(14);

        if (section.getSubsections() != null && !section.getSubsections().isEmpty()) {
            section.getSubsections().forEach(subsection -> addSubsection(document, subsection, 1));
        }
    }

    private void addSubsection(XWPFDocument document, SubSectionResponse subsection, int level) {
        if (subsection == null) {
            return;
        }
        int baseIndentation = 400;
        int indentationStep = 400;
        int currentIndentation = baseIndentation + (level * indentationStep);

        String subSectionNumber = subsection.getSubSectionNumber() == null ? "" : subsection.getSubSectionNumber();
        String subSectionTitle = subsection.getSubSectionTitle() == null ? "" : subsection.getSubSectionTitle();

        addTextParagraph(document, subSectionNumber + " " + subSectionTitle, currentIndentation / 2, 12, true);

        if (subsection.getRequirement() != null && !subsection.getRequirement().isEmpty()) {
            addTextParagraph(document, subsection.getRequirement(), currentIndentation, 11, false);
        }

        if (subsection.getContext() != null && !subsection.getContext().isEmpty()) {
            addTextParagraph(document, subsection.getContext(), currentIndentation, 11, false);
        }

        if (subsection.getSubsections() != null && !subsection.getSubsections().isEmpty()) {
            subsection.getSubsections().forEach(sub -> addSubsection(document, sub, level + 1));
        }
    }

    private void addTextParagraph(XWPFDocument document, String htmlText, int indentationLeft, int fontSize, boolean bold) {
        if (htmlText == null || htmlText.trim().isEmpty()) {
            return;
        }
        XWPFParagraph paragraph = document.createParagraph();
        paragraph.setIndentationLeft(indentationLeft);

        Document parsedHtml = Jsoup.parse(htmlText);
        Element body = parsedHtml.body();

        processHtmlElements(body.childNodes(), paragraph, fontSize, bold);
    }

    private void processHtmlElements(List<Node> nodes, XWPFParagraph paragraph, int fontSize, boolean parentBold) {
        if (nodes == null) {
            return;
        }
        for (Node node : nodes) {
            if (node instanceof TextNode) {
                addFormattedText(paragraph, ((TextNode) node).text(), fontSize, parentBold, false, null);
            } else if (node instanceof Element) {
                Element element = (Element) node;
                boolean isBold = parentBold || "b".equalsIgnoreCase(element.tagName()) || "strong".equalsIgnoreCase(element.tagName());
                boolean isItalic = "i".equalsIgnoreCase(element.tagName()) || "em".equalsIgnoreCase(element.tagName());
                boolean isLink = "a".equalsIgnoreCase(element.tagName());

                String linkHref = isLink ? element.attr("href") : null;
                processChildElements(element, paragraph, fontSize, isBold, isItalic, linkHref);
            }
        }
    }

    private void processChildElements(Element element, XWPFParagraph paragraph, int fontSize, boolean parentBold, boolean parentItalic, String linkHref) {
        if (element == null) {
            return;
        }
        for (Node child : element.childNodes()) {
            if (child instanceof TextNode) {
                addFormattedText(paragraph, ((TextNode) child).text(), fontSize, parentBold, parentItalic, linkHref);
            } else if (child instanceof Element) {
                Element childElement = (Element) child;
                boolean isBold = parentBold || "b".equalsIgnoreCase(childElement.tagName()) || "strong".equalsIgnoreCase(childElement.tagName());
                boolean isItalic = parentItalic || "i".equalsIgnoreCase(childElement.tagName()) || "em".equalsIgnoreCase(childElement.tagName());
                boolean isLink = "a".equalsIgnoreCase(childElement.tagName());

                String childLinkHref = isLink ? childElement.attr("href") : linkHref;
                processChildElements(childElement, paragraph, fontSize, isBold, isItalic, childLinkHref);
            }
        }
    }

    private void addFormattedText(XWPFParagraph paragraph, String text, int fontSize, boolean bold, boolean italic, String hyperlink) {
        if (text == null || text.trim().isEmpty()) {
            return;
        }
        if (hyperlink != null) {
            XWPFHyperlinkRun linkRun = paragraph.createHyperlinkRun(hyperlink);
            linkRun.setText(text);
            linkRun.setFontSize(fontSize);
            linkRun.setBold(bold);
            linkRun.setItalic(italic);
            linkRun.setColor("0000FF");
            linkRun.setUnderline(UnderlinePatterns.SINGLE);
        } else {
            XWPFRun run = paragraph.createRun();
            run.setText(text);
            run.setFontSize(fontSize);
            run.setBold(bold);
            run.setItalic(italic);
        }
    }

    public void deleteFilesInFolder(String solicitationId) {
        if (solicitationId == null || solicitationId.isEmpty()) {
            log.warn("Solicitation ID is null or empty. Skipping deletion.");
            return;
        }
        String folderPrefix = baseFolder + "/" + solicitationId + "/";
        try (S3Client s3Client = createS3Client()) {
            ListObjectsV2Request listRequest = ListObjectsV2Request.builder()
                    .bucket(stBucketName)
                    .prefix(folderPrefix)
                    .build();

            ListObjectsV2Response listResponse = s3Client.listObjectsV2(listRequest);

            List<ObjectIdentifier> objectsToDelete = listResponse.contents().stream()
                    .map(s3Object -> ObjectIdentifier.builder().key(s3Object.key()).build())
                    .collect(Collectors.toList());

            if (!objectsToDelete.isEmpty()) {
                DeleteObjectsRequest deleteRequest = DeleteObjectsRequest.builder()
                        .bucket(stBucketName)
                        .delete(Delete.builder().objects(objectsToDelete).build())
                        .build();

                s3Client.deleteObjects(deleteRequest);
                log.info("Cleaned folder: {}", folderPrefix);
            } else {
                log.info("Folder already empty: {}", folderPrefix);
            }
        }
    }

    public void copySpecificDocuments(Map<String, String> fileNameAndPath, String solicitationId) {
        if (fileNameAndPath == null || fileNameAndPath.isEmpty() || solicitationId == null || solicitationId.isEmpty()) {
            log.warn("File map or solicitationId is null or empty. Skipping copy.");
            return;
        }
        String folderPrefix = baseFolder + "/" + solicitationId + "/";
        try (S3Client s3Client = createS3Client()) {
            for (Map.Entry<String, String> entry : fileNameAndPath.entrySet()) {
                String targetKey = folderPrefix + entry.getKey();
                String sourceKey = entry.getValue();

                if (targetKey == null || sourceKey == null || targetKey.isEmpty() || sourceKey.isEmpty()) {
                    log.warn("Skipping copy due to null/empty source or target key. Source: {}, Target: {}", sourceKey, targetKey);
                    continue;
                }
                CopyObjectRequest copyRequest = CopyObjectRequest.builder()
                        .sourceBucket(stBucketName)
                        .sourceKey(sourceKey)
                        .destinationBucket(stBucketName)
                        .destinationKey(targetKey)
                        .build();
                try {
                    s3Client.copyObject(copyRequest);
                    log.info("Copied: {} -> {}", sourceKey, targetKey);
                } catch (S3Exception e) {
                    log.warn("Failed to copy: {} - {}", sourceKey, e.getMessage());
                }
            }
        }
    }

    public ResponseInputStream<GetObjectResponse> getDocumentFromS3(String fileKey) {
        if (fileKey == null || fileKey.isEmpty()) {
            throw new IllegalArgumentException("fileKey must not be null or empty");
        }
        try (S3Client s3Client = createS3Client()) {
            return s3Client.getObject(GetObjectRequest.builder()
                    .bucket(stBucketName)
                    .key(fileKey)
                    .build());
        } catch (S3Exception e) {
            log.error("Error fetching file from S3 with key {}: {}", fileKey, e.awsErrorDetails().errorMessage());
            throw new RuntimeException("Failed to retrieve document from S3", e);
        }
    }

}