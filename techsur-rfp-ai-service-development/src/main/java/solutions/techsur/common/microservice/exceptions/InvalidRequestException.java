package solutions.techsur.common.microservice.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception thrown when a request is invalid.
 * Responds with HTTP status 400 Bad Request.
 */
@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class InvalidRequestException extends RuntimeException {

    public InvalidRequestException(String message) {
        super(message);
    }

    public InvalidRequestException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidRequestException(Throwable cause) {
        super(cause);
    }

}