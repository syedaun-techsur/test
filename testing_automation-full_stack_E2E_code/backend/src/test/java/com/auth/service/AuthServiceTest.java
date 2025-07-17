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

    private static final String VALID_EMAIL = "admin@example.com";
    private static final String VALID_PASSWORD = "password123";
    private static final String HASHED_PASSWORD = "hashedPassword";
    private static final String TOKEN = "mock-token";
    private static final String INVALID_EMAIL = "nonexistent@example.com";
    private static final String INVALID_CREDENTIALS_MESSAGE = "Invalid email or password";
    private static final String USER_NOT_FOUND_MESSAGE = "User not found";

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
        testUser.setEmail(VALID_EMAIL);
        testUser.setPassword(HASHED_PASSWORD);
        testUser.setFirstName("John");
        testUser.setLastName("Doe");

        loginRequest = new LoginRequest(VALID_EMAIL, VALID_PASSWORD);
    }

    @Test
    @DisplayName("Test successful login returns expected response")
    void testLoginSuccess() {
        when(userRepository.findByEmail(VALID_EMAIL)).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches(VALID_PASSWORD, HASHED_PASSWORD)).thenReturn(true);
        when(jwtUtil.generateToken(VALID_EMAIL, 1L)).thenReturn(TOKEN);

        LoginResponse response = authService.login(loginRequest);

        assertNotNull(response, "Login response should not be null");
        assertEquals(TOKEN, response.getToken(), "Token should match generated token");
        assertEquals("Login successful", response.getMessage(), "Message should indicate success");
        assertNotNull(response.getUser(), "User DTO should not be null");
        assertEquals(VALID_EMAIL, response.getUser().getEmail(), "User email should match");
        assertEquals("John", response.getUser().getFirstName(), "User first name should match");
        assertEquals("Doe", response.getUser().getLastName(), "User last name should match");
    }

    @Test
    @DisplayName("Test login failure with invalid email throws exception")
    void testLoginWithInvalidEmail() {
        when(userRepository.findByEmail(VALID_EMAIL)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> authService.login(loginRequest));
        assertExceptionMessage(exception, INVALID_CREDENTIALS_MESSAGE);
    }

    @Test
    @DisplayName("Test login failure with invalid password throws exception")
    void testLoginWithInvalidPassword() {
        when(userRepository.findByEmail(VALID_EMAIL)).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches(VALID_PASSWORD, HASHED_PASSWORD)).thenReturn(false);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> authService.login(loginRequest));
        assertExceptionMessage(exception, INVALID_CREDENTIALS_MESSAGE);
    }

    @Test
    @DisplayName("Test successful retrieval of user by email")
    void testGetUserByEmailSuccess() {
        when(userRepository.findByEmail(VALID_EMAIL)).thenReturn(Optional.of(testUser));

        UserDto userDto = authService.getUserByEmail(VALID_EMAIL);

        assertNotNull(userDto, "User DTO should not be null");
        assertEquals(1L, userDto.getId(), "User ID should match");
        assertEquals(VALID_EMAIL, userDto.getEmail(), "User email should match");
        assertEquals("John", userDto.getFirstName(), "User first name should match");
        assertEquals("Doe", userDto.getLastName(), "User last name should match");
    }

    @Test
    @DisplayName("Test getUserByEmail throws exception when user not found")
    void testGetUserByEmailNotFound() {
        when(userRepository.findByEmail(INVALID_EMAIL)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> authService.getUserByEmail(INVALID_EMAIL));
        assertExceptionMessage(exception, USER_NOT_FOUND_MESSAGE);
    }

    private void assertExceptionMessage(RuntimeException exception, String expectedMessage) {
        assertEquals(expectedMessage, exception.getMessage(), "Exception message should match");
    }
}