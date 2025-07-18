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
        ReflectionTestUtils.setField(jwtUtil, "jwtExpiration", 86400000L); // 24 hours
    }

    @Test
    void testGenerateToken() {
        final String email = "test@example.com";
        final Long userId = 1L;

        final String token = jwtUtil.generateToken(email, userId);

        assertNotNull(token, "Token should not be null");
        assertFalse(token.isEmpty(), "Token should not be empty");
    }

    @Test
    void testGetEmailFromToken() {
        final String email = "test@example.com";
        final Long userId = 1L;
        final String token = jwtUtil.generateToken(email, userId);

        final String extractedEmail = jwtUtil.getEmailFromToken(token);

        assertEquals(email, extractedEmail, "Extracted email should match the original");
    }

    @Test
    void testGetUserIdFromToken() {
        final String email = "test@example.com";
        final Long userId = 1L;
        final String token = jwtUtil.generateToken(email, userId);

        final Long extractedUserId = jwtUtil.getUserIdFromToken(token);

        assertEquals(userId, extractedUserId, "Extracted userId should match the original");
    }

    @Test
    void testValidateTokenValid() {
        final String email = "test@example.com";
        final Long userId = 1L;
        final String token = jwtUtil.generateToken(email, userId);

        final boolean isValid = jwtUtil.validateToken(token);

        assertTrue(isValid, "Token should be valid");
    }

    @Test
    void testValidateTokenInvalid() {
        final String invalidToken = "invalid.token.here";

        final boolean isValid = jwtUtil.validateToken(invalidToken);

        assertFalse(isValid, "Invalid token should be detected as invalid");
    }

    @Test
    void testIsTokenExpiredFalse() {
        final String email = "test@example.com";
        final Long userId = 1L;
        final String token = jwtUtil.generateToken(email, userId);

        final boolean isExpired = jwtUtil.isTokenExpired(token);

        assertFalse(isExpired, "Token should not be expired");
    }

    @Test
    void testIsTokenExpiredTrue() {
        final String invalidToken = "invalid.token.here";

        final boolean isExpired = jwtUtil.isTokenExpired(invalidToken);

        assertTrue(isExpired, "Invalid token should be treated as expired");
    }
}