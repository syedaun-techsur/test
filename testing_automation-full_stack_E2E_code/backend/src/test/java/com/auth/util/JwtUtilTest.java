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
        // Inject secret and expiration using ReflectionTestUtils for isolated testing
        ReflectionTestUtils.setField(jwtUtil, "jwtSecret", "mySecretKeyForJWTTokenGenerationAndValidation");
        ReflectionTestUtils.setField(jwtUtil, "jwtExpiration", 86400000L); // 24 hours expiration
    }

    @Test
    void testGenerateToken() {
        final String email = "test@example.com";
        final Long userId = 1L;

        String token = jwtUtil.generateToken(email, userId);

        assertNotNull(token, "Generated token should not be null");
        assertFalse(token.isEmpty(), "Generated token should not be empty");
    }

    @Test
    void testGetEmailFromToken() {
        final String email = "test@example.com";
        final Long userId = 1L;
        String token = jwtUtil.generateToken(email, userId);

        String extractedEmail = jwtUtil.getEmailFromToken(token);

        assertEquals(email, extractedEmail, "Email extracted from token should match the original");
    }

    @Test
    void testGetUserIdFromToken() {
        final String email = "test@example.com";
        final Long userId = 1L;
        String token = jwtUtil.generateToken(email, userId);

        Long extractedUserId = jwtUtil.getUserIdFromToken(token);

        assertEquals(userId, extractedUserId, "User ID extracted from token should match the original");
    }

    @Test
    void testValidateTokenValid() {
        final String email = "test@example.com";
        final Long userId = 1L;
        String token = jwtUtil.generateToken(email, userId);

        boolean isValid = jwtUtil.validateToken(token);

        assertTrue(isValid, "Generated token should be valid");
    }

    @Test
    void testValidateTokenInvalid() {
        final String invalidToken = "invalid.token.here";

        // This tests resilience against malformed/invalid tokens
        boolean isValid = jwtUtil.validateToken(invalidToken);

        assertFalse(isValid, "Invalid token should not be validated as true");
    }

    @Test
    void testIsTokenExpiredFalse() {
        final String email = "test@example.com";
        final Long userId = 1L;
        String token = jwtUtil.generateToken(email, userId);

        boolean isExpired = jwtUtil.isTokenExpired(token);

        assertFalse(isExpired, "Newly generated token should not be expired");
    }

    @Test
    void testIsTokenExpiredTrue() {
        final String invalidToken = "invalid.token.here";

        // Assuming invalid token leads to treated as expired logically
        boolean isExpired = jwtUtil.isTokenExpired(invalidToken);

        assertTrue(isExpired, "Invalid token should be considered expired");
    }
}