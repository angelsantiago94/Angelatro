package io.angellsan94.angelatro.logic.persistence;

import io.angellsan94.angelatro.logic.game.HandLevelManager;
import io.angellsan94.angelatro.logic.game.HandType;
import io.angellsan94.angelatro.logic.jokers.Joker;
import io.angellsan94.angelatro.logic.model.DeckType;
import lombok.Getter;

import java.util.List;
import java.util.Map;

/**
 * Representa el estado de una sesión de juego que puede ser guardada y cargada.
 *
 * @author angellsan94
 * @version 1.0
 * @since 1.0
 */
@Getter
public class GameSession {

    private final int roundNumber;
    private final String deckTypeId;
    private final int walletAmount;
    private final List<Joker> activeJokers;
    private final Map<HandType, Integer> handLevels;

    /**
     * Constructor de GameSession.
     *
     * @param roundNumber   el número de ronda actual
     * @param deckTypeId    el identificador del tipo de mazo
     * @param walletAmount  el saldo del monedero
     * @param activeJokers  la lista de jokers activos
     * @param handLevels    los niveles de cada tipo de mano
     */
    public GameSession(int roundNumber, String deckTypeId, int walletAmount,
                       List<Joker> activeJokers, Map<HandType, Integer> handLevels) {
        this.roundNumber = roundNumber;
        this.deckTypeId = deckTypeId;
        this.walletAmount = walletAmount;
        this.activeJokers = activeJokers;
        this.handLevels = handLevels;
    }
}
