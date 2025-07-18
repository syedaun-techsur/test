package com.auth.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

/**
 * Represents a standardized error response for the API.
 */
public class ErrorResponse {

    private final int status;
    private final String message;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private final LocalDateTime timestamp;

    private final List<String> errors;

    /**
     * Constructs an ErrorResponse with status, message and an optional list of errors.
     *
     * @param status  the HTTP status code
     * @param message the error message
     * @param errors  the list of specific error details, can be null or empty
     */
    public ErrorResponse(int status, String message, List<String> errors) {
        this.status = status;
        this.message = message;
        this.timestamp = LocalDateTime.now();
        this.errors = errors == null ? Collections.emptyList() : errors;
    }

    /**
     * Constructs an ErrorResponse with status and message without any detailed errors.
     *
     * @param status  the HTTP status code
     * @param message the error message
     */
    public ErrorResponse(int status, String message) {
        this(status, message, Collections.emptyList());
    }

    /**
     * Static factory method to create an error response without errors.
     * 
     * @param status the HTTP status code
     * @param message the error message
     * @return a new ErrorResponse instance
     */
    public static ErrorResponse of(int status, String message) {
        return new ErrorResponse(status, message);
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