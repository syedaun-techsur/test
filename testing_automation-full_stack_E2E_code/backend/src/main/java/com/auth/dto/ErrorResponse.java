package com.auth.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class ErrorResponse {
    private final int status;
    private final String message;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "UTC")
    private final LocalDateTime timestamp;

    private final List<String> errors;

    /**
     * Private default constructor to prevent incomplete initialization.
     */
    private ErrorResponse() {
        this.status = 0;
        this.message = null;
        this.errors = null;
        this.timestamp = LocalDateTime.now();
    }

    /**
     * Constructs an ErrorResponse with status and message.
     *
     * @param status  the HTTP status code
     * @param message the error message
     */
    public ErrorResponse(int status, String message) {
        this.status = status;
        this.message = message;
        this.errors = null;
        this.timestamp = LocalDateTime.now();
    }

    /**
     * Constructs an ErrorResponse with status, message, and a list of detailed errors.
     *
     * @param status  the HTTP status code
     * @param message the error message
     * @param errors  list of detailed error messages
     */
    public ErrorResponse(int status, String message, List<String> errors) {
        this.status = status;
        this.message = message;
        this.errors = errors;
        this.timestamp = LocalDateTime.now();
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