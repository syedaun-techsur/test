package com.auth.dto;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Represents an error response with HTTP status, message, timestamp, and detailed errors.
 */
public final class ErrorResponse {

    private final int status;
    private final String message;
    private final LocalDateTime timestamp;
    private final List<String> errors;

    /**
     * Default constructor initializing timestamp.
     */
    public ErrorResponse() {
        this.status = 0;
        this.message = null;
        this.timestamp = LocalDateTime.now();
        this.errors = Collections.emptyList();
    }

    /**
     * Constructs an ErrorResponse with status and message.
     * 
     * @param status  the HTTP status code
     * @param message the error message
     */
    public ErrorResponse(int status, String message) {
        this(status, message, null);
    }

    /**
     * Constructs an ErrorResponse with status, message, and a list of errors.
     * 
     * @param status  the HTTP status code
     * @param message the error message
     * @param errors  a list of detailed error messages; if null, treated as empty list
     */
    public ErrorResponse(int status, String message, List<String> errors) {
        this.status = status;
        this.message = message;
        this.timestamp = LocalDateTime.now();
        this.errors = errors == null ? Collections.emptyList() : List.copyOf(errors);
    }

    /**
     * Returns the HTTP status code.
     * 
     * @return the status
     */
    public int getStatus() {
        return status;
    }

    /**
     * Returns the error message.
     * 
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Returns the timestamp when the error response was created.
     * 
     * @return the timestamp
     */
    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    /**
     * Returns an unmodifiable view of the detailed errors.
     * 
     * @return the errors list
     */
    public List<String> getErrors() {
        return Collections.unmodifiableList(errors);
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ErrorResponse)) return false;
        ErrorResponse that = (ErrorResponse) o;
        return status == that.status &&
                Objects.equals(message, that.message) &&
                Objects.equals(timestamp, that.timestamp) &&
                Objects.equals(errors, that.errors);
    }

    @Override
    public int hashCode() {
        return Objects.hash(status, message, timestamp, errors);
    }
}