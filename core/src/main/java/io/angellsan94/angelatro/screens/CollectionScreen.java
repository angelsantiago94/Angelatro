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
import io.angellsan94.angelatro.logic.model.DeckType;
import io.angellsan94.angelatro.logic.persistence.UnlockService;

/**
 * Pantalla de colección.
 * <p>
 * Muestra los jokers y mazos desbloqueados.
 * Usa Scene2D con TextButton y ClickListener según las especificaciones.
 * </p>
 *
 * @author angellsan94
 * @version 2.0
 * @since 1.0
 */
public class CollectionScreen implements Screen {

    private final AngelatroGame game;
    private final Viewport viewport;
    private final Stage stage;

    private final UnlockService unlockService;

    private TextButton jokersTab;
    private TextButton decksTab;
    private TextButton backButton;
    private Table contentTable;

    private boolean showingJokers;

    /**
     * Constructor de CollectionScreen.
     *
     * @param game la instancia principal del juego
     */
    public CollectionScreen(AngelatroGame game) {
        this.game = game;
        this.viewport = game.getViewport();
        this.stage = new Stage(viewport);

        this.unlockService = new UnlockService();
        this.showingJokers = true;

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
        Label titleLabel = new Label("COLECCIÓN", game.getSkin());
        titleLabel.setFontScale(1.5f);
        mainTable.add(titleLabel).padBottom(30).row();

        // Tabla de pestañas
        Table tabTable = new Table();
        mainTable.add(tabTable).padBottom(20).row();

        jokersTab = new TextButton("Jokers", game.getSkin());
        jokersTab.setSize(180, 40);
        jokersTab.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                showingJokers = true;
                updateTabColors();
                updateContent();
            }
        });
        tabTable.add(jokersTab).width(180).height(40).padRight(10);

        decksTab = new TextButton("Mazos", game.getSkin());
        decksTab.setSize(180, 40);
        decksTab.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                showingJokers = false;
                updateTabColors();
                updateContent();
            }
        });
        tabTable.add(decksTab).width(180).height(40).padLeft(10);

        // Tabla de contenido
        contentTable = new Table();
        mainTable.add(contentTable).padBottom(20).row();

        updateTabColors();
        updateContent();

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

    /**
     * Actualiza los colores de las pestañas.
     */
    private void updateTabColors() {
        if (showingJokers) {
            jokersTab.setColor(Color.YELLOW);
            decksTab.setColor(Color.GRAY);
        } else {
            jokersTab.setColor(Color.GRAY);
            decksTab.setColor(Color.YELLOW);
        }
    }

    /**
     * Actualiza el contenido según la pestaña seleccionada.
     */
    private void updateContent() {
        contentTable.clear();

        if (showingJokers) {
            Label jokersLabel = new Label("Jokers (TODO)", game.getSkin());
            contentTable.add(jokersLabel).pad(20);
            // TODO: Implementar cuadrícula de jokers
        } else {
            var unlockedDeckIds = unlockService.getUnlockedDeckIds();

            for (DeckType deck : DeckType.values()) {
                boolean isUnlocked = unlockedDeckIds.contains(deck.getId());
                String deckText = deck.name() + " - " + getDeckDescription(deck);
                if (!isUnlocked) {
                    deckText += " (BLOQUEADO)";
                }

                Label deckLabel = new Label(deckText, game.getSkin());
                if (!isUnlocked) {
                    deckLabel.setColor(Color.GRAY);
                }
                contentTable.add(deckLabel).pad(10).row();
            }
        }
    }

    /**
     * Obtiene la descripción del mazo.
     *
     * @param deck el tipo de mazo
     * @return la descripción
     */
    private String getDeckDescription(DeckType deck) {
        return switch (deck) {
            case STANDARD -> "Estándar";
            case WEALTHY -> "+4 monedas";
            case POWERED -> "+10 chips";
            case MULTIBASE -> "+2 mult";
        };
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
