package io.angellsan94.angelatro.logic.persistence;

import lombok.Getter;

import java.util.HashSet;
import java.util.Map;
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
     * <p>
     * Se evalúan todos los logros definidos en las especificaciones v3.
     * </p>
     *
     * @param bestRound        la mejor ronda alcanzada
     * @param bestScore        la mejor puntuación alcanzada
     * @param maxJokersHeld    el máximo de jokers activos alcanzado
     * @param handsPlayedByType mapa con el número de manos jugadas por tipo
     */
    public void evaluateAchievements(int bestRound, int bestScore, int maxJokersHeld,
                                     Map<String, Integer> handsPlayedByType) {
        // Logro: REACH_ROUND_5 - alcanzar la ronda 5 (índice 4)
        if (bestRound >= 4 && !unlockedAchievements.contains("REACH_ROUND_5")) {
            unlockedAchievements.add("REACH_ROUND_5");
            // Recompensa: Joker "Veterano" (a implementar en futuro)
            unlockJoker("J009"); // ID reservado para Veterano
        }

        // Logro: REACH_ROUND_10 - alcanzar la ronda 10 (índice 9)
        if (bestRound >= 9 && !unlockedAchievements.contains("REACH_ROUND_10")) {
            unlockedAchievements.add("REACH_ROUND_10");
            // Recompensa: Mazo "Resistente" (+1 mano por ronda)
            unlockDeck("RESISTENT");
        }

        // Logro: HAVE_6_JOKERS - tener 6 jokers activos alguna vez
        if (maxJokersHeld >= 6 && !unlockedAchievements.contains("HAVE_6_JOKERS")) {
            unlockedAchievements.add("HAVE_6_JOKERS");
            // Recompensa: Mazo "Jokerómano" (empieza con un joker aleatorio)
            unlockDeck("JOKEROMANO");
        }

        // Logro: PLAY_100_HANDS - jugar 100 manos en total
        int totalHandsPlayed = handsPlayedByType.values().stream()
                .mapToInt(Integer::intValue)
                .sum();
        if (totalHandsPlayed >= 100 && !unlockedAchievements.contains("PLAY_100_HANDS")) {
            unlockedAchievements.add("PLAY_100_HANDS");
            // Recompensa: Joker "Amuleto" (+10 chips por mano)
            unlockJoker("J010"); // ID reservado para Amuleto
        }

        // Logro: REACH_ROUND_15 - alcanzar la ronda 15 (índice 14)
        if (bestRound >= 14 && !unlockedAchievements.contains("REACH_ROUND_15")) {
            unlockedAchievements.add("REACH_ROUND_15");
            // Recompensa: Joker "Leyenda" (efecto complejo, implementar en futuro)
            unlockJoker("J011"); // ID reservado para Leyenda
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
