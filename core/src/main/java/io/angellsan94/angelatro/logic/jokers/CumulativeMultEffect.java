package io.angellsan94.angelatro.logic.jokers;

import io.angellsan94.angelatro.logic.game.HandEvaluationContext;
import io.angellsan94.angelatro.logic.game.HandType;

import java.util.HashMap;
import java.util.Map;

/**
 * Efecto del Joker J003 – Memorioso.
 * <p>
 * Añade +1 mult por cada vez que se ha jugado esa mano en la partida.
 * </p>
 *
 * @author angellsan94
 * @version 1.0
 * @since 1.0
 */
public class CumulativeMultEffect implements JokerEffect {

    private final Map<HandType, Integer> handCountMap;

    /**
     * Constructor que inicializa el mapa de contadores.
     */
    public CumulativeMultEffect() {
        this.handCountMap = new HashMap<>();
    }

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
     * Modifica el multiplicador añadiendo 1 por cada vez que se ha jugado esa mano.
     *
     * @param currentMult el multiplicador actual
     * @param context     el contexto de evaluación de la mano
     * @return el multiplicador modificado
     */
    @Override
    public int modifyMult(int currentMult, HandEvaluationContext context) {
        int count = handCountMap.getOrDefault(context.handType(), 0);
        return currentMult + count;
    }

    /**
     * Incrementa el contador de veces que se ha jugado un tipo de mano.
     *
     * @param handType el tipo de mano
     */
    public void incrementHandCount(HandType handType) {
        handCountMap.put(handType, handCountMap.getOrDefault(handType, 0) + 1);
    }
}
