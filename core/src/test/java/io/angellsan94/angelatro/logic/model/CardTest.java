package io.angellsan94.angelatro.logic.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitarios para la clase Card.
 * Verifica que las cartas deleguen en Rank para obtener chips
 * y que dos cartas con mismo rango y palo sean iguales.
 */
@DisplayName("Tests para Card")
class CardTest {

    /**
     * Test: Verifica que card.getRankChips() delega en Rank
     */
    @Test
    @DisplayName("getRankChips() debe delegar en Rank")
    void testGetRankChipsDelegatesToRank() {
        // Arrange
        Rank rank = Rank.AS;
        Suit suit = Suit.HEARTS;

        // Act
        Card card = new Card(rank, suit);
        int chips = card.getRankChips();

        // Assert
        assertEquals(11, chips, "getRankChips() debe delegar en Rank y devolver 11 para ACE");
    }

    /**
     * Test: Verifica que dos cartas con mismo rango y palo son iguales
     */
    @Test
    @DisplayName("Dos cartas con mismo rango y palo deben ser iguales")
    void testSameRankAndSuitAreEqual() {
        // Arrange
        Rank rank = Rank.REY;
        Suit suit = Suit.DIAMONDS;
        Card card1 = new Card(rank, suit);
        Card card2 = new Card(rank, suit);

        // Act & Assert
        assertEquals(card1, card2, "Dos cartas con mismo rango y palo deben ser iguales");
        assertEquals(card1.hashCode(), card2.hashCode(), "Dos cartas iguales deben tener el mismo hashCode");
    }

    /**
     * Test: Verifica que dos cartas con diferente rango no son iguales
     */
    @Test
    @DisplayName("Dos cartas con diferente rango no deben ser iguales")
    void testDifferentRankAreNotEqual() {
        // Arrange
        Rank rank1 = Rank.AS;
        Rank rank2 = Rank.DOS;
        Suit suit = Suit.HEARTS;
        Card card1 = new Card(rank1, suit);
        Card card2 = new Card(rank2, suit);

        // Act & Assert
        assertNotEquals(card1, card2, "Dos cartas con diferente rango no deben ser iguales");
    }

    /**
     * Test: Verifica que dos cartas con diferente palo no son iguales
     */
    @Test
    @DisplayName("Dos cartas con diferente palo no deben ser iguales")
    void testDifferentSuitAreNotEqual() {
        // Arrange
        Rank rank = Rank.REY;
        Suit suit1 = Suit.HEARTS;
        Suit suit2 = Suit.DIAMONDS;
        Card card1 = new Card(rank, suit1);
        Card card2 = new Card(rank, suit2);

        // Act & Assert
        assertNotEquals(card1, card2, "Dos cartas con diferente palo no deben ser iguales");
    }
}
