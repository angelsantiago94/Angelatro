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
import io.angellsan94.angelatro.logic.persistence.StatsManager;

/**
 * Pantalla de estadísticas.
 * <p>
 * Muestra las estadísticas del jugador.
 * Usa Scene2D con TextButton y ClickListener según las especificaciones.
 * </p>
 *
 * @author angellsan94
 * @version 2.0
 * @since 1.0
 */
public class StatsScreen implements Screen {

    private final AngelatroGame game;
    private final Viewport viewport;
    private final Stage stage;

    private final StatsManager statsManager;

    private TextButton backButton;

    /**
     * Constructor de StatsScreen.
     *
     * @param game la instancia principal del juego
     */
    public StatsScreen(AngelatroGame game) {
        this.game = game;
        this.viewport = game.getViewport();
        this.stage = new Stage(viewport);

        this.statsManager = new StatsManager();

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

        // Título
        Label titleLabel = new Label("ESTADÍSTICAS", game.getSkin());
        titleLabel.setFontScale(1.5f);
        mainTable.add(titleLabel).padBottom(40).row();

        // Tabla de estadísticas
        Table statsTable = new Table();
        mainTable.add(statsTable).padBottom(30).row();

        Label gamesPlayedLabel = new Label("Partidas jugadas: " + statsManager.getGamesPlayed(), game.getSkin());
        statsTable.add(gamesPlayedLabel).pad(10).row();

        Label bestRoundLabel = new Label("Mejor ronda: " + statsManager.getBestRound(), game.getSkin());
        statsTable.add(bestRoundLabel).pad(10).row();

        Label roundsCompletedLabel = new Label("Rondas completadas (total): " + statsManager.getRoundsCompleted(), game.getSkin());
        statsTable.add(roundsCompletedLabel).pad(10).row();

        Label bestScoreLabel = new Label("Mejor puntuación en una ronda: " + statsManager.getBestScore(), game.getSkin());
        statsTable.add(bestScoreLabel).pad(10).row();

        // Botón "Volver"
        backButton = new TextButton("Volver", game.getSkin());
        backButton.setSize(120, 40);
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MainMenuScreen(game));
            }
        });
        mainTable.add(backButton).width(120).height(40).padTop(20).row();
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
