package io.angellsan94.angelatro.logic.jokers;

import io.angellsan94.angelatro.logic.game.HandEvaluationContext;

/**
 * Interfaz que define el efecto de un Joker sobre la puntuación.
 * <p>
 * Sigue el patrón Strategy para permitir diferentes efectos de jokers.
 * </p>
 *
 * @author angellsan94
 * @version 1.0
 * @since 1.0
 */
public interface JokerEffect {

    /**
     * Modifica los chips actuales según el efecto del joker.
     *
     * @param currentChips los chips actuales
     * @param context      el contexto de evaluación de la mano
     * @return los chips modificados
     */
    int modifyChips(int currentChips, HandEvaluationContext context);

    /**
     * Modifica el multiplicador actual según el efecto del joker.
     *
     * @param currentMult el multiplicador actual
     * @param context     el contexto de evaluación de la mano
     * @return el multiplicador modificado
     */
    int modifyMult(int currentMult, HandEvaluationContext context);
}
