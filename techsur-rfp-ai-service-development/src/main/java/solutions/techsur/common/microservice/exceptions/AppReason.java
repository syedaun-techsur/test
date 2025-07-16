package solutions.techsur.common.microservice.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * Enum representing application-specific reasons for exceptions with 
 * associated HTTP status codes, error codes, and error messages.
 */
@Getter
@AllArgsConstructor
public enum AppReason implements Reason {

    INVALID_FILE(HttpStatus.BAD_REQUEST, 100, "File is having following problems [%s]"),
    INTERNAL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, 101, "An internal error has occurred"),
    RFP_DOCUMENT_NOT_FOUND(HttpStatus.NOT_FOUND, 102, "RFP Document not found"),
    RFP_NOT_FOUND(HttpStatus.NOT_FOUND, 103, "Request for proposal not found"),
    RESPONSE_OUTLINE_NOT_FOUND(HttpStatus.NOT_FOUND, 104, "Response outline not found"),
    RESPONSES_NOT_FOUND(HttpStatus.NOT_FOUND, 105, "Responses not found"),
    COMPLIANCE_MATRIX_NOT_FOUND(HttpStatus.NOT_FOUND, 106, "Compliance matrix not found");

    private final HttpStatus status;
    private final int errorCode;
    private final String message;

    /**
     * Returns the formatted message with specified arguments.
     *
     * @param args Arguments to format the message string.
     * @return Formatted message string.
     */
    public String getFormattedMessage(Object... args) {
        return String.format(this.message, args);
    }
}