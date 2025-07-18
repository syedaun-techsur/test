package com.auth.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

/**
 * DTO representing an error response to be sent to clients.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class ErrorResponse {

    private int status;
    private String message;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private final LocalDateTime timestamp;

    private List<String> errors;

    /**
     * Default constructor initializes timestamp and empty errors list.
     */
    public ErrorResponse() {
        this.timestamp = LocalDateTime.now();
        this.errors = Collections.emptyList();
    }

    /**
     * Constructor with status and message.
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
     * Constructor with status, message, and errors list.
     * 
     * @param status  HTTP status code
     * @param message Error message
     * @param errors  List of detailed error messages
     */
    public ErrorResponse(int status, String message, List<String> errors) {
        this(status, message);
        this.errors = errors == null ? Collections.emptyList() : List.copyOf(errors);
    }

    // Getters and Setters

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

    public List<String> getErrors() {
        return errors == null ? Collections.emptyList() : List.copyOf(errors);
    }

    public void setErrors(List<String> errors) {
        this.errors = errors == null ? Collections.emptyList() : List.copyOf(errors);
    }
}