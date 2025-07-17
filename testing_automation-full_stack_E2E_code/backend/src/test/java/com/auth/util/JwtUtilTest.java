package com.auth.util;

import com.auth.util.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilTest {

    private static final String TEST_EMAIL = "test@example.com";
    private static final Long TEST_USER_ID = 1L;
    private static final String SECRET_KEY = "mySecretKeyForJWTTokenGenerationAndValidation";
    private static final Long EXPIRATION_TIME = 86400000L; // 24 hours

    private JwtUtil jwtUtil;

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil();
        ReflectionTestUtils.setField(jwtUtil, "jwtSecret", SECRET_KEY);
        ReflectionTestUtils.setField(jwtUtil, "jwtExpiration", EXPIRATION_TIME);
    }

    @Test
    void generateToken_ShouldReturnNonEmptyToken() {
        String token = jwtUtil.generateToken(TEST_EMAIL, TEST_USER_ID);
        assertNotNull(token, "Generated token should not be null");
        assertFalse(token.isEmpty(), "Generated token should not be empty");
    }

    @Test
    void getEmailFromToken_ShouldReturnCorrectEmail() {
        String token = jwtUtil.generateToken(TEST_EMAIL, TEST_USER_ID);
        assertNotNull(token, "Token should not be null before extracting email");

        String extractedEmail = jwtUtil.getEmailFromToken(token);
        assertEquals(TEST_EMAIL, extractedEmail, "Extracted email should match the original email");
    }

    @Test
    void getUserIdFromToken_ShouldReturnCorrectUserId() {
        String token = jwtUtil.generateToken(TEST_EMAIL, TEST_USER_ID);
        assertNotNull(token, "Token should not be null before extracting user ID");

        Long extractedUserId = jwtUtil.getUserIdFromToken(token);
        assertEquals(TEST_USER_ID, extractedUserId, "Extracted user ID should match the original user ID");
    }

    @Test
    void validateToken_ShouldReturnTrueForValidToken() {
        String token = jwtUtil.generateToken(TEST_EMAIL, TEST_USER_ID);
        assertNotNull(token, "Token should not be null for validation");

        boolean isValid = jwtUtil.validateToken(token);
        assertTrue(isValid, "Valid token should be validated as true");
    }

    @Test
    void validateToken_ShouldReturnFalseForInvalidToken() {
        String invalidToken = "invalid.token.here";

        boolean isValid = jwtUtil.validateToken(invalidToken);
        assertFalse(isValid, "Invalid token should be validated as false");
    }

    @Test
    void isTokenExpired_ShouldReturnFalseForValidToken() {
        String token = jwtUtil.generateToken(TEST_EMAIL, TEST_USER_ID);
        assertNotNull(token, "Token should not be null when checking expiration");

        boolean isExpired = jwtUtil.isTokenExpired(token);
        assertFalse(isExpired, "Freshly generated token should not be expired");
    }

    @Test
    void isTokenExpired_ShouldReturnTrueForInvalidToken() {
        String invalidToken = "invalid.token.here";

        boolean isExpired = jwtUtil.isTokenExpired(invalidToken);
        assertTrue(isExpired, "Invalid token should be considered expired");
    }
}