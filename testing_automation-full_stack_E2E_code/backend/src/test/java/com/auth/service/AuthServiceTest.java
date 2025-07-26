package com.auth.service;

import com.auth.dto.LoginRequest;
import com.auth.dto.LoginResponse;
import com.auth.dto.UserDto;
import com.auth.entity.User;
import com.auth.repository.UserRepository;
import com.auth.util.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    private static final String EMAIL = "admin@example.com";
    private static final String PASSWORD = "password123";
    private static final String HASHED_PASSWORD = "hashedPassword";
    private static final String INVALID_EMAIL = "nonexistent@example.com";
    private static final String TOKEN = "mock-token";

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private AuthService authService;

    private User testUser;
    private LoginRequest loginRequest;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setEmail(EMAIL);
        testUser.setPassword(HASHED_PASSWORD);
        testUser.setFirstName("John");
        testUser.setLastName("Doe");

        loginRequest = new LoginRequest(EMAIL, PASSWORD);
    }

    @Test
    @DisplayName("Login succeeds with valid credentials")
    void loginSucceedsWithValidCredentials() {
        when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches(PASSWORD, HASHED_PASSWORD)).thenReturn(true);
        when(jwtUtil.generateToken(EMAIL, 1L)).thenReturn(TOKEN);

        LoginResponse response = authService.login(loginRequest);

        assertNotNull(response, "Response should not be null");
        assertEquals(TOKEN, response.getToken(), "Token should match expected value");
        assertEquals("Login successful", response.getMessage(), "Message should indicate successful login");

        UserDto userDto = response.getUser();
        assertNotNull(userDto, "User DTO should not be null");
        assertEquals(EMAIL, userDto.getEmail(), "User email mismatch");
        assertEquals("John", userDto.getFirstName(), "User first name mismatch");
        assertEquals("Doe", userDto.getLastName(), "User last name mismatch");
    }

    @Test
    @DisplayName("Login fails with invalid email")
    void loginFailsWithInvalidEmail() {
        when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
            () -> authService.login(loginRequest),
            "Expected RuntimeException for invalid email");

        assertEquals("Invalid email or password", exception.getMessage(), "Exception message mismatch");
    }

    @Test
    @DisplayName("Login fails with invalid password")
    void loginFailsWithInvalidPassword() {
        when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches(PASSWORD, HASHED_PASSWORD)).thenReturn(false);

        RuntimeException exception = assertThrows(RuntimeException.class,
            () -> authService.login(loginRequest),
            "Expected RuntimeException for invalid password");

        assertEquals("Invalid email or password", exception.getMessage(), "Exception message mismatch");
    }

    @Test
    @DisplayName("Get user by email succeeds when user exists")
    void getUserByEmailSucceedsWhenUserExists() {
        when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.of(testUser));

        UserDto userDto = authService.getUserByEmail(EMAIL);

        assertNotNull(userDto, "User DTO should not be null");
        assertEquals(1L, userDto.getId(), "User ID mismatch");
        assertEquals(EMAIL, userDto.getEmail(), "User email mismatch");
        assertEquals("John", userDto.getFirstName(), "User first name mismatch");
        assertEquals("Doe", userDto.getLastName(), "User last name mismatch");
    }

    @Test
    @DisplayName("Get user by email throws exception when user not found")
    void getUserByEmailThrowsWhenUserNotFound() {
        when(userRepository.findByEmail(INVALID_EMAIL)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
            () -> authService.getUserByEmail(INVALID_EMAIL),
            "Expected RuntimeException when user is not found");

        assertEquals("User not found", exception.getMessage(), "Exception message mismatch");
    }
}