package solutions.techsur.common.microservice.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class to define the OpenAPI specification with JWT bearer authentication.
 */
@Configuration
public class OpenAPIConfig {

    /**
     * Creates a custom OpenAPI bean to configure the API info and security schemes.
     *
     * @return OpenAPI specification object with configured metadata and security definitions.
     */
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Request for proposal")
                        .version("1.0")
                        .description("API Documentation with JWT Authentication via Keycloak"))
                .addSecurityItem(new SecurityRequirement().addList("bearerToken"))
                .components(new Components()
                        .addSecuritySchemes("bearerToken",
                                new SecurityScheme()
                                        .name("bearerToken")
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")));
    }
}