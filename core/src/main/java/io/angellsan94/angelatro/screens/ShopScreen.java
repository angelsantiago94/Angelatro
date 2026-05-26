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

/**
 * Pantalla de tienda.
 * <p>
 * Muestra jokers disponibles para comprar, jokers activos del jugador,
 * mejoras de mano y permite avanzar a la siguiente ronda.
 * Usa Scene2D con TextButton y ClickListener según las especificaciones.
 * </p>
 *
 * @author angellsan94
 * @version 2.0
 * @since 1.0
 */
public class ShopScreen implements Screen {

    private final AngelatroGame game;
    private final Viewport viewport;
    private final Stage stage;

    private final GameOrchestrator gameOrchestrator;

    private TextButton nextRoundButton;
    private TextButton backButton;

    /**
     * Constructor de ShopScreen.
     *
     * @param game la instancia principal del juego
     * @param gameOrchestrator el orquestador del juego
     */
    public ShopScreen(AngelatroGame game, GameOrchestrator gameOrchestrator) {
        this.game = game;
        this.viewport = game.getViewport();
        this.stage = new Stage(viewport);

        this.gameOrchestrator = gameOrchestrator;

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

        // Título y monedas
        Table headerTable = new Table();
        mainTable.add(headerTable).padBottom(30).row();

        Label titleLabel = new Label("TIENDA", game.getSkin());
        titleLabel.setFontScale(1.5f);
        headerTable.add(titleLabel).padRight(50);

        Label coinsLabel = new Label("Monedas: " + gameOrchestrator.getWallet().getAmount(), game.getSkin());
        headerTable.add(coinsLabel);

        // Jokers disponibles
        Label jokersLabel = new Label("Jokers disponibles (TODO)", game.getSkin());
        mainTable.add(jokersLabel).padBottom(20).row();

        // Mejoras de mano
        Label upgradesLabel = new Label("Mejoras de mano (TODO)", game.getSkin());
        mainTable.add(upgradesLabel).padBottom(30).row();

        // Botones de acción
        Table buttonTable = new Table();
        mainTable.add(buttonTable).padTop(20).row();

        backButton = new TextButton("Volver", game.getSkin());
        backButton.setSize(120, 40);
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MainMenuScreen(game));
            }
        });
        buttonTable.add(backButton).width(120).height(40).padRight(20);

        nextRoundButton = new TextButton("Siguiente Ronda", game.getSkin());
        nextRoundButton.setSize(200, 50);
        nextRoundButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                gameOrchestrator.completeRound(true);
                game.setScreen(new GameScreen(game, gameOrchestrator.getDeckType()));
            }
        });
        buttonTable.add(nextRoundButton).width(200).height(50).padLeft(20);
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
