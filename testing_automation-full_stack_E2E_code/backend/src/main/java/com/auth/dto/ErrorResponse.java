package com.auth.dto;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Represents a standardized error response containing HTTP status, message,
 * timestamp, and optional detailed errors.
 */
public final class ErrorResponse {
    private final int status;
    private final String message;
    private final LocalDateTime timestamp;
    private final List<String> errors;

    /**
     * Creates an ErrorResponse with status and message.
     *
     * @param status  HTTP status code
     * @param message Error message
     */
    public ErrorResponse(int status, String message) {
        this(status, message, Collections.emptyList());
    }

    /**
     * Creates an ErrorResponse with status, message, and detailed errors.
     *
     * @param status  HTTP status code
     * @param message Error message
     * @param errors  List of detailed error messages
     */
    public ErrorResponse(int status, String message, List<String> errors) {
        this.status = status;
        this.message = Objects.requireNonNull(message, "message cannot be null");
        this.timestamp = LocalDateTime.now();
        this.errors = errors == null ? Collections.emptyList() : List.copyOf(errors);
    }

    /**
     * Returns the HTTP status code.
     *
     * @return status code
     */
    public int getStatus() {
        return status;
    }

    /**
     * Returns the error message.
     *
     * @return error message string
     */
    public String getMessage() {
        return message;
    }

    /**
     * Returns the timestamp when the error was created.
     *
     * @return timestamp of the error
     */
    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    /**
     * Returns an unmodifiable list of detailed error messages.
     *
     * @return list of errors, never null
     */
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
}