package io.angellsan94.angelatro.logic.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitarios para la clase enum Rank.
 * Verifica que cada rango devuelva el valor correcto en chips.
 */
@DisplayName("Tests para Rank")
class RankTest {

    /**
     * Test: Verifica que Rank.ACE devuelve 11 chips
     */
    @Test
    @DisplayName("ACE debe devolver 11 chips")
    void testAceReturns11Chips() {
        // Arrange
        Rank rank = Rank.AS;

        // Act
        int chips = rank.getValueInChips();

        // Assert
        assertEquals(11, chips, "El Ace debe devolver 11 chips");
    }

    /**
     * Test: Verifica que Rank.KING devuelve 10 chips
     */
    @Test
    @DisplayName("KING debe devolver 10 chips")
    void testKingReturns10Chips() {
        // Arrange
        Rank rank = Rank.REY;

        // Act
        int chips = rank.getValueInChips();

        // Assert
        assertEquals(10, chips, "El King debe devolver 10 chips");
    }

    /**
     * Test: Verifica que Rank.TWO devuelve 2 chips
     */
    @Test
    @DisplayName("TWO debe devolver 2 chips")
    void testTwoReturns2Chips() {
        // Arrange
        Rank rank = Rank.DOS;

        // Act
        int chips = rank.getValueInChips();

        // Assert
        assertEquals(2, chips, "El Two debe devolver 2 chips");
    }
}
