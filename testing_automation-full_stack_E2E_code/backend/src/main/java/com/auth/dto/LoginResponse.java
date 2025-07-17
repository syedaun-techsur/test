package com.auth.dto;

import java.util.Objects;

/**
 * Data Transfer Object for login response.
 * Contains authentication token, user details, and an optional message.
 */
public final class LoginResponse {
    private String token;
    private UserDto user;
    private String message;

    /**
     * Default constructor.
     */
    public LoginResponse() {
    }

    /**
     * Constructor initializing all fields.
     *
     * @param token   the authentication token
     * @param user    the user details
     * @param message an optional message related to login
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LoginResponse)) return false;
        LoginResponse that = (LoginResponse) o;
        return Objects.equals(token, that.token) &&
                Objects.equals(user, that.user) &&
                Objects.equals(message, that.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(token, user, message);
    }
}