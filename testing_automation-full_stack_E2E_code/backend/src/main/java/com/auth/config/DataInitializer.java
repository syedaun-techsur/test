package com.auth.config;

import com.auth.entity.User;
import com.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        String adminEmail = "admin@example.com";
        String adminPassword = "password123";
        if (!userRepository.existsByEmail(adminEmail)) {
            User adminUser = new User();
            adminUser.setEmail(adminEmail);
            String encodedPassword = passwordEncoder.encode(adminPassword != null ? adminPassword : "");
            adminUser.setPassword(encodedPassword);
            adminUser.setFirstName("John");
            adminUser.setLastName("Doe");

            userRepository.save(adminUser);
            log.info("Demo user created: {} / {}", adminEmail, adminPassword);
        }
    }
}