package io.angellsan94.angelatro.logic.game;

import io.angellsan94.angelatro.logic.model.Deck;
import lombok.Getter;

/**
 * Gestiona el estado de la ronda actual, incluyendo manos, descartes,
 * puntuación objetivo y condiciones de victoria/derrota.
 *
 * @author angellsan94
 * @version 1.0
 * @since 1.0
 */
@Getter
public class RoundManager {

    private static final int INITIAL_HANDS = 3;
    private static final int INITIAL_DISCARDS = 3;
    private static final double TARGET_SCORE_MULTIPLIER = 1.6;
    private static final int BASE_TARGET_SCORE = 300;

    private int hands;
    private int discards;
    private int round;
    private int targetScore;
    private Deck deck;

    /**
     * Constructor que inicializa el gestor de ronda.
     */
    public RoundManager() {
        this.hands = INITIAL_HANDS;
        this.discards = INITIAL_DISCARDS;
        this.round = 0;
        this.targetScore = BASE_TARGET_SCORE;
        this.deck = new Deck();
    }

    /**
     * Inicia una nueva ronda con el número especificado.
     *
     * @param roundNumber el número de ronda
     */
    public void startRound(int roundNumber) {
        this.round = roundNumber;
        this.hands = INITIAL_HANDS;
        this.discards = INITIAL_DISCARDS;
        this.targetScore = calculateTargetScore(roundNumber);
        this.deck = new Deck();
        this.deck.shuffle();
    }

    /**
     * Calcula la puntuación objetivo para una ronda específica.
     *
     * @param roundNumber el número de ronda
     * @return la puntuación objetivo
     */
    private int calculateTargetScore(int roundNumber) {
        return (int) Math.round(BASE_TARGET_SCORE * Math.pow(TARGET_SCORE_MULTIPLIER, roundNumber));
    }

    /**
     * Juega una mano, reduciendo el contador de manos.
     *
     * @throws IllegalStateException si no quedan manos
     */
    public void playHand() {
        if (hands <= 0) {
            throw new IllegalStateException("No quedan manos para jugar");
        }
        hands--;
    }

    /**
     * Descarta cartas, reduciendo el contador de descartes.
     *
     * @throws IllegalStateException si no quedan descartes
     */
    public void discard() {
        if (discards <= 0) {
            throw new IllegalStateException("No quedan descartes disponibles");
        }
        discards--;
    }

    /**
     * Obtiene el número de manos restantes.
     *
     * @return número de manos
     */
    public int getHands() {
        return hands;
    }

    /**
     * Obtiene el número de descartes restantes.
     *
     * @return número de descartes
     */
    public int getDiscards() {
        return discards;
    }

    /**
     * Obtiene la puntuación objetivo de la ronda actual.
     *
     * @return la puntuación objetivo
     */
    public int getTargetScore() {
        return targetScore;
    }

    /**
     * Verifica si la ronda ha sido ganada.
     *
     * @param score la puntuación actual
     * @return true si la puntuación es mayor o igual al objetivo
     */
    public boolean isRoundWon(int score) {
        return score >= targetScore;
    }

    /**
     * Verifica si el juego ha terminado (condición de derrota 1).
     *
     * @param score la puntuación actual
     * @return true si no quedan manos y la puntuación es menor al objetivo
     */
    public boolean isGameOver(int score) {
        return hands == 0 && score < targetScore;
    }

    /**
     * Verifica si el juego ha terminado (condición de derrota 2).
     *
     * @param playerHand la mano del jugador
     * @param score la puntuación actual
     * @return true si la mano está vacía y el mazo está vacío (con manos restantes)
     */
    public boolean isGameOver(PlayerHand playerHand, int score) {
        return playerHand.size() == 0 && deck.isEmpty() && hands > 0;
    }

    /**
     * Obtiene el número de ronda actual.
     *
     * @return número de ronda
     */
    public int getRound() {
        return round;
    }
}
