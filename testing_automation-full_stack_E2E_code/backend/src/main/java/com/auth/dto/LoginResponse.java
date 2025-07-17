package com.auth.dto;

/**
 * Data Transfer Object representing the response returned after a login attempt.
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
     * Parameterized constructor for LoginResponse.
     *
     * @param token   the JWT token or authentication token
     * @param user    the user details associated with the login
     * @param message a message corresponding to the login status
     */
    public LoginResponse(String token, UserDto user, String message) {
        this.token = token;
        this.user = user;
        this.message = message;
    }

    /**
     * Gets the token.
     *
     * @return the token
     */
    public String getToken() {
        return token;
    }

    /**
     * Sets the token.
     *
     * @param token the token to set
     */
    public void setToken(String token) {
        this.token = token;
    }

    /**
     * Gets the user.
     *
     * @return the user
     */
    public UserDto getUser() {
        return user;
    }

    /**
     * Sets the user.
     *
     * @param user the user to set
     */
    public void setUser(UserDto user) {
        this.user = user;
    }

    /**
     * Gets the message.
     *
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Sets the message.
     *
     * @param message the message to set
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LoginResponse)) return false;

        LoginResponse that = (LoginResponse) o;

        if (token != null ? !token.equals(that.token) : that.token != null) return false;
        if (user != null ? !user.equals(that.user) : that.user != null) return false;
        return message != null ? message.equals(that.message) : that.message == null;
    }

    @Override
    public int hashCode() {
        int result = token != null ? token.hashCode() : 0;
        result = 31 * result + (user != null ? user.hashCode() : 0);
        result = 31 * result + (message != null ? message.hashCode() : 0);
        return result;
    }
}