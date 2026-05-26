package io.angellsan94.angelatro.logic.jokers;

import io.angellsan94.angelatro.exceptions.JokerLimitExceededException;
import io.angellsan94.angelatro.logic.economy.Wallet;
import io.angellsan94.angelatro.logic.model.DeckType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Clase de pruebas para {@link JokerManager}.
 * <p>
 * Sigue metodología TDD: primero se escribe el test, luego la implementación.
 * </p>
 *
 * @author angellsan94
 * @version 1.0
 * @since 1.0
 */
@DisplayName("Pruebas de JokerManager - TDD")
class JokerManagerTest {

    private JokerManager jokerManager;
    private Wallet wallet;

    @BeforeEach
    void setUp() {
        jokerManager = new JokerManager();
        wallet = new Wallet(DeckType.STANDARD);
    }

    // ==================== LÍMITE DE JOKERS ====================

    @Test
    @DisplayName("Límite de 6 jokers activos")
    void testMax6ActiveJokers() {
        JokerEffect dummyEffect = new JokerEffect() {
            @Override
            public int modifyChips(int currentChips, io.angellsan94.angelatro.logic.game.HandEvaluationContext context) {
                return currentChips;
            }

            @Override
            public int modifyMult(int currentMult, io.angellsan94.angelatro.logic.game.HandEvaluationContext context) {
                return currentMult;
            }
        };

        for (int i = 0; i < 6; i++) {
            Joker joker = new Joker("J00" + i, "Joker " + i, "Description", 4, Rarity.COMMON, dummyEffect);
            jokerManager.add(joker);
        }

        assertEquals(6, jokerManager.getActiveJokers().size());
    }

    @Test
    @DisplayName("add() con 6 jokers lanza JokerLimitExceededException")
    void testAddWith6JokersThrowsException() {
        JokerEffect dummyEffect = new JokerEffect() {
            @Override
            public int modifyChips(int currentChips, io.angellsan94.angelatro.logic.game.HandEvaluationContext context) {
                return currentChips;
            }

            @Override
            public int modifyMult(int currentMult, io.angellsan94.angelatro.logic.game.HandEvaluationContext context) {
                return currentMult;
            }
        };

        for (int i = 0; i < 6; i++) {
            Joker joker = new Joker("J00" + i, "Joker " + i, "Description", 4, Rarity.COMMON, dummyEffect);
            jokerManager.add(joker);
        }

        Joker seventhJoker = new Joker("J006", "Joker 6", "Description", 4, Rarity.COMMON, dummyEffect);
        assertThrows(JokerLimitExceededException.class, () -> jokerManager.add(seventhJoker));
    }

    // ==================== VENTA DE JOKERS ====================

    @Test
    @DisplayName("sell(joker) elimina el joker y devuelve floor(precio / 2)")
    void testSellJokerReturnsHalfPrice() {
        JokerEffect dummyEffect = new JokerEffect() {
            @Override
            public int modifyChips(int currentChips, io.angellsan94.angelatro.logic.game.HandEvaluationContext context) {
                return currentChips;
            }

            @Override
            public int modifyMult(int currentMult, io.angellsan94.angelatro.logic.game.HandEvaluationContext context) {
                return currentMult;
            }
        };

        Joker joker = new Joker("J001", "Matador", "Description", 4, Rarity.COMMON, dummyEffect);
        jokerManager.add(joker);

        int refund = jokerManager.sell(joker, wallet);
        assertEquals(2, refund); // floor(4 / 2) = 2
        assertEquals(6, wallet.getAmount()); // 4 inicial + 2 reembolso
        assertEquals(0, jokerManager.getActiveJokers().size());
    }

    @Test
    @DisplayName("sell() de un joker no activo lanza excepción")
    void testSellInactiveJokerThrowsException() {
        JokerEffect dummyEffect = new JokerEffect() {
            @Override
            public int modifyChips(int currentChips, io.angellsan94.angelatro.logic.game.HandEvaluationContext context) {
                return currentChips;
            }

            @Override
            public int modifyMult(int currentMult, io.angellsan94.angelatro.logic.game.HandEvaluationContext context) {
                return currentMult;
            }
        };

        Joker joker = new Joker("J001", "Matador", "Description", 4, Rarity.COMMON, dummyEffect);

        assertThrows(IllegalArgumentException.class, () -> jokerManager.sell(joker, wallet));
    }
}
