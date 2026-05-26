package io.angellsan94.angelatro.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.Viewport;
import io.angellsan94.angelatro.AngelatroGame;
import io.angellsan94.angelatro.logic.game.GameOrchestrator;
import io.angellsan94.angelatro.logic.model.Card;
import io.angellsan94.angelatro.logic.model.DeckType;
import io.angellsan94.angelatro.ui.CardView;

import java.util.ArrayList;
import java.util.List;

/**
 * Pantalla principal del juego.
 * <p>
 * Muestra el HUD, la mano del jugador, los jokers activos y permite jugar cartas.
 * Usa Scene2D con TextButton y ClickListener según las especificaciones.
 * </p>
 *
 * @author angellsan94
 * @version 2.0
 * @since 1.0
 */
public class GameScreen implements Screen {

    private final AngelatroGame game;
    private final Viewport viewport;
    private final Stage stage;

    private final GameOrchestrator gameOrchestrator;
    private final List<CardView> cardViews;
    private final List<Card> selectedCards;

    private TextButton playHandButton;
    private TextButton discardButton;
    private TextButton backButton;

    /**
     * Constructor de GameScreen.
     *
     * @param game la instancia principal del juego
     * @param deckType el tipo de mazo seleccionado
     */
    public GameScreen(AngelatroGame game, DeckType deckType) {
        this.game = game;
        this.viewport = game.getViewport();
        this.stage = new Stage(viewport);

        this.gameOrchestrator = new GameOrchestrator();
        this.cardViews = new ArrayList<>();
        this.selectedCards = new ArrayList<>();

        gameOrchestrator.startNewGame(deckType);
        createUI();
    }

    /**
     * Crea los elementos de la interfaz de usuario usando Scene2D.
     */
    private void createUI() {
        stage.clear();

        // Tabla principal centrada
        Table mainTable = new Table();
        mainTable.setFillParent(true);
        mainTable.center();
        stage.addActor(mainTable);

        // HUD
        Table hudTable = new Table();
        mainTable.add(hudTable).padBottom(30).row();

        Label scoreLabel = new Label("Puntuación: 0 / " + gameOrchestrator.getRoundManager().getTargetScore(), game.getSkin());
        hudTable.add(scoreLabel).padRight(15);

        Label roundLabel = new Label("Ronda: " + gameOrchestrator.getRoundManager().getRound(), game.getSkin());
        hudTable.add(roundLabel).padRight(15);

        Label handsLabel = new Label("Manos: " + gameOrchestrator.getRoundManager().getHands(), game.getSkin());
        hudTable.add(handsLabel).padRight(15);

        Label discardsLabel = new Label("Descartes: " + gameOrchestrator.getRoundManager().getDiscards(), game.getSkin());
        hudTable.add(discardsLabel).padRight(15);

        Label coinsLabel = new Label("Monedas: " + gameOrchestrator.getWallet().getAmount(), game.getSkin());
        hudTable.add(coinsLabel);

        // Mano del jugador
        Label handLabel = new Label("Mano del jugador (TODO)", game.getSkin());
        mainTable.add(handLabel).padBottom(30).row();

        // Botones de acción
        Table buttonTable = new Table();
        mainTable.add(buttonTable).padTop(20).row();

        playHandButton = new TextButton("Jugar", game.getSkin());
        playHandButton.setSize(150, 40);
        playHandButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (selectedCards.size() >= 1) {
                    // TODO: Implementar lógica de jugar mano
                    System.out.println("Jugar mano");
                }
            }
        });
        buttonTable.add(playHandButton).width(150).height(40).padRight(15);

        discardButton = new TextButton("Descartar", game.getSkin());
        discardButton.setSize(150, 40);
        discardButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (selectedCards.size() >= 1 && gameOrchestrator.getRoundManager().getDiscards() > 0) {
                    // TODO: Implementar lógica de descartar
                    System.out.println("Descartar");
                }
            }
        });
        buttonTable.add(discardButton).width(150).height(40).padRight(15);

        backButton = new TextButton("Volver", game.getSkin());
        backButton.setSize(120, 40);
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MainMenuScreen(game));
            }
        });
        buttonTable.add(backButton).width(120).height(40).padLeft(15);

        // Crear CardViews para las cartas de la mano
        // TODO: Implementar cuando PlayerHand esté disponible
    }

    /**
     * Alterna la selección de una carta.
     *
     * @param cardView la carta a alternar
     */
    private void toggleCardSelection(CardView cardView) {
        if (cardView.isSelected()) {
            cardView.setSelected(false);
            selectedCards.remove(cardView.getCard());
        } else {
            cardView.setSelected(true);
            selectedCards.add(cardView.getCard());
        }
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        createUI();
    }

    @Override
    public void pause() {
        // No implementado
    }

    @Override
    public void resume() {
        // No implementado
    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
