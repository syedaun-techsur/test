package solutions.techsur.common.microservice.exceptions;

/**
 * Exception indicating that data is stale, usually due to concurrent modification or outdated data version.
 */
public class StaleDataException extends Exception {
    private static final long serialVersionUID = 1L;

    /**
     * Constructs a new StaleDataException with the specified detail message.
     *
     * @param message the detail message
     */
    public StaleDataException(String message) {
        super(message);
    }

    /**
     * Constructs a new StaleDataException with the specified detail message and cause.
     *
     * @param message the detail message
     * @param cause the cause (which is saved for later retrieval)
     */
    public StaleDataException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a new StaleDataException with the specified cause.
     *
     * @param cause the cause (which is saved for later retrieval)
     */
    public StaleDataException(Throwable cause) {
        super(cause);
    }

    /**
     * Constructs a new StaleDataException with the specified detail message,
     * cause, suppression enabled or disabled, and writable stack trace enabled or disabled.
     *
     * @param message the detail message
     * @param cause the cause (which is saved for later retrieval)
     * @param enableSuppression whether or not suppression is enabled
     * @param writableStackTrace whether or not the stack trace should be writable
     */
    protected StaleDataException(String message, Throwable cause,
                                 boolean enableSuppression,
                                 boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}