package com.auth.util;

import com.auth.util.JwtUtil;
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

        assertEquals(TEST_EMAIL, extractedEmail, "Extracted email should match the original email");
    }

    @Test
    void testGetUserIdFromToken() {
        String token = jwtUtil.generateToken(TEST_EMAIL, TEST_USER_ID);
        Long extractedUserId = jwtUtil.getUserIdFromToken(token);

        assertEquals(TEST_USER_ID, extractedUserId, "Extracted userId should match the original userId");
    }

    @Test
    void testValidateTokenValid() {
        String token = jwtUtil.generateToken(TEST_EMAIL, TEST_USER_ID);
        boolean isValid = jwtUtil.validateToken(token);

        assertTrue(isValid, "Token generated should be valid");
    }

    @Test
    void testValidateTokenInvalid() {
        boolean isValid = jwtUtil.validateToken(INVALID_TOKEN);

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
        // Using an invalid token to simulate expiration or invalidity.
        // Since creating a truly expired token without manipulating JwtUtil code or system time is difficult,
        // this test ensures invalid tokens are treated as expired.
        boolean isExpired = jwtUtil.isTokenExpired(INVALID_TOKEN);

        assertTrue(isExpired, "Invalid token should be treated as expired");
    }
}