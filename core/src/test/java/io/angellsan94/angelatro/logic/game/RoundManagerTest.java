package io.angellsan94.angelatro.logic.game;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Clase de pruebas para {@link RoundManager}.
 * <p>
 * Sigue metodología TDD: primero se escribe el test, luego la implementación.
 * </p>
 *
 * @author angellsan94
 * @version 1.0
 * @since 1.0
 */
@DisplayName("Pruebas de RoundManager - TDD")
class RoundManagerTest {

    private RoundManager roundManager;

    @BeforeEach
    void setUp() {
        roundManager = new RoundManager();
    }

    // ==================== INICIO DE RONDA ====================

    @Test
    @DisplayName("Al iniciar ronda, manos = 3 y descartes = 3")
    void testInitialRoundState() {
        roundManager.startRound(0);
        assertEquals(3, roundManager.getHands());
        assertEquals(3, roundManager.getDiscards());
    }

    // ==================== MANOS Y DESCARTES ====================

    @Test
    @DisplayName("playHand() reduce manos en 1")
    void testPlayHandReducesHands() {
        roundManager.startRound(0);
        roundManager.playHand();
        assertEquals(2, roundManager.getHands());
    }

    @Test
    @DisplayName("discard() reduce descartes en 1")
    void testDiscardReducesDiscards() {
        roundManager.startRound(0);
        roundManager.discard();
        assertEquals(2, roundManager.getDiscards());
    }

    @Test
    @DisplayName("playHand() con 0 manos lanza excepción")
    void testPlayHandWithZeroHandsThrowsException() {
        roundManager.startRound(0);
        roundManager.playHand();
        roundManager.playHand();
        roundManager.playHand();

        assertThrows(IllegalStateException.class, () -> roundManager.playHand());
    }

    @Test
    @DisplayName("discard() con 0 descartes lanza excepción")
    void testDiscardWithZeroDiscardsThrowsException() {
        roundManager.startRound(0);
        roundManager.discard();
        roundManager.discard();
        roundManager.discard();

        assertThrows(IllegalStateException.class, () -> roundManager.discard());
    }

    // ==================== TARGET SCORE ====================

    @Test
    @DisplayName("targetScore ronda 0 → 300")
    void testTargetScoreRound0() {
        roundManager.startRound(0);
        assertEquals(300, roundManager.getTargetScore());
    }

    @Test
    @DisplayName("targetScore ronda 1 → 480 (round(300 * 1.6^1))")
    void testTargetScoreRound1() {
        roundManager.startRound(1);
        assertEquals(480, roundManager.getTargetScore());
    }

    @Test
    @DisplayName("targetScore ronda 3 → 1229 (round(300 * 1.6^3))")
    void testTargetScoreRound3() {
        roundManager.startRound(3);
        assertEquals(1229, roundManager.getTargetScore());
    }

    // ==================== VICTORIA Y DERROTA ====================

    @Test
    @DisplayName("isRoundWon() true si score >= targetScore")
    void testIsRoundWonWhenScoreMeetsTarget() {
        roundManager.startRound(0);
        assertTrue(roundManager.isRoundWon(300));
        assertTrue(roundManager.isRoundWon(350));
        assertFalse(roundManager.isRoundWon(299));
    }

    @Test
    @DisplayName("isGameOver() true si manos = 0 y score < targetScore")
    void testIsGameOverWhenHandsZeroAndScoreBelowTarget() {
        roundManager.startRound(0);
        roundManager.playHand();
        roundManager.playHand();
        roundManager.playHand();

        assertTrue(roundManager.isGameOver(250));
        assertFalse(roundManager.isGameOver(300));
    }

    // ==================== MAZO Y MANO ====================

    @Test
    @DisplayName("Reponer mazo al inicio de cada ronda (52 cartas barajadas)")
    void testReshuffleDeckAtRoundStart() {
        roundManager.startRound(0);
        assertNotNull(roundManager.getDeck());
        assertEquals(52, roundManager.getDeck().size());
    }

    @Test
    @DisplayName("isGameOver() true si mano vacía Y mazo vacío (con manos restantes)")
    void testIsGameOverWhenHandEmptyAndDeckEmpty() {
        roundManager.startRound(0);
        PlayerHand playerHand = new PlayerHand();

        // Vaciar el mazo
        while (!roundManager.getDeck().isEmpty()) {
            roundManager.getDeck().draw();
        }

        assertTrue(roundManager.isGameOver(playerHand, 0));
    }

    // ==================== DESCARTE ====================

    @Test
    @DisplayName("Al robar tras jugar, las cartas del PlayArea van al descarte (no vuelven al mazo)")
    void testPlayAreaCardsGoToDiscardAfterDraw() {
        roundManager.startRound(0);

        // Sacar cartas del mazo para simular que fueron jugadas
        io.angellsan94.angelatro.logic.model.Card card1 = roundManager.getDeck().draw();
        io.angellsan94.angelatro.logic.model.Card card2 = roundManager.getDeck().draw();

        roundManager.discardPlayedCards(List.of(card1, card2));

        // Verificar que están en el descarte
        assertEquals(2, roundManager.getDiscardPile().size());
        assertTrue(roundManager.getDiscardPile().contains(card1));
        assertTrue(roundManager.getDiscardPile().contains(card2));

        // Verificar que ya no están en el mazo
        assertFalse(roundManager.getDeck().getCards().contains(card1));
        assertFalse(roundManager.getDeck().getCards().contains(card2));
    }

    @Test
    @DisplayName("Al agotar el mazo, no se recicla el descarte")
    void testDiscardNotRecycledWhenDeckEmpty() {
        roundManager.startRound(0);

        // Sacar carta del mazo y descartarla
        io.angellsan94.angelatro.logic.model.Card card1 = roundManager.getDeck().draw();
        roundManager.discardPlayedCards(List.of(card1));

        // Vaciar el mazo
        while (!roundManager.getDeck().isEmpty()) {
            roundManager.getDeck().draw();
        }

        // Verificar que el descarte sigue teniendo las cartas
        assertEquals(1, roundManager.getDiscardPile().size());
        assertTrue(roundManager.getDiscardPile().contains(card1));

        // Verificar que el mazo sigue vacío
        assertEquals(0, roundManager.getDeck().size());
    }
}
