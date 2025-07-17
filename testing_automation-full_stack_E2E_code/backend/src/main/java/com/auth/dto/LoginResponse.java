package com.auth.dto;

import com.auth.dto.UserDto;

/**
 * DTO representing a response for login requests.
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
     * Constructs a LoginResponse with the token, user, and a message.
     *
     * @param token   the authentication token
     * @param user    the user details
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
        if (token != null) {
            this.token = token;
        }
    }

    public UserDto getUser() {
        return user;
    }

    public void setUser(UserDto user) {
        if (user != null) {
            this.user = user;
        }
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        if (message != null) {
            this.message = message;
        }
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