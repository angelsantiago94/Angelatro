package io.angellsan94.angelatro.logic.game;

import io.angellsan94.angelatro.logic.model.Card;
import io.angellsan94.angelatro.logic.model.Rank;
import io.angellsan94.angelatro.logic.model.Suit;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Clase para evaluar manos de póker.
 * <p>
 * Determina el tipo de mano siguiendo principios SOLID y KISS.
 * </p>
 *
 * @author angellsan94
 * @version 1.0
 * @since 1.0
 */
public class HandEvaluator {

    /**
     * Evalúa una mano de cartas y devuelve el tipo de mano con las cartas que puntuaron.
     *
     * @param cards las cartas a evaluar (mínimo 1 carta)
     * @return el tipo de mano con las cartas que puntuaron
     * @throws IllegalArgumentException si las cartas son null o está vacío
     */
    public ScoringHand evaluate(List<Card> cards) {
        if (cards == null || cards.isEmpty()) {
            throw new IllegalArgumentException("Las cartas no pueden ser null ni estar vacío");
        }
        if (cards.size() > 5) {
            throw new IllegalArgumentException("No se pueden jugar más de 5 cartas");
        }

        // Detectar cinco de color
        if (isFiveOfAKindOfSameSuit(cards)) {
            return new ScoringHand(HandType.CINCO_DE_COLOR, getScoringCardsForFiveOfAKind(cards));
        }

        // Detectar full de color
        if (isFullHouseOfSameSuit(cards)) {
            return new ScoringHand(HandType.FULL_DE_COLOR, getScoringCardsForFullHouse(cards));
        }

        // Detectar repóker
        if (isFiveOfAKind(cards)) {
            return new ScoringHand(HandType.REPOKER, getScoringCardsForFiveOfAKind(cards));
        }

        // Detectar escalera real
        if (isRoyalFlush(cards)) {
            return new ScoringHand(HandType.ESCALERA_REAL, cards);
        }

        // Detectar escalera de color
        if (isStraightFlush(cards)) {
            return new ScoringHand(HandType.ESCALERA_DE_COLOR, cards);
        }

        // Detectar póker
        if (isFourOfAKind(cards)) {
            return new ScoringHand(HandType.POKER, getScoringCardsForFourOfAKind(cards));
        }

        // Detectar full house
        if (isFullHouse(cards)) {
            return new ScoringHand(HandType.FULL_HOUSE, getScoringCardsForFullHouse(cards));
        }

        // Detectar color
        if (isFlush(cards)) {
            return new ScoringHand(HandType.COLOR, cards);
        }

        // Detectar escalera
        if (isStraight(cards)) {
            return new ScoringHand(HandType.ESCALERA, cards);
        }

        // Detectar trío
        if (isThreeOfAKind(cards)) {
            return new ScoringHand(HandType.TRIO, getScoringCardsForThreeOfAKind(cards));
        }

        // Detectar doble pareja
        if (isTwoPair(cards)) {
            return new ScoringHand(HandType.DOBLE_PAREJA, getScoringCardsForTwoPair(cards));
        }

        // Detectar pareja
        if (isOnePair(cards)) {
            return new ScoringHand(HandType.PAREJA, getScoringCardsForOnePair(cards));
        }

        // Carta alta: solo la carta más alta puntúa
        return new ScoringHand(HandType.CARTA_ALTA, getScoringCardsForHighCard(cards));
    }

    /**
     * Verifica si la mano es una Pareja (2 cartas del mismo rango).
     *
     * @param cards las cartas a verificar
     * @return true si es pareja, false en caso contrario
     */
    private boolean isOnePair(List<Card> cards) {
        Map<Rank, Long> rankCounts = cards.stream()
                .collect(Collectors.groupingBy(Card::rank, Collectors.counting()));
        return rankCounts.containsValue(2L);
    }

    /**
     * Verifica si la mano es Doble Pareja (2 pares distintos).
     *
     * @param cards las cartas a verificar
     * @return true si es doble pareja, false en caso contrario
     */
    private boolean isTwoPair(List<Card> cards) {
        Map<Rank, Long> rankCounts = cards.stream()
                .collect(Collectors.groupingBy(Card::rank, Collectors.counting()));
        long pairCount = rankCounts.values().stream().filter(count -> count == 2L).count();
        return pairCount == 2L;
    }

