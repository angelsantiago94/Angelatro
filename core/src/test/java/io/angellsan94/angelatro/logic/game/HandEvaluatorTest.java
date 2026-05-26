package io.angellsan94.angelatro.logic.game;

import io.angellsan94.angelatro.logic.model.Card;
import io.angellsan94.angelatro.logic.model.Rank;
import io.angellsan94.angelatro.logic.model.Suit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Clase de pruebas para {@link HandEvaluator}.
 * <p>
 * Sigue metodología TDD: primero se escribe el test, luego la implementación.
 * </p>
 *
 * @author angellsan94
 * @version 1.0
 * @since 1.0
 */
@DisplayName("Pruebas de HandEvaluator - TDD")
class HandEvaluatorTest {

    private HandEvaluator handEvaluator;

    /**
     * Configuración inicial para cada prueba.
     */
    @BeforeEach
    void setUp() {
        handEvaluator = new HandEvaluator();
    }

    // ==================== CARTA ALTA ====================

    @Test
    @DisplayName("Carta Alta: 5 cartas sin combinación → CARTA_ALTA")
    void testCartaAlta() {
        List<Card> cards = List.of(
                new Card(Rank.DOS, Suit.HEARTS),
                new Card(Rank.CINCO, Suit.DIAMONDS),
                new Card(Rank.SIETE, Suit.CLUBS),
                new Card(Rank.NUEVE, Suit.SPADES),
                new Card(Rank.JOTA, Suit.HEARTS)
        );

        ScoringHand result = handEvaluator.evaluate(cards);
        assertEquals(HandType.CARTA_ALTA, result.handType());
        assertEquals(1, result.scoringCards().size());
        assertEquals(new Card(Rank.JOTA, Suit.HEARTS), result.scoringCards().get(0));
    }

    // ==================== PAREJA ====================

