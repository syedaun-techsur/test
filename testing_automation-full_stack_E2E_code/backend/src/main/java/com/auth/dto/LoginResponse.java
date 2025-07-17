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
     * Parameterized constructor.
     *
     * @param token   the authentication token
     * @param user    the user details
     * @param message additional message related to the login response
     */
    public LoginResponse(String token, UserDto user, String message) {
        this.token = token;
        this.user = user;
        this.message = message;
    }

    /**
     * Returns the authentication token.
     *
     * @return token as String
     */
    public String getToken() {
        return token;
    }

    /**
     * Sets the authentication token.
     *
     * @param token authentication token
     */
    public void setToken(String token) {
        if (token != null) {
            this.token = token;
        }
    }

    /**
     * Returns the user details.
     *
     * @return user as UserDto
     */
    public UserDto getUser() {
        return user;
    }

    /**
     * Sets the user details.
     *
     * @param user user details
     */
    public void setUser(UserDto user) {
        if (user != null) {
            this.user = user;
        }
    }

    /**
     * Returns additional message related to login response.
     *
     * @return message as String
     */
    public String getMessage() {
        return message;
    }

    /**
     * Sets the additional message.
     *
     * @param message additional message
     */
    public void setMessage(String message) {
        if (message != null) {
            this.message = message;
        }
    }

    /**
     * Returns a string representation of the LoginResponse.
     *
     * @return string representation of the object
     */
    @Override
    public String toString() {
        return "LoginResponse{" +
                "token='" + token + '\'' +
                ", user=" + user +
                ", message='" + message + '\'' +
                '}';
    }
}