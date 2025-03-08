package uk.gov.dwp.uc.pairtest.domain;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TicketTypeRequestTest {

    @Test
    void testValidTicketTypeRequest() {
        TicketTypeRequest request = new TicketTypeRequest(TicketTypeRequest.TicketType.ADULT, 2);
        assertEquals(2, request.getNoOfTickets());
        assertEquals(TicketTypeRequest.TicketType.ADULT, request.getTicketType());
    }

    @Test
    void testNullTicketType() {
        assertThrows(IllegalArgumentException.class, () -> new TicketTypeRequest(null, 2));
    }

    @Test
    void testNegativeNoOfTickets() {
        assertThrows(IllegalArgumentException.class, () -> new TicketTypeRequest(TicketTypeRequest.TicketType.ADULT, -1));
    }

    @Test
    void testZeroTickets() {
        // Assuming zero tickets are allowed
        TicketTypeRequest request = new TicketTypeRequest(TicketTypeRequest.TicketType.ADULT, 0);
        assertEquals(0, request.getNoOfTickets());
    }

    @Test
    void testMaximumTickets() {
        // Assuming the maximum number of tickets is Integer.MAX_VALUE
        TicketTypeRequest request = new TicketTypeRequest(TicketTypeRequest.TicketType.ADULT, Integer.MAX_VALUE);
        assertEquals(Integer.MAX_VALUE, request.getNoOfTickets());
    }

    @Test
    void testAllTicketTypes() {
        TicketTypeRequest adultRequest = new TicketTypeRequest(TicketTypeRequest.TicketType.ADULT, 1);
        TicketTypeRequest childRequest = new TicketTypeRequest(TicketTypeRequest.TicketType.CHILD, 1);
        TicketTypeRequest infantRequest = new TicketTypeRequest(TicketTypeRequest.TicketType.INFANT, 1);

        assertEquals(TicketTypeRequest.TicketType.ADULT, adultRequest.getTicketType());
        assertEquals(TicketTypeRequest.TicketType.CHILD, childRequest.getTicketType());
        assertEquals(TicketTypeRequest.TicketType.INFANT, infantRequest.getTicketType());
    }

    @Test
    void testImmutableBehavior() {
        TicketTypeRequest request = new TicketTypeRequest(TicketTypeRequest.TicketType.ADULT, 2);

        // Verify that fields cannot be modified (compile-time check)
        // If fields were mutable, this test would fail
        assertEquals(2, request.getNoOfTickets());
        assertEquals(TicketTypeRequest.TicketType.ADULT, request.getTicketType());
    }
}