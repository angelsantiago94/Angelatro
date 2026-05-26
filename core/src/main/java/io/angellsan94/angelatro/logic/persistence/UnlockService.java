package io.angellsan94.angelatro.logic.persistence;

import lombok.Getter;

import java.util.HashSet;
import java.util.Set;

/**
 * Gestiona los desbloqueos del juego.
 * <p>
 * Evalúa logros y mantiene el estado de jokers y mazos desbloqueados.
 * </p>
 *
 * @author angellsan94
 * @version 1.0
 * @since 1.0
 */
@Getter
public class UnlockService {

    private final Set<String> unlockedAchievements;
    private final Set<String> unlockedJokerIds;
    private final Set<String> unlockedDeckIds;

    /**
     * Constructor que inicializa el servicio de desbloqueos.
     */
    public UnlockService() {
        this.unlockedAchievements = new HashSet<>();
        this.unlockedJokerIds = new HashSet<>();
        this.unlockedDeckIds = new HashSet<>();

        // Jokers base desbloqueados por defecto
        unlockedJokerIds.add("J001"); // Matador
        unlockedJokerIds.add("J002"); // Corazón Ardiente
        unlockedJokerIds.add("J003"); // Memorioso
        unlockedJokerIds.add("J004"); // Escalador

        // Mazos base desbloqueados por defecto
        unlockedDeckIds.add("STANDARD");
    }

    /**
     * Evalúa los logros basándose en las estadísticas del juego.
     *
     * @param bestRound     la mejor ronda alcanzada
     * @param bestScore     la mejor puntuación alcanzada
     */
    public void evaluateAchievements(int bestRound, int bestScore) {
        // Logro: REACH_ROUND_5 - alcanzar la ronda 5 (índice 4)
        if (bestRound >= 4 && !unlockedAchievements.contains("REACH_ROUND_5")) {
            unlockedAchievements.add("REACH_ROUND_5");
            // Desbloquear recompensas asociadas
            unlockDeck("WEALTHY");
        }
    }

    /**
     * Verifica si un logro está desbloqueado.
     *
     * @param achievementId el identificador del logro
     * @return true si el logro está desbloqueado
     */
    public boolean isAchievementUnlocked(String achievementId) {
        return unlockedAchievements.contains(achievementId);
    }

    /**
     * Desbloquea un joker.
     *
     * @param jokerId el identificador del joker
     */
    public void unlockJoker(String jokerId) {
        unlockedJokerIds.add(jokerId);
    }

    /**
     * Desbloquea un mazo.
     *
     * @param deckId el identificador del mazo
     */
    public void unlockDeck(String deckId) {
        unlockedDeckIds.add(deckId);
    }
}
