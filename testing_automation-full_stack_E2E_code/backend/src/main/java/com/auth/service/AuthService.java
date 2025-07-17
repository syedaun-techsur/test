package com.auth.service;

import com.auth.dto.LoginRequest;
import com.auth.dto.LoginResponse;
import com.auth.dto.UserDto;
import com.auth.entity.User;
import com.auth.repository.UserRepository;
import com.auth.util.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Objects;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthService(final UserRepository userRepository,
                       final PasswordEncoder passwordEncoder,
                       final JwtUtil jwtUtil) {
        this.userRepository = Objects.requireNonNull(userRepository, "userRepository must not be null");
        this.passwordEncoder = Objects.requireNonNull(passwordEncoder, "passwordEncoder must not be null");
        this.jwtUtil = Objects.requireNonNull(jwtUtil, "jwtUtil must not be null");
    }

    /**
     * Authenticates the user based on email and password.
     *
     * @param loginRequest the login request containing email and password
     * @return LoginResponse containing JWT token, user details, and message
     */
    public LoginResponse login(final LoginRequest loginRequest) {
        Objects.requireNonNull(loginRequest, "loginRequest must not be null");
        final Optional<User> userOptional = userRepository.findByEmail(loginRequest.getEmail());

        if (userOptional.isEmpty() || !passwordEncoder.matches(loginRequest.getPassword(), userOptional.get().getPassword())) {
            throw new IllegalArgumentException("Invalid email or password");
        }

        final User user = userOptional.get();
        final String token = jwtUtil.generateToken(user.getEmail(), user.getId());
        final UserDto userDto = toUserDto(user);

        return new LoginResponse(token, userDto, "Login successful");
    }

    /**
     * Retrieves a user by email.
     *
     * @param email the email of the user
     * @return UserDto of the found user
     */
    public UserDto getUserByEmail(final String email) {
        Objects.requireNonNull(email, "email must not be null");
        final User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        return toUserDto(user);
    }

    /**
     * Helper method to map User entity to UserDto.
     *
     * @param user the User entity
     * @return the UserDto representation
     */
    private UserDto toUserDto(final User user) {
        return new UserDto(
                user.getId(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName()
        );
    }
}