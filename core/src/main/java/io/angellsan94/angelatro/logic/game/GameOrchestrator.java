package io.angellsan94.angelatro.logic.game;

import io.angellsan94.angelatro.exceptions.NotEnoughMoneyException;
import io.angellsan94.angelatro.logic.economy.EconomyCalculator;
import io.angellsan94.angelatro.logic.economy.ShopGenerator;
import io.angellsan94.angelatro.logic.economy.Wallet;
import io.angellsan94.angelatro.logic.jokers.Joker;
import io.angellsan94.angelatro.logic.jokers.JokerManager;
import io.angellsan94.angelatro.logic.model.Card;
import io.angellsan94.angelatro.logic.model.DeckType;
import io.angellsan94.angelatro.logic.persistence.SaveManager;
import io.angellsan94.angelatro.logic.persistence.StatsManager;
import io.angellsan94.angelatro.logic.stats.GameStats;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

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
    private RoundManager roundManager;
    private HandLevelManager handLevelManager;
    private JokerManager jokerManager;
    private ScoreEngine scoreEngine;
    private StatsManager statsManager;
    private SaveManager saveManager;
    private PlayerHand playerHand;
    private int currentScore;
    private HandEvaluator handEvaluator;
    private ShopGenerator shopGenerator;
    private List<Joker> currentShopJokers;
    private List<HandType> currentShopHandUpgrades;
    private EconomyCalculator economyCalculator;
    private GameStats gameStats;


    /**
     * Constructor que inicializa el orquestador del juego.
     */
    public GameOrchestrator() {
        this.handLevelManager = new HandLevelManager();
        this.jokerManager = new JokerManager();
        this.scoreEngine = new ScoreEngine();
        this.statsManager = new StatsManager();
        this.saveManager = new SaveManager(System.getProperty("user.home") + "/.angelatro");
        this.handEvaluator        = new HandEvaluator();
        this.shopGenerator        = new ShopGenerator();
        this.economyCalculator    = new EconomyCalculator();
        this.currentShopJokers    = new ArrayList<>();
        this.currentShopHandUpgrades = new ArrayList<>();
        this.gameStats            = new GameStats();

    }

    /**
     * Inicia una nueva partida con el tipo de mazo especificado.
     *
     * @param deckType el tipo de mazo
     */
    public void startNewGame(DeckType deckType) {
        this.deckType = deckType;
        this.wallet = new Wallet(deckType);
        this.roundManager = new RoundManager();
        this.roundManager.startRound(0);
        this.currentScore = 0;
        this.playerHand   = new PlayerHand();
        this.gameStats    = new GameStats();
        drawCards();

    }

    // Llamado internamente tras jugar una mano o descartar.
    // Rellena playerHand hasta 8 cartas desde roundManager.getDeck().
    public void drawCards() {
        while (playerHand.size() < 8 && !roundManager.getDeck().isEmpty()) {
            playerHand.addCard(roundManager.getDeck().draw());
        }
    }


    /**
     * Completa una ronda y gestiona el flujo hacia la siguiente.
     *
     * @param won true si la ronda fue ganada
     */
    public void completeRound(
        boolean won // array de un elemento para poder mutarlo
    ) {
        if (won) {
            int nextRound = roundManager.getRound() + 1;
            roundManager.startRound(nextRound);
            gameStats.setCurrentRound(nextRound);
            currentScore = 0;
            playerHand.clear();         // ASUME: PlayerHand.clear() existe
            // drawCards se llama en los métodos de las pantallas
        }
    }

    public boolean isNewRecord() {
            return roundManager.getRound() > statsManager.getBestRound();
        }

        // ── Método para SaveManager ──────────────────────────────────────────
        // Añade este método a SaveManager si no existe:
        public boolean hasSaveFile() {
            return saveManager.saveExists();
        }



        // Juega las cartas seleccionadas y devuelve el resultado.
    // GameScreen llama a este método al pulsar "Jugar mano".
    //
    // ASUME: HandEvaluator.evaluate(List<Card>) -> ScoringHand
    //        ScoringHand.getHandType() -> HandType
    //        ScoringHand.getScoringCards() -> List<Card>
    //        ScoreEngine.calculateTotalScore(HandEvaluationContext, List<Joker>) -> int
    //        HandEvaluationContext tiene un constructor con los parámetros de abajo;
    //        ajusta si tu constructor difiere.
    public PlayResult playHand(
        List<Card> selectedCards
    ) {
        roundManager.playHand();

        ScoringHand scoringHand = handEvaluator.evaluate(selectedCards);

        // Registrar la mano jugada en GameStats
        gameStats.registerHandPlayed(scoringHand.handType());

        HandEvaluationContext ctx = new HandEvaluationContext(
            scoringHand.handType(),
            selectedCards,
            scoringHand.scoringCards(),
            handLevelManager, this.deckType,
            gameStats,
            wallet
        );
        int scoreGained = scoreEngine.calculateTotalScore(ctx, jokerManager.getActiveJokers());
        currentScore += scoreGained;



        boolean roundWon = roundManager.isRoundWon(currentScore);
        boolean gameOver = roundManager.isGameOver(currentScore)
            || roundManager.isGameOver(playerHand, currentScore);

        if(!roundWon && !gameOver){
            roundManager.discardPlayedCards(selectedCards);
            playerHand.removeCards(selectedCards);
            drawCards();
        }

        return new PlayResult(scoringHand.handType(), scoreGained, currentScore, roundWon, gameOver);
    }

    // Descarta las cartas seleccionadas.
    // Devuelve true si la derrota por cartas agotadas se activa.
    public boolean discardCards(
        List<Card> selectedCards
    ) {
        roundManager.discard();
        roundManager.discardPlayedCards(selectedCards);
        playerHand.removeCards(selectedCards);
        drawCards();
        return roundManager.isGameOver(playerHand, currentScore);
    }

    // Calcula y aplica las ganancias al final de una ronda ganada.
    // Llama a esto ANTES de ir a la tienda.
    //
    // ASUME: EconomyCalculator.calculate(handsLeft, discardsLeft, currentMoney) -> int
    //        Wallet.earn(int) -> void  (sin throws)
    public void applyRoundEarnings() {
        int earnings = economyCalculator.calculateEarnings(
            roundManager.getHands(),
            roundManager.getDiscards(),
            wallet.getAmount()
        );
        wallet.earn(earnings);
    }


    // Genera el contenido de la tienda. Llama a esto antes de navegar a ShopScreen.
    public void prepareShop(
    ) {
        currentShopJokers.clear();
        currentShopJokers.addAll(shopGenerator.generateJokers());
        currentShopHandUpgrades.clear();
        currentShopHandUpgrades.addAll(shopGenerator.generateHandUpgrades());
    }

    // Compra un joker de la tienda. Devuelve true si tuvo éxito.
    public boolean buyJoker(
        Joker joker
    ) {
        try {
            wallet.spend(joker.getPrice());
            jokerManager.add(joker);
            return true;
        } catch (io.angellsan94.angelatro.exceptions.NotEnoughMoneyException |
                 io.angellsan94.angelatro.exceptions.JokerLimitExceededException e) {
            return false;
        }
    }


    // Vende un joker activo. Devuelve true si tuvo éxito.
    // ASUME: JokerManager.remove(Joker) existe.
    public boolean sellJoker(
        Joker joker
    ) {
        jokerManager.remove(joker);          // añade este método si no existe
        wallet.earn(joker.getPrice() / 2);
        return true;
    }

    // Compra una mejora de nivel para un tipo de mano.
    public boolean buyHandUpgrade(
        HandType handType
    ) {
        try {
            wallet.spend(shopGenerator.getHandUpgradePrice());
            handLevelManager.upgrade(handType);
            return true;
        } catch (NotEnoughMoneyException e) {
            return false;
        }
    }



    /**
     * Verifica si el juego ha terminado y actualiza estadísticas si es necesario.
     *
     */
    public void checkGameOver() {
        boolean gameOver = roundManager.isGameOver(currentScore) ;

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
