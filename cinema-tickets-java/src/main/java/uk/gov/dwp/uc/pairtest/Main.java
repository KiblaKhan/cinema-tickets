package uk.gov.dwp.uc.pairtest;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import thirdparty.paymentgateway.TicketPaymentService;
import thirdparty.paymentgateway.TicketPaymentServiceImpl;
import thirdparty.seatbooking.SeatReservationService;
import thirdparty.seatbooking.SeatReservationServiceImpl;
import uk.gov.dwp.uc.pairtest.domain.TicketType;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;

public class Main {
    public static void main(String[] args) {
        
        TicketPaymentService paymentService = new TicketPaymentServiceImpl();
        SeatReservationService reservationService = new SeatReservationServiceImpl();

        // Create an instance of TicketServiceImpl
        TicketServiceImpl ticketService = new TicketServiceImpl(paymentService, reservationService);

        // Create a Scanner to read user input
        Scanner scanner = new Scanner(System.in);

        try {
            // Prompt for account ID
            System.out.print("Enter account ID: ");
            Long accountId = scanner.nextLong();

            // Prompt for number of adult tickets
            System.out.print("Enter number of adult tickets: ");
            int adultTickets = scanner.nextInt();

            // Prompt for number of child tickets
            System.out.print("Enter number of child tickets: ");
            int childTickets = scanner.nextInt();

            // Prompt for number of infant tickets
            System.out.print("Enter number of infant tickets: ");
            int infantTickets = scanner.nextInt();

            // Create TicketTypeRequest objects
            List<TicketTypeRequest> ticketRequests = new ArrayList<>();
            ticketRequests.add(new TicketTypeRequest(TicketType.ADULT, adultTickets));
            ticketRequests.add(new TicketTypeRequest(TicketType.CHILD, childTickets));
            ticketRequests.add(new TicketTypeRequest(TicketType.INFANT, infantTickets));

            // Call the purchaseTickets method
            ticketService.purchaseTickets(accountId, ticketRequests.toArray(new TicketTypeRequest[0]));

            System.out.println("Purchase successful!");
        } catch (IllegalArgumentException | InvalidPurchaseException e) {
            System.err.println("Error Invalid Purchase: " + e.getMessage());
        } finally {
            scanner.close();
        }
    }
}
