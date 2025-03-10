package uk.gov.dwp.uc.pairtest.domain;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TicketTypeTest {

    @Test
    void testInfantTicketType() {
        assertEquals(0, TicketType.INFANT.getPrice(), "Infant ticket price should be 0");
    }

    @Test
    void testChildTicketType() {
        assertEquals(15, TicketType.CHILD.getPrice(), "Child ticket price should be 15");
    }

    @Test
    void testAdultTicketType() {
        assertEquals(25, TicketType.ADULT.getPrice(), "Adult ticket price should be 25");
    }

    @Test
    void testTicketTypeValues() {
        // Test all enum values
        TicketType[] expectedValues = {TicketType.INFANT, TicketType.CHILD, TicketType.ADULT};
        assertArrayEquals(expectedValues, TicketType.values(), "TicketType should have INFANT, CHILD, and ADULT values");
    }

    @Test
    void testTicketTypeValueOf() {
        assertEquals(TicketType.INFANT, TicketType.valueOf("INFANT"), "valueOf should return INFANT for 'INFANT'");
        assertEquals(TicketType.CHILD, TicketType.valueOf("CHILD"), "valueOf should return CHILD for 'CHILD'");
        assertEquals(TicketType.ADULT, TicketType.valueOf("ADULT"), "valueOf should return ADULT for 'ADULT'");
    }

    @Test
    void testTicketTypeToString() {
        assertEquals("INFANT", TicketType.INFANT.toString(), "toString should return 'INFANT' for INFANT");
        assertEquals("CHILD", TicketType.CHILD.toString(), "toString should return 'CHILD' for CHILD");
        assertEquals("ADULT", TicketType.ADULT.toString(), "toString should return 'ADULT' for ADULT");
    }
}
