package com.auth.service;

import com.auth.dto.LoginRequest;
import com.auth.dto.LoginResponse;
import com.auth.dto.UserDto;
import com.auth.entity.User;
import com.auth.repository.UserRepository;
import com.auth.util.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
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

    private static final String TEST_EMAIL = "admin@example.com";
    private static final String INVALID_EMAIL = "nonexistent@example.com";
    private static final String TEST_PASSWORD = "password123";
    private static final String HASHED_PASSWORD = "hashedPassword";

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
        testUser.setEmail(TEST_EMAIL);
        testUser.setPassword(HASHED_PASSWORD);
        testUser.setFirstName("John");
        testUser.setLastName("Doe");

        loginRequest = new LoginRequest(TEST_EMAIL, TEST_PASSWORD);
    }

    @Test
    void testLoginSuccess() {
        when(userRepository.findByEmail(TEST_EMAIL)).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches(TEST_PASSWORD, HASHED_PASSWORD)).thenReturn(true);
        when(jwtUtil.generateToken(TEST_EMAIL, 1L)).thenReturn("mock-token");

        LoginResponse response = authService.login(loginRequest);

        assertNotNull(response, "Login response should not be null");
        assertEquals("mock-token", response.getToken(), "Token should match expected mock-token");
        assertEquals("Login successful", response.getMessage(), "Message should indicate successful login");
        assertNotNull(response.getUser(), "User in response should not be null");
        assertEquals(TEST_EMAIL, response.getUser().getEmail(), "User email should match");
        assertEquals("John", response.getUser().getFirstName(), "User first name should match");
        assertEquals("Doe", response.getUser().getLastName(), "User last name should match");
    }

    @Test
    void testLoginWithInvalidEmail() {
        when(userRepository.findByEmail(TEST_EMAIL)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            authService.login(loginRequest);
        }, "Expected login to throw IllegalArgumentException for invalid email");

        assertEquals("Invalid email or password", exception.getMessage(), "Exception message should match expected");
    }

    @Test
    void testLoginWithInvalidPassword() {
        when(userRepository.findByEmail(TEST_EMAIL)).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches(TEST_PASSWORD, HASHED_PASSWORD)).thenReturn(false);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            authService.login(loginRequest);
        }, "Expected login to throw IllegalArgumentException for invalid password");

        assertEquals("Invalid email or password", exception.getMessage(), "Exception message should match expected");
    }

    @Test
    void testGetUserByEmailSuccess() {
        when(userRepository.findByEmail(TEST_EMAIL)).thenReturn(Optional.of(testUser));

        UserDto userDto = authService.getUserByEmail(TEST_EMAIL);

        assertNotNull(userDto, "UserDto should not be null");
        assertEquals(1L, userDto.getId(), "UserDto ID should match");
        assertEquals(TEST_EMAIL, userDto.getEmail(), "UserDto email should match");
        assertEquals("John", userDto.getFirstName(), "UserDto first name should match");
        assertEquals("Doe", userDto.getLastName(), "UserDto last name should match");
    }

    @Test
    void testGetUserByEmailNotFound() {
        when(userRepository.findByEmail(INVALID_EMAIL)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            authService.getUserByEmail(INVALID_EMAIL);
        }, "Expected getUserByEmail to throw IllegalArgumentException if user not found");

        assertEquals("User not found", exception.getMessage(), "Exception message should match expected");
    }
}