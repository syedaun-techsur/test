package com.auth.config;

import com.auth.entity.User;
import com.auth.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);
    private static final String DEMO_EMAIL = "admin@example.com";
    private static final String DEMO_PASSWORD = "password123";

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        if (userRepository == null || passwordEncoder == null) {
            logger.error("UserRepository or PasswordEncoder not initialized properly");
            return;
        }

        if (!userRepository.existsByEmail(DEMO_EMAIL)) {
            User adminUser = new User();
            adminUser.setEmail(DEMO_EMAIL);
            adminUser.setPassword(passwordEncoder.encode(DEMO_PASSWORD));
            adminUser.setFirstName("John");
            adminUser.setLastName("Doe");

            userRepository.save(adminUser);
            logger.info("Demo user created: {} / {}", DEMO_EMAIL, DEMO_PASSWORD);
        }
    }
}