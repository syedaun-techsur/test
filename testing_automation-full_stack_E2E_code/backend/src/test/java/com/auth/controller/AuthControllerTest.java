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

    private static final String LOGIN_URL = "/api/login";
    private static final String ME_URL = "/api/me";
    private static final String LOGOUT_URL = "/api/logout";
    private static final String AUTH_HEADER = "Authorization";
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
        loginResponse = new LoginResponse("mock-token", userDto, "Login successful");
    }

    @Test
    @DisplayName("Successful login returns token and user info")
    void testLoginSuccess() throws Exception {
        when(authService.login(any(LoginRequest.class))).thenReturn(loginResponse);

        mockMvc.perform(post(LOGIN_URL)
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
    @DisplayName("Login with invalid credentials returns 401 Unauthorized")
    void testLoginWithInvalidCredentials() throws Exception {
        when(authService.login(any(LoginRequest.class)))
                .thenThrow(new RuntimeException("Invalid email or password"));

        mockMvc.perform(post(LOGIN_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validLoginRequest)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.status").value(401))
                .andExpect(jsonPath("$.message").value("Invalid email or password"));
    }

    @Test
    @DisplayName("Login with validation errors returns 400 Bad Request")
    void testLoginWithValidationErrors() throws Exception {
        LoginRequest invalidRequest = new LoginRequest("", "123");

        mockMvc.perform(post(LOGIN_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("Validation failed"));
    }

    @Test
    @DisplayName("Get current user success returns user info")
    void testGetCurrentUserSuccess() throws Exception {
        String token = BEARER_PREFIX + "mock-token";

        when(jwtUtil.validateToken("mock-token")).thenReturn(true);
        when(jwtUtil.getEmailFromToken("mock-token")).thenReturn("admin@example.com");
        when(authService.getUserByEmail("admin@example.com")).thenReturn(userDto);

        mockMvc.perform(get(ME_URL)
                .header(AUTH_HEADER, token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("admin@example.com"))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"));
    }

    @Test
    @DisplayName("Get current user with invalid token returns 401 Unauthorized")
    void testGetCurrentUserWithInvalidToken() throws Exception {
        String token = BEARER_PREFIX + "invalid-token";

        when(jwtUtil.validateToken("invalid-token")).thenReturn(false);

        mockMvc.perform(get(ME_URL)
                .header(AUTH_HEADER, token))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.status").value(401))
                .andExpect(jsonPath("$.message").value("Invalid token"));
    }

    @Test
    @DisplayName("Get current user with missing Authorization header returns 401 Unauthorized")
    void testGetCurrentUserWithMissingAuthHeader() throws Exception {
        mockMvc.perform(get(ME_URL))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.status").value(401))
                .andExpect(jsonPath("$.message").value("Invalid token"));
    }

    @Test
    @DisplayName("Logout returns success message")
    void testLogout() throws Exception {
        mockMvc.perform(post(LOGOUT_URL))
                .andExpect(status().isOk())
                .andExpect(content().string("{\"message\": \"Logout successful\"}"));
    }
}