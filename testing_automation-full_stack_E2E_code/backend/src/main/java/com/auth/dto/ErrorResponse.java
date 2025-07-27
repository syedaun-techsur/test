package com.auth.dto;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Represents a standardized error response containing status, message,
 * timestamp and optional error details.
 */
public final class ErrorResponse {
    private final int status;
    private final String message;
    private final LocalDateTime timestamp;
    private final List<String> errors;

    /**
     * Constructs an ErrorResponse with status and message.
     * Timestamp is set to current time. Errors list is null.
     *
     * @param status  HTTP status code
     * @param message error message
     */
    public ErrorResponse(int status, String message) {
        this(status, message, null);
    }

    /**
     * Constructs an ErrorResponse with status, message, and list of errors.
     * Timestamp is set to current time.
     *
     * @param status  HTTP status code
     * @param message error message
     * @param errors  list of error details, or null if none
     */
    public ErrorResponse(int status, String message, List<String> errors) {
        this.status = status;
        this.message = Objects.requireNonNull(message, "message must not be null");
        this.timestamp = LocalDateTime.now();
        this.errors = errors == null ? null : Collections.unmodifiableList(List.copyOf(errors));
    }

    /**
     * Gets the HTTP status code of the error response.
     *
     * @return status code
     */
    public int getStatus() {
        return status;
    }

    /**
     * Gets the message describing the error.
     *
     * @return error message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Gets the timestamp when the error response was created.
     *
     * @return timestamp
     */
    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    /**
     * Gets an unmodifiable list of error details.
     *
     * @return unmodifiable list of error strings, or null if none
     */
    public List<String> getErrors() {
        return errors;
    }
}