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
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    private static final String EMAIL = "admin@example.com";
    private static final String INVALID_EMAIL = "nonexistent@example.com";
    private static final String PASSWORD = "password123";
    private static final String HASHED_PASSWORD = "hashedPassword";
    private static final String INVALID_CREDENTIALS_MSG = "Invalid email or password";
    private static final String USER_NOT_FOUND_MSG = "User not found";
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
    void login_ShouldReturnLoginResponse_WhenCredentialsAreValid() {
        when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches(PASSWORD, HASHED_PASSWORD)).thenReturn(true);
        when(jwtUtil.generateToken(EMAIL, 1L)).thenReturn(TOKEN);

        LoginResponse response = authService.login(loginRequest);

        assertNotNull(response, "LoginResponse should not be null");
        assertEquals(TOKEN, response.getToken(), "Token should match expected");
        assertEquals("Login successful", response.getMessage(), "Message should indicate success");

        UserDto user = response.getUser();
        assertNotNull(user, "UserDto in response should not be null");
        assertEquals(EMAIL, user.getEmail(), "User email should match");
        assertEquals("John", user.getFirstName(), "User first name should match");
        assertEquals("Doe", user.getLastName(), "User last name should match");
        assertEquals(1L, user.getId(), "User id should match");
    }

    @Test
    void login_ShouldThrowException_WhenEmailNotFound() {
        when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> authService.login(loginRequest),
                "Expected RuntimeException when email not found");

        assertEquals(INVALID_CREDENTIALS_MSG, exception.getMessage());
    }

    @Test
    void login_ShouldThrowException_WhenPasswordDoesNotMatch() {
        when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches(PASSWORD, HASHED_PASSWORD)).thenReturn(false);

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> authService.login(loginRequest),
                "Expected RuntimeException when password does not match");

        assertEquals(INVALID_CREDENTIALS_MSG, exception.getMessage());
    }

    @Test
    void getUserByEmail_ShouldReturnUserDto_WhenUserExists() {
        when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.of(testUser));

        UserDto userDto = authService.getUserByEmail(EMAIL);

        assertNotNull(userDto, "UserDto should not be null");
        assertEquals(1L, userDto.getId(), "User id should match");
        assertEquals(EMAIL, userDto.getEmail(), "User email should match");
        assertEquals("John", userDto.getFirstName(), "User first name should match");
        assertEquals("Doe", userDto.getLastName(), "User last name should match");
    }

    @Test
    void getUserByEmail_ShouldThrowException_WhenUserNotFound() {
        when(userRepository.findByEmail(INVALID_EMAIL)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> authService.getUserByEmail(INVALID_EMAIL),
                "Expected RuntimeException when user not found");

        assertEquals(USER_NOT_FOUND_MSG, exception.getMessage());
    }
}