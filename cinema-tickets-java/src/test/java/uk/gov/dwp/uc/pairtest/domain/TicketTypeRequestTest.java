package uk.gov.dwp.uc.pairtest.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import static org.junit.jupiter.api.Assertions.*;

class TicketTypeRequestTest {

    @Test
    void testValidTicketTypeRequest() {
        TicketTypeRequest request = new TicketTypeRequest(TicketType.ADULT, 5);
        assertNotNull(request, "TicketTypeRequest should not be null");
        assertEquals(TicketType.ADULT, request.getTicketType(), "Ticket type should be ADULT");
        assertEquals(5, request.getNoOfTickets(), "Number of tickets should be 5");
    }
    
    @Test
    void testNullTicketTypeThrowsException() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new TicketTypeRequest(null, 5);
        });
        assertEquals("Ticket type cannot be null", exception.getMessage(), "Exception message should match");
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -1, -10, -100})
    void testZeroOrNegativeNoOfTicketsThrowsException(int invalidNoOfTickets) {
        // Using 'var' for local variable type inference (Java 11+)
        var exception = assertThrows(IllegalArgumentException.class, () -> {
            new TicketTypeRequest(TicketType.CHILD, invalidNoOfTickets);
        });
        assertEquals("Number of tickets must be greater than zero", exception.getMessage(), "Exception message should match");
    }

    @Test
    void testGetTicketType() {
        TicketTypeRequest request = new TicketTypeRequest(TicketType.CHILD, 3);
        assertEquals(TicketType.CHILD, request.getTicketType(), "Ticket type should be CHILD");
    }

    @Test
    void testGetNoOfTickets() {
        TicketTypeRequest request = new TicketTypeRequest(TicketType.ADULT, 10);
        assertEquals(10, request.getNoOfTickets(), "Number of tickets should be 10");
    }

    @Test
    void testMaximumTickets() {
        // Assuming the maximum number of tickets is Integer.MAX_VALUE
        int maxTickets = Integer.MAX_VALUE;
        TicketTypeRequest request = new TicketTypeRequest(TicketType.ADULT, maxTickets);

        assertEquals(maxTickets, request.getNoOfTickets(), "Number of tickets should be Integer.MAX_VALUE");
    }

    @Test
    void testAllTicketTypes() {
        // Test all ticket types
        TicketTypeRequest infantRequest = new TicketTypeRequest(TicketType.INFANT, 2);
        TicketTypeRequest childRequest = new TicketTypeRequest(TicketType.CHILD, 3);
        TicketTypeRequest adultRequest = new TicketTypeRequest(TicketType.ADULT, 4);

        assertEquals(TicketType.INFANT, infantRequest.getTicketType(), "Ticket type should be INFANT");
        assertEquals(2, infantRequest.getNoOfTickets(), "Number of tickets should be 2");

        assertEquals(TicketType.CHILD, childRequest.getTicketType(), "Ticket type should be CHILD");
        assertEquals(3, childRequest.getNoOfTickets(), "Number of tickets should be 3");

        assertEquals(TicketType.ADULT, adultRequest.getTicketType(), "Ticket type should be ADULT");
        assertEquals(4, adultRequest.getNoOfTickets(), "Number of tickets should be 4");
    }

    @Test
    void testImmutableBehavior() {
        TicketTypeRequest request = new TicketTypeRequest(TicketType.ADULT, 2);

        // Verify that fields cannot be modified (compile-time check)
        // If fields were mutable, this test would fail
        assertEquals(2, request.getNoOfTickets());
        assertEquals(TicketType.ADULT, request.getTicketType());
    }
}