package com.auth.dto;

import com.auth.dto.UserDto;

/**
 * DTO representing the login response including token, user details, and a message.
 */
public class LoginResponse {

    private String token;
    private UserDto user;
    private String message;

    /**
     * Default no-args constructor.
     */
    public LoginResponse() {}

    /**
     * All-args constructor to initialize all fields.
     *
     * @param token   the authentication token
     * @param user    the user details
     * @param message any message related to the login response
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
}