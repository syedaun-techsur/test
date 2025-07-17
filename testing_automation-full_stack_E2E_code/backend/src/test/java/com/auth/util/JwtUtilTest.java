package com.auth.util;

import com.auth.util.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilTest {

    private JwtUtil jwtUtil;

    private static final String TEST_EMAIL = "test@example.com";
    private static final Long TEST_USER_ID = 1L;
    private static final long EXPIRATION_TIME = 86400000L; // 24 hours

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil();
        ReflectionTestUtils.setField(jwtUtil, "jwtSecret", "mySecretKeyForJWTTokenGenerationAndValidation");
        ReflectionTestUtils.setField(jwtUtil, "jwtExpiration", EXPIRATION_TIME);
    }

    @Test
    @DisplayName("Generate token should return a non-null, non-empty token")
    void testGenerateToken() {
        String token = jwtUtil.generateToken(TEST_EMAIL, TEST_USER_ID);
        assertNotNull(token, "Token should not be null");
        assertFalse(token.isEmpty(), "Token should not be empty");
    }

    @Test
    @DisplayName("Extract email from token should return correct email")
    void testGetEmailFromToken() {
        String token = jwtUtil.generateToken(TEST_EMAIL, TEST_USER_ID);
        String extractedEmail = jwtUtil.getEmailFromToken(token);
        assertEquals(TEST_EMAIL, extractedEmail, "Extracted email should match the original");
    }

    @Test
    @DisplayName("Extract user ID from token should return correct user ID")
    void testGetUserIdFromToken() {
        String token = jwtUtil.generateToken(TEST_EMAIL, TEST_USER_ID);
        Long extractedUserId = jwtUtil.getUserIdFromToken(token);
        assertEquals(TEST_USER_ID, extractedUserId, "Extracted user ID should match the original");
    }

    @Test
    @DisplayName("Validate token should return true for valid token")
    void testValidateTokenValid() {
        String token = jwtUtil.generateToken(TEST_EMAIL, TEST_USER_ID);
        assertTrue(jwtUtil.validateToken(token), "Valid token should be validated successfully");
    }

    @Test
    @DisplayName("Validate token should return false for invalid token")
    void testValidateTokenInvalid() {
        String invalidToken = "invalid.token.here";
        assertFalse(jwtUtil.validateToken(invalidToken), "Invalid token should not be validated");
    }

    @Test
    @DisplayName("Token expiration check should return false for a freshly generated token")
    void testIsTokenExpiredFalse() {
        String token = jwtUtil.generateToken(TEST_EMAIL, TEST_USER_ID);
        assertFalse(jwtUtil.isTokenExpired(token), "Token should not be expired immediately after generation");
    }

    @Test
    @DisplayName("Token expiration check should return true for an invalid token")
    void testIsTokenExpiredTrue() {
        String invalidToken = "invalid.token.here";
        // Since the token is invalid, it should be considered expired or invalid
        assertTrue(jwtUtil.isTokenExpired(invalidToken), "Invalid token should be considered expired");
    }
}