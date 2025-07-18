package com.auth.service;

import com.auth.dto.LoginRequest;
import com.auth.dto.LoginResponse;
import com.auth.dto.UserDto;
import com.auth.entity.User;
import com.auth.repository.UserRepository;
import com.auth.util.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

/**
 * Service class for authentication-related operations.
 */
@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = Objects.requireNonNull(userRepository, "userRepository must not be null");
        this.passwordEncoder = Objects.requireNonNull(passwordEncoder, "passwordEncoder must not be null");
        this.jwtUtil = Objects.requireNonNull(jwtUtil, "jwtUtil must not be null");
    }

    /**
     * Authenticates a user with provided login credentials.
     *
     * @param loginRequest the login request containing email and password
     * @return LoginResponse containing JWT token, user info, and message
     * @throws IllegalArgumentException if loginRequest is null or its email/password are invalid
     * @throws AuthenticationException if authentication fails due to invalid credentials
     */
    public LoginResponse login(LoginRequest loginRequest) {
        if (loginRequest == null || loginRequest.getEmail() == null || loginRequest.getPassword() == null) {
            throw new IllegalArgumentException("Email and password must be provided");
        }

        Optional<User> userOptional = userRepository.findByEmail(loginRequest.getEmail());
        User user = userOptional.orElseThrow(() ->
                new AuthenticationException("Invalid email or password"));

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new AuthenticationException("Invalid email or password");
        }

        String token = jwtUtil.generateToken(user.getEmail(), user.getId());
        UserDto userDto = toUserDto(user);
        return new LoginResponse(token, userDto, "Login successful");
    }

    /**
     * Retrieves user details by email.
     *
     * @param email the user's email
     * @return UserDto containing user details
     * @throws IllegalArgumentException if email is null or blank
     * @throws UserNotFoundException if user is not found
     */
    public UserDto getUserByEmail(String email) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email must be provided");
        }

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));
        return toUserDto(user);
    }

    /**
     * Converts User entity to UserDto.
     *
     * @param user the User entity
     * @return corresponding UserDto
     */
    private UserDto toUserDto(User user) {
        return new UserDto(
                user.getId(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName()
        );
    }

    /**
     * Exception representing authentication failures.
     */
    public static class AuthenticationException extends RuntimeException {
        public AuthenticationException(String message) {
            super(message);
        }
    }

    /**
     * Exception representing user not found scenarios.
     */
    public static class UserNotFoundException extends RuntimeException {
        public UserNotFoundException(String message) {
            super(message);
        }
    }
}