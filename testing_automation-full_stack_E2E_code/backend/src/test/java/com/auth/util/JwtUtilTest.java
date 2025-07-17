package com.auth.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilTest {

    private JwtUtil jwtUtil;
    private static final String TEST_EMAIL = "test@example.com";
    private static final Long TEST_USER_ID = Long.valueOf(1);
    private static final String SECRET_KEY = "mySecretKeyForJWTTokenGenerationAndValidation";
    private static final long EXPIRATION_TIME = 86400000L; // 24 hours

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil();
        ReflectionTestUtils.setField(jwtUtil, "jwtSecret", SECRET_KEY);
        ReflectionTestUtils.setField(jwtUtil, "jwtExpiration", EXPIRATION_TIME);
    }

    @Test
    void testGenerateToken() {
        final String token = jwtUtil.generateToken(TEST_EMAIL, TEST_USER_ID);

        assertNotNull(token, "Generated token should not be null");
        assertFalse(token.isEmpty(), "Generated token should not be empty");
    }

    @Test
    void testGetEmailFromToken() {
        final String token = jwtUtil.generateToken(TEST_EMAIL, TEST_USER_ID);

        final String extractedEmail = jwtUtil.getEmailFromToken(token);

        assertEquals(TEST_EMAIL, extractedEmail, "Extracted email should match the original");
    }

    @Test
    void testGetUserIdFromToken() {
        final String token = jwtUtil.generateToken(TEST_EMAIL, TEST_USER_ID);

        final Long extractedUserId = jwtUtil.getUserIdFromToken(token);

        assertEquals(TEST_USER_ID, extractedUserId, "Extracted userId should match the original");
    }

    @Test
    void testValidateTokenValid() {
        final String token = jwtUtil.generateToken(TEST_EMAIL, TEST_USER_ID);

        final boolean isValid = jwtUtil.validateToken(token);

        assertTrue(isValid, "Valid token should pass validation");
    }

    @Test
    void testValidateTokenInvalid() {
        final String invalidToken = "invalid.token.here";

        final boolean isValid = jwtUtil.validateToken(invalidToken);

        assertFalse(isValid, "Invalid token should fail validation");
    }

    @Test
    void testIsTokenExpiredFalse() {
        final String token = jwtUtil.generateToken(TEST_EMAIL, TEST_USER_ID);

        final boolean isExpired = jwtUtil.isTokenExpired(token);

        assertFalse(isExpired, "Newly generated token should not be expired");
    }

    @Test
    void testIsTokenExpiredTrue() {
        final String invalidToken = "invalid.token.here";

        final boolean isExpired = jwtUtil.isTokenExpired(invalidToken);

        assertTrue(isExpired, "Invalid token should be considered expired");
    }
}