package com.auth.dto;

import com.auth.dto.UserDto;

/**
 * Represents the response returned after a login attempt.
 */
public class LoginResponse {
    private final String token;
    private final UserDto user;
    private final String message;

    /**
     * Constructs a new LoginResponse with token, user, and message.
     *
     * @param token   The authentication token.
     * @param user    The UserDto object representing the logged in user.
     * @param message Message related to the login process.
     */
    public LoginResponse(String token, UserDto user, String message) {
        this.token = token;
        this.user = user;
        this.message = message;
    }

    /**
     * Default constructor for frameworks that require it.
     */
    public LoginResponse() {
        this.token = null;
        this.user = null;
        this.message = null;
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