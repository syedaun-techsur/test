package solutions.techsur.common.microservice.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Configuration properties for authority-related settings in Rfpaiapi.
 * Bound from the application configuration properties prefixed with "rfpaiapi.authority".
 */
@ConfigurationProperties(prefix = "rfpaiapi.authority")
@Validated
@Getter
@Setter
public class AuthProperties {

    /**
     * List of endpoints or authorities allowed to upload documents.
     */
    @NotNull
    @NotEmpty
    private List<String> uploadDocuments;

    /**
     * List of endpoints or authorities allowed to delete proposals.
     */
    @NotNull
    @NotEmpty
    private List<String> deleteProposals;
}