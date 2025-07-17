package com.auth.dto;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Data Transfer Object representing the error response structure.
 * This class is immutable and thread-safe.
 */
public final class ErrorResponse {
    private final int status;
    private final String message;
    private final LocalDateTime timestamp;
    private final List<String> errors;

    /**
     * Constructs an ErrorResponse with status and message.
     * Timestamp is automatically set to current time.
     *
     * @param status  the HTTP status code
     * @param message the error message
     */
    public ErrorResponse(int status, String message) {
        this(status, message, null);
    }

    /**
     * Constructs an ErrorResponse with status, message and list of errors.
     * Timestamp is automatically set to current time.
     *
     * @param status  the HTTP status code
     * @param message the error message
     * @param errors  the list of error details, can be null
     */
    public ErrorResponse(int status, String message, List<String> errors) {
        this.status = status;
        this.message = Objects.requireNonNull(message, "message cannot be null");
        this.timestamp = LocalDateTime.now();
        this.errors = errors == null ? null : Collections.unmodifiableList(errors);
    }

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
        if (o == null || getClass() != o.getClass()) return false;

        ErrorResponse that = (ErrorResponse) o;

        if (status != that.status) return false;
        if (!message.equals(that.message)) return false;
        if (!timestamp.equals(that.timestamp)) return false;
        return Objects.equals(errors, that.errors);
    }

    @Override
    public int hashCode() {
        int result = status;
        result = 31 * result + message.hashCode();
        result = 31 * result + timestamp.hashCode();
        result = 31 * result + (errors != null ? errors.hashCode() : 0);
        return result;
    }
}