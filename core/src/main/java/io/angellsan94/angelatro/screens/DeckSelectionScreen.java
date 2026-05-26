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

import java.util.ArrayList;
import java.util.List;

/**
 * Pantalla de selección de mazo.
 * <p>
 * Muestra los mazos desbloqueados y permite seleccionar uno para iniciar una partida.
 * Usa Scene2D con TextButton y ClickListener según las especificaciones.
 * </p>
 *
 * @author angellsan94
 * @version 2.0
 * @since 1.0
 */
public class DeckSelectionScreen implements Screen {

    private final AngelatroGame game;
    private final Viewport viewport;
    private final Stage stage;

    private final List<DeckType> unlockedDecks;
    private final List<TextButton> deckButtons;
    private TextButton backButton;
    private TextButton playButton;
    private DeckType selectedDeck;

    /**
     * Constructor de DeckSelectionScreen.
     *
     * @param game la instancia principal del juego
     */
    public DeckSelectionScreen(AngelatroGame game) {
        this.game = game;
        this.viewport = game.getViewport();
        this.stage = new Stage(viewport);

        this.unlockedDecks = new ArrayList<>();
        this.deckButtons = new ArrayList<>();

        loadUnlockedDecks();
        createUI();
    }

    /**
     * Carga los mazos desbloqueados.
     */
    private void loadUnlockedDecks() {
        UnlockService unlockService = new UnlockService();
        var unlockedDeckIds = unlockService.getUnlockedDeckIds();

        for (String deckId : unlockedDeckIds) {
            DeckType.fromIdOptional(deckId).ifPresent(unlockedDecks::add);
        }

        if (unlockedDecks.isEmpty()) {
            // Si no hay mazos desbloqueados, mostrar STANDARD por defecto
            unlockedDecks.add(DeckType.STANDARD);
        }
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
        Label titleLabel = new Label("Selecciona tu mazo", game.getSkin());
        titleLabel.setFontScale(1.5f);
        mainTable.add(titleLabel).padBottom(40).row();

        // Botones de mazo
        float buttonWidth = 350;
        float buttonHeight = 60;
        float spacing = 15;

        for (int i = 0; i < unlockedDecks.size(); i++) {
            DeckType deck = unlockedDecks.get(i);
            String deckName = deck.name() + " - " + getDeckDescription(deck);

            TextButton deckButton = new TextButton(deckName, game.getSkin());
            deckButton.setSize(buttonWidth, buttonHeight);
            
            final DeckType currentDeck = deck;
            deckButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    selectDeck(currentDeck);
                }
            });
            
            deckButtons.add(deckButton);
            mainTable.add(deckButton).width(buttonWidth).height(buttonHeight).padBottom(spacing).row();
        }

        // Botón "Jugar" (inicialmente oculto)
        playButton = new TextButton("Jugar", game.getSkin());
        playButton.setSize(200, 50);
        playButton.setVisible(false);
        playButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (selectedDeck != null) {
                    game.setScreen(new GameScreen(game, selectedDeck));
                }
            }
        });
        mainTable.add(playButton).width(200).height(50).padTop(20).row();

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
     * Selecciona un mazo y actualiza la UI.
     *
     * @param deck el mazo seleccionado
     */
    private void selectDeck(DeckType deck) {
        selectedDeck = deck;

        // Actualizar colores de los botones
        for (int i = 0; i < Math.min(deckButtons.size(), unlockedDecks.size()); i++) {
            TextButton button = deckButtons.get(i);
            DeckType buttonDeck = unlockedDecks.get(i);

            if (buttonDeck == deck) {
                button.setColor(Color.YELLOW);
            } else {
                button.setColor(Color.WHITE);
            }
        }

        // Mostrar botón "Jugar"
        playButton.setVisible(true);
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
