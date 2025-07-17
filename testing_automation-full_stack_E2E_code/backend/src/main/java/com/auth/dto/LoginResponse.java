package com.auth.dto;

import com.auth.dto.UserDto;

/**
 * Data Transfer Object for Login Response.
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
     * Constructor with all fields.
     *
     * @param token   the authentication token
     * @param user    the user details
     * @param message response message
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

    @Override
    public String toString() {
        return "LoginResponse{" +
                "token='" + token + '\'' +
                ", user=" + user +
                ", message='" + message + '\'' +
                '}';
    }
}