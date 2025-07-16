package solutions.techsur.common.microservice.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.validation.annotation.Validated;

import java.util.List;

/**
 * Configuration properties for authorization-related settings.
 */
@ConfigurationProperties(prefix = "rfpaiapi.authority")
@ConstructorBinding
@Validated
public class AuthProperties {

    /**
     * List of authorities allowed to upload documents.
     */
    private final List<String> uploadDocument;

    /**
     * List of authorities allowed to delete proposals.
     */
    private final List<String> deleteProposal;

    public AuthProperties(List<String> uploadDocument, List<String> deleteProposal) {
        this.uploadDocument = uploadDocument;
        this.deleteProposal = deleteProposal;
    }

    public List<String> getUploadDocument() {
        return uploadDocument;
    }

    public List<String> getDeleteProposal() {
        return deleteProposal;
    }
}