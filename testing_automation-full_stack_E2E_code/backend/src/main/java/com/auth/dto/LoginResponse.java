package com.auth.dto;

import com.auth.dto.UserDto;

/**
 * Data Transfer Object for login response containing
 * authentication token, user details, and a message.
 */
public class LoginResponse {

    private String token;
    private UserDto user;
    private String message;

    /**
     * Default constructor.
     */
    public LoginResponse() {
    }

    /**
     * Parameterized constructor.
     *
     * @param token   the authentication token
     * @param user    the user information
     * @param message the response message
     */
    public LoginResponse(final String token, final UserDto user, final String message) {
        this.token = token;
        this.user = user;
        this.message = message;
    }

    public String getToken() {
        return token;
    }

    public void setToken(final String token) {
        this.token = token;
    }

    public UserDto getUser() {
        return user;
    }

    public void setUser(final UserDto user) {
        this.user = user;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(final String message) {
        this.message = message;
    }
}