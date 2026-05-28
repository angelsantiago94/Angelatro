package io.angellsan94.angelatro.logic.game;

import io.angellsan94.angelatro.logic.economy.Wallet;
import io.angellsan94.angelatro.logic.model.Card;
import io.angellsan94.angelatro.logic.model.DeckType;
import io.angellsan94.angelatro.logic.stats.GameStats;

import java.util.List;

/**
 * Contexto de evaluación de mano que contiene toda la información necesaria
 * para calcular la puntuación de una jugada.
 *
 * @param handType      el tipo de mano detectado
 * @param playedCards   todas las cartas jugadas en el PlayArea
 * @param scoringCards  solo las cartas que forman la mano puntuada
 * @param levelManager  el gestor de niveles de manos
 * @param deckType      el tipo de mazo de la partida
 * @param globalStats   las estadísticas globales del juego
 * @param wallet        el monedero del jugador
 * @author angellsan94
 * @version 1.0
 * @since 1.0
 */
public record HandEvaluationContext(
        HandType handType,
        List<Card> playedCards,
        List<Card> scoringCards,
        HandLevelManager levelManager,
        DeckType deckType,
        GameStats globalStats,
        Wallet wallet
) {
}
