package solutions.techsur.common.microservice.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import solutions.techsur.common.microservice.utils.JwtUtils;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {
	@Value("${common.microservice.cors.origins:*}")
	private List<String> corsOrigins;

	@Value("${common.microservice.cors.methods:*}")
	private List<String> corsMethods;

	@Value("${common.microservice.cors.headers:*}")
	private List<String> corsHeaders;

	@Value("${spring.security.oauth2.resourceserver.jwt.jwk-set-uri:https://example.com/jwk-set-uri}")
	private String jwkSetUri;

	@Bean
	public SecurityFilterChain customSecurityFilterChain(HttpSecurity http) throws Exception {
		log.info("CORS Origins: {}", corsOrigins);
		log.info("CORS Methods: {}", corsMethods);
		log.info("CORS Headers: {}", corsHeaders);
		log.info("JWK Set URL {}", jwkSetUri);

		http
//                .csrf(Customizer.withDefaults())
				.csrf(AbstractHttpConfigurer::disable)
				.cors(cors -> cors.configurationSource(request -> {
					CorsConfiguration config = new CorsConfiguration();
					config.setAllowedOrigins(corsOrigins);
					config.setAllowedMethods(corsMethods);
					config.setAllowedHeaders(corsHeaders);
					return config;
				}))
				.authorizeHttpRequests(
						authorize -> authorize
								.requestMatchers(
										"/v3/api-docs/**",
										"/swagger-resources/**",
										"/swagger-ui.html",
										"/webjars/**",
										"/swagger-ui/**",
										"/api/v1/**")
								.permitAll().anyRequest().authenticated())
				.oauth2ResourceServer(oauth2 -> oauth2
						.jwt(jwt -> jwt.jwkSetUri(jwkSetUri).jwtAuthenticationConverter(jwtAuthenticationConverter())));
		return http.build();
	}

	@Bean
	public JwtAuthenticationConverter jwtAuthenticationConverter() {
		JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
		jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(new KeycloakRoleConverter());
		return jwtAuthenticationConverter;
	}

	static class KeycloakRoleConverter implements Converter<Jwt, Collection<GrantedAuthority>> {
		@Override
		public Collection<GrantedAuthority> convert(@NonNull Jwt jwt) {
			return JwtUtils.getUserRoles(jwt).stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
		}
	}
}
