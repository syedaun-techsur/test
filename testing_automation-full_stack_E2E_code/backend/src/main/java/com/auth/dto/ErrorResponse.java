package com.auth.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents a standardized error response.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {

    @JsonProperty("status")
    private final int status;

    @JsonProperty("message")
    private final String message;

    @JsonProperty("timestamp")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private final LocalDateTime timestamp;

    @JsonProperty("errors")
    private final List<String> errors;

    /**
     * Constructs an ErrorResponse with status and message.
     * Timestamp is set to current time.
     *
     * @param status  the HTTP status code
     * @param message the error message
     */
    public ErrorResponse(int status, String message) {
        this(status, message, null, LocalDateTime.now());
    }

    /**
     * Constructs an ErrorResponse with status, message, and errors list.
     * Timestamp is set to current time.
     *
     * @param status  the HTTP status code
     * @param message the error message
     * @param errors  optional list of detailed error messages
     */
    public ErrorResponse(int status, String message, List<String> errors) {
        this(status, message, errors, LocalDateTime.now());
    }

    /**
     * Full constructor with all fields.
     *
     * @param status    the HTTP status code
     * @param message   the error message
     * @param errors    optional list of detailed error messages
     * @param timestamp the timestamp of the error occurrence
     */
    public ErrorResponse(int status, String message, List<String> errors, LocalDateTime timestamp) {
        if (message == null || message.trim().isEmpty()) {
            throw new IllegalArgumentException("message cannot be null or empty");
        }
        this.status = status;
        this.message = message;
        this.errors = errors;
        this.timestamp = Objects.requireNonNull(timestamp, "timestamp cannot be null");
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