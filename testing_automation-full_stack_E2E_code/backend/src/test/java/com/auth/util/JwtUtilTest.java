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
    private static final String JWT_SECRET = "mySecretKeyForJWTTokenGenerationAndValidation";
    private static final long JWT_EXPIRATION = 86400000L; // 24 hours

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil();
        // Set private fields of jwtUtil using reflection for test configuration
        ReflectionTestUtils.setField(jwtUtil, "jwtSecret", JWT_SECRET);
        ReflectionTestUtils.setField(jwtUtil, "jwtExpiration", JWT_EXPIRATION);
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

        String extractedEmail = jwtUtil.getEmailFromToken(token);

        assertEquals(TEST_EMAIL, extractedEmail);
    }

    @Test
    void getUserIdFromToken_ShouldReturnCorrectUserId() {
        String token = jwtUtil.generateToken(TEST_EMAIL, TEST_USER_ID);

        Long extractedUserId = jwtUtil.getUserIdFromToken(token);

        assertEquals(TEST_USER_ID, extractedUserId);
    }

    @Test
    void validateToken_ShouldReturnTrueForValidToken() {
        String token = jwtUtil.generateToken(TEST_EMAIL, TEST_USER_ID);

        boolean isValid = jwtUtil.validateToken(token);

        assertTrue(isValid);
    }

    @Test
    void validateToken_ShouldReturnFalseForInvalidToken() {
        String invalidToken = "invalid.token.here";

        boolean isValid = jwtUtil.validateToken(invalidToken);

        assertFalse(isValid);
    }

    @Test
    void isTokenExpired_ShouldReturnFalseForValidToken() {
        String token = jwtUtil.generateToken(TEST_EMAIL, TEST_USER_ID);

        boolean isExpired = jwtUtil.isTokenExpired(token);

        assertFalse(isExpired);
    }

    @Test
    void isTokenExpired_ShouldReturnTrueForInvalidToken() {
        String invalidToken = "invalid.token.here";

        boolean isExpired = jwtUtil.isTokenExpired(invalidToken);

        assertTrue(isExpired);
    }
}