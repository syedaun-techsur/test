package com.auth.util;

import io.jsonwebtoken.JwtException;
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
    void generateToken_ShouldReturnToken() {
        final String email = "test@example.com";
        final Long userId = 1L;

        final String token = jwtUtil.generateToken(email, userId);

        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    void getEmailFromToken_ShouldReturnCorrectEmail() {
        final String email = "test@example.com";
        final Long userId = 1L;
        final String token = jwtUtil.generateToken(email, userId);

        final String extractedEmail = jwtUtil.getEmailFromToken(token);

        assertEquals(email, extractedEmail);
    }

    @Test
    void getUserIdFromToken_ShouldReturnCorrectUserId() {
        final String email = "test@example.com";
        final Long userId = 1L;
        final String token = jwtUtil.generateToken(email, userId);

        final Long extractedUserId = jwtUtil.getUserIdFromToken(token);

        assertEquals(userId, extractedUserId);
    }

    @Test
    void validateToken_ValidToken_ShouldReturnTrue() {
        final String email = "test@example.com";
        final Long userId = 1L;
        final String token = jwtUtil.generateToken(email, userId);

        assertTrue(jwtUtil.validateToken(token));
    }

    @Test
    void validateToken_InvalidToken_ShouldReturnFalse() {
        final String invalidToken = "invalid.token.here";

        assertFalse(jwtUtil.validateToken(invalidToken));
    }

    @Test
    void isTokenExpired_ValidToken_ShouldReturnFalse() {
        final String email = "test@example.com";
        final Long userId = 1L;
        final String token = jwtUtil.generateToken(email, userId);

        assertFalse(jwtUtil.isTokenExpired(token));
    }

    @Test
    void isTokenExpired_InvalidToken_ShouldReturnTrue() {
        final String invalidToken = "invalid.token.here";

        assertTrue(jwtUtil.isTokenExpired(invalidToken));
    }
}