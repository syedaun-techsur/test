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
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

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
    void testLoginSuccess() {
        when(userRepository.findByEmail(eq("admin@example.com"))).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches(eq("password123"), eq("hashedPassword"))).thenReturn(true);
        when(jwtUtil.generateToken(eq("admin@example.com"), eq(1L))).thenReturn("mock-token");

        LoginResponse response = authService.login(loginRequest);

        assertNotNull(response, "Response should not be null");
        assertEquals("mock-token", response.getToken(), "Token should match the mock token");
        assertEquals("Login successful", response.getMessage(), "Message should indicate successful login");
        assertNotNull(response.getUser(), "User in response should not be null");
        assertEquals("admin@example.com", response.getUser().getEmail(), "Email should match the login email");
        assertEquals("John", response.getUser().getFirstName(), "First name should match");
        assertEquals("Doe", response.getUser().getLastName(), "Last name should match");
    }

    @Test
    void testLoginWithInvalidEmail() {
        when(userRepository.findByEmail(eq("admin@example.com"))).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            authService.login(loginRequest);
        }, "Expected RuntimeException for invalid email");

        assertEquals("Invalid email or password", exception.getMessage(), "Exception message should be for invalid credentials");
    }

    @Test
    void testLoginWithInvalidPassword() {
        when(userRepository.findByEmail(eq("admin@example.com"))).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches(eq("password123"), eq("hashedPassword"))).thenReturn(false);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            authService.login(loginRequest);
        }, "Expected RuntimeException for invalid password");

        assertEquals("Invalid email or password", exception.getMessage(), "Exception message should be for invalid credentials");
    }

    @Test
    void testGetUserByEmailSuccess() {
        when(userRepository.findByEmail(eq("admin@example.com"))).thenReturn(Optional.of(testUser));

        UserDto userDto = authService.getUserByEmail("admin@example.com");

        assertNotNull(userDto, "UserDto should not be null");
        assertEquals(1L, userDto.getId(), "User ID should match");
        assertEquals("admin@example.com", userDto.getEmail(), "Email should match");
        assertEquals("John", userDto.getFirstName(), "First name should match");
        assertEquals("Doe", userDto.getLastName(), "Last name should match");
    }

    @Test
    void testGetUserByEmailNotFound() {
        when(userRepository.findByEmail(eq("nonexistent@example.com"))).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            authService.getUserByEmail("nonexistent@example.com");
        }, "Expected RuntimeException for user not found");

        assertEquals("User not found", exception.getMessage(), "Exception message should indicate user not found");
    }
}