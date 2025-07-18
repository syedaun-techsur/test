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
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    private static final String TEST_EMAIL = "admin@example.com";
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
    @DisplayName("Successful login returns valid token and user data")
    void testLoginSuccess() {
        when(userRepository.findByEmail(TEST_EMAIL)).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches(TEST_PASSWORD, HASHED_PASSWORD)).thenReturn(true);
        when(jwtUtil.generateToken(TEST_EMAIL, 1L)).thenReturn("mock-token");

        LoginResponse response = authService.login(loginRequest);

        assertNotNull(response);
        assertEquals("mock-token", response.getToken());
        assertEquals("Login successful", response.getMessage());
        assertNotNull(response.getUser());
        assertEquals(TEST_EMAIL, response.getUser().getEmail());
        assertEquals("John", response.getUser().getFirstName());
        assertEquals("Doe", response.getUser().getLastName());
    }

    @Test
    @DisplayName("Login fails with invalid email")
    void testLoginWithInvalidEmail() {
        when(userRepository.findByEmail(TEST_EMAIL)).thenReturn(Optional.empty());

        Executable executable = () -> authService.login(loginRequest);
        AuthService.AuthenticationException exception = assertThrows(AuthService.AuthenticationException.class, executable);
        assertEquals("Invalid email or password", exception.getMessage());
    }

    @Test
    @DisplayName("Login fails with invalid password")
    void testLoginWithInvalidPassword() {
        when(userRepository.findByEmail(TEST_EMAIL)).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches(TEST_PASSWORD, HASHED_PASSWORD)).thenReturn(false);

        Executable executable = () -> authService.login(loginRequest);
        AuthService.AuthenticationException exception = assertThrows(AuthService.AuthenticationException.class, executable);
        assertEquals("Invalid email or password", exception.getMessage());
    }

    @Test
    @DisplayName("Get user by email returns user data successfully")
    void testGetUserByEmailSuccess() {
        when(userRepository.findByEmail(TEST_EMAIL)).thenReturn(Optional.of(testUser));

        UserDto userDto = authService.getUserByEmail(TEST_EMAIL);

        assertNotNull(userDto);
        assertEquals(1L, userDto.getId());
        assertEquals(TEST_EMAIL, userDto.getEmail());
        assertEquals("John", userDto.getFirstName());
        assertEquals("Doe", userDto.getLastName());
    }

    @Test
    @DisplayName("Get user by email throws UserNotFoundException when user not found")
    void testGetUserByEmailNotFound() {
        String missingEmail = "nonexistent@example.com";
        when(userRepository.findByEmail(missingEmail)).thenReturn(Optional.empty());

        Executable executable = () -> authService.getUserByEmail(missingEmail);
        AuthService.UserNotFoundException exception = assertThrows(AuthService.UserNotFoundException.class, executable);

        assertEquals("User not found with email: " + missingEmail, exception.getMessage());
    }
}