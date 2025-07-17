package com.auth.dto;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

/**
 * Represents an error response with status, message, timestamp, and optional detailed errors.
 */
public class ErrorResponse {
    private final int status;
    private final String message;
    private final LocalDateTime timestamp;
    private final List<String> errors;

    /**
     * Constructs an ErrorResponse with status and message.
     * Timestamp is set to current time.
     *
     * @param status  the HTTP status code
     * @param message the error message
     */
    public ErrorResponse(int status, String message) {
        this(status, message, null);
    }

    /**
     * Constructs an ErrorResponse with status, message, and detailed errors.
     * Timestamp is set to current time.
     *
     * @param status  the HTTP status code
     * @param message the error message
     * @param errors  a list of detailed error messages (can be null)
     */
    public ErrorResponse(int status, String message, List<String> errors) {
        this.status = status;
        this.message = message;
        this.timestamp = LocalDateTime.now();
        if (errors == null) {
            this.errors = Collections.emptyList();
        } else {
            this.errors = Collections.unmodifiableList(new ArrayList<>(errors));
        }
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
}