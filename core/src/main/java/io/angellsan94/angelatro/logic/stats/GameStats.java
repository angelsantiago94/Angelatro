package io.angellsan94.angelatro.logic.stats;

import io.angellsan94.angelatro.logic.game.HandType;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * Estadísticas globales del juego utilizadas por los jokers para efectos condicionales.
 * <p>
 * Contiene métricas acumuladas durante la partida actual que pueden influir
 * en el comportamiento de ciertos jokers.
 * </p>
 *
 * @author angellsan94
 * @version 1.0
 * @since 1.0
 */
@Getter
public class GameStats {

    private final Map<HandType, Integer> handsPlayedInCurrentGame;
    private int currentRound;

    /**
     * Constructor que inicializa las estadísticas globales.
     */
    public GameStats() {
        this.handsPlayedInCurrentGame = new HashMap<>();
        this.currentRound = 0;

        // Inicializar contador de manos en 0 para todos los tipos
        for (HandType handType : HandType.values()) {
            handsPlayedInCurrentGame.put(handType, 0);
        }
    }

    /**
     * Registra que se ha jugado una mano de un tipo específico.
     *
     * @param handType el tipo de mano jugado
     */
    public void registerHandPlayed(HandType handType) {
        handsPlayedInCurrentGame.merge(handType, 1, Integer::sum);
    }

    /**
     * Obtiene el número de veces que se ha jugado un tipo de mano en la partida actual.
     *
     * @param handType el tipo de mano
     * @return el número de veces jugado
     */
    public int getHandsPlayedCount(HandType handType) {
        return handsPlayedInCurrentGame.getOrDefault(handType, 0);
    }

    /**
     * Establece el número de ronda actual.
     *
     * @param round el número de ronda
     */
    public void setCurrentRound(int round) {
        this.currentRound = round;
    }
}
