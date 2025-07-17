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
    private static final String TEST_PASSWORD = "password123";
    private static final String HASHED_PASSWORD = "hashedPassword";
    private static final long USER_ID = 1L;

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
        testUser.setId(USER_ID);
        testUser.setEmail(TEST_EMAIL);
        testUser.setPassword(HASHED_PASSWORD);
        testUser.setFirstName("John");
        testUser.setLastName("Doe");

        loginRequest = new LoginRequest(TEST_EMAIL, TEST_PASSWORD);
    }

    @Test
    void login_ShouldReturnLoginResponse_WhenCredentialsAreValid() {
        // Arrange
        when(userRepository.findByEmail(TEST_EMAIL)).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches(TEST_PASSWORD, HASHED_PASSWORD)).thenReturn(true);
        when(jwtUtil.generateToken(TEST_EMAIL, USER_ID)).thenReturn("mock-token");

        // Act
        LoginResponse response = authService.login(loginRequest);

        // Assert
        assertNotNull(response, "LoginResponse should not be null");
        assertEquals("mock-token", response.getToken(), "Tokens should match");
        assertEquals("Login successful", response.getMessage(), "Messages should match");
        assertNotNull(response.getUser(), "UserDto should not be null");
        assertEquals(TEST_EMAIL, response.getUser().getEmail(), "Emails should match");
        assertEquals("John", response.getUser().getFirstName(), "First names should match");
        assertEquals("Doe", response.getUser().getLastName(), "Last names should match");
    }

    @Test
    void login_ShouldThrowException_WhenEmailNotFound() {
        // Arrange
        when(userRepository.findByEmail(TEST_EMAIL)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> authService.login(loginRequest));

        assertEquals("Invalid email or password", exception.getMessage());
    }

    @Test
    void login_ShouldThrowException_WhenPasswordDoesNotMatch() {
        // Arrange
        when(userRepository.findByEmail(TEST_EMAIL)).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches(TEST_PASSWORD, HASHED_PASSWORD)).thenReturn(false);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> authService.login(loginRequest));

        assertEquals("Invalid email or password", exception.getMessage());
    }

    @Test
    void getUserByEmail_ShouldReturnUserDto_WhenUserExists() {
        // Arrange
        when(userRepository.findByEmail(TEST_EMAIL)).thenReturn(Optional.of(testUser));

        // Act
        UserDto userDto = authService.getUserByEmail(TEST_EMAIL);

        // Assert
        assertNotNull(userDto, "UserDto should not be null");
        assertEquals(USER_ID, userDto.getId(), "User IDs should match");
        assertEquals(TEST_EMAIL, userDto.getEmail(), "Emails should match");
        assertEquals("John", userDto.getFirstName(), "First names should match");
        assertEquals("Doe", userDto.getLastName(), "Last names should match");
    }

    @Test
    void getUserByEmail_ShouldThrowException_WhenUserDoesNotExist() {
        // Arrange
        String nonexistentEmail = "nonexistent@example.com";
        when(userRepository.findByEmail(nonexistentEmail)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> authService.getUserByEmail(nonexistentEmail));

        assertEquals("User not found", exception.getMessage());
    }
}