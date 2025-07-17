package com.auth.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class ErrorResponse {
    private final int status;
    private final String message;
    private final LocalDateTime timestamp;
    private final List<String> errors;

    // Constructors
    public ErrorResponse(int status, String message) {
        this(status, message, null);
    }

    public ErrorResponse(int status, String message, List<String> errors) {
        this.status = status;
        this.message = Objects.requireNonNull(message, "message must not be null");
        this.timestamp = LocalDateTime.now();
        this.errors = errors;
    }

    // Getters - no setters to enforce immutability
    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public List<String> getErrors() {
        return errors;
    }

    @Override
    public String toString() {
        return "ErrorResponse{" +
               "status=" + status +
               ", message='" + message + '\'' +
               ", timestamp=" + timestamp +
               ", errors=" + errors +
               '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ErrorResponse)) return false;
        ErrorResponse that = (ErrorResponse) o;
        return status == that.status &&
               message.equals(that.message) &&
               timestamp.equals(that.timestamp) &&
               Objects.equals(errors, that.errors);
    }

    @Override
    public int hashCode() {
        return Objects.hash(status, message, timestamp, errors);
    }
}