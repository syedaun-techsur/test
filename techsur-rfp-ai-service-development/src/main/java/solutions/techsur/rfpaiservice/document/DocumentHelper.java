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

    public S3Client getS3Client() {
        Region region = Region.of(stRegion);
        AwsCredentials credentials = AwsBasicCredentials.create(accessKey, secretKey);
        return S3Client
                .builder()
                .region(region)
                .credentialsProvider(StaticCredentialsProvider.create(credentials))
                .build();
    }

    /**
     * Constructs the file path for storing documents, with folders structured by proposal ID and timestamp.
     * @param isBlueBook Whether to use the blue book base folder or not.
     * @param fileName The name of the file.
     * @param proposalId The proposal ID.
     * @return The constructed file path.
     */
    public String getFilePath(boolean isBlueBook, String fileName, Integer proposalId) {
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String postfix = "/" + proposalId + "_" + timestamp + "/" + fileName;
        return isBlueBook ? blueBookBaseFolder.concat(postfix) : baseFolder.concat(postfix);
    }

    /**
     * Constructs the file path for an admin blue book document.
     * @param fileName Name of the file.
     * @return The constructed admin blue book file path.
     */
    public String getFilePathForAdminBlueBook(String fileName) {
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String postfix = "/" + timestamp + "/" + fileName;
        String adminBasePath = blueBookBaseFolder + "/admin";
        return adminBasePath.concat(postfix);
    }

    /**
     * Uploads the given file to S3 under the specified file key.
     * @param file Multipart file to upload.
     * @param fileKey The S3 key under which to store the file.
     * @throws IOException If reading the file bytes fails.
     */
    public void uploadRFPDocument(MultipartFile file, String fileKey) throws IOException {
        try {
            getS3Client().putObject(PutObjectRequest.builder()
                    .bucket(stBucketName)
                    .key(fileKey)
                    .build(), RequestBody.fromBytes(file.getBytes()));
        } catch (Exception e) {
            log.error(String.format("Failed to upload document to S3 with key %s: %s", fileKey, e.getMessage()), e);
            throw e;
        }
    }

    /**
     * Deletes the document in S3 with the given file key.
     * @param fileKey The S3 key of the file to delete.
     */
    public void deleteRFPDocument(String fileKey) {
        try {
            getS3Client().deleteObject(DeleteObjectRequest
                    .builder()
                    .bucket(stBucketName)
                    .key(fileKey).build());
        } catch (Exception e) {
            log.error(String.format("Failed to delete document from S3 with key %s: %s", fileKey, e.getMessage()), e);
        }
    }

    /**
     * Creates an XWPFDocument based on the outline response, populating sections and subsections.
     * @param outlineResponse The outline response containing title and sections.
     * @return Created XWPFDocument.
     */
    public XWPFDocument createDocument(OutlineResponse outlineResponse) {
        XWPFDocument document = new XWPFDocument();
        addTitle(document, outlineResponse.getOutlineTitle());
        if (outlineResponse.getSections() != null) {
            outlineResponse.getSections().forEach(this::addSection);
        }
        return document;
    }

    private void addTitle(XWPFDocument document, String titleText) {
        XWPFParagraph title = document.createParagraph();
        title.setAlignment(ParagraphAlignment.CENTER);
        XWPFRun titleRun = title.createRun();
        titleRun.setText(titleText != null ? titleText : "");
        titleRun.setBold(true);
        titleRun.setFontSize(16);
    }

    private void addSection(XWPFDocument document, SectionResponse section) {
        XWPFParagraph sectionTitle = document.createParagraph();
        sectionTitle.setSpacingBefore(200);
        XWPFRun sectionRun = sectionTitle.createRun();
        String sectionTitleText = section.getSectionNumber() + ". " + (section.getSectionTitle() != null ? section.getSectionTitle() : "");
        sectionRun.setText(sectionTitleText);
        sectionRun.setBold(true);
        sectionRun.setFontSize(14);

        if (section.getSubsections() != null) {
            section.getSubsections().forEach(subsection -> addSubsection(document, subsection, 1));
        }
    }

    private void addSubsection(XWPFDocument document, SubSectionResponse subsection, int level) {
        int baseIndentation = 400;
        int indentationStep = 400;
        int currentIndentation = baseIndentation + (level * indentationStep);

        String subSectionTitle = (subsection.getSubSectionNumber() != null ? subsection.getSubSectionNumber() : "") + " " +
                (subsection.getSubSectionTitle() != null ? subsection.getSubSectionTitle() : "");
        addTextParagraph(document, subSectionTitle.trim(), currentIndentation / 2, 12, true);
        addTextParagraph(document, subsection.getRequirement() != null ? subsection.getRequirement() : "", currentIndentation, 11, false);
        addTextParagraph(document, subsection.getContext() != null ? subsection.getContext() : "", currentIndentation, 11, false);

        if (subsection.getSubsections() != null) {
            subsection.getSubsections().forEach(sub -> addSubsection(document, sub, level + 1));
        }
    }

    private void addTextParagraph(XWPFDocument document, String htmlText, int indentationLeft, int fontSize, boolean bold) {
        XWPFParagraph paragraph = document.createParagraph();
        paragraph.setIndentationLeft(indentationLeft);

        if (htmlText == null || htmlText.trim().isEmpty()) {
            return; // Avoid creating empty paragraphs unnecessarily
        }

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
                boolean isBold = parentBold || element.tagName().equalsIgnoreCase("b") || element.tagName().equalsIgnoreCase("strong");
                boolean isItalic = element.tagName().equalsIgnoreCase("i") || element.tagName().equalsIgnoreCase("em");
                boolean isLink = element.tagName().equalsIgnoreCase("a");

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
                boolean isBold = parentBold || childElement.tagName().equalsIgnoreCase("b") || childElement.tagName().equalsIgnoreCase("strong");
                boolean isItalic = parentItalic || childElement.tagName().equalsIgnoreCase("i") || childElement.tagName().equalsIgnoreCase("em");
                boolean isLink = childElement.tagName().equalsIgnoreCase("a");

                String childLinkHref = isLink ? childElement.attr("href") : linkHref;
                processChildElements(childElement, paragraph, fontSize, isBold, isItalic, childLinkHref);
            }
        }
    }

    private void addFormattedText(XWPFParagraph paragraph, String text, int fontSize, boolean bold, boolean italic, String hyperlink) {
        if (text == null || text.trim().isEmpty()) {
            return;
        }
        if (hyperlink != null && !hyperlink.trim().isEmpty()) {
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

    /**
     * Deletes all files in S3 folder corresponding to the given solicitation ID.
     * @param solicitationId The solicitation ID folder to clean.
     */
    public void deleteFilesInFolder(String solicitationId) {
        String folderPrefix = baseFolder + "/" + solicitationId + "/";
        S3Client s3Client = getS3Client();

        ListObjectsV2Request listRequest = ListObjectsV2Request.builder()
                .bucket(stBucketName)
                .prefix(folderPrefix)
                .build();

        ListObjectsV2Response listResponse = s3Client.listObjectsV2(listRequest);

        List<ObjectIdentifier> objectsToDelete = listResponse.contents() == null ? List.of() :
                listResponse.contents().stream()
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

    /**
     * Copies specific documents inside S3 from source keys to the target solicitation folder.
     * @param fileNameAndPath Map with fileName (destination) as key and source path (sourceKey) as value.
     * @param solicitationId The solicitation ID used to build destination folder path.
     */
    public void copySpecificDocuments(Map<String, String> fileNameAndPath, String solicitationId) {
        String folderPrefix = baseFolder + "/" + solicitationId + "/";
        S3Client s3Client = getS3Client();

        for (Map.Entry<String, String> entry : fileNameAndPath.entrySet()) {
            String targetKey = folderPrefix + entry.getKey();
            String sourceKey = entry.getValue();
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

    /**
     * Retrieves the document from S3 with the given file key.
     * @param fileKey The key of the file in S3 to retrieve.
     * @return ResponseInputStream with the document content.
     */
    public ResponseInputStream<GetObjectResponse> getDocumentFromS3(String fileKey) {
        try {
            return getS3Client().getObject(GetObjectRequest.builder()
                    .bucket(stBucketName)
                    .key(fileKey)
                    .build());
        } catch (S3Exception e) {
            log.error("Error fetching file from S3 with key {}: {}", fileKey, e.awsErrorDetails().errorMessage());
            throw new RuntimeException("Failed to retrieve document from S3", e);
        }
    }

}