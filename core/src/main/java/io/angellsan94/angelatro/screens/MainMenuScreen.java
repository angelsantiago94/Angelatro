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
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.Viewport;
import io.angellsan94.angelatro.AngelatroGame;

import java.io.File;

/**
 * Pantalla principal del menú del juego.
 * <p>
 * Muestra opciones para iniciar nueva partida, continuar partida,
 * ver colección, estadísticas y hacer hard reset.
 * Usa Scene2D con TextButton y ClickListener según las especificaciones.
 * </p>
 *
 * @author angellsan94
 * @version 2.0
 * @since 1.0
 */
public class MainMenuScreen implements Screen {

    private final AngelatroGame game;
    private final Viewport viewport;
    private final Stage stage;

    private TextButton newGameButton;
    private TextButton continueButton;
    private TextButton collectionButton;
    private TextButton statsButton;
    private TextButton hardResetButton;
    private Window resetConfirmationDialog;
    private TextButton confirmResetButton;
    private TextButton cancelResetButton;

    private boolean saveFileExists;

    /**
     * Constructor de MainMenuScreen.
     *
     * @param game la instancia principal del juego
     */
    public MainMenuScreen(AngelatroGame game) {
        this.game = game;
        this.viewport = game.getViewport();
        this.stage = new Stage(viewport);

        checkSaveFile();
        createUI();
    }

    /**
     * Verifica si existe el archivo de guardado.
     */
    private void checkSaveFile() {
        String savePath = System.getProperty("user.home") + "/.angelatro/savegame.json";
        File saveFile = new File(savePath);
        saveFileExists = saveFile.exists();
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
        Label titleLabel = new Label("ANGELATRO", game.getSkin());
        titleLabel.setFontScale(2f);
        mainTable.add(titleLabel).padBottom(50).row();

        // Botones del menú principal
        float buttonWidth = 250;
        float buttonHeight = 60;
        float spacing = 15;

        newGameButton = new TextButton("Nueva Partida", game.getSkin());
        newGameButton.setSize(buttonWidth, buttonHeight);
        newGameButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new DeckSelectionScreen(game));
            }
        });
        mainTable.add(newGameButton).width(buttonWidth).height(buttonHeight).padBottom(spacing).row();

        if (saveFileExists) {
            continueButton = new TextButton("Continuar", game.getSkin());
            continueButton.setSize(buttonWidth, buttonHeight);
            continueButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    // TODO: Implementar carga de partida
                    System.out.println("Continuar partida");
                }
            });
            mainTable.add(continueButton).width(buttonWidth).height(buttonHeight).padBottom(spacing).row();

            collectionButton = new TextButton("Colección", game.getSkin());
            collectionButton.setSize(buttonWidth, buttonHeight);
            collectionButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    game.setScreen(new CollectionScreen(game));
                }
            });
            mainTable.add(collectionButton).width(buttonWidth).height(buttonHeight).padBottom(spacing).row();

            statsButton = new TextButton("Estadísticas", game.getSkin());
            statsButton.setSize(buttonWidth, buttonHeight);
            statsButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    game.setScreen(new StatsScreen(game));
                }
            });
            mainTable.add(statsButton).width(buttonWidth).height(buttonHeight).padBottom(spacing).row();

            hardResetButton = new TextButton("Hard Reset", game.getSkin());
            hardResetButton.setSize(buttonWidth, buttonHeight);
            hardResetButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    showResetConfirmationDialog();
                }
            });
            mainTable.add(hardResetButton).width(buttonWidth).height(buttonHeight).padBottom(spacing).row();
        } else {
            collectionButton = new TextButton("Colección", game.getSkin());
            collectionButton.setSize(buttonWidth, buttonHeight);
            collectionButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    game.setScreen(new CollectionScreen(game));
                }
            });
            mainTable.add(collectionButton).width(buttonWidth).height(buttonHeight).padBottom(spacing).row();

            statsButton = new TextButton("Estadísticas", game.getSkin());
            statsButton.setSize(buttonWidth, buttonHeight);
            statsButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    game.setScreen(new StatsScreen(game));
                }
            });
            mainTable.add(statsButton).width(buttonWidth).height(buttonHeight).padBottom(spacing).row();

            hardResetButton = new TextButton("Hard Reset", game.getSkin());
            hardResetButton.setSize(buttonWidth, buttonHeight);
            hardResetButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    showResetConfirmationDialog();
                }
            });
            mainTable.add(hardResetButton).width(buttonWidth).height(buttonHeight).padBottom(spacing).row();
        }
    }

    /**
     * Muestra el diálogo de confirmación de Hard Reset.
     */
    private void showResetConfirmationDialog() {
        resetConfirmationDialog = new Window("Hard Reset", game.getSkin());
        resetConfirmationDialog.setSize(450, 250);
        resetConfirmationDialog.setPosition(
                (viewport.getWorldWidth() - resetConfirmationDialog.getWidth()) / 2,
                (viewport.getWorldHeight() - resetConfirmationDialog.getHeight()) / 2
        );

        Label messageLabel = new Label("¿Estás seguro de que quieres hacer Hard Reset?\nEsto borrará todos los archivos de guardado.", game.getSkin());
        resetConfirmationDialog.add(messageLabel).pad(30).padTop(40);

        resetConfirmationDialog.row();

        Table buttonTable = new Table();
        resetConfirmationDialog.add(buttonTable).padBottom(30);

        confirmResetButton = new TextButton("Confirmar", game.getSkin());
        confirmResetButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                performHardReset();
                resetConfirmationDialog.remove();
            }
        });
        buttonTable.add(confirmResetButton).pad(10);

        cancelResetButton = new TextButton("Cancelar", game.getSkin());
        cancelResetButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                resetConfirmationDialog.remove();
            }
        });
        buttonTable.add(cancelResetButton).pad(10);

        stage.addActor(resetConfirmationDialog);
    }

    /**
     * Realiza el Hard Reset borrando todos los archivos de guardado.
     */
    private void performHardReset() {
        String savePath = System.getProperty("user.home") + "/.angelatro";
        File saveDirectory = new File(savePath);

        if (saveDirectory.exists()) {
            File[] files = saveDirectory.listFiles();
            if (files != null) {
                for (File file : files) {
                    file.delete();
                }
            }
        }

        checkSaveFile();
        createUI();
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
        checkSaveFile();
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
