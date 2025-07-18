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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    private static final String EMAIL = "admin@example.com";
    private static final String PASSWORD = "password123";
    private static final String HASHED_PASSWORD = "hashedPassword";
    private static final String INVALID_EMAIL = "nonexistent@example.com";
    private static final String INVALID_CREDENTIALS_MSG = "Invalid email or password";
    private static final String USER_NOT_FOUND_MSG = "User not found";
    private static final String MOCK_TOKEN = "mock-token";

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
    @DisplayName("Test login success scenario with valid credentials")
    void testLoginSuccess() {
        when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches(PASSWORD, HASHED_PASSWORD)).thenReturn(true);
        when(jwtUtil.generateToken(EMAIL, 1L)).thenReturn(MOCK_TOKEN);

        LoginResponse response = authService.login(loginRequest);

        assertNotNull(response, "LoginResponse should not be null");
        assertEquals(MOCK_TOKEN, response.getToken(), "Token mismatch");
        assertEquals("Login successful", response.getMessage(), "Login message mismatch");
        assertNotNull(response.getUser(), "User data in response should not be null");
        assertEquals(EMAIL, response.getUser().getEmail(), "User email mismatch");
        assertEquals("John", response.getUser().getFirstName(), "User first name mismatch");
        assertEquals("Doe", response.getUser().getLastName(), "User last name mismatch");
    }

    @Test
    @DisplayName("Test login failure due to invalid email")
    void testLoginWithInvalidEmail() {
        when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> authService.login(loginRequest));
        assertEquals(INVALID_CREDENTIALS_MSG, exception.getMessage(), "Exception message mismatch for invalid email");
    }

    @Test
    @DisplayName("Test login failure due to invalid password")
    void testLoginWithInvalidPassword() {
        when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches(PASSWORD, HASHED_PASSWORD)).thenReturn(false);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> authService.login(loginRequest));
        assertEquals(INVALID_CREDENTIALS_MSG, exception.getMessage(), "Exception message mismatch for invalid password");
    }

    @Test
    @DisplayName("Test fetching user by email - success")
    void testGetUserByEmailSuccess() {
        when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.of(testUser));

        UserDto userDto = authService.getUserByEmail(EMAIL);

        assertNotNull(userDto, "UserDto should not be null");
        assertEquals(1L, userDto.getId(), "User ID mismatch");
        assertEquals(EMAIL, userDto.getEmail(), "User email mismatch");
        assertEquals("John", userDto.getFirstName(), "User first name mismatch");
        assertEquals("Doe", userDto.getLastName(), "User last name mismatch");
    }

    @Test
    @DisplayName("Test fetching user by email - user not found")
    void testGetUserByEmailNotFound() {
        when(userRepository.findByEmail(INVALID_EMAIL)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> authService.getUserByEmail(INVALID_EMAIL));
        assertEquals(USER_NOT_FOUND_MSG, exception.getMessage(), "Exception message mismatch for user not found");
    }
}