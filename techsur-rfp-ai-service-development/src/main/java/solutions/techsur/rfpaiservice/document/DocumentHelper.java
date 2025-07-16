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

    public String getFilePath(boolean isBlueBook, String fileName, Integer proposalId) {
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String postFix = "/" + proposalId + "_" + timestamp + "/" + fileName;
        return isBlueBook ? blueBookBaseFolder.concat(postFix) : baseFolder.concat(postFix);
    }

    public String getFilePathForAdminBlueBook(String fileName) {
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String postFix = "/" + timestamp + "/" + fileName;
        String adminBasePath = blueBookBaseFolder + "/admin";
        return adminBasePath.concat(postFix);
    }

    public void uploadRFPDocument(MultipartFile file, String fileKey) throws IOException {
        getS3Client().putObject(PutObjectRequest.builder()
                .bucket(stBucketName)
                .key(fileKey)
                .build(), RequestBody.fromBytes(file.getBytes()));
    }

    public void deleteRFPDocument(String fileKey) {
        getS3Client().deleteObject(DeleteObjectRequest
                .builder()
                .bucket(stBucketName)
                .key(fileKey).build());
    }

    public XWPFDocument createDocument(OutlineResponse outlineResponse) {
        XWPFDocument document = new XWPFDocument();
        addTitle(document, outlineResponse.getOutlineTitle());
        outlineResponse.getSections().forEach(section -> addSection(document, section));
        return document;
    }

    private void addTitle(XWPFDocument document, String titleText) {
        XWPFParagraph title = document.createParagraph();
        title.setAlignment(ParagraphAlignment.CENTER);
        XWPFRun titleRun = title.createRun();
        titleRun.setText(titleText);
        titleRun.setBold(true);
        titleRun.setFontSize(16);
    }

    private void addSection(XWPFDocument document, SectionResponse section) {
        XWPFParagraph sectionTitle = document.createParagraph();
        sectionTitle.setSpacingBefore(200);
        XWPFRun sectionRun = sectionTitle.createRun();
        sectionRun.setText(section.getSectionNumber() + ". " + section.getSectionTitle());
        sectionRun.setBold(true);
        sectionRun.setFontSize(14);

        section.getSubsections().forEach(subsection -> addSubsection(document, subsection, 1));
    }

    private void addSubsection(XWPFDocument document, SubSectionResponse subsection, int level) {
        int baseIndentation = 400;
        int indentationStep = 400;
        int currentIndentation = baseIndentation + (level * indentationStep);

        addTextParagraph(document, subsection.getSubSectionNumber() + " " + subsection.getSubSectionTitle(), currentIndentation / 2, 12, true);
        addTextParagraph(document, subsection.getRequirement(), currentIndentation, 11, false);
        addTextParagraph(document, subsection.getContext(), currentIndentation, 11, false);

        // Recursively process nested subsections with increased level
        if (subsection.getSubsections() != null) {
            subsection.getSubsections().forEach(sub -> addSubsection(document, sub, level + 1));
        }
    }

    private void addTextParagraph(XWPFDocument document, String htmlText, int indentationLeft, int fontSize, boolean bold) {
        XWPFParagraph paragraph = document.createParagraph();
        paragraph.setIndentationLeft(indentationLeft);

        Document parsedHtml = Jsoup.parse(htmlText);
        Element body = parsedHtml.body();

        processHtmlElements(body.childNodes(), paragraph, fontSize, bold);
    }

    private void processHtmlElements(List<Node> nodes, XWPFParagraph paragraph, int fontSize, boolean parentBold) {
        for (Node node : nodes) {
            if (node instanceof TextNode) {
                addFormattedText(paragraph, ((TextNode) node).text(), fontSize, parentBold, false, null);
            } else if (node instanceof Element) {
                Element element = (Element) node;
                boolean isBold = parentBold || element.tagName().equals("b") || element.tagName().equals("strong");
                boolean isItalic = element.tagName().equals("i") || element.tagName().equals("em");
                boolean isLink = element.tagName().equals("a");

                String linkHref = isLink ? element.attr("href") : null;
                processChildElements(element, paragraph, fontSize, isBold, isItalic, linkHref);
            }
        }
    }

    private void processChildElements(Element element, XWPFParagraph paragraph, int fontSize, boolean parentBold, boolean parentItalic, String linkHref) {
        for (Node child : element.childNodes()) {
            if (child instanceof TextNode) {
                addFormattedText(paragraph, ((TextNode) child).text(), fontSize, parentBold, parentItalic, linkHref);
            } else if (child instanceof Element) {
                Element childElement = (Element) child;
                boolean isBold = parentBold || childElement.tagName().equals("b") || childElement.tagName().equals("strong");
                boolean isItalic = parentItalic || childElement.tagName().equals("i") || childElement.tagName().equals("em");
                boolean isLink = childElement.tagName().equals("a");

                String childLinkHref = isLink ? childElement.attr("href") : linkHref;
                processChildElements(childElement, paragraph, fontSize, isBold, isItalic, childLinkHref);
            }
        }
    }

    private void addFormattedText(XWPFParagraph paragraph, String text, int fontSize, boolean bold, boolean italic, String hyperlink) {
        if (!text.trim().isEmpty()) {
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
    }

    public void deleteFilesInFolder(String solicitationId) {
        String folderPrefix = baseFolder + "/" + solicitationId + "/";

        ListObjectsV2Request listRequest = ListObjectsV2Request.builder()
                .bucket(stBucketName)
                .prefix(folderPrefix)
                .build();
        S3Client s3Client = getS3Client();
        ListObjectsV2Response listResponse = s3Client.listObjectsV2(listRequest);

        List<ObjectIdentifier> objectsToDelete = listResponse.contents().stream()
                .map(s3Object -> ObjectIdentifier.builder().key(s3Object.key()).build())
                .toList();

        if (!objectsToDelete.isEmpty()) {
            DeleteObjectsRequest deleteRequest = DeleteObjectsRequest.builder()
                    .bucket(stBucketName)
                    .delete(Delete.builder().objects(objectsToDelete).build())
                    .build();

            s3Client.deleteObjects(deleteRequest);
            log.info("Cleaned folder: " + folderPrefix);
        } else {
            log.info("Folder already empty: " + folderPrefix);
        }
    }

    public void copySpecificDocuments(Map<String, String> fileNameAndPath, String solicitationId) {
        String folderPrefix = baseFolder + "/" + solicitationId + "/";
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
                getS3Client().copyObject(copyRequest);
                log.info("Copied: " + sourceKey + " -> " + targetKey);
            } catch (S3Exception e) {
                log.warn("Failed to copy: " + sourceKey + " - " + e.getMessage());
            }
        }
    }

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
