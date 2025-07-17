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

    private static final String TEST_EMAIL = "admin@example.com";
    private static final String TEST_PASSWORD = "password123";
    private static final String HASHED_PASSWORD = "hashedPassword";
    private static final String TEST_FIRST_NAME = "John";
    private static final String TEST_LAST_NAME = "Doe";
    private static final long TEST_USER_ID = 1L;
    private static final String MOCK_TOKEN = "mock-token";

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
        testUser.setId(TEST_USER_ID);
        testUser.setEmail(TEST_EMAIL);
        testUser.setPassword(HASHED_PASSWORD);
        testUser.setFirstName(TEST_FIRST_NAME);
        testUser.setLastName(TEST_LAST_NAME);

        loginRequest = new LoginRequest(TEST_EMAIL, TEST_PASSWORD);
    }

    @Test
    void login_ShouldReturnLoginResponse_WhenCredentialsAreValid() {
        when(userRepository.findByEmail(TEST_EMAIL)).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches(TEST_PASSWORD, HASHED_PASSWORD)).thenReturn(true);
        when(jwtUtil.generateToken(TEST_EMAIL, TEST_USER_ID)).thenReturn(MOCK_TOKEN);

        LoginResponse response = authService.login(loginRequest);

        assertNotNull(response, "Response should not be null");
        assertEquals(MOCK_TOKEN, response.getToken(), "Token should match the mock token");
        assertEquals("Login successful", response.getMessage(), "Message should confirm login success");
        assertNotNull(response.getUser(), "User in response should not be null");
        assertEquals(TEST_EMAIL, response.getUser().getEmail(), "User email should match");
        assertEquals(TEST_FIRST_NAME, response.getUser().getFirstName(), "User first name should match");
        assertEquals(TEST_LAST_NAME, response.getUser().getLastName(), "User last name should match");
    }

    @Test
    void login_ShouldThrowException_WhenEmailIsInvalid() {
        when(userRepository.findByEmail(TEST_EMAIL)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> authService.login(loginRequest),
                "Expected login to throw when email is invalid");

        assertEquals("Invalid email or password", exception.getMessage());
    }

    @Test
    void login_ShouldThrowException_WhenPasswordIsInvalid() {
        when(userRepository.findByEmail(TEST_EMAIL)).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches(TEST_PASSWORD, HASHED_PASSWORD)).thenReturn(false);

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> authService.login(loginRequest),
                "Expected login to throw when password is invalid");

        assertEquals("Invalid email or password", exception.getMessage());
    }

    @Test
    void getUserByEmail_ShouldReturnUserDto_WhenUserExists() {
        when(userRepository.findByEmail(TEST_EMAIL)).thenReturn(Optional.of(testUser));

        UserDto userDto = authService.getUserByEmail(TEST_EMAIL);

        assertNotNull(userDto, "UserDto should not be null");
        assertEquals(TEST_USER_ID, userDto.getId(), "User ID should match");
        assertEquals(TEST_EMAIL, userDto.getEmail(), "User email should match");
        assertEquals(TEST_FIRST_NAME, userDto.getFirstName(), "User first name should match");
        assertEquals(TEST_LAST_NAME, userDto.getLastName(), "User last name should match");
    }

    @Test
    void getUserByEmail_ShouldThrowException_WhenUserNotFound() {
        final String nonExistentEmail = "nonexistent@example.com";
        when(userRepository.findByEmail(nonExistentEmail)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> authService.getUserByEmail(nonExistentEmail),
                "Expected getUserByEmail to throw when user does not exist");

        assertEquals("User not found", exception.getMessage());
    }
}