    /**
     * Verifica si la mano es un Trío (3 cartas del mismo rango).
     *
     * @param cards las cartas a verificar
     * @return true si es trío, false en caso contrario
     */
    private boolean isThreeOfAKind(List<Card> cards) {
        Map<Rank, Long> rankCounts = cards.stream()
                .collect(Collectors.groupingBy(Card::rank, Collectors.counting()));
        return rankCounts.containsValue(3L);
    }

    /**
     * Verifica si la mano es un Full House (trío + pareja).
     *
     * @param cards las cartas a verificar
     * @return true si es full house, false en caso contrario
     */
    private boolean isFullHouse(List<Card> cards) {
        Map<Rank, Long> rankCounts = cards.stream()
                .collect(Collectors.groupingBy(Card::rank, Collectors.counting()));
        return rankCounts.containsValue(3L) && rankCounts.containsValue(2L);
    }

    /**
     * Verifica si la mano es un Full de Color (trío + pareja del mismo palo).
     *
     * @param cards las cartas a verificar
     * @return true si es full de color, false en caso contrario
     */
    private boolean isFullHouseOfSameSuit(List<Card> cards) {
        return isFullHouse(cards) && isFlush(cards);
    }

    /**
     * Verifica si la mano es un Póker (4 cartas del mismo rango).
     *
     * @param cards las cartas a verificar
     * @return true si es póker, false en caso contrario
     */
    private boolean isFourOfAKind(List<Card> cards) {
        Map<Rank, Long> rankCounts = cards.stream()
                .collect(Collectors.groupingBy(Card::rank, Collectors.counting()));
        return rankCounts.containsValue(4L);
    }

    /**
     * Verifica si la mano es un Repóker (5 cartas del mismo rango).
     *
     * @param cards las cartas a verificar
     * @return true si es repóker, false en caso contrario
     */
    private boolean isFiveOfAKind(List<Card> cards) {
        Map<Rank, Long> rankCounts = cards.stream()
                .collect(Collectors.groupingBy(Card::rank, Collectors.counting()));
        return rankCounts.containsValue(5L);
    }

    /**
     * Verifica si la mano es Cinco de Color (5 cartas del mismo rango y mismo palo).
     *
     * @param cards las cartas a verificar
     * @return true si es cinco de color, false en caso contrario
     */
    private boolean isFiveOfAKindOfSameSuit(List<Card> cards) {
        return isFiveOfAKind(cards) && isFlush(cards);
    }

    /**
     * Verifica si la mano es un Color (5 cartas del mismo palo).
     *
     * @param cards las cartas a verificar
     * @return true si es color, false en caso contrario
     */
    private boolean isFlush(List<Card> cards) {
        Suit firstSuit = cards.get(0).suit();
        return cards.stream().allMatch(c -> c.suit() == firstSuit);
    }

    /**
     * Verifica si la mano es una Escalera (5 rangos consecutivos).
     *
     * @param cards las cartas a verificar
     * @return true si es escalera, false en caso contrario
     */
    private boolean isStraight(List<Card> cards) {
        List<Integer> orders = cards.stream()
                .map(card -> card.rank().getOrder())
                .sorted()
                .toList();

        // Verificar escalera normal
        boolean isNormalStraight = true;
        for (int i = 0; i < orders.size() - 1; i++) {
            if (orders.get(i + 1) - orders.get(i) != 1) {
                isNormalStraight = false;
                break;
            }
        }
        if (isNormalStraight) {
            return true;
        }

        // Verificar escalera especial A-2-3-4-5 (Ace low)
        // AS tiene order 14, pero puede actuar como 1
        boolean hasAce = orders.contains(14);
        boolean hasTwo = orders.contains(2);
        boolean hasThree = orders.contains(3);
        boolean hasFour = orders.contains(4);
        boolean hasFive = orders.contains(5);

        return hasAce && hasTwo && hasThree && hasFour && hasFive;
    }

    /**
     * Verifica si la mano es una Escalera de Color (5 consecutivos del mismo palo).
     *
     * @param cards las cartas a verificar
     * @return true si es escalera de color, false en caso contrario
     */
    private boolean isStraightFlush(List<Card> cards) {
        return isFlush(cards) && isStraight(cards);
    }

