package io.angellsan94.angelatro.logic.game;

import io.angellsan94.angelatro.exceptions.InvalidPlayAreaSizeException;

import java.util.List;

/**
 * Motor de cálculo de puntuación para manos de póker.
 * <p>
 * Calcula la puntuación total siguiendo el orden especificado:
 * 1. Chips base de la mano
 * 2. Chips individuales de las cartas puntuadas
 * 3. Modificadores de chips de jokers (futuro)
 * 4. Mult base de la mano
 * 5. Modificadores de mult de jokers (futuro)
 * 6. Puntuación final = chips * mult
 * </p>
 *
 * @author angellsan94
 * @version 1.0
 * @since 1.0
 */
public class ScoreEngine {

    /**
     * Calcula la puntuación total de una mano.
     *
     * @param context     el contexto de evaluación de la mano
     * @param activeJokers la lista de jokers activos (null si no hay jokers)
     * @return la puntuación total
     * @throws InvalidPlayAreaSizeException si playedCards está vacía
     */
    public int calculateTotalScore(HandEvaluationContext context, List<?> activeJokers) {
        if (context.playedCards().isEmpty()) {
            throw new InvalidPlayAreaSizeException("El PlayArea no puede estar vacío");
        }

        // 1. Chips base de la mano
        int chips = context.levelManager().getChips(context.handType());

        // 2. Chips individuales de las cartas puntuadas
        for (var card : context.scoringCards()) {
            chips += card.rank().getValueInChips();
        }

        // 3. Bonus de chips del DeckType
        chips += context.deckType().getBonusChips();

        // 4. Modificadores de chips de jokers (futuro)

        // 5. Mult base de la mano
        int mult = context.levelManager().getMult(context.handType());

        // 6. Bonus de mult del DeckType
        mult += context.deckType().getBonusMult();

        // 7. Modificadores de mult de jokers (futuro)

        // 8. Puntuación final
        return chips * mult;
    }
}
