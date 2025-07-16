package solutions.techsur.common.microservice.exceptions;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * Custom application exception that encapsulates a Reason enum to represent error reasons.
 */
@Getter
@Slf4j
public class AppException extends RuntimeException {
    private static final String GLOBAL_MESSAGE_PREFIX = "----> Exception global message: ";
    private final Reason reason;

    /**
     * Constructs a new AppException with the specified reason.
     *
     * @param reason the reason for this exception
     * @throws IllegalArgumentException if reason is null
     */
    public AppException(Reason reason) {
        super(reason == null ? null : reason.getMessage());
        if (reason == null) {
            throw new IllegalArgumentException("Reason must not be null");
        }
        this.reason = reason;
        log.warn(GLOBAL_MESSAGE_PREFIX + reason.getMessage());
    }

    /**
     * Constructs a new AppException with the specified reason and message parameters.
     *
     * @param reason        the reason for this exception
     * @param messageParams optional parameters for the message
     * @throws IllegalArgumentException if reason is null
     */
    public AppException(Reason reason, String... messageParams) {
        super(reason == null ? null : reason.getMessage(messageParams));
        if (reason == null) {
            throw new IllegalArgumentException("Reason must not be null");
        }
        this.reason = reason;
        log.warn(GLOBAL_MESSAGE_PREFIX + reason.getMessage(messageParams));
    }
}