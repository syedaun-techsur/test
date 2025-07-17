package com.auth.util;

import com.auth.util.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilTest {

    private JwtUtil jwtUtil;

    private static final String TEST_EMAIL = "test@example.com";
    private static final long TEST_USER_ID = 1L;

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil();
        ReflectionTestUtils.setField(jwtUtil, "jwtSecret", "mySecretKeyForJWTTokenGenerationAndValidation");
        ReflectionTestUtils.setField(jwtUtil, "jwtExpiration", 86400000L); // 24 hours
    }

    @Test
    void testGenerateToken() {
        String token = jwtUtil.generateToken(TEST_EMAIL, TEST_USER_ID);

        assertNotNull(token, "Token should not be null");
        assertFalse(token.isEmpty(), "Token should not be empty");
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

        long extractedUserId = jwtUtil.getUserIdFromToken(token);

        assertEquals(TEST_USER_ID, extractedUserId, "Extracted user ID should match the original");
    }

    @Test
    void testValidateTokenValid() {
        String token = jwtUtil.generateToken(TEST_EMAIL, TEST_USER_ID);

        boolean isValid = jwtUtil.validateToken(token);

        assertTrue(isValid, "Token should be valid");
    }

    @Test
    void testValidateTokenInvalid() {
        String invalidToken = "invalid.token.here";

        boolean isValid = jwtUtil.validateToken(invalidToken);

        assertFalse(isValid, "Invalid token should not be valid");
    }

    @Test
    void testIsTokenExpiredFalse() {
        String token = jwtUtil.generateToken(TEST_EMAIL, TEST_USER_ID);

        boolean isExpired = jwtUtil.isTokenExpired(token);

        assertFalse(isExpired, "Newly generated token should not be expired");
    }

    @Test
    void testIsTokenExpiredTrue() {
        // Using an invalid token here to test expired or invalid token scenario
        String invalidToken = "invalid.token.here";

        boolean isExpired = jwtUtil.isTokenExpired(invalidToken);

        // It's safer to expect true here if token can't be parsed or considered expired by implementation
        assertTrue(isExpired, "Invalid token should be considered expired or invalid");
    }
}