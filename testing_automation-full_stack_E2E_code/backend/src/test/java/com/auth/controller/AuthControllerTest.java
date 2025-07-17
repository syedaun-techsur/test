package com.auth.controller;

import com.auth.dto.LoginRequest;
import com.auth.dto.LoginResponse;
import com.auth.dto.UserDto;
import com.auth.service.AuthService;
import com.auth.util.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;

    @MockBean
    private JwtUtil jwtUtil;

    @Autowired
    private ObjectMapper objectMapper;

    private LoginRequest validLoginRequest;
    private LoginResponse loginResponse;
    private UserDto userDto;

    private static final String VALID_TOKEN = "mock-token";
    private static final String BEARER_PREFIX = "Bearer ";
    private static final String LOGIN_SUCCESS_MESSAGE = "Login successful";
    private static final String LOGOUT_SUCCESS_MESSAGE = "{\"message\": \"Logout successful\"}";
    private static final String INVALID_CREDENTIALS_MSG = "Invalid email or password";
    private static final String INVALID_TOKEN_MSG = "Invalid token";
    private static final String VALIDATION_FAILURE_MSG = "Validation failed";

    @BeforeEach
    void setUp() {
        validLoginRequest = new LoginRequest("admin@example.com", "password123");
        userDto = new UserDto(1L, "admin@example.com", "John", "Doe");
        loginResponse = new LoginResponse(VALID_TOKEN, userDto, LOGIN_SUCCESS_MESSAGE);
    }

    @Test
    @DisplayName("Login succeeds with valid credentials")
    void testLoginSuccess() throws Exception {
        when(authService.login(any(LoginRequest.class))).thenReturn(loginResponse);

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validLoginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value(VALID_TOKEN))
                .andExpect(jsonPath("$.user.email").value("admin@example.com"))
                .andExpect(jsonPath("$.user.firstName").value("John"))
                .andExpect(jsonPath("$.user.lastName").value("Doe"))
                .andExpect(jsonPath("$.message").value(LOGIN_SUCCESS_MESSAGE));
    }

    @Test
    @DisplayName("Login fails with invalid credentials")
    void testLoginWithInvalidCredentials() throws Exception {
        when(authService.login(any(LoginRequest.class)))
                .thenThrow(new RuntimeException(INVALID_CREDENTIALS_MSG));

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validLoginRequest)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.status").value(401))
                .andExpect(jsonPath("$.message").value(INVALID_CREDENTIALS_MSG));
    }

    @Test
    @DisplayName("Login fails with validation errors")
    void testLoginWithValidationErrors() throws Exception {
        // Using invalid email format and short password to simulate validation errors
        LoginRequest invalidRequest = new LoginRequest("invalid-email", "12");

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value(VALIDATION_FAILURE_MSG));
    }

    @Test
    @DisplayName("Get current user succeeds with valid token")
    void testGetCurrentUserSuccess() throws Exception {
        String authHeader = BEARER_PREFIX + VALID_TOKEN;

        when(jwtUtil.validateToken(VALID_TOKEN)).thenReturn(true);
        when(jwtUtil.getEmailFromToken(VALID_TOKEN)).thenReturn("admin@example.com");
        when(authService.getUserByEmail("admin@example.com")).thenReturn(userDto);

        mockMvc.perform(get("/api/auth/me")
                .header("Authorization", authHeader))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("admin@example.com"))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"));
    }

    @Test
    @DisplayName("Get current user fails with invalid token")
    void testGetCurrentUserWithInvalidToken() throws Exception {
        String invalidAuthHeader = BEARER_PREFIX + "invalid-token";

        when(jwtUtil.validateToken("invalid-token")).thenReturn(false);

        mockMvc.perform(get("/api/auth/me")
                .header("Authorization", invalidAuthHeader))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.status").value(401))
                .andExpect(jsonPath("$.message").value(INVALID_TOKEN_MSG));
    }

    @Test
    @DisplayName("Logout returns success message")
    void testLogout() throws Exception {
        mockMvc.perform(post("/api/auth/logout"))
                .andExpect(status().isOk())
                .andExpect(content().string(LOGOUT_SUCCESS_MESSAGE));
    }
}