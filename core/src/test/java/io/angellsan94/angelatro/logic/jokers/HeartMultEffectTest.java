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
 * Clase de pruebas para {@link HeartMultEffect}.
 * <p>
 * Sigue metodología TDD: primero se escribe el test, luego la implementación.
 * </p>
 *
 * @author angellsan94
 * @version 1.0
 * @since 1.0
 */
@DisplayName("Pruebas de HeartMultEffect - TDD")
class HeartMultEffectTest {

    private HeartMultEffect effect;
    private HandLevelManager levelManager;

    @BeforeEach
    void setUp() {
        effect = new HeartMultEffect();
        levelManager = new HandLevelManager();
    }

    @Test
    @DisplayName("+2 mult por cada carta de HEARTS en scoringCards")
    void testAdds2MultPerHeartCard() {
        List<Card> playedCards = List.of(
                new Card(Rank.AS, Suit.HEARTS),
                new Card(Rank.REY, Suit.HEARTS),
                new Card(Rank.REINA, Suit.DIAMONDS)
        );
        List<Card> scoringCards = List.of(
                new Card(Rank.AS, Suit.HEARTS),
                new Card(Rank.REY, Suit.HEARTS)
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

        int modifiedMult = effect.modifyMult(5, context);
        assertEquals(9, modifiedMult); // 5 + 2*2 = 9
    }

    @Test
    @DisplayName("0 cartas de HEARTS → mult sin cambio")
    void testNoHeartsNoMultChange() {
        List<Card> playedCards = List.of(
                new Card(Rank.AS, Suit.DIAMONDS),
                new Card(Rank.REY, Suit.SPADES)
        );
        List<Card> scoringCards = List.of(
                new Card(Rank.AS, Suit.DIAMONDS)
        );

        HandEvaluationContext context = new HandEvaluationContext(
                HandType.CARTA_ALTA,
                playedCards,
                scoringCards,
                levelManager,
                DeckType.STANDARD,
                null,
                null
        );

        int modifiedMult = effect.modifyMult(5, context);
        assertEquals(5, modifiedMult); // Sin cambio
    }

    @Test
    @DisplayName("modifyChips no modifica los chips")
    void testModifyChipsDoesNotChangeChips() {
        List<Card> playedCards = List.of(
                new Card(Rank.AS, Suit.HEARTS),
                new Card(Rank.REY, Suit.HEARTS)
        );
        List<Card> scoringCards = List.of(
                new Card(Rank.AS, Suit.HEARTS),
                new Card(Rank.REY, Suit.HEARTS)
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
