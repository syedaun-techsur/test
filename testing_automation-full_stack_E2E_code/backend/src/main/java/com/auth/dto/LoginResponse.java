package com.auth.dto;

import com.auth.dto.UserDto;

/**
 * Represents the response returned after a successful or failed login attempt.
 */
public class LoginResponse {
    
    private final String token;
    private final UserDto user;
    private final String message;
    
    /**
     * Default constructor initializing fields to null.
     */
    public LoginResponse() {
        this.token = null;
        this.user = null;
        this.message = null;
    }
    
    /**
     * Parameterized constructor to initialize LoginResponse with given values.
     * 
     * @param token the authentication token
     * @param user the authenticated user details
     * @param message response message
     */
    public LoginResponse(String token, UserDto user, String message) {
        this.token = token;
        this.user = user;
        this.message = message;
    }

    /**
     * Returns the authentication token.
     * 
     * @return the token
     */
    public String getToken() {
        return token;
    }

    /**
     * Returns the authenticated user details.
     * 
     * @return the user
     */
    public UserDto getUser() {
        return user;
    }

    /**
     * Returns the response message.
     * 
     * @return the message
     */
    public String getMessage() {
        return message;
    }
}