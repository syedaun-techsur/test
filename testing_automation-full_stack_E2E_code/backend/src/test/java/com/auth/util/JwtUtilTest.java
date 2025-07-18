package com.auth.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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
    @DisplayName("Generate token should create a non-null, non-empty JWT token")
    void testGenerateToken() {
        final String email = "test@example.com";
        final Long userId = 1L;

        final String token = jwtUtil.generateToken(email, userId);

        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    @DisplayName("Extracted email from token should match original email")
    void testGetEmailFromToken() {
        final String email = "test@example.com";
        final Long userId = 1L;
        final String token = jwtUtil.generateToken(email, userId);

        final String extractedEmail = jwtUtil.getEmailFromToken(token);

        assertEquals(email, extractedEmail);
    }

    @Test
    @DisplayName("Extracted userId from token should match original userId")
    void testGetUserIdFromToken() {
        final String email = "test@example.com";
        final Long userId = 1L;
        final String token = jwtUtil.generateToken(email, userId);

        final Long extractedUserId = jwtUtil.getUserIdFromToken(token);

        assertEquals(userId, extractedUserId);
    }

    @Test
    @DisplayName("Valid token should pass validation")
    void testValidateTokenValid() {
        final String email = "test@example.com";
        final Long userId = 1L;
        final String token = jwtUtil.generateToken(email, userId);

        final boolean isValid = jwtUtil.validateToken(token);

        assertTrue(isValid);
    }

    @Test
    @DisplayName("Invalid token should fail validation")
    void testValidateTokenInvalid() {
        final String invalidToken = "invalid.token.here";

        final boolean isValid = jwtUtil.validateToken(invalidToken);

        assertFalse(isValid);
    }

    @Test
    @DisplayName("Non-expired token should not be reported as expired")
    void testIsTokenExpiredFalse() {
        final String email = "test@example.com";
        final Long userId = 1L;
        final String token = jwtUtil.generateToken(email, userId);

        final boolean isExpired = jwtUtil.isTokenExpired(token);

        assertFalse(isExpired);
    }

    @Test
    @DisplayName("Invalid token should be treated as expired")
    void testIsTokenExpiredTrue() {
        final String invalidToken = "invalid.token.here";

        final boolean isExpired = jwtUtil.isTokenExpired(invalidToken);

        assertTrue(isExpired);
    }
}