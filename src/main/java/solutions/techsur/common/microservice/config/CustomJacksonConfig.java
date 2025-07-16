package solutions.techsur.common.microservice.config;

import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.web.config.SpringDataJacksonConfiguration;

@Configuration
public class CustomJacksonConfig {
    @Bean
    public Jackson2ObjectMapperBuilderCustomizer addCustomPageModule() {
        return builder -> builder.modulesToInstall(new SpringDataJacksonConfiguration.PageModule());
    }
}