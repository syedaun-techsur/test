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

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

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

    // Using a nested static class to leverage validation annotations
    static class ValidatedLoginRequest extends LoginRequest {
        @NotBlank(message = "Email is required")
        @Email(message = "Please provide a valid email address")
        private String email;

        @NotBlank(message = "Password is required")
        @Size(min = 6, message = "Password must be at least 6 characters")
        private String password;

        public ValidatedLoginRequest() {
            super();
        }

        public ValidatedLoginRequest(String email, String password) {
            super(email, password);
            this.email = email;
            this.password = password;
        }
    }

    private ValidatedLoginRequest validLoginRequest;
    private LoginResponse loginResponse;
    private UserDto userDto;

    @BeforeEach
    void setUp() {
        validLoginRequest = new ValidatedLoginRequest("admin@example.com", "password123");
        userDto = new UserDto(1L, "admin@example.com", "John", "Doe");
        loginResponse = new LoginResponse("mock-token", userDto, "Login successful");
    }

    @Test
    void testLoginSuccess() throws Exception {
        when(authService.login(any(LoginRequest.class))).thenReturn(loginResponse);

        mockMvc.perform(post("/api/login")
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
    void testLoginWithInvalidCredentials() throws Exception {
        when(authService.login(any(LoginRequest.class)))
                .thenThrow(new RuntimeException("Invalid email or password"));

        mockMvc.perform(post("/api/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validLoginRequest)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.status").value(401))
                .andExpect(jsonPath("$.message").value("Authentication failed: Invalid email or password"));
    }

    @Test
    void testLoginWithValidationErrors() throws Exception {
        ValidatedLoginRequest invalidRequest = new ValidatedLoginRequest("", "123");

        mockMvc.perform(post("/api/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("Validation failed"));
    }

    @Test
    void testGetCurrentUserSuccess() throws Exception {
        String token = "Bearer mock-token";

        when(jwtUtil.validateToken("mock-token")).thenReturn(true);
        when(jwtUtil.getEmailFromToken("mock-token")).thenReturn("admin@example.com");
        when(authService.getUserByEmail("admin@example.com")).thenReturn(userDto);

        mockMvc.perform(get("/api/me")
                .header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("admin@example.com"))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"));
    }

    @Test
    void testGetCurrentUserWithInvalidToken() throws Exception {
        String token = "Bearer invalid-token";

        when(jwtUtil.validateToken("invalid-token")).thenReturn(false);

        // The response message in controller is "Invalid or expired token"
        mockMvc.perform(get("/api/me")
                .header("Authorization", token))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.status").value(401))
                .andExpect(jsonPath("$.message").value("Invalid or expired token"));
    }

    @Test
    void testLogout() throws Exception {
        mockMvc.perform(post("/api/logout"))
                .andExpect(status().isOk())
                .andExpect(content().string("{\"message\": \"Logout successful\"}"));
    }
}