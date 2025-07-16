package solutions.techsur.common.microservice.exceptions;

import org.springframework.http.HttpStatus;

import java.io.Serializable;

public interface Reason extends Serializable {

    String getMessage();

    default String getMessage(String... messageParam) {
        return messageParam == null || messageParam.length == 0 ? getMessage() : String.format(getMessage(), messageParam);
    }

    HttpStatus getStatus();

    int getErrorCode();

    String name();
}
