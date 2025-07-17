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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@WebMvcTest(AuthController.class)
class AuthControllerTest {

    private static final String LOGIN_URL = "/api/auth/login";
    private static final String CURRENT_USER_URL = "/api/auth/me";
    private static final String LOGOUT_URL = "/api/auth/logout";
    private static final String AUTH_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";
    private static final String VALID_EMAIL = "admin@example.com";
    private static final String VALID_PASSWORD = "password123";
    private static final String MOCK_TOKEN = "mock-token";

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
        validLoginRequest = new LoginRequest(VALID_EMAIL, VALID_PASSWORD);
        userDto = new UserDto(1L, VALID_EMAIL, "John", "Doe");
        loginResponse = new LoginResponse(MOCK_TOKEN, userDto, "Login successful");
    }

    @Test
    void testLoginSuccess() throws Exception {
        when(authService.login(any(LoginRequest.class))).thenReturn(loginResponse);

        mockMvc.perform(post(LOGIN_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validLoginRequest)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value(MOCK_TOKEN))
                .andExpect(jsonPath("$.user.email").value(VALID_EMAIL))
                .andExpect(jsonPath("$.user.firstName").value("John"))
                .andExpect(jsonPath("$.user.lastName").value("Doe"))
                .andExpect(jsonPath("$.message").value("Login successful"));
    }

    @Test
    void testLoginWithInvalidCredentials() throws Exception {
        when(authService.login(any(LoginRequest.class)))
                .thenThrow(new RuntimeException("Invalid email or password"));

        mockMvc.perform(post(LOGIN_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validLoginRequest)))
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.status").value(401))
                .andExpect(jsonPath("$.message").value("Invalid email or password"));
    }

    @Test
    void testLoginWithValidationErrors() throws Exception {
        LoginRequest invalidRequest = new LoginRequest("", "123");

        mockMvc.perform(post(LOGIN_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("Validation failed"));
    }

    @Test
    void testGetCurrentUserSuccess() throws Exception {
        String authHeader = BEARER_PREFIX + MOCK_TOKEN;

        when(jwtUtil.validateToken(MOCK_TOKEN)).thenReturn(true);
        when(jwtUtil.getEmailFromToken(MOCK_TOKEN)).thenReturn(VALID_EMAIL);
        when(authService.getUserByEmail(VALID_EMAIL)).thenReturn(userDto);

        mockMvc.perform(get(CURRENT_USER_URL)
                .header(AUTH_HEADER, authHeader))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(VALID_EMAIL))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"));
    }

    @Test
    void testGetCurrentUserWithInvalidToken() throws Exception {
        String invalidToken = "invalid-token";
        String authHeader = BEARER_PREFIX + invalidToken;

        when(jwtUtil.validateToken(invalidToken)).thenReturn(false);

        mockMvc.perform(get(CURRENT_USER_URL)
                .header(AUTH_HEADER, authHeader))
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.status").value(401))
                .andExpect(jsonPath("$.message").value("Invalid token"));
    }

    @Test
    void testLogout() throws Exception {
        mockMvc.perform(post(LOGOUT_URL))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json("{\"message\": \"Logout successful\"}"));
    }
}