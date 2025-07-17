package com.auth.config;

import com.auth.entity.User;
import com.auth.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * DataInitializer runs at application startup to initialize demo data.
 * Creates a demo admin user if not already present.
 */
@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        if (userRepository == null) {
            throw new IllegalArgumentException("UserRepository must not be null");
        }
        if (passwordEncoder == null) {
            throw new IllegalArgumentException("PasswordEncoder must not be null");
        }
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        final String demoEmail = "admin@example.com";
        final String demoPassword = "password123";

        if (!userRepository.existsByEmail(demoEmail)) {
            User adminUser = new User();
            adminUser.setEmail(demoEmail);
            adminUser.setPassword(passwordEncoder.encode(demoPassword));
            adminUser.setFirstName("John");
            adminUser.setLastName("Doe");

            userRepository.save(adminUser);
            logger.info("Demo user created: {} / {}", demoEmail, demoPassword);
        }
    }
}