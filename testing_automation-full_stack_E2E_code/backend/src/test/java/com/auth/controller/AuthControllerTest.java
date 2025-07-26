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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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

    @BeforeEach
    void setUp() {
        validLoginRequest = new LoginRequest("admin@example.com", "password123");
        userDto = new UserDto(1L, "admin@example.com", "John", "Doe");
        loginResponse = new LoginResponse("mock-token", userDto, "Login successful");
    }

    @Test
    @DisplayName("Login success returns valid token and user info")
    void testLoginSuccess() throws Exception {
        when(authService.login(any(LoginRequest.class))).thenReturn(loginResponse);

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validLoginRequest)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.token").value("mock-token"))
            .andExpect(jsonPath("$.user.email").value("admin@example.com"))
            .andExpect(jsonPath("$.user.firstName").value("John"))
            .andExpect(jsonPath("$.user.lastName").value("Doe"))
            .andExpect(jsonPath("$.message").value("Login successful"));
    }

    @Test
    @DisplayName("Login with invalid credentials returns Unauthorized with error message")
    void testLoginWithInvalidCredentials() throws Exception {
        when(authService.login(any(LoginRequest.class)))
            .thenThrow(new RuntimeException("Invalid email or password"));

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validLoginRequest)))
            .andExpect(status().isUnauthorized())
            .andExpect(jsonPath("$.status").value(401))
            .andExpect(jsonPath("$.message").value("Invalid email or password"));
    }

    @Test
    @DisplayName("Login with validation errors returns Bad Request with validation message")
    void testLoginWithValidationErrors() throws Exception {
        LoginRequest invalidRequest = new LoginRequest("", "123");

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
            .andExpect(status().isBadRequest())
            // Assuming standard Spring validation response structure with status and message fields
            .andExpect(jsonPath("$.status").value(400))
            .andExpect(jsonPath("$.message").value("Validation failed"));
    }

    @Test
    @DisplayName("Get current user returns user details for valid token")
    void testGetCurrentUserSuccess() throws Exception {
        final String rawToken = "mock-token";
        final String authHeader = "Bearer " + rawToken;

        when(jwtUtil.validateToken(rawToken)).thenReturn(true);
        when(jwtUtil.getEmailFromToken(rawToken)).thenReturn("admin@example.com");
        when(authService.getUserByEmail("admin@example.com")).thenReturn(userDto);

        mockMvc.perform(get("/api/auth/me")
                .header("Authorization", authHeader))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.email").value("admin@example.com"))
            .andExpect(jsonPath("$.firstName").value("John"))
            .andExpect(jsonPath("$.lastName").value("Doe"));
    }

    @Test
    @DisplayName("Get current user with invalid token returns Unauthorized")
    void testGetCurrentUserWithInvalidToken() throws Exception {
        final String rawToken = "invalid-token";
        final String authHeader = "Bearer " + rawToken;

        when(jwtUtil.validateToken(rawToken)).thenReturn(false);

        mockMvc.perform(get("/api/auth/me")
                .header("Authorization", authHeader))
            .andExpect(status().isUnauthorized())
            .andExpect(jsonPath("$.status").value(401))
            .andExpect(jsonPath("$.message").value("Invalid token"));
    }

    @Test
    @DisplayName("Logout returns success message")
    void testLogout() throws Exception {
        mockMvc.perform(post("/api/auth/logout"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.message").value("Logout successful"));
    }
}