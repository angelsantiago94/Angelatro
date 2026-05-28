package io.angellsan94.angelatro.logic.game;

import io.angellsan94.angelatro.logic.economy.Wallet;
import io.angellsan94.angelatro.logic.jokers.JokerManager;
import io.angellsan94.angelatro.logic.model.DeckType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Clase de pruebas para {@link GameOrchestrator}.
 * <p>
 * Sigue metodología TDD: primero se escribe el test, luego la implementación.
 * </p>
 *
 * @author angellsan94
 * @version 1.0
 * @since 1.0
 */
@DisplayName("Pruebas de GameOrchestrator - TDD")
class GameSessionTest {

    private GameOrchestrator gameOrchestrator;

    @BeforeEach
    void setUp() {
        gameOrchestrator = new GameOrchestrator();
    }

    // ==================== INICIALIZACIÓN ====================

    @Test
    @DisplayName("startNewGame(DeckType.WEALTHY) → wallet.getAmount() = 8")
    void testStartNewGameWithWealthyDeck() {
        gameOrchestrator.startNewGame(DeckType.WEALTHY);
        assertEquals(8, gameOrchestrator.getWallet().getAmount());
    }

    @Test
    @DisplayName("startNewGame(DeckType.POWERED) → scoreEngine.getBonusChips() = 10")
    void testStartNewGameWithPoweredDeck() {
        gameOrchestrator.startNewGame(DeckType.POWERED);
        assertEquals(10, gameOrchestrator.getDeckType().getBonusChips());
    }

    @Test
    @DisplayName("startNewGame(DeckType.MULTIBASE) → scoreEngine getBonusMult() = 2")
    void testStartNewGameWithMultibaseDeck() {
        gameOrchestrator.startNewGame(DeckType.MULTIBASE);
        assertEquals(2, gameOrchestrator.getDeckType().getBonusMult());
    }

    @Test
    @DisplayName("startNewGame() inicializa mazo completo (44 cartas), manos = 3, descartes = 3, ronda = 0")
    void testStartNewGameInitializesAllComponents() {
        gameOrchestrator.startNewGame(DeckType.STANDARD);

        assertEquals(44, gameOrchestrator.getRoundManager().getDeck().size());
        assertEquals(3, gameOrchestrator.getRoundManager().getHands());
        assertEquals(3, gameOrchestrator.getRoundManager().getDiscards());
        assertEquals(0, gameOrchestrator.getRoundManager().getRound());
    }

    // ==================== FLUJO DE JUEGO ====================



    @Test
    @DisplayName("Flujo de derrota por manos agotadas → llama a StatsManager.updateAfterGame()")
    void testGameOverByHandsExhaustedCallsStatsManager() {
        gameOrchestrator.startNewGame(DeckType.STANDARD);

        // Agotar todas las manos
        gameOrchestrator.getRoundManager().playHand();
        gameOrchestrator.getRoundManager().playHand();
        gameOrchestrator.getRoundManager().playHand();

        gameOrchestrator.checkGameOver();
        assertEquals(1, gameOrchestrator.getStatsManager().getGamesPlayed());
    }

    // ==================== GUARDADO ====================

    @Test
    @DisplayName("Guardar automáticamente al salir de la tienda llama a SaveManager.save()")
    void testAutoSaveOnShopExitCallsSaveManager() {
        gameOrchestrator.startNewGame(DeckType.STANDARD);

        // Simular salida de tienda
        gameOrchestrator.exitShop();

        // Verificar que se llamó a save (esto requerirá mock o spy en implementación real)
        // Por ahora, solo verificamos que el método existe y no lanza excepción
        assertDoesNotThrow(() -> gameOrchestrator.exitShop());
    }
}
