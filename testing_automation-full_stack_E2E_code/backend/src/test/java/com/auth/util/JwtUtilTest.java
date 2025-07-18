package com.auth.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilTest {

    private JwtUtil jwtUtil;

    // Use a valid Base64 encoded key with length >= 64 bytes (512 bits) for HS512
    private static final String VALID_BASE64_SECRET = "dGhpc2lzdmFsaWRiYXNlNjRrZXl0byBzZWN1cmVqdzY4Ynl0ZXNlY2NyZXRrZXk=";

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil();
        // Reflection is used to inject private fields for testing purpose
        ReflectionTestUtils.setField(jwtUtil, "jwtSecret", VALID_BASE64_SECRET);
        ReflectionTestUtils.setField(jwtUtil, "jwtExpiration", 86400000L); // 24 hours
    }

    private String generateSampleToken() {
        return jwtUtil.generateToken("test@example.com", 1L);
    }

    @Test
    @DisplayName("Generate token should return non-null and non-empty string")
    void generateTokenShouldReturnValidToken() {
        String token = generateSampleToken();
        assertNotNull(token, "Token should not be null");
        assertFalse(token.isEmpty(), "Token should not be empty");
    }

    @Test
    @DisplayName("Extract email from token should return the correct email")
    void getEmailFromTokenShouldReturnCorrectEmail() {
        String email = "test@example.com";
        String token = jwtUtil.generateToken(email, 1L);

        String extractedEmail = jwtUtil.getEmailFromToken(token);

        assertNotNull(extractedEmail, "Extracted email should not be null");
        assertEquals(email, extractedEmail, "Extracted email should match input email");
    }

    @Test
    @DisplayName("Extract userId from token should return the correct user ID")
    void getUserIdFromTokenShouldReturnCorrectUserId() {
        Long userId = 1L;
        String token = jwtUtil.generateToken("test@example.com", userId);

        Long extractedUserId = jwtUtil.getUserIdFromToken(token);

        assertNotNull(extractedUserId, "Extracted user ID should not be null");
        assertEquals(userId, extractedUserId, "Extracted user ID should match input user ID");
    }

    @Test
    @DisplayName("Validate token should return true for valid token")
    void validateTokenShouldReturnTrueForValidToken() {
        String token = generateSampleToken();

        boolean isValid = jwtUtil.validateToken(token);

        assertTrue(isValid, "Valid token should return true on validate");
    }

    @Test
    @DisplayName("Validate token should return false for invalid token")
    void validateTokenShouldReturnFalseForInvalidToken() {
        String invalidToken = "invalid.token.here";

        boolean isValid = jwtUtil.validateToken(invalidToken);

        assertFalse(isValid, "Invalid token should return false on validate");
    }

    @Test
    @DisplayName("isTokenExpired should return false for valid non-expired token")
    void isTokenExpiredShouldReturnFalseForValidToken() {
        String token = generateSampleToken();

        boolean isExpired = jwtUtil.isTokenExpired(token);

        assertFalse(isExpired, "Valid non-expired token should return false for isTokenExpired");
    }

    @Test
    @DisplayName("isTokenExpired should return true for invalid token")
    void isTokenExpiredShouldReturnTrueForInvalidToken() {
        String invalidToken = "invalid.token.here";

        boolean isExpired = jwtUtil.isTokenExpired(invalidToken);

        assertTrue(isExpired, "Invalid token should return true for isTokenExpired");
    }
}