package solutions.techsur.common.microservice.exceptions;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Slf4j
public class AppException extends RuntimeException {
    private static final String GLOBAL_MESSAGE_PREFIX = "----> Exception global message: ";
    private final Reason reason;

    public AppException(Reason reason) {
        super(reason.getMessage());
        log.warn(GLOBAL_MESSAGE_PREFIX + reason.getMessage());
        this.reason = reason;
    }

    public AppException(Reason reason, String... messageParam) {
        super(reason.getMessage(messageParam));
        log.warn(GLOBAL_MESSAGE_PREFIX + reason.getMessage(messageParam));
        this.reason = reason;
    }

}
