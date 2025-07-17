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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    private static final String INVALID_EMAIL_OR_PASSWORD = "Invalid email or password";
    private static final String USER_NOT_FOUND = "User not found";

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
        testUser.setEmail("admin@example.com");
        testUser.setPassword("hashedPassword");
        testUser.setFirstName("John");
        testUser.setLastName("Doe");

        loginRequest = new LoginRequest("admin@example.com", "password123");
    }

    @Test
    void login_ShouldReturnSuccessResponse_WhenCredentialsAreValid() {
        when(userRepository.findByEmail("admin@example.com")).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches("password123", "hashedPassword")).thenReturn(true);
        when(jwtUtil.generateToken("admin@example.com", 1L)).thenReturn("mock-token");

        LoginResponse response = authService.login(loginRequest);

        assertNotNull(response, "Response should not be null");
        assertEquals("mock-token", response.getToken(), "Token should match expected value");
        assertEquals("Login successful", response.getMessage(), "Message should indicate successful login");
        assertNotNull(response.getUser(), "User in response should not be null");
        assertEquals("admin@example.com", response.getUser().getEmail(), "User email should match");
        assertEquals("John", response.getUser().getFirstName(), "User first name should match");
        assertEquals("Doe", response.getUser().getLastName(), "User last name should match");
    }

    @Test
    void login_ShouldThrowRuntimeException_WhenEmailIsInvalid() {
        when(userRepository.findByEmail("admin@example.com")).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> authService.login(loginRequest));
        assertEquals(INVALID_EMAIL_OR_PASSWORD, exception.getMessage(), "Exception message should indicate invalid credentials");
    }

    @Test
    void login_ShouldThrowRuntimeException_WhenPasswordIsInvalid() {
        when(userRepository.findByEmail("admin@example.com")).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches("password123", "hashedPassword")).thenReturn(false);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> authService.login(loginRequest));
        assertEquals(INVALID_EMAIL_OR_PASSWORD, exception.getMessage(), "Exception message should indicate invalid credentials");
    }

    @Test
    void getUserByEmail_ShouldReturnUserDto_WhenUserExists() {
        when(userRepository.findByEmail("admin@example.com")).thenReturn(Optional.of(testUser));

        UserDto userDto = authService.getUserByEmail("admin@example.com");

        assertNotNull(userDto, "UserDto should not be null");
        assertEquals(1L, userDto.getId(), "UserDto ID should match");
        assertEquals("admin@example.com", userDto.getEmail(), "UserDto email should match");
        assertEquals("John", userDto.getFirstName(), "UserDto first name should match");
        assertEquals("Doe", userDto.getLastName(), "UserDto last name should match");
    }

    @Test
    void getUserByEmail_ShouldThrowRuntimeException_WhenUserNotFound() {
        when(userRepository.findByEmail("nonexistent@example.com")).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> authService.getUserByEmail("nonexistent@example.com"));
        assertEquals(USER_NOT_FOUND, exception.getMessage(), "Exception message should indicate user not found");
    }
}