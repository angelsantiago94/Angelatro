package io.angellsan94.angelatro.logic.game;

import lombok.Getter;

/**
 * Resultado de jugar una mano. Lo devuelve GameOrchestrator.playHand()
 * y lo consume GameScreen para actualizar la UI y decidir navegación.
 */
@Getter
public class PlayResult {

    private final HandType handType;
    private final int scoreGained;
    private final int totalScore;
    private final boolean roundWon;
    private final boolean gameOver;

    public PlayResult(HandType handType, int scoreGained, int totalScore,
                      boolean roundWon, boolean gameOver) {
        this.handType = handType;
        this.scoreGained = scoreGained;
        this.totalScore = totalScore;
        this.roundWon = roundWon;
        this.gameOver = gameOver;
    }

}

