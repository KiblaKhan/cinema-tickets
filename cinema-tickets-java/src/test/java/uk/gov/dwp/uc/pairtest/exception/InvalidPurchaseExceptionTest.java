package uk.gov.dwp.uc.pairtest.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

class InvalidPurchaseExceptionTest {

    @Test
    void testConstructorWithMessage() {
        String errorMessage = "Invalid purchase request";
        InvalidPurchaseException exception = new InvalidPurchaseException(errorMessage);

        // Verify the message is set correctly
        assertEquals(errorMessage, exception.getMessage());

        // Verify the cause is null (not set)
        assertNull(exception.getCause());
    }

    @Test
    void testConstructorWithMessageAndCause() {
        String errorMessage = "Invalid purchase request";
        Throwable cause = new IllegalArgumentException("Invalid argument");

        InvalidPurchaseException exception = new InvalidPurchaseException(errorMessage, cause);

        // Verify the message is set correctly
        assertEquals(errorMessage, exception.getMessage());

        // Verify the cause is set correctly
        assertEquals(cause, exception.getCause());
    }

    @Test
    void testConstructorWithNullMessage() {
        InvalidPurchaseException exception = new InvalidPurchaseException(null);

        // Verify the message is null
        assertNull(exception.getMessage());

        // Verify the cause is null (not set)
        assertNull(exception.getCause());
    }

    @Test
    void testConstructorWithNullMessageAndCause() {
        Throwable cause = new IllegalArgumentException("Invalid argument");

        InvalidPurchaseException exception = new InvalidPurchaseException(null, cause);

        // Verify the message is null
        assertNull(exception.getMessage());

        // Verify the cause is set correctly
        assertEquals(cause, exception.getCause());
    }
}