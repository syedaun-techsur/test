package solutions.techsur.common.microservice.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("properties")
@ConfigurationProperties(prefix = "rfpaiapi.authority")
@Getter
@Setter
public class AuthProperties {
    private List<String> uploadDocument;
    private List<String> deleteProposal;
}
