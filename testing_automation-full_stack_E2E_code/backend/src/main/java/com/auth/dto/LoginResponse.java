package com.auth.dto;

import com.auth.dto.UserDto;

/**
 * Represents the response returned after a successful login attempt.
 */
public class LoginResponse {
    private final String token;
    private final UserDto user;
    private final String message;

    /**
     * Default constructor for frameworks that require it.
     * Initializes all fields to null.
     */
    public LoginResponse() {
        this.token = null;
        this.user = null;
        this.message = null;
    }

    /**
     * Constructs a LoginResponse with the given token, user, and message.
     *
     * @param token the authentication token
     * @param user the authenticated user's details
     * @param message the message related to login
     */
    public LoginResponse(String token, UserDto user, String message) {
        this.token = token;
        this.user = user;
        this.message = message;
    }

    public String getToken() {
        return token;
    }

    public UserDto getUser() {
        return user;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "LoginResponse{" +
                "token='" + token + '\'' +
                ", user=" + user +
                ", message='" + message + '\'' +
                '}';
    }
}