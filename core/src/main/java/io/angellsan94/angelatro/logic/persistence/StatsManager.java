package io.angellsan94.angelatro.logic.persistence;

import io.angellsan94.angelatro.logic.game.HandType;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * Gestiona las estadísticas del juego.
 * <p>
 * Rastrea partidas jugadas, mejor ronda, mejor puntuación y manos jugadas por tipo.
 * </p>
 *
 * @author angellsan94
 * @version 1.0
 * @since 1.0
 */
@Getter
public class StatsManager {

    private int gamesPlayed;
    private int bestRound;
    private int roundsCompleted;
    private int bestScore;
    private final Map<HandType, Integer> handsPlayedByType;

    /**
     * Constructor que inicializa el gestor de estadísticas.
     */
    public StatsManager() {
        this.gamesPlayed = 0;
        this.bestRound = 0;
        this.roundsCompleted = 0;
        this.bestScore = 0;
        this.handsPlayedByType = new HashMap<>();

        // Inicializar contador de manos en 0 para todos los tipos
        for (HandType handType : HandType.values()) {
            handsPlayedByType.put(handType, 0);
        }
    }

    /**
     * Actualiza las estadísticas después de una partida.
     *
     * @param roundNumber      el número de ronda alcanzado
     * @param roundsCompleted  el número de rondas completadas en esta partida
     * @param score            la puntuación final de la partida
     * @param handsPlayed      las manos jugadas en esta partida por tipo
     */
    public void updateAfterGame(int roundNumber, int roundsCompleted, int score,
                                 Map<HandType, Integer> handsPlayed) {
        gamesPlayed++;

        if (roundNumber > bestRound) {
            bestRound = roundNumber;
        }

        this.roundsCompleted += roundsCompleted;

        if (score > bestScore) {
            bestScore = score;
        }

        // Acumular manos jugadas por tipo
        for (Map.Entry<HandType, Integer> entry : handsPlayed.entrySet()) {
            HandType handType = entry.getKey();
            int count = entry.getValue();
            handsPlayedByType.merge(handType, count, Integer::sum);
        }
    }
}
