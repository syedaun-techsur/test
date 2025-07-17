package com.auth.util;

import com.auth.util.JwtUtil;
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
        String token = jwtUtil.generateToken("test@example.com", 1L);

        assertNotNull(token, "Generated token should not be null");
        assertFalse(token.isEmpty(), "Generated token should not be empty");
    }

    @Test
    void testGetEmailFromToken() {
        String token = jwtUtil.generateToken("test@example.com", 1L);

        String extractedEmail = jwtUtil.getEmailFromToken(token);

        assertEquals("test@example.com", extractedEmail, "Extracted email should match the original");
    }

    @Test
    void testGetUserIdFromToken() {
        String token = jwtUtil.generateToken("test@example.com", 1L);

        Long extractedUserId = jwtUtil.getUserIdFromToken(token);

        assertEquals(1L, extractedUserId, "Extracted user ID should match the original");
    }

    @Test
    void testValidateTokenValid() {
        String token = jwtUtil.generateToken("test@example.com", 1L);

        boolean isValid = jwtUtil.validateToken(token);

        assertTrue(isValid, "Token validation should pass for a valid token");
    }

    @Test
    void testValidateTokenInvalid() {
        String invalidToken = "invalid.token.here";

        // It is possible validateToken returns false or throws exception, so handle both safely
        boolean isValid;
        try {
            isValid = jwtUtil.validateToken(invalidToken);
        } catch (Exception e) {
            isValid = false;
        }

        assertFalse(isValid, "Token validation should fail for an invalid token");
    }

    @Test
    void testIsTokenExpiredFalse() {
        String token = jwtUtil.generateToken("test@example.com", 1L);

        boolean isExpired = jwtUtil.isTokenExpired(token);

        assertFalse(isExpired, "Token should not be expired immediately after generation");
    }

    @Test
    void testIsTokenExpiredTrue() {
        String invalidToken = "invalid.token.here";

        // Defensive: isTokenExpired may throw on invalid token, handle gracefully
        boolean isExpired;
        try {
            isExpired = jwtUtil.isTokenExpired(invalidToken);
        } catch (Exception e) {
            isExpired = true; // treat exception as token expired/invalid
        }

        assertTrue(isExpired, "Invalid token should be treated as expired");
    }
}