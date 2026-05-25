package io.angellsan94.angelatro.logic.game;

import io.angellsan94.angelatro.logic.model.Card;

import java.util.List;

public record ScoringHand(HandType handType, List<Card> scoringCards) {
}
