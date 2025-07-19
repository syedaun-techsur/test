package com.auth.config;

import com.auth.entity.User;
import com.auth.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        if (userRepository == null || passwordEncoder == null) {
            logger.error("UserRepository or PasswordEncoder not initialized");
            return;
        }
        // Create demo user if not exists
        if (!userRepository.existsByEmail("admin@example.com")) {
            User adminUser = new User();
            adminUser.setEmail("admin@example.com");
            adminUser.setPassword(passwordEncoder.encode("password123"));
            adminUser.setFirstName("John");
            adminUser.setLastName("Doe");

            userRepository.save(adminUser);
            logger.info("Demo user created: admin@example.com / password123");
        }
    }
}