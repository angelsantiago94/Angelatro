package io.angellsan94.angelatro.logic.game;

import io.angellsan94.angelatro.exceptions.InvalidPlayAreaSizeException;
import io.angellsan94.angelatro.logic.jokers.Joker;
import io.angellsan94.angelatro.logic.jokers.PairChipsEffect;
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
 * Clase de pruebas para {@link ScoreEngine}.
 * <p>
 * Sigue metodología TDD: primero se escribe el test, luego la implementación.
 * </p>
 *
 * @author angellsan94
 * @version 1.0
 * @since 1.0
 */
@DisplayName("Pruebas de ScoreEngine - TDD")
class ScoreEngineTest {

    private ScoreEngine scoreEngine;
    private HandLevelManager levelManager;

    @BeforeEach
    void setUp() {
        scoreEngine = new ScoreEngine();
        levelManager = new HandLevelManager();
    }

    // ==================== CARTA ALTA ====================

    @Test
    @DisplayName("Carta Alta con As, DeckType STANDARD → chips = 5 + 11 = 16, mult = 1 → score = 16")
    void testCartaAltaAsStandardDeck() {
        List<Card> playedCards = List.of(
                new Card(Rank.AS, Suit.HEARTS),
                new Card(Rank.DOS, Suit.DIAMONDS),
                new Card(Rank.TRES, Suit.CLUBS),
                new Card(Rank.CUATRO, Suit.SPADES),
                new Card(Rank.CINCO, Suit.HEARTS)
        );
        List<Card> scoringCards = List.of(new Card(Rank.AS, Suit.HEARTS));

        HandEvaluationContext context = new HandEvaluationContext(
                HandType.CARTA_ALTA,
                playedCards,
                scoringCards,
                levelManager,
                DeckType.STANDARD,
                null,
                null
        );

        int score = scoreEngine.calculateTotalScore(context, null);
        assertEquals(16, score);
    }

    @Test
    @DisplayName("Carta Alta con As, DeckType POWERED (bonusChips=10) → chips = 5 + 11 + 10 = 26, mult = 1 → score = 26")
    void testCartaAltaAsPoweredDeck() {
        List<Card> playedCards = List.of(
                new Card(Rank.AS, Suit.HEARTS),
                new Card(Rank.DOS, Suit.DIAMONDS),
                new Card(Rank.TRES, Suit.CLUBS),
                new Card(Rank.CUATRO, Suit.SPADES),
                new Card(Rank.CINCO, Suit.HEARTS)
        );
        List<Card> scoringCards = List.of(new Card(Rank.AS, Suit.HEARTS));

        HandEvaluationContext context = new HandEvaluationContext(
                HandType.CARTA_ALTA,
                playedCards,
                scoringCards,
                levelManager,
                DeckType.POWERED,
                null,
                null
        );

        int score = scoreEngine.calculateTotalScore(context, null);
        assertEquals(26, score);
    }

    // ==================== PAREJA ====================

