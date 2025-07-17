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

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        // Create demo user if not exists
        String adminEmail = "admin@example.com";
        if (adminEmail != null && !adminEmail.trim().isEmpty() && !userRepository.existsByEmail(adminEmail.trim())) {
            User adminUser = new User();
            adminUser.setEmail(adminEmail.trim());
            adminUser.setPassword(passwordEncoder.encode("password123"));
            adminUser.setFirstName("John");
            adminUser.setLastName("Doe");

            userRepository.save(adminUser);
            logger.info("Demo user created: {} / {}", adminEmail, "password123");
        }
    }
}