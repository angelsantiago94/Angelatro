package io.angellsan94.angelatro.screens;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import io.angellsan94.angelatro.AngelatroGame;
import io.angellsan94.angelatro.exceptions.InvalidPlayAreaSizeException;
import io.angellsan94.angelatro.logic.game.GameOrchestrator;
import io.angellsan94.angelatro.logic.game.PlayResult;
import io.angellsan94.angelatro.logic.jokers.Joker;
import io.angellsan94.angelatro.logic.model.Card;
import io.angellsan94.angelatro.ui.CardActor;
import io.angellsan94.angelatro.ui.JokerActor;

/**
 * Pantalla principal de juego.
 *
 * Layout (800x480):
 *   ┌────────────────────────────── HUD (44px) ──────────────────────────────┐
 *   │ Jokers  │              Zona de mensaje                                 │
 *   │ (100px) │                                                              │
 *   │         │──────── Mano del jugador (cartas) ─────────────────────────  │
 *   │         │  [Jugar mano]                    [Descartar]                 │
 *   └─────────┴────────────────────────────────────────────────────────────-─┘
 */
public class GameScreen extends BaseScreen {

    // HUD
    private Label lblScore;
    private Label lblRound;
    private Label lblHands;
    private Label lblDiscards;
    private Label lblMoney;
    private Label lblMessage;

    // Cartas en mano
    private final List<CardActor> cardActors = new ArrayList<>();
    private Group handGroup;

    // Jokers laterales
    private Table jokersTable;

    // Botones de acción
    private TextButton btnPlay;
    private TextButton btnDiscard;

    // Modal de ronda ganada
    private Dialog roundWonDialog;

    public GameScreen(AngelatroGame game) {
        super(game);
        buildUI();
        refreshAll();
    }

    // ── Construcción de UI ───────────────────────────────────────────────

    private void buildUI() {
        Table root = new Table();
        root.setFillParent(true);
        stage.addActor(root);

        // ── Fila HUD ────────────────────────────────────────────────────
        Table hud = new Table();
        hud.setBackground(skin.newDrawable("tex-up")); // fondo oscuro leve

        lblRound    = new Label("", skin);
        lblScore    = new Label("", skin, "gold");
        lblHands    = new Label("", skin);
        lblDiscards = new Label("", skin);
        lblMoney    = new Label("", skin, "gold");

        hud.add(lblRound).padRight(20);
        hud.add(new Label("Puntos: ", skin));
        hud.add(lblScore);
        hud.row();
        hud.add(new Label("Manos: ", skin));
        hud.add(lblHands).padRight(20);
        hud.add(new Label("Descartes: ", skin));
        hud.add(lblDiscards).padRight(20);
        hud.add(new Label("$", skin));
        hud.add(lblMoney).padRight(20);

        root.add(hud).fillX().fillY().height(176).colspan(2).row();

        // ── Columna izquierda: jokers ────────────────────────────────────
        jokersTable = new Table();
        jokersTable.add(new Label("Jokers", skin)).padBottom(6).row();

        // ── Columna central: mensaje + mano + botones ────────────────────
        Table center = new Table();

        lblMessage = new Label("Selecciona cartas para jugar", skin);
        center.add(lblMessage).padTop(16).row();

        // Zona de cartas (las cartas se posicionan manualmente en handGroup)
        handGroup = new Group();
        handGroup.setSize(AngelatroGame.WIDTH - 300f, CardActor.CARD_H + 10f);
        center.add(handGroup)
            .width(handGroup.getWidth())
            .height(handGroup.getHeight())
            .padBottom(30).row();

        // Botones
        Table actions = new Table();
        btnPlay = new TextButton("Jugar mano", skin, "gold");
        btnPlay.addListener(new ClickListener() {
            @Override public void clicked(InputEvent e, float x, float y) { onPlayHand(); }
        });

        btnDiscard = new TextButton("Descartar", skin, "danger");
        btnDiscard.addListener(new ClickListener() {
            @Override public void clicked(InputEvent e, float x, float y) { onDiscard(); }
        });

        actions.add(btnPlay).width(360).height(99).padRight(20);
        actions.add(btnDiscard).width(360).height(99);
        center.add(actions).row();

        root.add(jokersTable).width(300).left().padLeft(10).fillY();
        root.add(center).expand().fill();
    }

    // ── Acciones ─────────────────────────────────────────────────────────

    private void onPlayHand() {
        List<Card> selected = getSelectedCards();
        if (selected.isEmpty()) {
            lblMessage.setText("Selecciona al menos una carta.");
            return;
        }
        if (selected.size() > 5) {
            lblMessage.setText("Máximo 5 cartas por mano.");
            return;
        }

        try {
            PlayResult result = game.getOrchestrator().playHand(selected);
            lblMessage.setText("+" + result.getScoreGained()
                + " pts  [" + handTypeName(result.getHandType().name()) + "]");
            refreshAll();

            if (result.isRoundWon()) {
                showRoundWonDialog();
            } else if (result.isGameOver()) {
               //TODO game.getOrchestrator().handleGameOver();
                game.showGameOver();
            }
        } catch (InvalidPlayAreaSizeException ex) {
            lblMessage.setText("Selecciona al menos una carta.");
        }
    }

