package io.angellsan94.angelatro.logic.jokers;

import io.angellsan94.angelatro.logic.game.HandEvaluationContext;
import io.angellsan94.angelatro.logic.game.HandLevelManager;
import io.angellsan94.angelatro.logic.game.HandType;
import io.angellsan94.angelatro.logic.model.Card;
import io.angellsan94.angelatro.logic.model.DeckType;
import io.angellsan94.angelatro.logic.model.Rank;
import io.angellsan94.angelatro.logic.model.Suit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Clase de pruebas para {@link CumulativeMultEffect}.
 * <p>
 * Sigue metodología TDD: primero se escribe el test, luego la implementación.
 * </p>
 *
 * @author angellsan94
 * @version 1.0
 * @since 1.0
 */
@DisplayName("Pruebas de CumulativeMultEffect - TDD")
class CumulativeMultEffectTest {

    private CumulativeMultEffect effect;
    private HandLevelManager levelManager;

    @BeforeEach
    void setUp() {
        effect = new CumulativeMultEffect();
        levelManager = new HandLevelManager();
    }

    @Test
    @DisplayName("+1 mult por cada vez que se ha jugado esa mano en la partida")
    void testAdds1MultPerHandPlayed() {
        List<Card> playedCards = List.of(
                new Card(Rank.REY, Suit.HEARTS),
                new Card(Rank.REY, Suit.DIAMONDS)
        );
        List<Card> scoringCards = List.of(
                new Card(Rank.REY, Suit.HEARTS),
                new Card(Rank.REY, Suit.DIAMONDS)
        );

        HandEvaluationContext context = new HandEvaluationContext(
                HandType.PAREJA,
                playedCards,
                scoringCards,
                levelManager,
                DeckType.STANDARD,
                null,
                null
        );

        // Primera vez: +0 mult adicional
        int modifiedMult1 = effect.modifyMult(5, context);
        assertEquals(5, modifiedMult1);

        // Segunda vez: +1 mult adicional (simulado incrementando contador)
        effect.incrementHandCount(HandType.PAREJA);
        int modifiedMult2 = effect.modifyMult(5, context);
        assertEquals(6, modifiedMult2);

        // Tercera vez: +2 mult adicional
        effect.incrementHandCount(HandType.PAREJA);
        int modifiedMult3 = effect.modifyMult(5, context);
        assertEquals(7, modifiedMult3);
    }

    @Test
    @DisplayName("modifyChips no modifica los chips")
    void testModifyChipsDoesNotChangeChips() {
        List<Card> playedCards = List.of(
                new Card(Rank.REY, Suit.HEARTS),
                new Card(Rank.REY, Suit.DIAMONDS)
        );
        List<Card> scoringCards = List.of(
                new Card(Rank.REY, Suit.HEARTS),
                new Card(Rank.REY, Suit.DIAMONDS)
        );

        HandEvaluationContext context = new HandEvaluationContext(
                HandType.PAREJA,
                playedCards,
                scoringCards,
                levelManager,
                DeckType.STANDARD,
                null,
                null
        );

        int modifiedChips = effect.modifyChips(50, context);
        assertEquals(50, modifiedChips); // Sin cambio
    }
}
