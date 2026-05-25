package io.angellsan94.angelatro.logic.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test para el enum Suit.
 */
class SuitTest {

    @Test
    void testSuitExists() {
        // Assert: El enum Suit debe existir con los 4 palos
        assertNotNull(Suit.HEARTS);
        assertNotNull(Suit.DIAMONDS);
        assertNotNull(Suit.CLUBS);
        assertNotNull(Suit.SPADES);
    }

    @Test
    void testSuitCount() {
        // Assert: El enum Suit debe tener exactamente 4 valores
        assertEquals(4, Suit.values().length);
    }

    @Test
    void testSuitNames() {
        // Assert: Los nombres de los palos deben ser correctos
        assertEquals("HEARTS", Suit.HEARTS.name());
        assertEquals("DIAMONDS", Suit.DIAMONDS.name());
        assertEquals("CLUBS", Suit.CLUBS.name());
        assertEquals("SPADES", Suit.SPADES.name());
    }
}