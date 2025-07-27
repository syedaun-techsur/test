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
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    private static final String EMAIL = "admin@example.com";
    private static final String PASSWORD = "password123";
    private static final String HASHED_PASSWORD = "hashedPassword";
    private static final String INVALID_EMAIL = "nonexistent@example.com";
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
    void login_Success_ReturnsValidResponse() {
        when(userRepository.findByEmail(eq(EMAIL))).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches(eq(PASSWORD), eq(HASHED_PASSWORD))).thenReturn(true);
        when(jwtUtil.generateToken(eq(EMAIL), eq(1L))).thenReturn(TOKEN);

        LoginResponse response = authService.login(loginRequest);

        assertNotNull(response);
        assertEquals(TOKEN, response.getToken());
        assertEquals("Login successful", response.getMessage());
        assertNotNull(response.getUser());
        assertEquals(EMAIL, response.getUser().getEmail());
        assertEquals("John", response.getUser().getFirstName());
        assertEquals("Doe", response.getUser().getLastName());
    }

    @Test
    void login_WithInvalidEmail_ThrowsIllegalArgumentException() {
        when(userRepository.findByEmail(eq(EMAIL))).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            authService.login(loginRequest);
        });

        assertEquals("Invalid email or password", exception.getMessage());
    }

    @Test
    void login_WithInvalidPassword_ThrowsIllegalArgumentException() {
        when(userRepository.findByEmail(eq(EMAIL))).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches(eq(PASSWORD), eq(HASHED_PASSWORD))).thenReturn(false);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            authService.login(loginRequest);
        });

        assertEquals("Invalid email or password", exception.getMessage());
    }

    @Test
    void getUserByEmail_Success_ReturnsUserDto() {
        when(userRepository.findByEmail(eq(EMAIL))).thenReturn(Optional.of(testUser));

        UserDto userDto = authService.getUserByEmail(EMAIL);

        assertNotNull(userDto);
        assertEquals(1L, userDto.getId());
        assertEquals(EMAIL, userDto.getEmail());
        assertEquals("John", userDto.getFirstName());
        assertEquals("Doe", userDto.getLastName());
    }

    @Test
    void getUserByEmail_UserNotFound_ThrowsIllegalArgumentException() {
        when(userRepository.findByEmail(eq(INVALID_EMAIL))).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            authService.getUserByEmail(INVALID_EMAIL);
        });

        assertEquals("User not found", exception.getMessage());
    }
}