package com.auth.config;

import com.auth.entity.User;
import com.auth.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import java.util.Objects;

/**
 * DataInitializer runs at application startup to ensure demo user exists in database.
 */
@Component
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private static final String DEMO_USER_EMAIL = "admin@example.com";
    private static final String DEMO_USER_PASSWORD = "password123";

    public DataInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = Objects.requireNonNull(userRepository, "userRepository must not be null");
        this.passwordEncoder = Objects.requireNonNull(passwordEncoder, "passwordEncoder must not be null");
    }

    @Override
    public void run(String... args) {
        try {
            if (!userRepository.existsByEmail(DEMO_USER_EMAIL)) {
                User adminUser = new User();
                adminUser.setEmail(DEMO_USER_EMAIL);
                adminUser.setPassword(passwordEncoder.encode(DEMO_USER_PASSWORD));
                adminUser.setFirstName("John");
                adminUser.setLastName("Doe");

                userRepository.save(adminUser);
                log.info("Demo user created: {} / {}", DEMO_USER_EMAIL, DEMO_USER_PASSWORD);
            } else {
                log.info("Demo user already exists: {}", DEMO_USER_EMAIL);
            }
        } catch (Exception e) {
            log.error("Failed to initialize demo user", e);
        }
    }
}