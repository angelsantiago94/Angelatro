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
 * Clase de pruebas para {@link PairChipsEffect}.
 * <p>
 * Sigue metodología TDD: primero se escribe el test, luego la implementación.
 * </p>
 *
 * @author angellsan94
 * @version 1.0
 * @since 1.0
 */
@DisplayName("Pruebas de PairChipsEffect - TDD")
class PairChipsEffectTest {

    private PairChipsEffect effect;
    private HandLevelManager levelManager;

    @BeforeEach
    void setUp() {
        effect = new PairChipsEffect();
        levelManager = new HandLevelManager();
    }

    @Test
    @DisplayName("+30 chips si la mano es PAREJA")
    void testAdds30ChipsForPair() {
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
        assertEquals(80, modifiedChips); // 50 + 30
    }

    @Test
    @DisplayName("0 chips extra si la mano no es PAREJA")
    void testNoExtraChipsForNonPair() {
        List<Card> playedCards = List.of(
                new Card(Rank.AS, Suit.HEARTS),
                new Card(Rank.DOS, Suit.DIAMONDS)
        );
        List<Card> scoringCards = List.of(
                new Card(Rank.AS, Suit.HEARTS)
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

        int modifiedChips = effect.modifyChips(50, context);
        assertEquals(50, modifiedChips); // Sin cambio
    }

    @Test
    @DisplayName("modifyMult no modifica el multiplicador")
    void testModifyMultDoesNotChangeMult() {
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

        int modifiedMult = effect.modifyMult(5, context);
        assertEquals(5, modifiedMult); // Sin cambio
    }
}
