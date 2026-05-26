package io.angellsan94.angelatro.logic.game;

import io.angellsan94.angelatro.exceptions.HandLimitExceededException;
import io.angellsan94.angelatro.logic.model.Card;
import io.angellsan94.angelatro.logic.model.Rank;
import io.angellsan94.angelatro.logic.model.Suit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Clase de pruebas para {@link PlayerHand}.
 * <p>
 * Sigue metodología TDD: primero se escribe el test, luego la implementación.
 * </p>
 *
 * @author angellsan94
 * @version 1.0
 * @since 1.0
 */
@DisplayName("Pruebas de PlayerHand - TDD")
class PlayerHandTest {

    private PlayerHand playerHand;

    @BeforeEach
    void setUp() {
        playerHand = new PlayerHand();
    }

    // ==================== CAPACIDAD MÁXIMA ====================

    @Test
    @DisplayName("Capacidad máxima de 8 cartas")
    void testMaxCapacityOf8Cards() {
        Card card1 = new Card(Rank.AS, Suit.HEARTS);
        Card card2 = new Card(Rank.REY, Suit.DIAMONDS);
        Card card3 = new Card(Rank.REINA, Suit.CLUBS);
        Card card4 = new Card(Rank.JOTA, Suit.SPADES);
        Card card5 = new Card(Rank.DIEZ, Suit.HEARTS);
        Card card6 = new Card(Rank.NUEVE, Suit.DIAMONDS);
        Card card7 = new Card(Rank.OCHO, Suit.CLUBS);
        Card card8 = new Card(Rank.SIETE, Suit.SPADES);

        playerHand.addCard(card1);
        playerHand.addCard(card2);
        playerHand.addCard(card3);
        playerHand.addCard(card4);
        playerHand.addCard(card5);
        playerHand.addCard(card6);
        playerHand.addCard(card7);
        playerHand.addCard(card8);

        assertEquals(8, playerHand.size());
    }

    // ==================== SELECCIÓN ====================

    @Test
    @DisplayName("select(card) marca la carta como seleccionada")
    void testSelectCardMarksAsSelected() {
        Card card1 = new Card(Rank.AS, Suit.HEARTS);
        Card card2 = new Card(Rank.REY, Suit.DIAMONDS);

        playerHand.addCard(card1);
        playerHand.addCard(card2);

        playerHand.select(card1);

        List<Card> selected = playerHand.getSelected();
        assertEquals(1, selected.size());
        assertTrue(selected.contains(card1));
        assertFalse(selected.contains(card2));
    }

    @Test
    @DisplayName("deselect(card) la desmarca")
    void testDeselectCardUnmarks() {
        Card card1 = new Card(Rank.AS, Suit.HEARTS);
        Card card2 = new Card(Rank.REY, Suit.DIAMONDS);

        playerHand.addCard(card1);
        playerHand.addCard(card2);

        playerHand.select(card1);
        playerHand.select(card2);

        playerHand.deselect(card1);

        List<Card> selected = playerHand.getSelected();
        assertEquals(1, selected.size());
        assertFalse(selected.contains(card1));
        assertTrue(selected.contains(card2));
    }

    @Test
    @DisplayName("getSelected() devuelve solo las marcadas")
    void testGetSelectedReturnsOnlyMarkedCards() {
        Card card1 = new Card(Rank.AS, Suit.HEARTS);
        Card card2 = new Card(Rank.REY, Suit.DIAMONDS);
        Card card3 = new Card(Rank.REINA, Suit.CLUBS);

        playerHand.addCard(card1);
        playerHand.addCard(card2);
        playerHand.addCard(card3);

        playerHand.select(card1);
        playerHand.select(card3);

        List<Card> selected = playerHand.getSelected();
        assertEquals(2, selected.size());
        assertTrue(selected.contains(card1));
        assertFalse(selected.contains(card2));
        assertTrue(selected.contains(card3));
    }

    // ==================== LÍMITE DE PLAY AREA ====================

    @Test
    @DisplayName("HandLimitExceededException si se intentan añadir más de 5 cartas al PlayArea")
    void testHandLimitExceededExceptionWhenMoreThan5Cards() {
        Card card1 = new Card(Rank.AS, Suit.HEARTS);
        Card card2 = new Card(Rank.REY, Suit.DIAMONDS);
        Card card3 = new Card(Rank.REINA, Suit.CLUBS);
        Card card4 = new Card(Rank.JOTA, Suit.SPADES);
        Card card5 = new Card(Rank.DIEZ, Suit.HEARTS);
        Card card6 = new Card(Rank.NUEVE, Suit.DIAMONDS);

        playerHand.addCard(card1);
        playerHand.addCard(card2);
        playerHand.addCard(card3);
        playerHand.addCard(card4);
        playerHand.addCard(card5);
        playerHand.addCard(card6);

        playerHand.select(card1);
        playerHand.select(card2);
        playerHand.select(card3);
        playerHand.select(card4);
        playerHand.select(card5);

        assertThrows(HandLimitExceededException.class, () -> playerHand.select(card6));
    }
}
