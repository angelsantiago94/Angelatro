package io.angellsan94.angelatro.logic.persistence;

import io.angellsan94.angelatro.logic.model.DeckType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Clase de pruebas para {@link UnlockService}.
 * <p>
 * Sigue metodología TDD: primero se escribe el test, luego la implementación.
 * </p>
 *
 * @author angellsan94
 * @version 1.0
 * @since 1.0
 */
@DisplayName("Pruebas de UnlockService - TDD")
class UnlockServiceTest {

    private UnlockService unlockService;

    @BeforeEach
    void setUp() {
        unlockService = new UnlockService();
    }

    // ==================== LOGROS ====================

    @Test
    @DisplayName("Logro REACH_ROUND_5 se concede cuando bestRound >= 4")
    void testReachRound5AchievementGrantedWhenBestRound4() {
        assertFalse(unlockService.isAchievementUnlocked("REACH_ROUND_5"));

        unlockService.evaluateAchievements(4, 0);
        assertTrue(unlockService.isAchievementUnlocked("REACH_ROUND_5"));
    }

    @Test
    @DisplayName("Un logro ya concedido no se concede dos veces")
    void testAlreadyGrantedAchievementNotGrantedTwice() {
        unlockService.evaluateAchievements(4, 0);
        assertTrue(unlockService.isAchievementUnlocked("REACH_ROUND_5"));

        unlockService.evaluateAchievements(4, 0);
        assertTrue(unlockService.isAchievementUnlocked("REACH_ROUND_5"));
    }

    // ==================== JOKERS DESBLOQUEADOS ====================

    @Test
    @DisplayName("getUnlockedJokerIds() incluye los jokers base más los desbloqueados")
    void testGetUnlockedJokerIdsIncludesBaseAndUnlocked() {
        Set<String> baseJokers = unlockService.getUnlockedJokerIds();
        assertTrue(baseJokers.contains("J001")); // Joker base
        assertTrue(baseJokers.contains("J002")); // Joker base

        unlockService.unlockJoker("J005");
        Set<String> unlockedJokers = unlockService.getUnlockedJokerIds();
        assertTrue(unlockedJokers.contains("J001"));
        assertTrue(unlockedJokers.contains("J002"));
        assertTrue(unlockedJokers.contains("J005"));
    }

    // ==================== MAZOS DESBLOQUEADOS ====================

    @Test
    @DisplayName("getUnlockedDeckIds() incluye los mazos base más los desbloqueados")
    void testGetUnlockedDeckIdsIncludesBaseAndUnlocked() {
        Set<String> baseDecks = unlockService.getUnlockedDeckIds();
        assertTrue(baseDecks.contains("STANDARD")); // Mazo base

        unlockService.unlockDeck("WEALTHY");
        Set<String> unlockedDecks = unlockService.getUnlockedDeckIds();
        assertTrue(unlockedDecks.contains("STANDARD"));
        assertTrue(unlockedDecks.contains("WEALTHY"));
    }

    @Test
    @DisplayName("Un DeckType bloqueado no aparece en getUnlockedDeckIds()")
    void testBlockedDeckTypeNotInUnlockedDeckIds() {
        Set<String> unlockedDecks = unlockService.getUnlockedDeckIds();
        assertFalse(unlockedDecks.contains("POWERED")); // Mazo bloqueado
    }

    @Test
    @DisplayName("Al desbloquear un mazo por logro, getUnlockedDeckIds() lo incluye en la siguiente consulta")
    void testUnlockDeckByAchievementIncludesInNextQuery() {
        Set<String> initialDecks = unlockService.getUnlockedDeckIds();
        assertFalse(initialDecks.contains("POWERED"));

        unlockService.unlockDeck("POWERED");
        Set<String> updatedDecks = unlockService.getUnlockedDeckIds();
        assertTrue(updatedDecks.contains("POWERED"));
    }
}
