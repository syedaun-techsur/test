package solutions.techsur.common.microservice.config;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.OAuthFlow;
import io.swagger.v3.oas.annotations.security.OAuthFlows;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for setting up OpenAPI security schemes using Springdoc.
 * Defines an OAuth2 password flow security scheme named "bearerToken".
 * 
 * Note: Property placeholders in annotations might not be resolved automatically.
 * If dynamic values are required, consider programmatic configuration alternative.
 */
@Configuration
@SecurityScheme(
    name = "bearerToken",
    type = SecuritySchemeType.OAUTH2,
    flows = @OAuthFlows(
        password = @OAuthFlow(
            authorizationUrl = "${springdoc.oAuthFlow.authorizationUrl}", // Ensure resolution via configuration or consider hardcoding for build-time
            tokenUrl = "${springdoc.oAuthFlow.tokenUrl}"
        )
    )
)
public class SpringdocConfig {
}