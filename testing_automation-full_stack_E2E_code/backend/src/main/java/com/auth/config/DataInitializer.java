package com.auth.config;

import com.auth.entity.User;
import com.auth.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * DataInitializer is responsible for seeding initial data into the database.
 * In this case, it creates a demo admin user if it does not exist.
 */
@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private static final String DEMO_EMAIL = "admin@example.com";
    private static final String DEMO_PASSWORD = "password123";

    public DataInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        try {
            if (!userRepository.existsByEmail(DEMO_EMAIL)) {
                User adminUser = new User();
                adminUser.setEmail(DEMO_EMAIL);
                adminUser.setPassword(passwordEncoder.encode(DEMO_PASSWORD));
                adminUser.setFirstName("John");
                adminUser.setLastName("Doe");

                userRepository.save(adminUser);
                logger.info("Demo user created: {} / {}", DEMO_EMAIL, DEMO_PASSWORD);
            } else {
                logger.info("Demo user with email {} already exists. No action taken.", DEMO_EMAIL);
            }
        } catch (Exception e) {
            logger.error("Error occurred during data initialization", e);
        }
    }
}