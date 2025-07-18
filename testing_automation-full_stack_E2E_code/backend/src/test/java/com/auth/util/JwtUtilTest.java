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
    @DisplayName("Generate token returns non-null, non-empty JWT string")
    void testGenerateToken() {
        String email = "test@example.com";
        Long userId = 1L;

        String token = jwtUtil.generateToken(email, userId);

        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    @DisplayName("Extract email from generated token matches the input email")
    void testGetEmailFromToken() {
        String email = "test@example.com";
        Long userId = 1L;
        String token = jwtUtil.generateToken(email, userId);

        String extractedEmail = jwtUtil.getEmailFromToken(token);

        assertEquals(email, extractedEmail);
    }

    @Test
    @DisplayName("Extract userId from token matches the input userId")
    void testGetUserIdFromToken() {
        String email = "test@example.com";
        Long userId = 1L;
        String token = jwtUtil.generateToken(email, userId);

        Long extractedUserId = jwtUtil.getUserIdFromToken(token);

        assertEquals(userId, extractedUserId);
    }

    @Test
    @DisplayName("Valid token is validated successfully")
    void testValidateTokenValid() {
        String email = "test@example.com";
        Long userId = 1L;
        String token = jwtUtil.generateToken(email, userId);

        boolean isValid = jwtUtil.validateToken(token);

        assertTrue(isValid);
    }

    @Test
    @DisplayName("Invalid token fails validation")
    void testValidateTokenInvalid() {
        String invalidToken = "invalid.token.here";

        boolean isValid = jwtUtil.validateToken(invalidToken);

        assertFalse(isValid);
    }

    @Test
    @DisplayName("Token is not expired immediately after generation")
    void testIsTokenExpiredFalse() {
        String email = "test@example.com";
        Long userId = 1L;
        String token = jwtUtil.generateToken(email, userId);

        boolean isExpired = jwtUtil.isTokenExpired(token);

        assertFalse(isExpired);
    }

    @Test
    @DisplayName("Invalid token is considered expired")
    void testIsTokenExpiredTrue() {
        String invalidToken = "invalid.token.here";

        boolean isExpired = jwtUtil.isTokenExpired(invalidToken);

        assertTrue(isExpired);
    }
}