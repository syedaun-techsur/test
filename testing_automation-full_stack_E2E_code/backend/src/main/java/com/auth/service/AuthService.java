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
import org.springframework.util.StringUtils;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * Authenticate user and generate login response with JWT token.
     *
     * @param loginRequest the login request containing email and password
     * @return LoginResponse containing token, user details, and message
     * @throws IllegalArgumentException if email or password is blank
     * @throws NoSuchElementException if user is not found or password mismatch
     */
    public LoginResponse login(final LoginRequest loginRequest) {
        if (loginRequest == null || !StringUtils.hasText(loginRequest.getEmail()) || !StringUtils.hasText(loginRequest.getPassword())) {
            throw new IllegalArgumentException("Email and password must be provided");
        }

        Optional<User> userOptional = userRepository.findByEmail(loginRequest.getEmail());

        if (userOptional.isEmpty()) {
            throw new NoSuchElementException("Invalid email or password");
        }

        User user = userOptional.get();

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new NoSuchElementException("Invalid email or password");
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
     * Retrieve user details by email.
     *
     * @param email the user's email
     * @return UserDto containing user details
     * @throws IllegalArgumentException if email is blank
     * @throws NoSuchElementException if user not found
     */
    public UserDto getUserByEmail(final String email) {
        if (!StringUtils.hasText(email)) {
            throw new IllegalArgumentException("Email must be provided");
        }

        Optional<User> userOptional = userRepository.findByEmail(email);

        if (userOptional.isEmpty()) {
            throw new NoSuchElementException("User not found");
        }

        User user = userOptional.get();
        return new UserDto(
            user.getId(),
            user.getEmail(),
            user.getFirstName(),
            user.getLastName()
        );
    }
}