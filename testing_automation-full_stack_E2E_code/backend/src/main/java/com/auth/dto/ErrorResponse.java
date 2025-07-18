package com.auth.dto;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Represents a standard error response that can be returned by the API.
 * Encapsulates HTTP status, error message, timestamp, and detailed errors.
 */
public class ErrorResponse {
    private int status;
    private String message;
    private LocalDateTime timestamp;
    private List<String> errors;

    /**
     * Default constructor initializing timestamp to current time.
     */
    public ErrorResponse() {
        this.timestamp = LocalDateTime.now();
    }

    /**
     * Constructs an error response with a status and message.
     * 
     * @param status  HTTP status code
     * @param message Error message
     */
    public ErrorResponse(int status, String message) {
        this();
        this.status = status;
        this.message = message;
    }

    /**
     * Constructs an error response with a status, message, and list of error details.
     * 
     * @param status  HTTP status code
     * @param message Error message
     * @param errors  List of detailed error strings
     */
    public ErrorResponse(int status, String message, List<String> errors) {
        this(status, message);
        this.errors = errors;
    }

    /**
     * Gets the HTTP status code.
     *
     * @return the status code
     */
    public int getStatus() {
        return status;
    }

    /**
     * Sets the HTTP status code.
     *
     * @param status the status code
     */
    public void setStatus(int status) {
        this.status = status;
    }

    /**
     * Gets the error message.
     *
     * @return the error message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Sets the error message.
     *
     * @param message the error message
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Gets the timestamp when the error occurred.
     *
     * @return the timestamp
     */
    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    /**
     * Sets the timestamp when the error occurred.
     *
     * @param timestamp the timestamp
     */
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * Gets the list of detailed error strings.
     *
     * @return the list of errors
     */
    public List<String> getErrors() {
        return errors;
    }

    /**
     * Sets the list of detailed error strings.
     *
     * @param errors the list of errors
     */
    public void setErrors(List<String> errors) {
        this.errors = errors;
    }
}