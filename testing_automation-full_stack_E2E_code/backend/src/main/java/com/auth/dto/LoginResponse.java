package com.auth.dto;

import com.auth.dto.UserDto;

/**
 * Data Transfer Object for the login response containing authentication token, user details, and an optional message.
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
     * Parameterized constructor to initialize all fields.
     *
     * @param token   JWT or authentication token
     * @param user    User details encapsulated in UserDto
     * @param message Optional message about login status
     */
    public LoginResponse(String token, UserDto user, String message) {
        this.token = token;
        this.user = user;
        this.message = message;
    }

    /**
     * Gets the authentication token.
     *
     * @return token string
     */
    public String getToken() {
        return token;
    }

    /**
     * Sets the authentication token.
     *
     * @param token token string
     */
    public void setToken(String token) {
        this.token = token;
    }

    /**
     * Gets the user details.
     *
     * @return user as UserDto
     */
    public UserDto getUser() {
        return user;
    }

    /**
     * Sets the user details.
     *
     * @param user user as UserDto
     */
    public void setUser(UserDto user) {
        this.user = user;
    }

    /**
     * Gets the optional message.
     *
     * @return message string
     */
    public String getMessage() {
        return message;
    }

    /**
     * Sets the optional message.
     *
     * @param message message string
     */
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