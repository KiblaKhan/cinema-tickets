package uk.gov.dwp.uc.pairtest.domain;

/**
 * Represents type of ticket and its associated price.
 * 
 */
public enum TicketType {
    INFANT(0),
    CHILD(15),
    ADULT(25);

    private final int price;

    /**
     * Constructs a TicketType with the price.
     *
     * @param price The price associated with the ticket type.
     */
    TicketType(int price) {
        this.price = price;
    }

    /**
     * Returns the price of the ticket type.
     *
     * @return The price of the ticket type.
     */
    public int getPrice() {
        return price;
    }
}
