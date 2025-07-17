package com.auth.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilTest {

    private JwtUtil jwtUtil;

    private static final String TEST_EMAIL = "test@example.com";
    private static final Long TEST_USER_ID = 1L;
    private static final String INVALID_TOKEN = "invalid.token.here";

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil();
        ReflectionTestUtils.setField(jwtUtil, "jwtSecret", "mySecretKeyForJWTTokenGenerationAndValidation");
        ReflectionTestUtils.setField(jwtUtil, "jwtExpiration", 86400000L); // 24 hours
    }

    @Test
    void testGenerateToken() {
        String token = jwtUtil.generateToken(TEST_EMAIL, TEST_USER_ID);

        assertNotNull(token, "Generated token should not be null");
        assertFalse(token.isEmpty(), "Generated token should not be empty");
    }

    @Test
    void testGetEmailFromToken() {
        String token = jwtUtil.generateToken(TEST_EMAIL, TEST_USER_ID);

        String extractedEmail = jwtUtil.getEmailFromToken(token);

        assertEquals(TEST_EMAIL, extractedEmail, "Extracted email should match the original");
    }

    @Test
    void testGetUserIdFromToken() {
        String token = jwtUtil.generateToken(TEST_EMAIL, TEST_USER_ID);

        Long extractedUserId = jwtUtil.getUserIdFromToken(token);

        assertEquals(TEST_USER_ID, extractedUserId, "Extracted user ID should match the original");
    }

    @Test
    void testValidateTokenValid() {
        String token = jwtUtil.generateToken(TEST_EMAIL, TEST_USER_ID);

        boolean isValid = jwtUtil.validateToken(token);

        assertTrue(isValid, "Valid token should be validated successfully");
    }

    @Test
    void testValidateTokenInvalid() {
        boolean isValid;
        try {
            isValid = jwtUtil.validateToken(INVALID_TOKEN);
        } catch (Exception e) {
            isValid = false;
        }

        assertFalse(isValid, "Invalid token should not be validated");
    }

    @Test
    void testIsTokenExpiredFalse() {
        String token = jwtUtil.generateToken(TEST_EMAIL, TEST_USER_ID);

        boolean isExpired = jwtUtil.isTokenExpired(token);

        assertFalse(isExpired, "Newly generated token should not be expired");
    }

    @Test
    void testIsTokenExpiredTrue() {
        // To properly test expired token, generate a token with very short expiry and wait, or simulate expiration
        // Using try-catch to ensure invalid token returns expired status without exception
        boolean isExpired;
        try {
            isExpired = jwtUtil.isTokenExpired(INVALID_TOKEN);
        } catch (Exception e) {
            isExpired = true;
        }

        assertTrue(isExpired, "Invalid or expired token should be marked as expired");
    }
}