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
 * Clase de pruebas para {@link StairChipsEffect}.
 * <p>
 * Sigue metodología TDD: primero se escribe el test, luego la implementación.
 * </p>
 *
 * @author angellsan94
 * @version 1.0
 * @since 1.0
 */
@DisplayName("Pruebas de StairChipsEffect - TDD")
class StairChipsEffectTest {

    private StairChipsEffect effect;
    private HandLevelManager levelManager;

    @BeforeEach
    void setUp() {
        effect = new StairChipsEffect();
        levelManager = new HandLevelManager();
    }

    @Test
    @DisplayName("+15 chips si la mano es ESCALERA o superior")
    void testAdds15ChipsForStairOrBetter() {
        List<Card> playedCards = List.of(
                new Card(Rank.DOS, Suit.HEARTS),
                new Card(Rank.TRES, Suit.DIAMONDS),
                new Card(Rank.CUATRO, Suit.CLUBS),
                new Card(Rank.CINCO, Suit.SPADES),
                new Card(Rank.SEIS, Suit.HEARTS)
        );
        List<Card> scoringCards = List.of(
                new Card(Rank.DOS, Suit.HEARTS),
                new Card(Rank.TRES, Suit.DIAMONDS),
                new Card(Rank.CUATRO, Suit.CLUBS),
                new Card(Rank.CINCO, Suit.SPADES),
                new Card(Rank.SEIS, Suit.HEARTS)
        );

        HandEvaluationContext context = new HandEvaluationContext(
                HandType.ESCALERA,
                playedCards,
                scoringCards,
                levelManager,
                DeckType.STANDARD,
                null,
                null
        );

        int modifiedChips = effect.modifyChips(50, context);
        assertEquals(65, modifiedChips); // 50 + 15
    }

    @Test
    @DisplayName("PAREJA no activa el efecto")
    void testPairDoesNotActivateEffect() {
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

    @Test
    @DisplayName("modifyMult no modifica el multiplicador")
    void testModifyMultDoesNotChangeMult() {
        List<Card> playedCards = List.of(
                new Card(Rank.DOS, Suit.HEARTS),
                new Card(Rank.TRES, Suit.DIAMONDS),
                new Card(Rank.CUATRO, Suit.CLUBS),
                new Card(Rank.CINCO, Suit.SPADES),
                new Card(Rank.SEIS, Suit.HEARTS)
        );
        List<Card> scoringCards = List.of(
                new Card(Rank.DOS, Suit.HEARTS),
                new Card(Rank.TRES, Suit.DIAMONDS),
                new Card(Rank.CUATRO, Suit.CLUBS),
                new Card(Rank.CINCO, Suit.SPADES),
                new Card(Rank.SEIS, Suit.HEARTS)
        );

        HandEvaluationContext context = new HandEvaluationContext(
                HandType.ESCALERA,
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
