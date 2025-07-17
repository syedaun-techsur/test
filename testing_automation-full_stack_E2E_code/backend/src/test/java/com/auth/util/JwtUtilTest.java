package com.auth.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilTest {

    private JwtUtil jwtUtil;

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil();
        ReflectionTestUtils.setField(jwtUtil, "jwtSecret", "mySecretKeyForJWTTokenGenerationAndValidation");
        ReflectionTestUtils.setField(jwtUtil, "jwtExpiration", 86400000); // 24 hours in milliseconds
    }

    // Test token generation for given email and userId
    @Test
    void testGenerateToken() {
        String testEmail = "test@example.com";
        Long testUserId = 1L;

        String token = jwtUtil.generateToken(testEmail, testUserId);

        assertNotNull(token, "Generated token should not be null");
        assertFalse(token.isEmpty(), "Generated token should not be empty");
    }

    // Test email extraction from generated token
    @Test
    void testGetEmailFromToken() {
        String testEmail = "test@example.com";
        Long testUserId = 1L;
        String token = jwtUtil.generateToken(testEmail, testUserId);

        String extractedEmail = jwtUtil.getEmailFromToken(token);

        assertEquals(testEmail, extractedEmail, "Extracted email should match the original email");
    }

    // Test userId extraction from generated token
    @Test
    void testGetUserIdFromToken() {
        String testEmail = "test@example.com";
        Long testUserId = 1L;
        String token = jwtUtil.generateToken(testEmail, testUserId);

        Long extractedUserId = jwtUtil.getUserIdFromToken(token);

        assertEquals(testUserId, extractedUserId, "Extracted userId should match the original userId");
    }

    // Test validation for a valid token
    @Test
    void testValidateTokenValid() {
        String testEmail = "test@example.com";
        Long testUserId = 1L;
        String validToken = jwtUtil.generateToken(testEmail, testUserId);

        boolean isValid = jwtUtil.validateToken(validToken);

        assertTrue(isValid, "Token should be valid");
    }

    // Test validation for an invalid token
    @Test
    void testValidateTokenInvalid() {
        String invalidToken = "invalid.token.here";

        boolean isValid = jwtUtil.validateToken(invalidToken);

        assertFalse(isValid, "Token should be invalid");
    }

    // Test expiration check for a valid non-expired token
    @Test
    void testIsTokenExpiredFalse() {
        String testEmail = "test@example.com";
        Long testUserId = 1L;
        String token = jwtUtil.generateToken(testEmail, testUserId);

        boolean isExpired = jwtUtil.isTokenExpired(token);

        assertFalse(isExpired, "Token should not be expired");
    }

    // Test expiration check for an invalid token (treated as expired)
    @Test
    void testIsTokenExpiredTrue() {
        String invalidToken = "invalid.token.here";

        boolean isExpired = jwtUtil.isTokenExpired(invalidToken);

        assertTrue(isExpired, "Invalid token should be treated as expired");
    }

}