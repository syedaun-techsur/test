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

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    /**
     * Authenticate user and generate a JWT token.
     * @param loginRequest contains email and password.
     * @return LoginResponse with token and user details.
     * @throws IllegalArgumentException if authentication fails.
     */
    public LoginResponse login(final LoginRequest loginRequest) {
        final User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Invalid email or password");
        }

        final String token = jwtUtil.generateToken(user.getEmail(), user.getId());

        final UserDto userDto = new UserDto(
                user.getId(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName()
        );

        return new LoginResponse(token, userDto, "Login successful");
    }

    /**
     * Retrieve user details by email.
     * @param email email address of the user.
     * @return UserDto with user information.
     * @throws IllegalArgumentException if user is not found.
     */
    public UserDto getUserByEmail(final String email) {
        final User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        return new UserDto(
                user.getId(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName()
        );
    }
}