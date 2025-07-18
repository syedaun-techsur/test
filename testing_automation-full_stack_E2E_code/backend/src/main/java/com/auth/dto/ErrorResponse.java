package com.auth.dto;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * DTO representing a structured error response.
 */
public final class ErrorResponse {
    private int status;
    private String message;
    private LocalDateTime timestamp;
    private List<String> errors;

    /**
     * Default constructor initializes timestamp to current time
     * and errors list to an empty immutable list.
     */
    public ErrorResponse() {
        this.timestamp = LocalDateTime.now();
        this.errors = Collections.emptyList();
    }

    /**
     * Constructs an ErrorResponse with status and message.
     * 
     * @param status  HTTP status code of the error
     * @param message Error message
     */
    public ErrorResponse(int status, String message) {
        this();
        this.status = status;
        this.message = message;
    }

    /**
     * Constructs an ErrorResponse with status, message, and list of errors.
     * 
     * @param status  HTTP status code of the error
     * @param message Error message
     * @param errors  List of detailed errors
     */
    public ErrorResponse(int status, String message, List<String> errors) {
        this(status, message);
        this.errors = errors == null ? Collections.emptyList() : errors;
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
     * Sets the HTTP status code.
     * 
     * @param status HTTP status code to set
     */
    public void setStatus(int status) {
        this.status = status;
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
     * Sets the error message.
     * 
     * @param message error message string to set; must not be null
     */
    public void setMessage(String message) {
        this.message = Objects.requireNonNull(message, "message cannot be null");
    }

    /**
     * Returns the timestamp of the error response.
     * 
     * @return timestamp as LocalDateTime
     */
    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    /**
     * Sets the timestamp of the error response.
     * 
     * @param timestamp timestamp to set; must not be null
     */
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = Objects.requireNonNull(timestamp, "timestamp cannot be null");
    }

    /**
     * Returns the list of detailed error messages.
     * 
     * @return list of error strings
     */
    public List<String> getErrors() {
        return errors;
    }

    /**
     * Sets the list of detailed error messages.
     * 
     * @param errors list of error strings; if null it will be set to empty list
     */
    public void setErrors(List<String> errors) {
        this.errors = errors == null ? Collections.emptyList() : errors;
    }
}