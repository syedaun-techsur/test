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
        testUser.setPassword("hashedPassword");
        testUser.setFirstName("John");
        testUser.setLastName("Doe");

        loginRequest = new LoginRequest(TEST_EMAIL, "password123");
    }

    @Test
    void login_Success_ReturnsValidResponse() {
        when(userRepository.findByEmail(TEST_EMAIL)).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches("password123", "hashedPassword")).thenReturn(true);
        when(jwtUtil.generateToken(TEST_EMAIL, 1L)).thenReturn("mock-token");

        LoginResponse response = authService.login(loginRequest);

        assertNotNull(response, "Response should not be null");
        assertEquals("mock-token", response.getToken(), "Token should match the mocked token");
        assertEquals("Login successful", response.getMessage(), "Message should indicate successful login");
        assertNotNull(response.getUser(), "User in response should not be null");

        assertEquals(TEST_EMAIL, response.getUser().getEmail(), "User email should match");
        assertEquals("John", response.getUser().getFirstName(), "User first name should match");
        assertEquals("Doe", response.getUser().getLastName(), "User last name should match");
    }

    @Test
    void login_InvalidEmail_ThrowsException() {
        when(userRepository.findByEmail(TEST_EMAIL)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
            () -> authService.login(loginRequest),
            "Expected login to throw RuntimeException for invalid email"
        );
        assertEquals("Invalid email or password", exception.getMessage());
    }

    @Test
    void login_InvalidPassword_ThrowsException() {
        when(userRepository.findByEmail(TEST_EMAIL)).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches("password123", "hashedPassword")).thenReturn(false);

        RuntimeException exception = assertThrows(RuntimeException.class,
            () -> authService.login(loginRequest),
            "Expected login to throw RuntimeException for invalid password"
        );
        assertEquals("Invalid email or password", exception.getMessage());
    }

    @Test
    void getUserByEmail_ExistingUser_ReturnsUserDto() {
        when(userRepository.findByEmail(TEST_EMAIL)).thenReturn(Optional.of(testUser));

        UserDto userDto = authService.getUserByEmail(TEST_EMAIL);

        assertNotNull(userDto, "UserDto should not be null");
        assertEquals(1L, userDto.getId(), "User ID should match");
        assertEquals(TEST_EMAIL, userDto.getEmail(), "User email should match");
        assertEquals("John", userDto.getFirstName(), "User first name should match");
        assertEquals("Doe", userDto.getLastName(), "User last name should match");
    }

    @Test
    void getUserByEmail_NonexistentUser_ThrowsException() {
        String nonExistentEmail = "nonexistent@example.com";
        when(userRepository.findByEmail(nonExistentEmail)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
            () -> authService.getUserByEmail(nonExistentEmail),
            "Expected getUserByEmail to throw RuntimeException when user not found"
        );
        assertEquals("User not found", exception.getMessage());
    }
}