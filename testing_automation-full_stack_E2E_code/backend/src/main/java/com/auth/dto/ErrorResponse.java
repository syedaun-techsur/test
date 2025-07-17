package com.auth.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO for representing error responses in the API.
 * Provides status code, message, timestamp, and optional detailed errors.
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ErrorResponse {
    
    @JsonProperty("status")
    private final int status;
    
    @JsonProperty("message")
    private final String message;
    
    @JsonProperty("timestamp")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private final LocalDateTime timestamp;
    
    @JsonProperty("errors")
    private List<String> errors;

    /**
     * Default constructor initializes timestamp to current time.
     * For frameworks requiring default ctor - sets status=0 and message=null.
     */
    public ErrorResponse() {
        this.status = 0;
        this.message = null;
        this.timestamp = LocalDateTime.now();
    }

    /**
     * Construct error response with status and message.
     * @param status HTTP status code
     * @param message descriptive error message
     */
    public ErrorResponse(int status, String message) {
        this.status = status;
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }

    /**
     * Construct error response with status, message, and list of errors.
     * @param status HTTP status code
     * @param message descriptive error message
     * @param errors list of detailed error messages
     */
    public ErrorResponse(int status, String message, List<String> errors) {
        this.status = status;
        this.message = message;
        this.timestamp = LocalDateTime.now();
        this.errors = errors;
    }

    /**
     * @return HTTP status code of the error
     */
    public int getStatus() {
        return status;
    }

    /**
     * @return descriptive error message
     */
    public String getMessage() {
        return message;
    }

    /**
     * @return timestamp when error was created
     */
    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    /**
     * @return list of error details, may be null or empty
     */
    public List<String> getErrors() {
        return errors;
    }

    /**
     * Set list of detailed errors.
     * @param errors list of error messages
     */
    public void setErrors(List<String> errors) {
        this.errors = errors;
    }
}