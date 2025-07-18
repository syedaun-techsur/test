package com.auth.controller;

import com.auth.dto.LoginRequest;
import com.auth.dto.LoginResponse;
import com.auth.dto.UserDto;
import com.auth.service.AuthService;
import com.auth.util.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
class AuthControllerTest {

    private static final String API_AUTH_LOGIN = "/api/auth/login";
    private static final String API_AUTH_ME = "/api/auth/me";
    private static final String API_AUTH_LOGOUT = "/api/auth/logout";

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
    void testLoginSuccess() throws Exception {
        // Simulate successful login
        when(authService.login(any(LoginRequest.class))).thenReturn(loginResponse);

        mockMvc.perform(post(API_AUTH_LOGIN)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validLoginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token", is("mock-token")))
                .andExpect(jsonPath("$.user.email", is("admin@example.com")))
                .andExpect(jsonPath("$.user.firstName", is("John")))
                .andExpect(jsonPath("$.user.lastName", is("Doe")))
                .andExpect(jsonPath("$.message", is("Login successful")));
    }

    @Test
    void testLoginWithInvalidCredentials() throws Exception {
        // Simulate login failure due to invalid credentials throwing exception in service.
        when(authService.login(any(LoginRequest.class)))
                .thenThrow(new RuntimeException("Invalid email or password"));

        // Expect unauthorized status and proper JSON error response.
        mockMvc.perform(post(API_AUTH_LOGIN)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validLoginRequest)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.status", is(401)))
                .andExpect(jsonPath("$.message", is("Invalid email or password")));
    }

    @Test
    void testLoginWithValidationErrors() throws Exception {
        // Login request with invalid fields (empty email, short password)
        LoginRequest invalidRequest = new LoginRequest("", "123");

        // Expect bad request status and validation failure message from controller
        mockMvc.perform(post(API_AUTH_LOGIN)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.message", is("Validation failed")));
    }

    @Test
    void testGetCurrentUserSuccess() throws Exception {
        final String tokenBearer = "Bearer mock-token";
        final String extractedToken = "mock-token";

        when(jwtUtil.validateToken(extractedToken)).thenReturn(true);
        when(jwtUtil.getEmailFromToken(extractedToken)).thenReturn("admin@example.com");
        when(authService.getUserByEmail("admin@example.com")).thenReturn(userDto);

        mockMvc.perform(get(API_AUTH_ME)
                .header("Authorization", tokenBearer))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email", is("admin@example.com")))
                .andExpect(jsonPath("$.firstName", is("John")))
                .andExpect(jsonPath("$.lastName", is("Doe")));
    }

    @Test
    void testGetCurrentUserWithInvalidToken() throws Exception {
        final String tokenBearer = "Bearer invalid-token";
        final String extractedToken = "invalid-token";

        when(jwtUtil.validateToken(extractedToken)).thenReturn(false);

        mockMvc.perform(get(API_AUTH_ME)
                .header("Authorization", tokenBearer))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.status", is(401)))
                .andExpect(jsonPath("$.message", is("Invalid token")));
    }

    @Test
    void testLogout() throws Exception {
        // Test logout returns success message as JSON string
        mockMvc.perform(post(API_AUTH_LOGOUT))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"message\": \"Logout successful\"}"));
    }
}