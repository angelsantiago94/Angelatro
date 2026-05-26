package io.angellsan94.angelatro.logic.jokers;

import io.angellsan94.angelatro.logic.game.HandEvaluationContext;
import io.angellsan94.angelatro.logic.model.Suit;

/**
 * Efecto del Joker J002 – Corazón Ardiente.
 * <p>
 * Añade +2 mult por cada carta de HEARTS en scoringCards.
 * </p>
 *
 * @author angellsan94
 * @version 1.0
 * @since 1.0
 */
public class HeartMultEffect implements JokerEffect {

    private static final int MULT_PER_HEART = 2;

    /**
     * No modifica los chips.
     *
     * @param currentChips los chips actuales
     * @param context      el contexto de evaluación de la mano
     * @return los chips sin cambios
     */
    @Override
    public int modifyChips(int currentChips, HandEvaluationContext context) {
        return currentChips;
    }

    /**
     * Modifica el multiplicador añadiendo 2 por cada carta de HEARTS en scoringCards.
     *
     * @param currentMult el multiplicador actual
     * @param context     el contexto de evaluación de la mano
     * @return el multiplicador modificado
     */
    @Override
    public int modifyMult(int currentMult, HandEvaluationContext context) {
        long heartCount = context.scoringCards().stream()
                .filter(card -> card.suit() == Suit.HEARTS)
                .count();
        return currentMult + (int) heartCount * MULT_PER_HEART;
    }
}
