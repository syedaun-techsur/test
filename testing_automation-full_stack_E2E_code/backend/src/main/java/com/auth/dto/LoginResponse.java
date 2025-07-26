package com.auth.dto;

/**
 * Represents the response returned upon a successful login.
 * Contains the authentication token, user details, and an optional message.
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
     * Constructs a LoginResponse with token, user and message.
     *
     * @param token   the authentication token
     * @param user    the user details
     * @param message an optional message
     */
    public LoginResponse(String token, UserDto user, String message) {
        this.token = token;
        this.user = user;
        this.message = message;
    }

    /**
     * Gets the authentication token.
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
     * Gets the user details.
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
     * Gets the optional message.
     *
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Sets the optional message.
     *
     * @param message the message to set
     */
    public void setMessage(String message) {
        this.message = message;
    }
}