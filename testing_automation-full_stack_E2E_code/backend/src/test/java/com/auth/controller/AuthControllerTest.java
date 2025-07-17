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
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import javax.naming.AuthenticationException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
class AuthControllerTest {

    private static final String LOGIN_URL = "/api/auth/login";
    private static final String CURRENT_USER_URL = "/api/auth/me";
    private static final String LOGOUT_URL = "/api/auth/logout";
    private static final String MOCK_TOKEN = "mock-token";
    private static final String BEARER_PREFIX = "Bearer ";

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

    @BeforeEach
    void setUp() {
        validLoginRequest = new LoginRequest("admin@example.com", "password123");
        userDto = new UserDto(1L, "admin@example.com", "John", "Doe");
        loginResponse = new LoginResponse(MOCK_TOKEN, userDto, "Login successful");
    }

    @Test
    @DisplayName("Test successful login returns valid token and user info")
    void testLoginSuccess() throws Exception {
        when(authService.login(any(LoginRequest.class))).thenReturn(loginResponse);

        mockMvc.perform(post(LOGIN_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validLoginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value(MOCK_TOKEN))
                .andExpect(jsonPath("$.user.email").value("admin@example.com"))
                .andExpect(jsonPath("$.user.firstName").value("John"))
                .andExpect(jsonPath("$.user.lastName").value("Doe"))
                .andExpect(jsonPath("$.message").value("Login successful"));
    }

    @Test
    @DisplayName("Test login with invalid credentials returns 401 unauthorized")
    void testLoginWithInvalidCredentials() throws Exception {
        when(authService.login(any(LoginRequest.class)))
                .thenThrow(new AuthenticationException("Invalid email or password"));

        mockMvc.perform(post(LOGIN_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validLoginRequest)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.status").value(HttpStatus.UNAUTHORIZED.value()))
                .andExpect(jsonPath("$.message").value("Invalid email or password"));
    }

    @Test
    @DisplayName("Test login fails with validation errors returns 400 bad request")
    void testLoginWithValidationErrors() throws Exception {
        // An invalid request with empty email and short password - assumed backend validation constraints on DTO
        LoginRequest invalidRequest = new LoginRequest("", "123");

        mockMvc.perform(post(LOGIN_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.message").value("Validation failed"));
    }

    @Test
    @DisplayName("Test get current user returns correct user info with valid token")
    void testGetCurrentUserSuccess() throws Exception {
        final String authHeader = BEARER_PREFIX + MOCK_TOKEN;

        when(jwtUtil.validateToken(MOCK_TOKEN)).thenReturn(true);
        when(jwtUtil.getEmailFromToken(MOCK_TOKEN)).thenReturn("admin@example.com");
        when(authService.getUserByEmail("admin@example.com")).thenReturn(userDto);

        mockMvc.perform(get(CURRENT_USER_URL)
                .header("Authorization", authHeader))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("admin@example.com"))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"));
    }

    @Test
    @DisplayName("Test get current user with invalid token returns 401 unauthorized")
    void testGetCurrentUserWithInvalidToken() throws Exception {
        final String invalidToken = "invalid-token";
        final String authHeader = BEARER_PREFIX + invalidToken;

        when(jwtUtil.validateToken(invalidToken)).thenReturn(false);

        mockMvc.perform(get(CURRENT_USER_URL)
                .header("Authorization", authHeader))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.status").value(HttpStatus.UNAUTHORIZED.value()))
                .andExpect(jsonPath("$.message").value("Invalid token"));
    }

    @Test
    @DisplayName("Test logout returns successful message")
    void testLogout() throws Exception {
        mockMvc.perform(post(LOGOUT_URL))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"message\": \"Logout successful\"}"));
    }
}