package com.auth.dto;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class ErrorResponse {
    private final int status;
    private final String message;
    private final LocalDateTime timestamp;
    private final List<String> errors;

    public ErrorResponse(int status, String message) {
        this(status, message, Collections.emptyList());
    }

    public ErrorResponse(int status, String message, List<String> errors) {
        this.status = status;
        this.message = Objects.requireNonNull(message, "message must not be null");
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