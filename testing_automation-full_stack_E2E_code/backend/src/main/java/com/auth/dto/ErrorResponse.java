package com.auth.dto;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * DTO for Error Responses
 */
public class ErrorResponse {
    private final int status;
    private final String message;
    private final LocalDateTime timestamp;
    private final List<String> errors;

    /**
     * Default constructor initializing with default values and current timestamp.
     * Errors list initialized as empty.
     */
    public ErrorResponse() {
        this(0, null, Collections.emptyList());
    }

    /**
     * Constructor with status and message.
     *
     * @param status  HTTP status code
     * @param message Error message
     */
    public ErrorResponse(int status, String message) {
        this(status, message, Collections.emptyList());
    }

    /**
     * Constructor with status, message and error details.
     *
     * @param status  HTTP status code
     * @param message Error message
     * @param errors  List of error details
     */
    public ErrorResponse(int status, String message, List<String> errors) {
        this.status = status;
        this.message = message;
        this.timestamp = LocalDateTime.now();
        this.errors = errors == null ? Collections.emptyList() : Collections.unmodifiableList(errors);
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
     * Returns an unmodifiable list of error details.
     *
     * @return List of errors, never null.
     */
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