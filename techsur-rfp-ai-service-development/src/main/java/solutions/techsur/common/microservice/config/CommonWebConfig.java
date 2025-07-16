package solutions.techsur.common.microservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.web.filter.ForwardedHeaderFilter;

/**
 * Common web configuration for microservices.
 * Enables Spring Data Web support and configures forwarded header filter.
 */
@Configuration
@EnableSpringDataWebSupport(pageSerializationMode = EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO)
public class CommonWebConfig {

    /**
     * Bean to handle forwarding of headers such as X-Forwarded-For.
     * This helps in scenarios where the service is behind a proxy/load balancer.
     */
    @Bean
    public ForwardedHeaderFilter forwardedHeaderFilter() {
        return new ForwardedHeaderFilter();
    }
}