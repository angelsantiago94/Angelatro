package io.angellsan94.angelatro.logic.game;

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
 * Clase de pruebas para {@link HandEvaluationContext}.
 *
 * @author angellsan94
 * @version 1.0
 * @since 1.0
 */
@DisplayName("Pruebas de HandEvaluationContext")
class HandEvaluationContextTest {

    private HandLevelManager levelManager;

    @BeforeEach
    void setUp() {
        levelManager = new HandLevelManager();
    }

    @Test
    @DisplayName("Constructor: crea contexto con todos los campos")
    void testConstructor() {
        HandType handType = HandType.PAREJA;
        List<Card> playedCards = List.of(
                new Card(Rank.SIETE, Suit.HEARTS),
                new Card(Rank.SIETE, Suit.DIAMONDS),
                new Card(Rank.DOS, Suit.CLUBS),
                new Card(Rank.CINCO, Suit.SPADES),
                new Card(Rank.NUEVE, Suit.HEARTS)
        );
        List<Card> scoringCards = List.of(
                new Card(Rank.SIETE, Suit.HEARTS),
                new Card(Rank.SIETE, Suit.DIAMONDS)
        );

        HandEvaluationContext context = new HandEvaluationContext(
                handType,
                playedCards,
                scoringCards,
                levelManager,
                DeckType.STANDARD,
                null,
                null
        );

        assertEquals(handType, context.handType());
        assertEquals(playedCards, context.playedCards());
        assertEquals(scoringCards, context.scoringCards());
        assertEquals(levelManager, context.levelManager());
        assertEquals(DeckType.STANDARD, context.deckType());
        assertNull(context.globalStats());
        assertNull(context.wallet());
    }
}