    private void onDiscard() {
        List<Card> selected = getSelectedCards();
        if (selected.isEmpty()) {
            lblMessage.setText("Selecciona cartas para descartar.");
            return;
        }

        boolean gameOver = game.getOrchestrator().discardCards(selected);
        lblMessage.setText("Descartadas " + selected.size() + " carta(s).");
        refreshAll();

        if (gameOver) {
            //TODO game.getOrchestrator().handleGameOver();
            game.showGameOver();
        }
    }

    // ── Refresco de UI ───────────────────────────────────────────────────

    /** Actualiza HUD, mano y jokers desde el estado del orquestador. */
    private void refreshAll() {
        refreshHUD();
        refreshHand();
        refreshJokers();
        updateButtonState();
    }

    private void refreshHUD() {
        GameOrchestrator o = game.getOrchestrator();
        lblRound.setText("Ronda " + (o.getRoundManager().getRound() + 1));
        lblScore.setText(o.getCurrentScore() + " / " + o.getRoundManager().getTargetScore());
        lblHands.setText(String.valueOf(o.getRoundManager().getHands()));
        lblDiscards.setText(String.valueOf(o.getRoundManager().getDiscards()));
        lblMoney.setText(String.valueOf(o.getWallet().getAmount()));
    }

    private void refreshHand() {
        handGroup.clear();
        cardActors.clear();

        List<Card> cards = game.getOrchestrator().getPlayerHand().getCards();
        float gap   = 6f;
        float total = cards.size() * (CardActor.CARD_W + gap) - gap;
        float startX = (handGroup.getWidth() - total) / 2f;

        for (int i = 0; i < cards.size(); i++) {
            CardActor actor = new CardActor(cards.get(i), game.getFont());
            actor.setPosition(startX + i * (CardActor.CARD_W + gap), 0);
            cardActors.add(actor);
            handGroup.addActor(actor);
        }
    }

    private void refreshJokers() {
        jokersTable.clearChildren();
        jokersTable.add(new Label("Jokers", skin)).padBottom(6).row();

        for (Joker j : game.getOrchestrator().getJokerManager().getActiveJokers()) {
            JokerActor jokerActor = new JokerActor(j, skin, game.getFont());
            jokersTable.add(jokerActor).padBottom(4).row();
        }
    }

    private void updateButtonState() {
        GameOrchestrator o = game.getOrchestrator();
        btnPlay.setDisabled(o.getRoundManager().getHands() <= 0);
        btnDiscard.setDisabled(o.getRoundManager().getDiscards() <= 0);
    }

    // ── Helpers ──────────────────────────────────────────────────────────

    private List<Card> getSelectedCards() {
        return cardActors.stream()
            .filter(CardActor::isSelected)
            .map(CardActor::getCard)
            .collect(Collectors.toList());
    }

    /** Traduce el nombre del enum HandType a texto legible en español. */
    private String handTypeName(String enumName) {
        return switch (enumName) {
            case "HIGH_CARD"       -> "Carta Alta";
            case "PAIR"            -> "Pareja";
            case "TWO_PAIR"        -> "Doble Pareja";
            case "THREE_OF_A_KIND" -> "Trío";
            case "STRAIGHT"        -> "Escalera";
            case "FLUSH"           -> "Color";
            case "FULL_HOUSE"      -> "Full House";
            case "FOUR_OF_A_KIND"  -> "Póker";
            case "STRAIGHT_FLUSH"  -> "Escalera de Color";
            case "ROYAL_FLUSH"     -> "Escalera Real";
            default                -> enumName;
        };
    }

    /**
     * Muestra un modal anunciando que se ha ganado la ronda.
     * El modal contiene un botón para continuar a la tienda.
     */
    private void showRoundWonDialog() {
        if (roundWonDialog == null) {
            roundWonDialog = new Dialog("¡Ronda Ganada!", skin) {
                @Override
                protected void result(Object object) {
                    // Al cerrar el dialog, aplicar ganancias y mostrar tienda
                    game.getOrchestrator().applyRoundEarnings();
                    game.showShop();
                }
            };

            TextButton btnContinue = new TextButton("Ir a la Tienda", skin, "gold");
            roundWonDialog.button(btnContinue);
            roundWonDialog.text("¡Has alcanzado el objetivo de puntuación!\n\nContinúa a la tienda para mejorar tu mazo.");
        }

        roundWonDialog.show(stage);
    }
}