    /**
     * Verifica si la mano es una Escalera Real (10-J-Q-K-A del mismo palo).
     *
     * @param cards las cartas a verificar
     * @return true si es escalera real, false en caso contrario
     */
    private boolean isRoyalFlush(List<Card> cards) {
        return isFlush(cards) &&
               cards.stream().anyMatch(c -> c.rank() == Rank.DIEZ) &&
               cards.stream().anyMatch(c -> c.rank() == Rank.JOTA) &&
               cards.stream().anyMatch(c -> c.rank() == Rank.REINA) &&
               cards.stream().anyMatch(c -> c.rank() == Rank.REY) &&
               cards.stream().anyMatch(c -> c.rank() == Rank.AS);
    }

    /**
     * Obtiene las cartas que puntuaron para una Pareja.
     *
     * @param cards las cartas a evaluar
     * @return las cartas que forman la pareja
     */
    private List<Card> getScoringCardsForOnePair(List<Card> cards) {
        Map<Rank, Long> rankCounts = cards.stream()
                .collect(Collectors.groupingBy(Card::rank, Collectors.counting()));
        Rank pairRank = rankCounts.entrySet().stream()
                .filter(e -> e.getValue() == 2L)
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse(null);
        return cards.stream().filter(c -> c.rank() == pairRank).toList();
    }

    /**
     * Obtiene las cartas que puntuaron para Doble Pareja.
     *
     * @param cards las cartas a evaluar
     * @return las cartas que forman las dos parejas
     */
    private List<Card> getScoringCardsForTwoPair(List<Card> cards) {
        Map<Rank, Long> rankCounts = cards.stream()
                .collect(Collectors.groupingBy(Card::rank, Collectors.counting()));
        List<Rank> pairRanks = rankCounts.entrySet().stream()
                .filter(e -> e.getValue() == 2L)
                .map(Map.Entry::getKey)
                .toList();
        return cards.stream().filter(c -> pairRanks.contains(c.rank())).toList();
    }

    /**
     * Obtiene las cartas que puntuaron para Trío.
     *
     * @param cards las cartas a evaluar
     * @return las cartas que forman el trío
     */
    private List<Card> getScoringCardsForThreeOfAKind(List<Card> cards) {
        Map<Rank, Long> rankCounts = cards.stream()
                .collect(Collectors.groupingBy(Card::rank, Collectors.counting()));
        Rank trioRank = rankCounts.entrySet().stream()
                .filter(e -> e.getValue() == 3L)
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse(null);
        return cards.stream().filter(c -> c.rank() == trioRank).toList();
    }

    /**
     * Obtiene las cartas que puntuaron para Full House.
     *
     * @param cards las cartas a evaluar
     * @return todas las cartas (trío + pareja)
     */
    private List<Card> getScoringCardsForFullHouse(List<Card> cards) {
        return cards;
    }

    /**
     * Obtiene las cartas que puntuaron para Póker.
     *
     * @param cards las cartas a evaluar
     * @return las cartas que forman el póker
     */
    private List<Card> getScoringCardsForFourOfAKind(List<Card> cards) {
        Map<Rank, Long> rankCounts = cards.stream()
                .collect(Collectors.groupingBy(Card::rank, Collectors.counting()));
        Rank fourRank = rankCounts.entrySet().stream()
                .filter(e -> e.getValue() == 4L)
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse(null);
        return cards.stream().filter(c -> c.rank() == fourRank).toList();
    }

    /**
     * Obtiene las cartas que puntuaron para Repóker.
     *
     * @param cards las cartas a evaluar
     * @return todas las cartas del mismo rango
     */
    private List<Card> getScoringCardsForFiveOfAKind(List<Card> cards) {
        Map<Rank, Long> rankCounts = cards.stream()
                .collect(Collectors.groupingBy(Card::rank, Collectors.counting()));
        Rank fiveRank = rankCounts.entrySet().stream()
                .filter(e -> e.getValue() == 5L)
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse(null);
        return cards.stream().filter(c -> c.rank() == fiveRank).toList();
    }

    /**
     * Obtiene las cartas que puntuaron para Carta Alta.
     *
     * @param cards las cartas a evaluar
     * @return la carta más alta
     */
    private List<Card> getScoringCardsForHighCard(List<Card> cards) {
        return cards.stream()
                .max(Comparator.comparingInt(c -> c.rank().getOrder()))
                .map(List::of)
                .orElse(List.of());
    }
}
