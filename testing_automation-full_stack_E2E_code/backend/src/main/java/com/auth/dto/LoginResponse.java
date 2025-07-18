package com.auth.dto;

import java.util.Objects;

public final class LoginResponse {
    private final String token;
    private final UserDto user;
    private final String message;

    public LoginResponse(String token, UserDto user, String message) {
        this.token = token;
        this.user = user;
        this.message = message;
    }

    public String getToken() {
        return token;
    }

    public UserDto getUser() {
        return user;
    }

    public String getMessage() {
        return message;
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