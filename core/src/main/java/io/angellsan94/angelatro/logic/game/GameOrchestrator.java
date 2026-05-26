package io.angellsan94.angelatro.logic.game;

import io.angellsan94.angelatro.logic.economy.Wallet;
import io.angellsan94.angelatro.logic.jokers.JokerManager;
import io.angellsan94.angelatro.logic.model.Deck;
import io.angellsan94.angelatro.logic.model.DeckType;
import io.angellsan94.angelatro.logic.persistence.SaveManager;
import io.angellsan94.angelatro.logic.persistence.StatsManager;
import lombok.Getter;

/**
 * Orquesta todos los sistemas del juego durante una partida activa.
 * <p>
 * Gestiona el flujo del juego, incluyendo inicialización, rondas,
 * tienda, y condiciones de fin de juego.
 * </p>
 *
 * @author angellsan94
 * @version 1.0
 * @since 1.0
 */
@Getter
public class GameOrchestrator {

    private Wallet wallet;
    private DeckType deckType;
    private Deck deck;
    private RoundManager roundManager;
    private HandLevelManager handLevelManager;
    private JokerManager jokerManager;
    private ScoreEngine scoreEngine;
    private StatsManager statsManager;
    private SaveManager saveManager;

    /**
     * Constructor que inicializa el orquestador del juego.
     */
    public GameOrchestrator() {
        this.handLevelManager = new HandLevelManager();
        this.jokerManager = new JokerManager();
        this.scoreEngine = new ScoreEngine();
        this.statsManager = new StatsManager();
        this.saveManager = new SaveManager(System.getProperty("user.home") + "/.angelatro");
    }

    /**
     * Inicia una nueva partida con el tipo de mazo especificado.
     *
     * @param deckType el tipo de mazo
     */
    public void startNewGame(DeckType deckType) {
        this.deckType = deckType;
        this.wallet = new Wallet(deckType);
        this.deck = new Deck();
        this.deck.shuffle();
        this.roundManager = new RoundManager();
        this.roundManager.startRound(0);
    }

    /**
     * Completa una ronda y gestiona el flujo hacia la siguiente.
     *
     * @param won true si la ronda fue ganada
     */
    public void completeRound(boolean won) {
        if (won) {
            int nextRound = roundManager.getRound() + 1;
            roundManager.startRound(nextRound);
        }
    }

    /**
     * Verifica si el juego ha terminado y actualiza estadísticas si es necesario.
     *
     * @param currentScore la puntuación actual
     */
    public void checkGameOver(int currentScore) {
        boolean gameOver = roundManager.isGameOver(currentScore) || deck.isEmpty();

        if (gameOver) {
            statsManager.updateAfterGame(
                    roundManager.getRound(),
                    roundManager.getRound(),
                    currentScore,
                    handLevelManager.getHandLevels()
            );
        }
    }

    /**
     * Sale de la tienda y guarda automáticamente el progreso.
     */
    public void exitShop() {
        try {
            // Crear GameSession de persistencia y guardar
            io.angellsan94.angelatro.logic.persistence.GameSession saveSession =
                    new io.angellsan94.angelatro.logic.persistence.GameSession(
                            roundManager.getRound(),
                            deckType.getId(),
                            wallet.getAmount(),
                            jokerManager.getActiveJokers(),
                            handLevelManager.getHandLevels()
                    );
            saveManager.save(saveSession);
        } catch (Exception e) {
            // Log error pero no interrumpir el flujo
            System.err.println("Error al guardar partida: " + e.getMessage());
        }
    }
}
