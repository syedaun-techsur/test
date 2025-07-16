package solutions.techsur.common.microservice.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * Enum representing application-specific error reasons with associated HTTP status, error code, and message.
 */
@Getter
@AllArgsConstructor
public enum AppReason implements Reason {

    INVALID_FILE(HttpStatus.BAD_REQUEST, 100, "File has the following problems: [%s]"),
    INTERNAL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, 101, "An internal error has occurred"),
    RFP_DOCUMENT_NOT_FOUND(HttpStatus.NOT_FOUND, 102, "RFP document not found"),
    RFP_NOT_FOUND(HttpStatus.NOT_FOUND, 103, "Request for proposal not found"),
    RESPONSE_OUTLINE_NOT_FOUND(HttpStatus.NOT_FOUND, 104, "Response outline not found"),
    RESPONSES_NOT_FOUND(HttpStatus.NOT_FOUND, 105, "Responses not found"),
    COMPLIANCE_MATRIX_NOT_FOUND(HttpStatus.NOT_FOUND, 106, "Compliance matrix not found");

    private final HttpStatus status;
    private final int errorCode;
    private final String message;

    @Override
    public HttpStatus getStatus() {
        return status;
    }

    @Override
    public int getErrorCode() {
        return errorCode;
    }

    @Override
    public String getMessage() {
        return message;
    }
}