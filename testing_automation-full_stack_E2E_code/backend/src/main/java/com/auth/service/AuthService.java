package com.auth.service;

import com.auth.dto.LoginRequest;
import com.auth.dto.LoginResponse;
import com.auth.dto.UserDto;
import com.auth.entity.User;
import com.auth.repository.UserRepository;
import com.auth.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Autowired
    public AuthService(final UserRepository userRepository,
                       final PasswordEncoder passwordEncoder,
                       final JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    /**
     * Authenticates a user based on email and password.
     *
     * @param loginRequest contains user's email and password
     * @return LoginResponse containing JWT token and user info if login successful
     * @throws IllegalArgumentException if email or password is invalid
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
     * Retrieves user details by email.
     *
     * @param email the user email
     * @return UserDto containing user information
     * @throws NoSuchElementException if user is not found
     */
    public UserDto getUserByEmail(final String email) {
        final User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NoSuchElementException("User not found"));

        return new UserDto(
                user.getId(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName()
        );
    }
}