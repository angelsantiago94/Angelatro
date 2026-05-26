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
import io.angellsan94.angelatro.logic.persistence.StatsManager;

/**
 * Pantalla de fin de juego.
 * <p>
 * Muestra la ronda alcanzada, puntuaciones y permite volver al menú principal.
 * Usa Scene2D con TextButton y ClickListener según las especificaciones.
 * </p>
 *
 * @author angellsan94
 * @version 2.0
 * @since 1.0
 */
public class GameOverScreen implements Screen {

    private final AngelatroGame game;
    private final Viewport viewport;
    private final Stage stage;

    private final GameOrchestrator gameOrchestrator;
    private final StatsManager statsManager;

    private final int roundReached;
    private final boolean isNewRecord;

    private TextButton mainMenuButton;

    /**
     * Constructor de GameOverScreen.
     *
     * @param game la instancia principal del juego
     * @param gameOrchestrator el orquestador del juego
     */
    public GameOverScreen(AngelatroGame game, GameOrchestrator gameOrchestrator) {
        this.game = game;
        this.viewport = game.getViewport();
        this.stage = new Stage(viewport);

        this.gameOrchestrator = gameOrchestrator;
        this.statsManager = new StatsManager();

        this.roundReached = gameOrchestrator.getRoundManager().getRound();
        this.isNewRecord = roundReached > statsManager.getBestRound();

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
        Label titleLabel = new Label("GAME OVER", game.getSkin());
        titleLabel.setFontScale(2f);
        titleLabel.setColor(Color.RED);
        mainTable.add(titleLabel).padBottom(40).row();

        // Tabla de información
        Table infoTable = new Table();
        mainTable.add(infoTable).padBottom(30).row();

        Label roundLabel = new Label("Ronda alcanzada: " + roundReached, game.getSkin());
        infoTable.add(roundLabel).pad(10).row();

        if (isNewRecord) {
            Label recordLabel = new Label("¡NUEVO RÉCORD!", game.getSkin());
            recordLabel.setFontScale(1.5f);
            recordLabel.setColor(Color.YELLOW);
            infoTable.add(recordLabel).pad(10).row();
        }

        Label bestRoundLabel = new Label("Mejor ronda: " + statsManager.getBestRound(), game.getSkin());
        infoTable.add(bestRoundLabel).pad(10).row();

        Label gamesPlayedLabel = new Label("Partidas jugadas: " + statsManager.getGamesPlayed(), game.getSkin());
        infoTable.add(gamesPlayedLabel).pad(10).row();

        // Botón "Menú Principal"
        mainMenuButton = new TextButton("Menú Principal", game.getSkin());
        mainMenuButton.setSize(200, 50);
        mainMenuButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MainMenuScreen(game));
            }
        });
        mainTable.add(mainMenuButton).width(200).height(50).padTop(20).row();
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