    @Test
    @DisplayName("Pareja de Reyes nivel 0, DeckType STANDARD → chips = 20+10+10 = 40, mult = 2 → score = 80")
    void testParejaReyesNivel0StandardDeck() {
        List<Card> playedCards = List.of(
                new Card(Rank.REY, Suit.HEARTS),
                new Card(Rank.REY, Suit.DIAMONDS),
                new Card(Rank.DOS, Suit.CLUBS),
                new Card(Rank.TRES, Suit.SPADES),
                new Card(Rank.CUATRO, Suit.HEARTS)
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

        int score = scoreEngine.calculateTotalScore(context, null);
        assertEquals(80, score);
    }

    @Test
    @DisplayName("Pareja de Reyes nivel 0, DeckType MULTIBASE (bonusMult=2) → chips = 20+10+10 = 40, mult = 2+2 = 4 → score = 160")
    void testParejaReyesNivel0MultibaseDeck() {
        List<Card> playedCards = List.of(
                new Card(Rank.REY, Suit.HEARTS),
                new Card(Rank.REY, Suit.DIAMONDS),
                new Card(Rank.DOS, Suit.CLUBS),
                new Card(Rank.TRES, Suit.SPADES),
                new Card(Rank.CUATRO, Suit.HEARTS)
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
                DeckType.MULTIBASE,
                null,
                null
        );

        int score = scoreEngine.calculateTotalScore(context, null);
        assertEquals(160, score);
    }

    @Test
    @DisplayName("Pareja de Reyes nivel 3, DeckType STANDARD → chips = 65+10+10 = 85, mult = 5 → score = 425")
    void testParejaReyesNivel3StandardDeck() {
        levelManager.upgrade(HandType.PAREJA);
        levelManager.upgrade(HandType.PAREJA);
        levelManager.upgrade(HandType.PAREJA);

        List<Card> playedCards = List.of(
                new Card(Rank.REY, Suit.HEARTS),
                new Card(Rank.REY, Suit.DIAMONDS),
                new Card(Rank.DOS, Suit.CLUBS),
                new Card(Rank.TRES, Suit.SPADES),
                new Card(Rank.CUATRO, Suit.HEARTS)
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

        int score = scoreEngine.calculateTotalScore(context, null);
        assertEquals(425, score);
    }

    // ==================== VALIDACIÓN ====================

    @Test
    @DisplayName("InvalidPlayAreaSizeException si playedCards está vacía")
    void testInvalidPlayAreaSizeExceptionWhenEmpty() {
        List<Card> playedCards = List.of();
        List<Card> scoringCards = List.of();

        HandEvaluationContext context = new HandEvaluationContext(
                HandType.CARTA_ALTA,
                playedCards,
                scoringCards,
                levelManager,
                DeckType.STANDARD,
                null,
                null
        );

        assertThrows(InvalidPlayAreaSizeException.class, () -> scoreEngine.calculateTotalScore(context, null));
    }

    // ==================== SCORING CARDS ====================

    @Test
    @DisplayName("Solo las scoringCards aportan chips individuales, no todas las del PlayArea")
    void testOnlyScoringCardsContributeIndividualChips() {
        List<Card> playedCards = List.of(
                new Card(Rank.REY, Suit.HEARTS),
                new Card(Rank.REY, Suit.DIAMONDS),
                new Card(Rank.AS, Suit.CLUBS),
                new Card(Rank.TRES, Suit.SPADES),
                new Card(Rank.CUATRO, Suit.HEARTS)
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

        int score = scoreEngine.calculateTotalScore(context, null);
        // chips = 20 (base) + 10 + 10 (solo los REYES, no el AS) = 40
        // mult = 2
        // score = 40 * 2 = 80
        assertEquals(80, score);
    }

    // ==================== INTEGRACIÓN CON JOKERS ====================

    @Test
    @DisplayName("Pareja nivel 0 con J001 activo → chips = 20 + 20 + 20 + 30 = 90, mult = 2 → 180")
    void testPairWithJ001Active() {
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

        PairChipsEffect effect = new PairChipsEffect();
        Joker joker = new Joker("J001", "Matador", "+30 chips si la mano es PAREJA", 4,
                io.angellsan94.angelatro.logic.jokers.Rarity.COMMON, effect);
        List<Joker> activeJokers = List.of(joker);

        int score = scoreEngine.calculateTotalScore(context, activeJokers);
        // chips = 20 (base) + 10 + 10 (reyes) + 30 (joker) = 70
        // mult = 2
        // score = 70 * 2 = 140
        assertEquals(140, score);
    }

    @Test
    @DisplayName("El orden de aplicación es chips primero, luego mult")
    void testChipsAppliedBeforeMult() {
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

        PairChipsEffect chipsEffect = new PairChipsEffect();
        Joker chipsJoker = new Joker("J001", "Matador", "+30 chips", 4,
                io.angellsan94.angelatro.logic.jokers.Rarity.COMMON, chipsEffect);

        io.angellsan94.angelatro.logic.jokers.HeartMultEffect multEffect = new io.angellsan94.angelatro.logic.jokers.HeartMultEffect();
        Joker multJoker = new Joker("J002", "Corazón Ardiente", "+2 mult por HEARTS", 4,
                io.angellsan94.angelatro.logic.jokers.Rarity.COMMON, multEffect);

        List<Joker> activeJokers = List.of(chipsJoker, multJoker);

        int score = scoreEngine.calculateTotalScore(context, activeJokers);
        // chips = 20 (base) + 11 + 10 (rey) + 30 (joker) = 71
        // mult = 2 + 2*2 (2 corazones) = 6
        // score = 71 * 6 = 426
        assertEquals(426, score);
    }

    @Test
    @DisplayName("Con 0 jokers activos el resultado es el mismo que sin jokers")
    void testZeroJokersSameAsNoJokers() {
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

        int scoreWithNull = scoreEngine.calculateTotalScore(context, null);
        int scoreWithEmpty = scoreEngine.calculateTotalScore(context, List.of());

        assertEquals(scoreWithNull, scoreWithEmpty);
        assertEquals(80, scoreWithNull);
    }

    @Test
    @DisplayName("Los bonos de DeckType se aplican antes que los jokers (orden: base + bonusDeck + jokers)")
    void testDeckTypeBonusesAppliedBeforeJokers() {
        List<Card> playedCards = List.of(
                new Card(Rank.REY, Suit.HEARTS),
                new Card(Rank.REY, Suit.DIAMONDS)
        );
        List<Card> scoringCards = List.of(
                new Card(Rank.REY, Suit.HEARTS),
                new Card(Rank.REY, Suit.DIAMONDS)
        );

        // Usar POWERED (bonusChips=10) y MULTIBASE (bonusMult=2) para verificar el orden
        HandEvaluationContext context = new HandEvaluationContext(
                HandType.PAREJA,
                playedCards,
                scoringCards,
                levelManager,
                DeckType.POWERED,
                null,
                null
        );

        PairChipsEffect effect = new PairChipsEffect();
        Joker joker = new Joker("J001", "Matador", "+30 chips si la mano es PAREJA", 4,
                io.angellsan94.angelatro.logic.jokers.Rarity.COMMON, effect);
        List<Joker> activeJokers = List.of(joker);

        int score = scoreEngine.calculateTotalScore(context, activeJokers);
        // chips = 20 (base) + 10 + 10 (reyes) + 10 (bonusDeck) + 30 (joker) = 80
        // mult = 2 + 0 (bonusMult no aplicado en POWERED) = 2
        // score = 80 * 2 = 160
        assertEquals(160, score);
    }
}
