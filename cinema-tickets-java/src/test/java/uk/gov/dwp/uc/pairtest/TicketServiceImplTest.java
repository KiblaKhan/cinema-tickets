package uk.gov.dwp.uc.pairtest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import thirdparty.paymentgateway.TicketPaymentService;
import thirdparty.seatbooking.SeatReservationService;
import uk.gov.dwp.uc.pairtest.domain.TicketType;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.never;

class TicketServiceImplTest {

    private TicketPaymentService paymentService;
    private SeatReservationService reservationService;
    private TicketServiceImpl ticketService;

    @BeforeEach
    void setUp() {
        paymentService = Mockito.mock(TicketPaymentService.class);
        reservationService = Mockito.mock(SeatReservationService.class);
        ticketService = new TicketServiceImpl(paymentService, reservationService);
    }

    // Test valid purchases
    @Test
    void testValidPurchase_AdultOnly() throws InvalidPurchaseException {
        TicketTypeRequest adultRequest = new TicketTypeRequest(TicketType.ADULT, 5);

        ticketService.purchaseTickets(1L, adultRequest);

        verify(paymentService).makePayment(1L, 125); // 5 adults * £25
        verify(reservationService).reserveSeat(1L, 5); // 5 adults
    }

    @Test
    void testValidPurchase_AdultAndChild() throws InvalidPurchaseException {
        TicketTypeRequest adultRequest = new TicketTypeRequest(TicketType.ADULT, 2);
        TicketTypeRequest childRequest = new TicketTypeRequest(TicketType.CHILD, 3);

        ticketService.purchaseTickets(1L, adultRequest, childRequest);

        verify(paymentService).makePayment(1L, 95); // 2 adults * £25 + 3 children * £15
        verify(reservationService).reserveSeat(1L, 5); // 2 adults + 3 children
    }

    @Test
    void testValidPurchase_AdultChildAndInfant() throws InvalidPurchaseException {
        TicketTypeRequest adultRequest = new TicketTypeRequest(TicketType.ADULT, 2);
        TicketTypeRequest childRequest = new TicketTypeRequest(TicketType.CHILD, 1);
        TicketTypeRequest infantRequest = new TicketTypeRequest(TicketType.INFANT, 1);

        ticketService.purchaseTickets(1L, adultRequest, childRequest, infantRequest);

        verify(paymentService).makePayment(1L, 65); // 2 adults * £25 + 1 child * £15
        verify(reservationService).reserveSeat(1L, 3); // 2 adults + 1 child
    }

    @Test
    void testValidPurchase_MaxTickets() throws InvalidPurchaseException {
        TicketTypeRequest adultRequest = new TicketTypeRequest(TicketType.ADULT, 25);

        ticketService.purchaseTickets(1L, adultRequest);

        verify(paymentService).makePayment(1L, 625); // 25 adults * £25
        verify(reservationService).reserveSeat(1L, 25); // 25 adults
    }

    // Test invalid purchases
    @Test
    void testInvalidPurchase_NoAdultWithChild() {
        TicketTypeRequest childRequest = new TicketTypeRequest(TicketType.CHILD, 1);

        assertThrows(InvalidPurchaseException.class, () -> {
            ticketService.purchaseTickets(1L, childRequest);
        });

        // Verify that the payment and reservation services are never called
        verify(paymentService, never()).makePayment(Mockito.anyLong(), Mockito.anyInt());
        verify(reservationService, never()).reserveSeat(Mockito.anyLong(), Mockito.anyInt());
    }

    @Test
    void testInvalidPurchase_NoAdultWithInfant() {
        TicketTypeRequest infantRequest = new TicketTypeRequest(TicketType.INFANT, 1);

        assertThrows(InvalidPurchaseException.class, () -> {
            ticketService.purchaseTickets(1L, infantRequest);
        });

        verify(paymentService, never()).makePayment(Mockito.anyLong(), Mockito.anyInt());
        verify(reservationService, never()).reserveSeat(Mockito.anyLong(), Mockito.anyInt());
    }

    @Test
    void testInvalidPurchase_MoreThanMaxTickets() {
        TicketTypeRequest adultRequest = new TicketTypeRequest(TicketType.ADULT, 26);

        assertThrows(InvalidPurchaseException.class, () -> {
            ticketService.purchaseTickets(1L, adultRequest);
        });

        verify(paymentService, never()).makePayment(Mockito.anyLong(), Mockito.anyInt());
        verify(reservationService, never()).reserveSeat(Mockito.anyLong(), Mockito.anyInt());
    }

