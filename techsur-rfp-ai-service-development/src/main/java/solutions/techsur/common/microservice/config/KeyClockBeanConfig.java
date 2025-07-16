package solutions.techsur.common.microservice.config;

import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KeyClockBeanConfig {
    private static final String ADMIN_CLI = "admin-cli";

    @Value("${rfpaiapi.keycloak.url}")
    private String serverUrl;

    @Value("${rfpaiapi.keycloak.masterrealm}")
    private String masterRealm;

    @Value("${rfpaiapi.keycloak.realm}")
    private String keycloakRealm;

    @Value("${rfpaiapi.keycloak.user}")
    private String keycloakUser;

    @Value("${rfpaiapi.keycloak.password}")
    private String keycloakPassword;

    @Bean
    public Keycloak keycloak() {
        return KeycloakBuilder.builder()
                .serverUrl(serverUrl)
                .realm(masterRealm)
                .clientId(ADMIN_CLI)
                .username(keycloakUser)
                .password(keycloakPassword)
                .build();
    }
}