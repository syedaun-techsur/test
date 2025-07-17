package com.auth.dto;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

/**
 * Error response DTO to encapsulate error details returned by API endpoints.
 */
public class ErrorResponse {
    private final int status;
    private final String message;
    private final LocalDateTime timestamp;
    private final List<String> errors;

    /**
     * Constructs an ErrorResponse with status and message.
     * Timestamp is set to current time.
     * Errors list is empty.
     * 
     * @param status HTTP status code
     * @param message error message
     */
    public ErrorResponse(int status, String message) {
        this(status, message, Collections.emptyList(), LocalDateTime.now());
    }

    /**
     * Constructs an ErrorResponse with status, message, and a list of errors.
     * Timestamp is set to current time.
     * 
     * @param status HTTP status code
     * @param message error message
     * @param errors list of specific errors
     */
    public ErrorResponse(int status, String message, List<String> errors) {
        this(status, message, errors, LocalDateTime.now());
    }

    /**
     * Primary constructor to set all fields, including timestamp.
     * 
     * @param status HTTP status code
     * @param message error message
     * @param errors list of specific errors
     * @param timestamp time of error occurrence
     */
    private ErrorResponse(int status, String message, List<String> errors, LocalDateTime timestamp) {
        this.status = status;
        this.message = message;
        this.errors = errors != null ? errors : Collections.emptyList();
        this.timestamp = timestamp != null ? timestamp : LocalDateTime.now();
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
}