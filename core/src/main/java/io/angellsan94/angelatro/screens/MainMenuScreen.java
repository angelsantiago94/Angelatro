package io.angellsan94.angelatro.screens;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import io.angellsan94.angelatro.AngelatroGame;

import java.io.IOException;

/**
 * Menú principal. Muestra las opciones de inicio y navegación global.
 */
public class MainMenuScreen extends BaseScreen {

    private boolean confirmingReset = false;
    private TextButton btnReset;
    private Label      lblConfirm;

    private static final float BUTTON_WIDTH  = 720f;
    private static final float BUTTON_HEIGHT = 132f;

    public MainMenuScreen(AngelatroGame game) {
        super(game);
        buildUI();
    }

    private void buildUI() {
        Table root = new Table();
        root.setFillParent(true);
        stage.addActor(root);

        // Título
        Label labelTitle = new Label("ANGELATRO", skin);
        labelTitle.setFontScale(2f);
        root.add(labelTitle).padBottom(40).row();

        // Continuar — solo si existe guardado
        if (game.getOrchestrator().hasSaveFile()) {
            TextButton btnContinue = new TextButton("Continuar partida", skin);
            btnContinue.addListener(new ClickListener() {
                @Override public void clicked(InputEvent e, float x, float y) {
                    onContinue();
                }
            });
            root.add(btnContinue).width(BUTTON_WIDTH).height(BUTTON_HEIGHT).padBottom(12).row();
        }

        // Nueva partida
        TextButton btnNew = new TextButton("Nueva partida", skin);
        btnNew.addListener(new ClickListener() {
            @Override public void clicked(InputEvent e, float x, float y) {
                game.showDeckSelection();
            }
        });
        root.add(btnNew).width(BUTTON_WIDTH).height(BUTTON_HEIGHT).padBottom(12).row();

        // Colección
        TextButton btnCol = new TextButton("Coleccion", skin);
        btnCol.addListener(new ClickListener() {
            @Override public void clicked(InputEvent e, float x, float y) {
                game.showCollection();
            }
        });
        root.add(btnCol).width(BUTTON_WIDTH).height(BUTTON_HEIGHT).padBottom(12).row();

        // Estadísticas
        TextButton btnStats = new TextButton("Estadísticas", skin);
        btnStats.addListener(new ClickListener() {
            @Override public void clicked(InputEvent e, float x, float y) {
                game.showStats();
            }
        });
        root.add(btnStats).width(BUTTON_WIDTH).height(BUTTON_HEIGHT).padBottom(32).row();

        // Hard Reset
        btnReset = new TextButton("Hard Reset", skin, "danger");
        btnReset.addListener(new ClickListener() {
            @Override public void clicked(InputEvent e, float x, float y) {
                onResetPressed();
            }
        });
        root.add(btnReset).width(BUTTON_WIDTH).height(BUTTON_HEIGHT).padBottom(4).row();

        lblConfirm = new Label("", skin, "red");
        root.add(lblConfirm).row();
    }

    private void onContinue() {
        // ASUME: SaveManager.load() devuelve Optional<GameSession>
        // y GameOrchestrator tiene loadSession(GameSession) que restaura el estado
        game.getOrchestrator().getSaveManager().load().ifPresent(session -> {
            //TODO game.getOrchestrator().loadSession(session);
            game.showGame();
        });
    }

    private void onResetPressed() {
        if (!confirmingReset) {
            confirmingReset = true;
            lblConfirm.setText("¿Seguro? Pulsa de nuevo para confirmar.");
            btnReset.setText("Confirmar reset");
        } else {
            try {
                game.getOrchestrator().getSaveManager().delete();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            // ASUME: StatsManager.reset() y UnlockService.reset() existen
            // o simplemente borra los archivos directamente:
            // Gdx.files.local("stats.json").delete();
            // Gdx.files.local("unlocks.json").delete();
            game.showMainMenu();
        }
    }
}
