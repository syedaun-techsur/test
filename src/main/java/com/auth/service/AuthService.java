package com.auth.service;

import com.auth.dto.LoginRequest;
import com.auth.dto.LoginResponse;
import com.auth.dto.UserDto;
import com.auth.entity.User;
import com.auth.repository.UserRepository;
import com.auth.util.JwtUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class AuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    private static final String INVALID_CREDENTIALS_MSG = "Invalid email or password";
    private static final String USER_NOT_FOUND_MSG = "User not found";

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = Objects.requireNonNull(userRepository, "userRepository must not be null");
        this.passwordEncoder = Objects.requireNonNull(passwordEncoder, "passwordEncoder must not be null");
        this.jwtUtil = Objects.requireNonNull(jwtUtil, "jwtUtil must not be null");
    }

    /**
     * Authenticates user based on provided email and password.
     *
     * @param loginRequest containing user email and password
     * @return LoginResponse containing JWT token and user info
     * @throws RuntimeException if credentials are invalid
     */
    public LoginResponse login(LoginRequest loginRequest) {
        Objects.requireNonNull(loginRequest, "loginRequest must not be null");

        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> {
                    logger.warn("Login failed: User with email '{}' not found", loginRequest.getEmail());
                    return new RuntimeException(INVALID_CREDENTIALS_MSG);
                });

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            logger.warn("Login failed: Password mismatch for user '{}'", loginRequest.getEmail());
            throw new RuntimeException(INVALID_CREDENTIALS_MSG);
        }

        String token = jwtUtil.generateToken(user.getEmail(), user.getId());

        UserDto userDto = new UserDto(
                user.getId(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName()
        );

        return new LoginResponse(token, userDto, "Login successful");
    }

    /**
     * Retrieves user details by email.
     *
     * @param email user email
     * @return UserDto containing user details
     * @throws RuntimeException if user is not found by email
     */
    public UserDto getUserByEmail(String email) {
        Objects.requireNonNull(email, "email must not be null");

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    logger.warn("User not found with email '{}'", email);
                    return new RuntimeException(USER_NOT_FOUND_MSG);
                });

        return new UserDto(
                user.getId(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName()
        );
    }

    /**
     * Custom unchecked exception for authentication errors.
     */
    public static class AuthenticationException extends RuntimeException {
        public AuthenticationException(String message) {
            super(message);
        }
    }

    /**
     * Custom unchecked exception when user is not found.
     */
    public static class UserNotFoundException extends RuntimeException {
        public UserNotFoundException(String message) {
            super(message);
        }
    }
}