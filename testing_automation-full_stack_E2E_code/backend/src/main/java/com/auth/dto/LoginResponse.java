package com.auth.dto;

import com.auth.dto.UserDto;

/**
 * DTO representing the response returned after a login attempt.
 * It includes the authentication token, user details, and an optional message.
 */
public class LoginResponse {

    private String token;
    private UserDto user;
    private String message;

    /**
     * Default no-args constructor.
     */
    public LoginResponse() {
    }

    /**
     * Constructs a LoginResponse with specified token, user, and message.
     *
     * @param token   the authentication token
     * @param user    the user details
     * @param message an optional message related to login process
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
     * Sets the authentication token.
     *
     * @param token the token to set
     */
    public void setToken(String token) {
        this.token = token;
    }

    /**
     * Returns the user details.
     *
     * @return the user
     */
    public UserDto getUser() {
        return user;
    }

    /**
     * Sets the user details.
     *
     * @param user the user to set
     */
    public void setUser(UserDto user) {
        this.user = user;
    }

    /**
     * Returns the message associated with the login response.
     *
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Sets the message associated with the login response.
     *
     * @param message the message to set
     */
    public void setMessage(String message) {
        this.message = message;
    }
}