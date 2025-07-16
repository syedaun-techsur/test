package solutions.techsur.common.microservice.exceptions;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

@Getter
@Slf4j
public class AppException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    private static final String GLOBAL_MESSAGE_PREFIX = "----> Exception global message: ";
    private final Reason reason;

    public AppException(Reason reason) {
        this(reason, new String[0]);
    }

    public AppException(Reason reason, String... messageParam) {
        super(buildMessage(reason, messageParam));
        this.reason = Objects.requireNonNull(reason, "Reason must not be null");
        logExceptionMessage(this.getMessage(), this);
    }

    private static String buildMessage(Reason reason, String... messageParam) {
        if (reason == null) {
            return "Unknown reason";
        }
        if (messageParam == null || messageParam.length == 0) {
            return reason.getMessage();
        } else {
            return reason.getMessage(messageParam);
        }
    }

    private void logExceptionMessage(String message, Throwable throwable) {
        if (message != null) {
            log.warn(GLOBAL_MESSAGE_PREFIX + message, throwable);
        } else {
            log.warn(GLOBAL_MESSAGE_PREFIX + "No message available", throwable);
        }
    }
}