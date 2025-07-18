package com.auth.dto;

import com.auth.dto.UserDto;

/**
 * Represents the response returned after a login attempt.
 */
public class LoginResponse {
    private String token;
    private UserDto user;
    private String message;

    /**
     * Default constructor.
     */
    public LoginResponse() {}

    /**
     * Constructs a LoginResponse with the given token, user, and message.
     *
     * @param token   the authentication token
     * @param user    the user data transfer object
     * @param message the response message
     */
    public LoginResponse(String token, UserDto user, String message) {
        this.token = token;
        this.user = user;
        this.message = message;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public UserDto getUser() {
        return user;
    }

    public void setUser(UserDto user) {
        this.user = user;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
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