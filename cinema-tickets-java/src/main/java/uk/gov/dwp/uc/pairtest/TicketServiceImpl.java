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
    
        validateAccountId(accountId);

        validateTicketRequests(ticketTypeRequests);

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

        validateTotalNumberOfTickets(totalTickets);

        validateAdultPresence(adultTickets, childTickets, infantTickets);

        validateInfantToAdultRatio(adultTickets, infantTickets);

        validateAtLeastOneTicketPurchased(totalTickets);

        logPurchaseDetails(accountId, totalTickets, totalAmount, totalSeats, adultTickets, childTickets, infantTickets);
        
        // If all validations pass, proceed with the purchase
        // Make payment request
        paymentService.makePayment(accountId, totalAmount);

        // Make seat reservation request
        reservationService.reserveSeat(accountId, totalSeats);
        
        System.out.println("Ticket Service completed:");
    }

    private void validateAccountId(Long accountId) throws InvalidPurchaseException {
        if (accountId == null || accountId <= 0) {
            throw new InvalidPurchaseException("Invalid account ID");
        }
    }

    private void validateTicketRequests(TicketTypeRequest... ticketTypeRequests) throws InvalidPurchaseException {
        if (ticketTypeRequests == null || ticketTypeRequests.length == 0) {
            throw new InvalidPurchaseException("No ticket requests provided");
        }
    }

    private void validateTotalNumberOfTickets(int totalTickets) throws InvalidPurchaseException {
        if (totalTickets > MAX_TICKETS_PER_PURCHASE) {
            throw new InvalidPurchaseException("Maximum of " + MAX_TICKETS_PER_PURCHASE + " tickets per purchase");
        }
    }

    private void validateAdultPresence(int adultTickets, int childTickets, int infantTickets) throws InvalidPurchaseException {
        //Ensure at least one adult ticket is purchased with child/infant tickets
        if (adultTickets == 0 && (childTickets > 0 || infantTickets > 0)) {
            throw new InvalidPurchaseException("Child or Infant tickets cannot be purchased without an Adult ticket");
        }
    }

    private void validateInfantToAdultRatio(int adultTickets, int infantTickets) throws InvalidPurchaseException {
        //Every infant must be accompanied by an adult - as they are seated on adults lap
        if (infantTickets > adultTickets) {
            throw new InvalidPurchaseException("Each infant must be accompanied by an adult");
        }
    }

    private void validateAtLeastOneTicketPurchased(int totalTickets) throws InvalidPurchaseException {
        if (totalTickets == 0) {
            throw new InvalidPurchaseException("At least one ticket must be purchased");
        }
    }

    private void logPurchaseDetails(Long accountId, int totalTickets, int totalAmount, int totalSeats,
                                    int adultTickets, int childTickets, int infantTickets) {
        System.out.println("\nPayment processed: Account ID = " + accountId);
        System.out.println("Total number of tickets: " + totalTickets);
        System.out.println("Total amount to pay: " + totalAmount);
        System.out.println("Total seats to reserve: " + totalSeats);
        System.out.println("Tickets comprise of: ");
        System.out.println("   Adult Tickets: " + adultTickets);
        System.out.println("   Child Tickets: " + childTickets);
        System.out.println("   Infant Tickets: " + infantTickets);
    }
}
