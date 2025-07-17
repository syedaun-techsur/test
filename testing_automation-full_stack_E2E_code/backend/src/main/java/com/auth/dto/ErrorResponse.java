package com.auth.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

/**
 * Represents an error response with HTTP status, message, timestamp, and optional detailed errors.
 */
public class ErrorResponse {

    private int status;
    private String message;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;

    private List<String> errors;

    /**
     * Default constructor initializing timestamp and empty errors list.
     */
    public ErrorResponse() {
        this.timestamp = LocalDateTime.now();
        this.errors = Collections.emptyList();
    }

    /**
     * Constructor initializing status and message with current timestamp.
     *
     * @param status  the HTTP status code
     * @param message the error message
     */
    public ErrorResponse(int status, String message) {
        this();
        this.status = status;
        this.message = message;
    }

    /**
     * Constructor initializing status, message, and errors with current timestamp.
     * Creates an unmodifiable copy of errors list to ensure immutability.
     *
     * @param status  the HTTP status code
     * @param message the error message
     * @param errors  the list of detailed errors
     */
    public ErrorResponse(int status, String message, List<String> errors) {
        this(status, message);
        this.errors = errors == null ? Collections.emptyList() : Collections.unmodifiableList(errors);
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
        this.message = message;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors == null ? Collections.emptyList() : Collections.unmodifiableList(errors);
    }
}