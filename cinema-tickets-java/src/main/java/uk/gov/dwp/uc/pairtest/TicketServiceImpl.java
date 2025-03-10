package uk.gov.dwp.uc.pairtest;

import thirdparty.paymentgateway.TicketPaymentService;
import thirdparty.seatbooking.SeatReservationService;
import uk.gov.dwp.uc.pairtest.domain.TicketType;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;

public class TicketServiceImpl implements TicketService {

    private static final int MAX_TICKETS_PER_PURCHASE = 25;

    private final TicketPaymentService paymentService;
    private final SeatReservationService reservationService;

    // Constructor for dependency injection
    public TicketServiceImpl(TicketPaymentService paymentService, SeatReservationService reservationService) {
        this.paymentService = paymentService;
        this.reservationService = reservationService;
    }

    @Override
    public void purchaseTickets(Long accountId, TicketTypeRequest... ticketTypeRequests) throws InvalidPurchaseException {
        // Validate account ID
        if (accountId == null || accountId <= 0) {
            throw new InvalidPurchaseException("Invalid account ID");
        }

        // Validate ticket requests
        if (ticketTypeRequests == null || ticketTypeRequests.length == 0) {
            throw new InvalidPurchaseException("No ticket requests provided");
        }

        int totalTickets = 0;
        int totalAmount = 0;
        int totalSeats = 0;
        int adultTickets = 0;
        int childTickets = 0;
        int infantTickets = 0;

        // Calculate totals
        for (TicketTypeRequest request : ticketTypeRequests) {
            totalTickets += request.getNoOfTickets();

            switch (request.getTicketType()) {
                case ADULT:
                    adultTickets += request.getNoOfTickets();
                    totalAmount += request.getNoOfTickets() * TicketType.ADULT.getPrice();
                    totalSeats += request.getNoOfTickets();
                    break;
                case CHILD:
                    childTickets += request.getNoOfTickets();
                    totalAmount += request.getNoOfTickets() * TicketType.CHILD.getPrice();
                    totalSeats += request.getNoOfTickets();
                    break;
                case INFANT:
                    infantTickets += request.getNoOfTickets();
                    // Infants do not pay or occupy a seat
                    break;
            }
        }

        // Validate total tickets
        if (totalTickets > MAX_TICKETS_PER_PURCHASE) {
            throw new InvalidPurchaseException("Maximum of " + MAX_TICKETS_PER_PURCHASE + " tickets per purchase");
        }

        // Validate adult presence
        if (adultTickets == 0 && (childTickets > 0 || infantTickets > 0)) {
            throw new InvalidPurchaseException("Child or Infant tickets cannot be purchased without an Adult ticket");
        }

        // Validate infant to adult ratio
        if (infantTickets > adultTickets) {
            throw new InvalidPurchaseException("Each infant must be accompanied by an adult");
        }

        // Validate zero tickets
        if (totalTickets == 0) {
            throw new InvalidPurchaseException("At least one ticket must be purchased");
        }

        System.out.println("Total amount to pay: " + totalAmount);
        System.out.println("Total seats to reserve: " + totalSeats);
        
        // If all validations pass, proceed with the purchase
        // Make payment request
        paymentService.makePayment(accountId, totalAmount);

        // Make seat reservation request
        reservationService.reserveSeat(accountId, totalSeats);
        
        System.out.println("Purchase successful:");
    }
}
