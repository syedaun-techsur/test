package com.auth.util;

import com.auth.util.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilTest {

    private JwtUtil jwtUtil;

    private static final String SECRET = "mySecretKeyForJWTTokenGenerationAndValidation";
    private static final long EXPIRATION = 86400000L; // 24 hours
    private static final String EMAIL = "test@example.com";
    private static final Long USER_ID = 1L;
    private static final String INVALID_TOKEN = "invalid.token.here";

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil();
        ReflectionTestUtils.setField(jwtUtil, "jwtSecret", SECRET);
        ReflectionTestUtils.setField(jwtUtil, "jwtExpiration", EXPIRATION);
    }

    @Test
    void testGenerateToken() {
        final String token = jwtUtil.generateToken(EMAIL, USER_ID);

        assertNotNull(token, "Generated token should not be null");
        assertFalse(token.isEmpty(), "Generated token should not be empty");
    }

    @Test
    void testGetEmailFromToken() {
        final String token = jwtUtil.generateToken(EMAIL, USER_ID);
        final String extractedEmail = jwtUtil.getEmailFromToken(token);

        assertEquals(EMAIL, extractedEmail, "Extracted email should match the original email");
    }

    @Test
    void testGetUserIdFromToken() {
        final String token = jwtUtil.generateToken(EMAIL, USER_ID);
        final Long extractedUserId = jwtUtil.getUserIdFromToken(token);

        assertEquals(USER_ID, extractedUserId, "Extracted user ID should match the original user ID");
    }

    @Test
    void testValidateTokenValid() {
        final String token = jwtUtil.generateToken(EMAIL, USER_ID);
        final boolean isValid = jwtUtil.validateToken(token);

        assertTrue(isValid, "Valid token should return true on validation");
    }

    @Test
    void testValidateTokenInvalid() {
        final boolean isValid = jwtUtil.validateToken(INVALID_TOKEN);

        assertFalse(isValid, "Invalid token should return false on validation");
    }

    @Test
    void testIsTokenExpiredFalse() {
        final String token = jwtUtil.generateToken(EMAIL, USER_ID);
        final boolean isExpired = jwtUtil.isTokenExpired(token);

        assertFalse(isExpired, "Newly generated token should not be expired");
    }

    @Test
    void testIsTokenExpiredTrue() {
        // For an invalid or malformed token, most likely the token will be considered expired or invalid
        final boolean isExpired = jwtUtil.isTokenExpired(INVALID_TOKEN);

        assertTrue(isExpired, "Invalid token should be considered expired");
    }
}