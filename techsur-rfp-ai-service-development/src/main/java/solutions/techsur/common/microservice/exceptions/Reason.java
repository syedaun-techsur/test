package solutions.techsur.common.microservice.exceptions;

import org.springframework.http.HttpStatus;

import java.io.Serializable;
import java.util.Objects;

public interface Reason extends Serializable {

    String getMessage();

    default String getMessage(String... messageParams) {
        String message = getMessage();
        Objects.requireNonNull(message, "getMessage() cannot return null");
        return (messageParams == null || messageParams.length == 0) ? message : String.format(message, (Object[]) messageParams);
    }

    HttpStatus getStatus();

    int getErrorCode();

    String name();
}