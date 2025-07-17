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
    void login_Success() {
        when(userRepository.findByEmail("admin@example.com")).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches("password123", "hashedPassword")).thenReturn(true);
        when(jwtUtil.generateToken("admin@example.com", 1L)).thenReturn("mock-token");

        LoginResponse response = authService.login(loginRequest);

        assertNotNull(response, "Response should not be null");
        assertEquals("mock-token", response.getToken(), "Token mismatch");
        assertEquals("Login successful", response.getMessage(), "Message mismatch");
        assertNotNull(response.getUser(), "User should not be null");
        assertEquals("admin@example.com", response.getUser().getEmail(), "User email mismatch");
        assertEquals("John", response.getUser().getFirstName(), "User firstName mismatch");
        assertEquals("Doe", response.getUser().getLastName(), "User lastName mismatch");
    }

    @Test
    void login_WithInvalidEmail_ThrowsException() {
        when(userRepository.findByEmail("admin@example.com")).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            authService.login(loginRequest);
        });

        assertEquals("Invalid email or password", exception.getMessage(), "Exception message mismatch");
    }

    @Test
    void login_WithInvalidPassword_ThrowsException() {
        when(userRepository.findByEmail("admin@example.com")).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches("password123", "hashedPassword")).thenReturn(false);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            authService.login(loginRequest);
        });

        assertEquals("Invalid email or password", exception.getMessage(), "Exception message mismatch");
    }

    @Test
    void getUserByEmail_Success() {
        when(userRepository.findByEmail("admin@example.com")).thenReturn(Optional.of(testUser));

        UserDto userDto = authService.getUserByEmail("admin@example.com");

        assertNotNull(userDto, "UserDto should not be null");
        assertEquals(1L, userDto.getId(), "User ID mismatch");
        assertEquals("admin@example.com", userDto.getEmail(), "Email mismatch");
        assertEquals("John", userDto.getFirstName(), "FirstName mismatch");
        assertEquals("Doe", userDto.getLastName(), "LastName mismatch");
    }

    @Test
    void getUserByEmail_NotFound_ThrowsException() {
        when(userRepository.findByEmail("nonexistent@example.com")).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            authService.getUserByEmail("nonexistent@example.com");
        });

        assertEquals("User not found", exception.getMessage(), "Exception message mismatch");
    }
}