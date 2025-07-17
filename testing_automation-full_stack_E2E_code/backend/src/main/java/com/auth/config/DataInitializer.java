package com.auth.config;

import com.auth.entity.User;
import com.auth.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import java.util.Objects;

/**
 * Initializes demo data on application startup.
 */
@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = Objects.requireNonNull(userRepository, "userRepository must not be null");
        this.passwordEncoder = Objects.requireNonNull(passwordEncoder, "passwordEncoder must not be null");
    }

    @Override
    public void run(String... args) {
        try {
            final String adminEmail = "admin@example.com";
            if (!userRepository.existsByEmail(adminEmail)) {
                final User adminUser = new User();
                adminUser.setEmail(adminEmail);
                adminUser.setPassword(passwordEncoder.encode("password123"));
                adminUser.setFirstName("John");
                adminUser.setLastName("Doe");

                userRepository.save(adminUser);
                logger.info("Demo user created: {} / {}", adminEmail, "password123");
            }
        } catch (Exception e) {
            logger.error("Failed to initialize demo user data", e);
        }
    }
}