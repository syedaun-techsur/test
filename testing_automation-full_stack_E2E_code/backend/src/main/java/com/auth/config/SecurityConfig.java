package com.auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * Bean to provide a PasswordEncoder instance.
     * 
     * @return a BCryptPasswordEncoder instance
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Bean to configure the SecurityFilterChain with custom settings.
     * 
     * @param http the HttpSecurity instance
     * @return the configured SecurityFilterChain
     * @throws Exception if an error occurs during configuration
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // Configure CORS
        http.cors(cors -> cors.configurationSource(corsConfigurationSource()))
            // Disable CSRF protection
            .csrf(csrf -> csrf.disable())
            // Set session management to stateless
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            // Configure authorization rules
            .authorizeHttpRequests(authz -> authz
                .requestMatchers("/api/login", "/api/logout").permitAll()
                .anyRequest().authenticated()
            )
            // Permit OPTIONS requests for CORS pre-flight
            .requestMatchers(HttpMethod.OPTIONS).permitAll();

        return http.build();
    }

    /**
     * Bean to provide a CorsConfigurationSource with custom settings.
     * 
     * @return the CorsConfigurationSource
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(Arrays.asList("http://localhost:5173"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}