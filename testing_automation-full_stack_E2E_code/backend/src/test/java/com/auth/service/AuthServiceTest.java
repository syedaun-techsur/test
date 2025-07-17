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
        // Setup test user and login request
        testUser = new User();
        testUser.setId(1L);
        testUser.setEmail("admin@example.com");
        testUser.setPassword("hashedPassword");
        testUser.setFirstName("John");
        testUser.setLastName("Doe");

        loginRequest = new LoginRequest("admin@example.com", "password123");
    }

    @Test
    @DisplayName("Login succeeds with valid credentials")
    void loginSuccess() {
        when(userRepository.findByEmail("admin@example.com")).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches("password123", "hashedPassword")).thenReturn(true);
        when(jwtUtil.generateToken("admin@example.com", 1L)).thenReturn("mock-token");

        LoginResponse response = authService.login(loginRequest);

        assertNotNull(response);
        assertEquals("mock-token", response.getToken());
        assertEquals("Login successful", response.getMessage());
        assertNotNull(response.getUser());
        assertEquals("admin@example.com", response.getUser().getEmail());
        assertEquals("John", response.getUser().getFirstName());
        assertEquals("Doe", response.getUser().getLastName());
    }

    @Test
    @DisplayName("Login fails with invalid email")
    void loginFailsWithInvalidEmail() {
        when(userRepository.findByEmail("admin@example.com")).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            authService.login(loginRequest);
        });

        assertEquals("Invalid email or password", exception.getMessage());
    }

    @Test
    @DisplayName("Login fails with invalid password")
    void loginFailsWithInvalidPassword() {
        when(userRepository.findByEmail("admin@example.com")).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches("password123", "hashedPassword")).thenReturn(false);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            authService.login(loginRequest);
        });

        assertEquals("Invalid email or password", exception.getMessage());
    }

    @Test
    @DisplayName("Get user by email succeeds when user exists")
    void getUserByEmailSuccess() {
        when(userRepository.findByEmail("admin@example.com")).thenReturn(Optional.of(testUser));

        UserDto userDto = authService.getUserByEmail("admin@example.com");

        assertNotNull(userDto);
        assertEquals(1L, userDto.getId());
        assertEquals("admin@example.com", userDto.getEmail());
        assertEquals("John", userDto.getFirstName());
        assertEquals("Doe", userDto.getLastName());
    }

    @Test
    @DisplayName("Get user by email fails when user not found")
    void getUserByEmailNotFound() {
        when(userRepository.findByEmail("nonexistent@example.com")).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            authService.getUserByEmail("nonexistent@example.com");
        });

        assertEquals("User not found", exception.getMessage());
    }
}