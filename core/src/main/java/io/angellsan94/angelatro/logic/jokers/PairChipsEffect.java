package io.angellsan94.angelatro.logic.jokers;

import io.angellsan94.angelatro.logic.game.HandEvaluationContext;
import io.angellsan94.angelatro.logic.game.HandType;

/**
 * Efecto del Joker J001 – Matador.
 * <p>
 * Añade 30 chips si la mano es PAREJA.
 * </p>
 *
 * @author angellsan94
 * @version 1.0
 * @since 1.0
 */
public class PairChipsEffect implements JokerEffect {

    private static final int BONUS_CHIPS = 30;

    /**
     * Modifica los chips añadiendo 30 si la mano es PAREJA.
     *
     * @param currentChips los chips actuales
     * @param context      el contexto de evaluación de la mano
     * @return los chips modificados
     */
    @Override
    public int modifyChips(int currentChips, HandEvaluationContext context) {
        if (context.handType() == HandType.PAREJA) {
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
}
