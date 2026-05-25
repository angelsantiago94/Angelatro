package io.angellsan94.angelatro.logic.model;

import io.angellsan94.angelatro.exceptions.DeckEmptyException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests para la clase Deck.
 *
 * @author angellsan94
 * @version 1.0
 * @since 1.0
 */
class DeckTest {

    private Deck deck;

    @BeforeEach
    void setUp() {
        deck = new Deck();
    }

    @Test
    @DisplayName("Un mazo nuevo tiene exactamente 52 cartas")
    void testNewDeckHas52Cards() {
        assertEquals(52, deck.size(), "Un mazo nuevo debe tener exactamente 52 cartas");
    }

    @Test
    @DisplayName("El mazo contiene exactamente 4 cartas de cada rango")
    void testDeckHasFourOfEachRank() {
        for (Rank rank : Rank.values()) {
            long count = deck.getCards().stream()
                    .filter(card -> card.rank() == rank)
                    .count();
            assertEquals(4, count, "Debe haber exactamente 4 cartas de cada rango");
        }
    }

    @Test
    @DisplayName("El mazo contiene exactamente 13 cartas de cada palo")
    void testDeckHasFourOfEachSuit() {
        for (Suit suit : Suit.values()) {
            long count = deck.getCards().stream()
                    .filter(card -> card.suit() == suit)
                    .count();
            assertEquals(13, count, "Debe haber exactamente 13 cartas de cada palo");
        }
    }

    @Test
    @DisplayName("Shuffle no pierde ni duplica cartas")
    void testShufflePreservesCardCount() {
        int initialSize = deck.size();
        deck.shuffle();
        assertEquals(initialSize, deck.size(), "Shuffle no debe perder ni duplicar cartas");
    }

    @Test
    @DisplayName("Shuffle mantiene el mismo número de cartas por rango")
    void testShufflePreservesRankDistribution() {
        for (Rank rank : Rank.values()) {
            long initialCount = deck.getCards().stream()
                    .filter(card -> card.rank() == rank)
                    .count();
            deck.shuffle();
            long finalCount = deck.getCards().stream()
                    .filter(card -> card.rank() == rank)
                    .count();
            assertEquals(initialCount, finalCount, "Shuffle debe mantener la distribución de rangos");
        }
    }

    @Test
    @DisplayName("Shuffle mantiene el mismo número de cartas por palo")
    void testShufflePreservesSuitDistribution() {
        for (Suit suit : Suit.values()) {
            long initialCount = deck.getCards().stream()
                    .filter(card -> card.suit() == suit)
                    .count();
            deck.shuffle();
            long finalCount = deck.getCards().stream()
                    .filter(card -> card.suit() == suit)
                    .count();
            assertEquals(initialCount, finalCount, "Shuffle debe mantener la distribución de palos");
        }
    }

    @Test
    @DisplayName("Draw reduce el tamaño del mazo en 1")
    void testDrawReducesSize() {
        int initialSize = deck.size();
        deck.draw();
        assertEquals(initialSize - 1, deck.size(), "Draw debe reducir el tamaño del mazo en 1");
    }

    @Test
    @DisplayName("Draw sobre mazo vacío lanza DeckEmptyException")
    void testDrawOnEmptyDeckThrowsException() {
        // Extraer todas las cartas
        while (!deck.isEmpty()) {
            deck.draw();
        }

        assertThrows(DeckEmptyException.class, () -> deck.draw(),
                "Draw sobre mazo vacío debe lanzar DeckEmptyException");
    }

    @Test
    @DisplayName("IsEmpty es true cuando no quedan cartas")
    void testIsEmptyWhenNoCardsLeft() {
        // Extraer todas las cartas
        while (!deck.isEmpty()) {
            deck.draw();
        }
        assertTrue(deck.isEmpty(), "IsEmpty debe ser true cuando no quedan cartas");
    }

    @Test
    @DisplayName("IsEmpty es false cuando hay cartas")
    void testIsEmptyWhenHasCards() {
        assertFalse(deck.isEmpty(), "IsEmpty debe ser false cuando hay cartas");
    }
}
