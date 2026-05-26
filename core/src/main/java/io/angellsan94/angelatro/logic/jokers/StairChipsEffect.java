package io.angellsan94.angelatro.logic.jokers;

import io.angellsan94.angelatro.logic.game.HandEvaluationContext;
import io.angellsan94.angelatro.logic.game.HandType;

/**
 * Efecto del Joker J004 – Escalador.
 * <p>
 * Añade +15 chips si la mano es ESCALERA o superior.
 * </p>
 *
 * @author angellsan94
 * @version 1.0
 * @since 1.0
 */
public class StairChipsEffect implements JokerEffect {

    private static final int BONUS_CHIPS = 15;

    /**
     * Modifica los chips añadiendo 15 si la mano es ESCALERA o superior.
     *
     * @param currentChips los chips actuales
     * @param context      el contexto de evaluación de la mano
     * @return los chips modificados
     */
    @Override
    public int modifyChips(int currentChips, HandEvaluationContext context) {
        if (isStairOrBetter(context.handType())) {
            return currentChips + BONUS_CHIPS;
        }
        return currentChips;
    }

    /**
     * No modifica el multiplicador.
     *
     * @param currentMult el multiplicador actual
     * @param context     el contexto de evaluación de la mano
     * @return el multiplicador sin cambios
     */
    @Override
    public int modifyMult(int currentMult, HandEvaluationContext context) {
        return currentMult;
    }

    /**
     * Verifica si la mano es ESCALERA o superior.
     *
     * @param handType el tipo de mano
     * @return true si es ESCALERA o superior
     */
    private boolean isStairOrBetter(HandType handType) {
        return handType == HandType.ESCALERA ||
               handType == HandType.COLOR ||
               handType == HandType.FULL_HOUSE ||
               handType == HandType.POKER ||
               handType == HandType.ESCALERA_REAL ||
               handType == HandType.ESCALERA_DE_COLOR;
    }
}
