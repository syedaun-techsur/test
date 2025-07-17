package com.auth.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * Represents an error response with status, message, timestamp, and optional error details.
 */
public final class ErrorResponse {

    private int status;
    private String message;
    private final LocalDateTime timestamp;
    private List<String> errors;

    /**
     * Default constructor initializing timestamp to current time.
     */
    public ErrorResponse() {
        this.timestamp = LocalDateTime.now();
    }

    /**
     * Constructs an error response with status and message.
     *
     * @param status  the HTTP status code
     * @param message the error message
     */
    public ErrorResponse(int status, String message) {
        this();
        this.status = status;
        this.message = Objects.requireNonNull(message, "message must not be null");
    }

    /**
     * Constructs an error response with status, message, and error details.
     *
     * @param status  the HTTP status code
     * @param message the error message
     * @param errors  the list of detailed errors
     */
    public ErrorResponse(int status, String message, List<String> errors) {
        this(status, message);
        this.errors = errors;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = Objects.requireNonNull(message, "message must not be null");
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
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
}