package solutions.techsur.rfpaiservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import solutions.techsur.common.microservice.config.AuthProperties;

@SpringBootApplication
@EnableJpaAuditing
@ComponentScan(basePackages = {"solutions.techsur"})
@EnableConfigurationProperties(AuthProperties.class) // Enable properties class
public class RfpAiServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(RfpAiServiceApplication.class, args);
	}

}