    @Test
    void testInvalidPurchase_ZeroTickets() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new TicketTypeRequest(TicketType.ADULT, 0);
        });

        // Verify the exception message
        assertEquals("Number of tickets must be greater than zero", exception.getMessage());

        verify(paymentService, never()).makePayment(Mockito.anyLong(), Mockito.anyInt());
        verify(reservationService, never()).reserveSeat(Mockito.anyLong(), Mockito.anyInt());
    }

    @Test
    void testInvalidPurchase_InfantsExceedAdults() {
        TicketTypeRequest adultRequest = new TicketTypeRequest(TicketType.ADULT, 1);
        TicketTypeRequest infantRequest = new TicketTypeRequest(TicketType.INFANT, 2);

        assertThrows(InvalidPurchaseException.class, () -> {
            ticketService.purchaseTickets(1L, adultRequest, infantRequest);
        });

        verify(paymentService, never()).makePayment(Mockito.anyLong(), Mockito.anyInt());
        verify(reservationService, never()).reserveSeat(Mockito.anyLong(), Mockito.anyInt());
    }

    @Test
    void testInvalidPurchase_NullAccountId() {
        TicketTypeRequest adultRequest = new TicketTypeRequest(TicketType.ADULT, 1);

        assertThrows(InvalidPurchaseException.class, () -> {
            ticketService.purchaseTickets(null, adultRequest);
        });

        verify(paymentService, never()).makePayment(Mockito.anyLong(), Mockito.anyInt());
        verify(reservationService, never()).reserveSeat(Mockito.anyLong(), Mockito.anyInt());
    }

    @Test
    void testInvalidPurchase_InvalidAccountId() {
        TicketTypeRequest adultRequest = new TicketTypeRequest(TicketType.ADULT, 1);

        assertThrows(InvalidPurchaseException.class, () -> {
            ticketService.purchaseTickets(0L, adultRequest);
        });

        verify(paymentService, never()).makePayment(Mockito.anyLong(), Mockito.anyInt());
        verify(reservationService, never()).reserveSeat(Mockito.anyLong(), Mockito.anyInt());
    }

    @Test
    void testInvalidPurchase_NullTicketRequests() {
        assertThrows(InvalidPurchaseException.class, () -> {
            ticketService.purchaseTickets(1L, (TicketTypeRequest[]) null);
        });

        verify(paymentService, never()).makePayment(Mockito.anyLong(), Mockito.anyInt());
        verify(reservationService, never()).reserveSeat(Mockito.anyLong(), Mockito.anyInt());
    }

    @Test
    void testInvalidPurchase_EmptyTicketRequests() {
        assertThrows(InvalidPurchaseException.class, () -> {
            ticketService.purchaseTickets(1L);
        });

        verify(paymentService, never()).makePayment(Mockito.anyLong(), Mockito.anyInt());
        verify(reservationService, never()).reserveSeat(Mockito.anyLong(), Mockito.anyInt());
    }

    // Additional tests for price and seat allocation correctness
    @Test
    void testPriceCalculation_OnlyAdults() throws InvalidPurchaseException {
        TicketTypeRequest adultRequest = new TicketTypeRequest(TicketType.ADULT, 4);

        ticketService.purchaseTickets(1L, adultRequest);

        verify(paymentService).makePayment(1L, 100); // 4 adults * £25 = £100
        verify(reservationService).reserveSeat(1L, 4); // 4 adults = 4 seats
    }

    @Test
    void testPriceCalculation_AdultsAndChildren() throws InvalidPurchaseException {
        TicketTypeRequest adultRequest = new TicketTypeRequest(TicketType.ADULT, 2);
        TicketTypeRequest childRequest = new TicketTypeRequest(TicketType.CHILD, 3);

        ticketService.purchaseTickets(1L, adultRequest, childRequest);

        verify(paymentService).makePayment(1L, 95); // (2 adults * £25) + (3 children * £15) = £95
        verify(reservationService).reserveSeat(1L, 5); // 2 adults + 3 children = 5 seats
    }

    @Test
    void testPriceCalculation_AdultsChildrenAndInfants() throws InvalidPurchaseException {
        TicketTypeRequest adultRequest = new TicketTypeRequest(TicketType.ADULT, 2);
        TicketTypeRequest childRequest = new TicketTypeRequest(TicketType.CHILD, 1);
        TicketTypeRequest infantRequest = new TicketTypeRequest(TicketType.INFANT, 1);

        ticketService.purchaseTickets(1L, adultRequest, childRequest, infantRequest);

        verify(paymentService).makePayment(1L, 65); // (2 adults * £25) + (1 child * £15) + (1 infant * £0) = £65
        verify(reservationService).reserveSeat(1L, 3); // 2 adults + 1 child = 3 seats
    }

    @Test
    void testPriceCalculation_OnlyInfantsWithAdults() throws InvalidPurchaseException {
        TicketTypeRequest adultRequest = new TicketTypeRequest(TicketType.ADULT, 2);
        TicketTypeRequest infantRequest = new TicketTypeRequest(TicketType.INFANT, 2);

        ticketService.purchaseTickets(1L, adultRequest, infantRequest);

        verify(paymentService).makePayment(1L, 50); // (2 adults * £25) + (2 infants * £0) = £50
        verify(reservationService).reserveSeat(1L, 2); // 2 adults = 2 seats
    }

    @Test
    void testSeatAllocation_OnlyChildrenWithAdults() throws InvalidPurchaseException {
        TicketTypeRequest adultRequest = new TicketTypeRequest(TicketType.ADULT, 1);
        TicketTypeRequest childRequest = new TicketTypeRequest(TicketType.CHILD, 3);

        ticketService.purchaseTickets(1L, adultRequest, childRequest);

        verify(paymentService).makePayment(1L, 70); // (1 adult * £25) + (3 children * £15) = £70
        verify(reservationService).reserveSeat(1L, 4); // 1 adult + 3 children = 4 seats
    }

    @Test
    void testSeatAllocation_InfantsDoNotGetSeats() throws InvalidPurchaseException {
        TicketTypeRequest adultRequest = new TicketTypeRequest(TicketType.ADULT, 2);
        TicketTypeRequest infantRequest = new TicketTypeRequest(TicketType.INFANT, 2);

        ticketService.purchaseTickets(1L, adultRequest, infantRequest);

        verify(paymentService).makePayment(1L, 50); // (2 adults * £25) + (2 infants * £0) = £50
        verify(reservationService).reserveSeat(1L, 2); // 2 adults = 2 seats
    }
}
