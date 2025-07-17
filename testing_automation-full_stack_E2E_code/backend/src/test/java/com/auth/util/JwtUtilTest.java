package com.auth.util;

import com.auth.util.JwtUtil;
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
    @DisplayName("Generate token successfully")
    void testGenerateToken() {
        String email = "test@example.com";
        Long userId = 1L;

        String token = jwtUtil.generateToken(email, userId);

        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    @DisplayName("Extract email from token")
    void testGetEmailFromToken() {
        String email = "test@example.com";
        Long userId = 1L;
        String token = jwtUtil.generateToken(email, userId);

        String extractedEmail = jwtUtil.getEmailFromToken(token);

        assertEquals(email, extractedEmail);
    }

    @Test
    @DisplayName("Extract user ID from token")
    void testGetUserIdFromToken() {
        String email = "test@example.com";
        Long userId = 1L;
        String token = jwtUtil.generateToken(email, userId);

        Long extractedUserId = jwtUtil.getUserIdFromToken(token);

        assertEquals(userId, extractedUserId);
    }

    @Test
    @DisplayName("Validate a valid token")
    void testValidateTokenValid() {
        String email = "test@example.com";
        Long userId = 1L;
        String token = jwtUtil.generateToken(email, userId);

        boolean isValid = jwtUtil.validateToken(token);

        assertTrue(isValid);
    }

    @Test
    @DisplayName("Invalidate a malformed token")
    void testValidateTokenInvalid() {
        String invalidToken = "invalid.token.here";

        boolean isValid = jwtUtil.validateToken(invalidToken);

        assertFalse(isValid);
    }

    @Test
    @DisplayName("Verify token is not expired")
    void testIsTokenExpiredFalse() {
        String email = "test@example.com";
        Long userId = 1L;
        String token = jwtUtil.generateToken(email, userId);

        boolean isExpired = jwtUtil.isTokenExpired(token);

        assertFalse(isExpired);
    }

    @Test
    @DisplayName("Verify malformed token is treated as expired")
    void testIsTokenExpiredTrue() {
        String invalidToken = "invalid.token.here";

        boolean isExpired = jwtUtil.isTokenExpired(invalidToken);

        assertTrue(isExpired);
    }
}