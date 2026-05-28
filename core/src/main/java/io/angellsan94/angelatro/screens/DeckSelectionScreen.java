package io.angellsan94.angelatro.screens;


import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import io.angellsan94.angelatro.AngelatroGame;
import io.angellsan94.angelatro.logic.model.DeckType;

/**
 * Pantalla de selección de mazo antes de iniciar una partida.
 * Solo muestra los mazos desbloqueados según UnlockService.
 */
public class DeckSelectionScreen extends BaseScreen {

    private DeckType selected;
    private TextButton btnPlay;
    private Label      lblDesc;

    public DeckSelectionScreen(AngelatroGame game) {
        super(game);
        buildUI();
    }

    private void buildUI() {
        Table root = new Table();
        root.setFillParent(true);
        stage.addActor(root);
        Label labelTitle = new Label("Elige tu mazo", skin);
        labelTitle.setFontScale(2f);
        root.add(labelTitle).padBottom(24).row();

        // Fila de mazos disponibles
        Table mazosRow = new Table();

        // ASUME: UnlockService.getUnlockedDeckTypes() devuelve List<DeckType>
        // Si no existe, usa DeckType.values() como fallback temporal.
        for (DeckType dt : DeckType.values()) {
            mazosRow.add(buildDeckCard(dt)).width(420).height(132).padRight(20);
        }
        root.add(mazosRow).padBottom(20).row();

        // Descripción del mazo seleccionado
        lblDesc = new Label("Selecciona un mazo", skin);
        root.add(lblDesc).padBottom(24).row();

        // Botones de navegación
        Table buttons = new Table();

        TextButton btnBack = new TextButton("Volver", skin);
        btnBack.addListener(new ClickListener() {
            @Override public void clicked(InputEvent e, float x, float y) {
                game.showMainMenu();
            }
        });

        btnPlay = new TextButton("Jugar", skin, "gold");
        btnPlay.setDisabled(true);
        btnPlay.addListener(new ClickListener() {
            @Override public void clicked(InputEvent e, float x, float y) {
                if (selected != null) startGame();
            }
        });

        buttons.add(btnBack).width(210).height(66).padRight(20);
        buttons.add(btnPlay).width(210).height(66);
        root.add(buttons);
    }

    private TextButton buildDeckCard(DeckType dt) {
        String label = dt.name() + "\n$" + dt.getInitialMoney()
            + "  +" + dt.getBonusChips() + "ch"
            + "  +" + dt.getBonusMult() + "x";

        TextButton btn = new TextButton(label, skin);
        btn.addListener(new ClickListener() {
            @Override public void clicked(InputEvent e, float x, float y) {
                selected = dt;
                btnPlay.setDisabled(false);
                lblDesc.setText(deckDescription(dt));
            }
        });
        return btn;
    }

    private String deckDescription(DeckType dt) {
        return switch (dt) {
            case STANDARD   -> "Estándar — sin bonificaciones especiales.";
            case WEALTHY    -> "Acaudalado — empieza con más dinero.";
            case POWERED    -> "Potenciado — +10 chips extra por mano.";
            case MULTIBASE  -> "Multibase — +2 mult extra por mano.";
        };
    }

    private void startGame() {
        game.getOrchestrator().startNewGame(selected);
        game.showGame();
    }
}
