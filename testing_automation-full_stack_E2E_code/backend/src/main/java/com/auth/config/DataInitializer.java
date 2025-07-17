package com.auth.config;

import com.auth.entity.User;
import com.auth.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Initializes demo user data on application startup if not already present.
 */
@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);
    private static final String DEMO_USER_EMAIL = "admin@example.com";
    private static final String DEMO_USER_PASSWORD = "password123";

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (userRepository == null || passwordEncoder == null) {
            logger.error("UserRepository or PasswordEncoder not initialized.");
            return;
        }

        if (!userRepository.existsByEmail(DEMO_USER_EMAIL)) {
            User adminUser = new User();
            adminUser.setEmail(DEMO_USER_EMAIL);
            adminUser.setPassword(passwordEncoder.encode(DEMO_USER_PASSWORD));
            adminUser.setFirstName("John");
            adminUser.setLastName("Doe");

            userRepository.save(adminUser);
            logger.info("Demo user created: {} / {}", DEMO_USER_EMAIL, DEMO_USER_PASSWORD);
        } else {
            logger.info("Demo user already exists: {}", DEMO_USER_EMAIL);
        }
    }
}