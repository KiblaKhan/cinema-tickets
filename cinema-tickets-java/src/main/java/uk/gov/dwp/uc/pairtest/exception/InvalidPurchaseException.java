package uk.gov.dwp.uc.pairtest.exception;

/**
 * Custom unchecked exception to indicate invalid ticket purchase requests.
 * 
 * Improvements to InvalidPurchaseException:
 * - accept custom error message - makes debugging easier
 * - accept custom error message and a cause for exception chaining - useful for debugging and logging
 */
public class InvalidPurchaseException extends RuntimeException {

    /**
     * Constructs a new InvalidPurchaseException with a custom error message.
     *
     * @param message the detail message
     */
    public InvalidPurchaseException(String message) {
        super(message);
    }

    /**
     * Constructs a new InvalidPurchaseException with a custom error message and a cause.
     *
     * @param message the detail message
     * @param cause   the cause of the exception
     */
    public InvalidPurchaseException(String message, Throwable cause) {
        super(message, cause);
    }
}
