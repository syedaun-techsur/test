package com.auth.dto;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

/**
 * Represents a standardized error response structure.
 */
public final class ErrorResponse {
    private final int status;
    private final String message;
    private final LocalDateTime timestamp;
    private final List<String> errors;

    /**
     * Constructs an ErrorResponse with status and message, timestamp is set to now,
     * and errors list is empty.
     *
     * @param status  the HTTP status code
     * @param message the error message
     */
    public ErrorResponse(int status, String message) {
        this(status, message, Collections.emptyList());
    }

    /**
     * Constructs an ErrorResponse with status, message, list of errors,
     * and timestamp set to now.
     *
     * @param status  the HTTP status code
     * @param message the error message
     * @param errors  a list of detailed error messages, can be empty
     */
    public ErrorResponse(int status, String message, List<String> errors) {
        this.status = status;
        this.message = message;
        this.timestamp = LocalDateTime.now();
        this.errors = errors == null ? Collections.emptyList() : List.copyOf(errors);
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

    /**
     * Returns an unmodifiable list of errors.
     *
     * @return unmodifiable list of error messages
     */
    public List<String> getErrors() {
        return errors;
    }
}