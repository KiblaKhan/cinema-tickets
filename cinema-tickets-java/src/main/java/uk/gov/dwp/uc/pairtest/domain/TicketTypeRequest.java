package uk.gov.dwp.uc.pairtest.domain;

/**
 * Immutable Object
 * 
 * To ensure TicketTypeRequest class is immutable updated as follows:
 * - class as final.
 * - fields as final.
 * - validate input parameters in the constructor.
 * - no setters or methods that modify state.
 * 
 * Updated Type to TicketType as more descriptive
 */

public final class TicketTypeRequest {

    private final int noOfTickets;
    private final TicketType ticketType;

    /**
     * Constructs a new TicketTypeRequest.
     *
     * @param ticketType  the type of ticket (must not be null)
     * @param noOfTickets the number of tickets requested (must be non-negative)
     * @throws IllegalArgumentException if ticketType is null or noOfTickets is negative
     */
    public TicketTypeRequest(TicketType ticketType, int noOfTickets) {
        if(ticketType == null) {
            throw new IllegalArgumentException("Ticket type cannot be null");
        }
        if (noOfTickets < 0) {
            throw new IllegalArgumentException("Number of tickets cannot be negative");
        }
        this.ticketType = ticketType;
        this.noOfTickets = noOfTickets;
    }

    public int getNoOfTickets() {
        return noOfTickets;
    }

    public TicketType getTicketType() {
        return ticketType;
    }

    public enum TicketType {
        ADULT, CHILD , INFANT
    }

}
