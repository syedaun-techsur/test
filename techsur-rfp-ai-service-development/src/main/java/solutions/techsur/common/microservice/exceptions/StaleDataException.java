package solutions.techsur.common.microservice.exceptions;

/**
 * Exception thrown when stale or outdated data is encountered
 * typically in concurrent or versioned data access scenarios.
 */
public class StaleDataException extends RuntimeException {

    /**
     * Constructs a new StaleDataException with the specified detail message.
     *
     * @param message the detail message
     */
    public StaleDataException(String message) {
        super(message);
    }

    /**
     * Constructs a new StaleDataException with the specified cause.
     *
     * @param cause the cause of the exception
     */
    public StaleDataException(Throwable cause) {
        super(cause);
    }

    /**
     * Constructs a new StaleDataException with the specified detail message and cause.
     *
     * @param message the detail message
     * @param cause   the cause of the exception
     */
    public StaleDataException(String message, Throwable cause) {
        super(message, cause);
    }
}