    @Test
    @DisplayName("Pareja: exactamente dos cartas del mismo rango → PAREJA")
    void testPareja() {
        List<Card> cards = List.of(
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

        ScoringHand result = handEvaluator.evaluate(cards);
        assertEquals(HandType.PAREJA, result.handType());
        assertEquals(scoringCards, result.scoringCards());
    }

    // ==================== DOBLE PAREJA ====================

    @Test
    @DisplayName("Doble Pareja: dos pares distintos → DOBLE_PAREJA")
    void testDoblePareja() {
        List<Card> cards = List.of(
                new Card(Rank.SIETE, Suit.HEARTS),
                new Card(Rank.SIETE, Suit.DIAMONDS),
                new Card(Rank.CINCO, Suit.CLUBS),
                new Card(Rank.CINCO, Suit.SPADES),
                new Card(Rank.DOS, Suit.HEARTS)
        );

        List<Card> scoringCards = List.of(
            new Card(Rank.SIETE, Suit.HEARTS),
            new Card(Rank.SIETE, Suit.DIAMONDS),
            new Card(Rank.CINCO, Suit.CLUBS),
            new Card(Rank.CINCO, Suit.SPADES)
        );

        ScoringHand result = handEvaluator.evaluate(cards);
        assertEquals(HandType.DOBLE_PAREJA, result.handType());
        assertEquals(scoringCards, result.scoringCards());
    }

    // ==================== TRÍO ====================

    @Test
    @DisplayName("Trío: tres cartas del mismo rango → TRIO")
    void testTrio() {
        List<Card> cards = List.of(
                new Card(Rank.SIETE, Suit.HEARTS),
                new Card(Rank.SIETE, Suit.DIAMONDS),
                new Card(Rank.SIETE, Suit.CLUBS),
                new Card(Rank.DOS, Suit.SPADES),
                new Card(Rank.CINCO, Suit.HEARTS)
        );
        List<Card> scoringCards = List.of(
            new Card(Rank.SIETE, Suit.HEARTS),
            new Card(Rank.SIETE, Suit.DIAMONDS),
            new Card(Rank.SIETE, Suit.CLUBS)
        );

        ScoringHand result = handEvaluator.evaluate(cards);
        assertEquals(HandType.TRIO, result.handType());
        assertEquals(scoringCards, result.scoringCards());
    }

    // ==================== COLOR ====================

    @Test
    @DisplayName("Color: 5 cartas del mismo palo sin escalera → COLOR")
    void testColor() {
        List<Card> cards = List.of(
                new Card(Rank.DOS, Suit.HEARTS),
                new Card(Rank.CINCO, Suit.HEARTS),
                new Card(Rank.SIETE, Suit.HEARTS),
                new Card(Rank.NUEVE, Suit.HEARTS),
                new Card(Rank.JOTA, Suit.HEARTS)
        );

        ScoringHand result = handEvaluator.evaluate(cards);
        assertEquals(HandType.COLOR, result.handType());
        assertEquals(cards, result.scoringCards());
    }

    // ==================== FULL HOUSE ====================

    @Test
    @DisplayName("Full House: trío + pareja → FULL_HOUSE")
    void testFullHouse() {
        List<Card> cards = List.of(
                new Card(Rank.SIETE, Suit.HEARTS),
                new Card(Rank.SIETE, Suit.DIAMONDS),
                new Card(Rank.SIETE, Suit.CLUBS),
                new Card(Rank.CINCO, Suit.SPADES),
                new Card(Rank.CINCO, Suit.HEARTS)
        );

        ScoringHand result = handEvaluator.evaluate(cards);
        assertEquals(HandType.FULL_HOUSE, result.handType());
        assertEquals(cards, result.scoringCards());
    }

    // ==================== PÓKER ====================

    @Test
    @DisplayName("Póker: cuatro cartas del mismo rango → POKER")
    void testPoker() {
        List<Card> cards = List.of(
                new Card(Rank.SIETE, Suit.HEARTS),
                new Card(Rank.SIETE, Suit.DIAMONDS),
                new Card(Rank.SIETE, Suit.CLUBS),
                new Card(Rank.SIETE, Suit.SPADES),
                new Card(Rank.DOS, Suit.HEARTS)
        );

        List<Card> scoringCards = List.of(
            new Card(Rank.SIETE, Suit.HEARTS),
            new Card(Rank.SIETE, Suit.DIAMONDS),
            new Card(Rank.SIETE, Suit.CLUBS),
            new Card(Rank.SIETE, Suit.SPADES)
        );

        ScoringHand result = handEvaluator.evaluate(cards);
        assertEquals(HandType.POKER, result.handType());
        assertEquals(scoringCards, result.scoringCards());
    }

    // ==================== PRIORIDAD ====================

    @Test
    @DisplayName("Prioridad: Full House no se confunde con Trío")
    void testPrioridadFullHouseVsTrio() {
        List<Card> cards = List.of(
                new Card(Rank.SIETE, Suit.HEARTS),
                new Card(Rank.SIETE, Suit.DIAMONDS),
                new Card(Rank.SIETE, Suit.CLUBS),
                new Card(Rank.CINCO, Suit.SPADES),
                new Card(Rank.CINCO, Suit.HEARTS)
        );

        ScoringHand result = handEvaluator.evaluate(cards);
        assertEquals(HandType.FULL_HOUSE, result.handType());
        assertNotEquals(HandType.TRIO, result.handType());
        assertEquals(cards, result.scoringCards());
    }

    @Test
    @DisplayName("Prioridad: Color tiene prioridad sobre Trío si no es Full House")
    void testPrioridadColorVsTrio() {
        List<Card> cards = List.of(
                new Card(Rank.SIETE, Suit.HEARTS),
                new Card(Rank.DOS, Suit.HEARTS),
                new Card(Rank.CINCO, Suit.HEARTS),
                new Card(Rank.NUEVE, Suit.HEARTS),
                new Card(Rank.JOTA, Suit.HEARTS)
        );

        ScoringHand result = handEvaluator.evaluate(cards);
        assertEquals(HandType.COLOR, result.handType());
        assertNotEquals(HandType.TRIO, result.handType());
        assertEquals(cards, result.scoringCards());
    }

    // ==================== ESCALERA ====================

    @Test
    @DisplayName("Escalera: 5 rangos consecutivos de distintos palos → ESCALERA")
    void testEscalera() {
        List<Card> cards = List.of(
                new Card(Rank.DOS, Suit.HEARTS),
                new Card(Rank.TRES, Suit.DIAMONDS),
                new Card(Rank.CUATRO, Suit.CLUBS),
                new Card(Rank.CINCO, Suit.SPADES),
                new Card(Rank.SEIS, Suit.HEARTS)
        );

        ScoringHand result = handEvaluator.evaluate(cards);
        assertEquals(HandType.ESCALERA, result.handType());
        assertEquals(cards, result.scoringCards());
    }

    @Test
    @DisplayName("Escalera: A-2-3-4-5 (Ace low) → ESCALERA")
    void testEscaleraAceLow() {
        List<Card> cards = List.of(
                new Card(Rank.AS, Suit.HEARTS),
                new Card(Rank.DOS, Suit.DIAMONDS),
                new Card(Rank.TRES, Suit.CLUBS),
                new Card(Rank.CUATRO, Suit.SPADES),
                new Card(Rank.CINCO, Suit.HEARTS)
        );

        ScoringHand result = handEvaluator.evaluate(cards);
        assertEquals(HandType.ESCALERA, result.handType());
        assertEquals(cards, result.scoringCards());
    }

    @Test
    @DisplayName("Escalera: 10-J-Q-K-A (Ace high) → ESCALERA")
    void testEscaleraAceHigh() {
        List<Card> cards = List.of(
                new Card(Rank.DIEZ, Suit.HEARTS),
                new Card(Rank.JOTA, Suit.DIAMONDS),
                new Card(Rank.REINA, Suit.CLUBS),
                new Card(Rank.REY, Suit.SPADES),
                new Card(Rank.AS, Suit.HEARTS)
        );

        ScoringHand result = handEvaluator.evaluate(cards);
        assertEquals(HandType.ESCALERA, result.handType());
        assertEquals(cards, result.scoringCards());
    }

    // ==================== ESCALERA DE COLOR ====================

    @Test
    @DisplayName("Escalera de Color: 5 consecutivos del mismo palo (no 10-J-Q-K-A) → ESCALERA_DE_COLOR")
    void testEscaleraDeColor() {
        List<Card> cards = List.of(
                new Card(Rank.DOS, Suit.HEARTS),
                new Card(Rank.TRES, Suit.HEARTS),
                new Card(Rank.CUATRO, Suit.HEARTS),
                new Card(Rank.CINCO, Suit.HEARTS),
                new Card(Rank.SEIS, Suit.HEARTS)
        );

        ScoringHand result = handEvaluator.evaluate(cards);
        assertEquals(HandType.ESCALERA_DE_COLOR, result.handType());
        assertEquals(cards, result.scoringCards());
    }

    // ==================== ESCALERA REAL ====================

    @Test
    @DisplayName("Escalera Real: 10-J-Q-K-A del mismo palo → ESCALERA_REAL")
    void testEscaleraReal() {
        List<Card> cards = List.of(
                new Card(Rank.DIEZ, Suit.HEARTS),
                new Card(Rank.JOTA, Suit.HEARTS),
                new Card(Rank.REINA, Suit.HEARTS),
                new Card(Rank.REY, Suit.HEARTS),
                new Card(Rank.AS, Suit.HEARTS)
        );

        ScoringHand result = handEvaluator.evaluate(cards);
        assertEquals(HandType.ESCALERA_REAL, result.handType());
        assertEquals(cards, result.scoringCards());
    }

    // ==================== SCORING CARDS ====================

    @Test
    @DisplayName("Scoring Cards: en una Pareja de 7 dentro de 5 cartas, scoringCards contiene solo las dos 7")
    void testScoringCardsPareja() {
        List<Card> cards = List.of(
                new Card(Rank.SIETE, Suit.HEARTS),
                new Card(Rank.SIETE, Suit.DIAMONDS),
                new Card(Rank.DOS, Suit.CLUBS),
                new Card(Rank.CINCO, Suit.SPADES),
                new Card(Rank.NUEVE, Suit.HEARTS)
        );

        ScoringHand result = handEvaluator.evaluate(cards);
        assertEquals(HandType.PAREJA, result.handType());
        assertEquals(2, result.scoringCards().size());
        assertEquals(Rank.SIETE, result.scoringCards().get(0).rank());
        assertEquals(Rank.SIETE, result.scoringCards().get(1).rank());
    }

    // ==================== REPOKER ====================

    @Test
    @DisplayName("Repóker: 5 cartas del mismo rango → REPOKER")
    void testRepoker() {
        List<Card> cards = List.of(
                new Card(Rank.SIETE, Suit.HEARTS),
                new Card(Rank.SIETE, Suit.DIAMONDS),
                new Card(Rank.SIETE, Suit.CLUBS),
                new Card(Rank.SIETE, Suit.SPADES),
                new Card(Rank.SIETE, Suit.HEARTS)
        );

        ScoringHand result = handEvaluator.evaluate(cards);
        assertEquals(HandType.REPOKER, result.handType());
        assertEquals(cards, result.scoringCards());
    }

    // ==================== CINCO DE COLOR ====================

    @Test
    @DisplayName("Cinco de Color: 5 cartas del mismo rango y mismo palo → CINCO_DE_COLOR")
    void testCincoDeColor() {
        List<Card> cards = List.of(
                new Card(Rank.SIETE, Suit.HEARTS),
                new Card(Rank.SIETE, Suit.HEARTS),
                new Card(Rank.SIETE, Suit.HEARTS),
                new Card(Rank.SIETE, Suit.HEARTS),
                new Card(Rank.SIETE, Suit.HEARTS)
        );

        ScoringHand result = handEvaluator.evaluate(cards);
        assertEquals(HandType.CINCO_DE_COLOR, result.handType());
        assertEquals(cards, result.scoringCards());
    }

    // ==================== FULL DE COLOR ====================

    @Test
    @DisplayName("Full de Color: trío + pareja del mismo palo → FULL_DE_COLOR")
    void testFullDeColor() {
        List<Card> cards = List.of(
                new Card(Rank.SIETE, Suit.HEARTS),
                new Card(Rank.SIETE, Suit.HEARTS),
                new Card(Rank.SIETE, Suit.HEARTS),
                new Card(Rank.CINCO, Suit.HEARTS),
                new Card(Rank.CINCO, Suit.HEARTS)
        );

        ScoringHand result = handEvaluator.evaluate(cards);
        assertEquals(HandType.FULL_DE_COLOR, result.handType());
        assertEquals(cards, result.scoringCards());
    }
}
