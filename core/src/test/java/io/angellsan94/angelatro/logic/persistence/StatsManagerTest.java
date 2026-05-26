package io.angellsan94.angelatro.logic.persistence;

import io.angellsan94.angelatro.logic.game.HandType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Clase de pruebas para {@link StatsManager}.
 * <p>
 * Sigue metodología TDD: primero se escribe el test, luego la implementación.
 * </p>
 *
 * @author angellsan94
 * @version 1.0
 * @since 1.0
 */
@DisplayName("Pruebas de StatsManager - TDD")
class StatsManagerTest {

    private StatsManager statsManager;

    @BeforeEach
    void setUp() {
        statsManager = new StatsManager();
    }

    // ==================== GAMES PLAYED ====================

    @Test
    @DisplayName("updateAfterGame() incrementa gamesPlayed")
    void testUpdateAfterGameIncrementsGamesPlayed() {
        assertEquals(0, statsManager.getGamesPlayed());

        statsManager.updateAfterGame(0, 0, 0, Map.of());
        assertEquals(1, statsManager.getGamesPlayed());

        statsManager.updateAfterGame(0, 0, 0, Map.of());
        assertEquals(2, statsManager.getGamesPlayed());
    }

    // ==================== BEST ROUND ====================

    @Test
    @DisplayName("updateAfterGame() actualiza bestRound solo si la ronda actual es mayor")
    void testUpdateAfterGameUpdatesBestRoundOnlyIfGreater() {
        assertEquals(0, statsManager.getBestRound());

        statsManager.updateAfterGame(3, 0, 0, Map.of());
        assertEquals(3, statsManager.getBestRound());

        statsManager.updateAfterGame(2, 0, 0, Map.of());
        assertEquals(3, statsManager.getBestRound()); // No cambia

        statsManager.updateAfterGame(5, 0, 0, Map.of());
        assertEquals(5, statsManager.getBestRound()); // Actualiza
    }

    // ==================== ROUNDS COMPLETED ====================

    @Test
    @DisplayName("updateAfterGame() acumula roundsCompleted")
    void testUpdateAfterGameAccumulatesRoundsCompleted() {
        assertEquals(0, statsManager.getRoundsCompleted());

        statsManager.updateAfterGame(0, 0, 0, Map.of());
        assertEquals(0, statsManager.getRoundsCompleted());

        statsManager.updateAfterGame(0, 3, 0, Map.of());
        assertEquals(3, statsManager.getRoundsCompleted());

        statsManager.updateAfterGame(0, 2, 0, Map.of());
        assertEquals(5, statsManager.getRoundsCompleted());
    }

    // ==================== BEST SCORE ====================

    @Test
    @DisplayName("updateAfterGame() actualiza bestScore solo si es mayor")
    void testUpdateAfterGameUpdatesBestScoreOnlyIfGreater() {
        assertEquals(0, statsManager.getBestScore());

        statsManager.updateAfterGame(0, 0, 1000, Map.of());
        assertEquals(1000, statsManager.getBestScore());

        statsManager.updateAfterGame(0, 0, 500, Map.of());
        assertEquals(1000, statsManager.getBestScore()); // No cambia

        statsManager.updateAfterGame(0, 0, 1500, Map.of());
        assertEquals(1500, statsManager.getBestScore()); // Actualiza
    }

    // ==================== HANDS PLAYED BY TYPE ====================

    @Test
    @DisplayName("updateAfterGame() suma a handsPlayedByType")
    void testUpdateAfterGameSumsToHandsPlayedByType() {
        Map<HandType, Integer> handsPlayed = Map.of(
                HandType.PAREJA, 3,
                HandType.ESCALERA, 2
        );

        statsManager.updateAfterGame(0, 0, 0, handsPlayed);

        assertEquals(3, statsManager.getHandsPlayedByType().get(HandType.PAREJA));
        assertEquals(2, statsManager.getHandsPlayedByType().get(HandType.ESCALERA));
        assertEquals(0, statsManager.getHandsPlayedByType().getOrDefault(HandType.POKER, 0));
    }
}
