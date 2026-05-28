package io.angellsan94.angelatro.screens;


import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import io.angellsan94.angelatro.AngelatroGame;
import io.angellsan94.angelatro.logic.persistence.StatsManager;

/**
 * Pantalla de estadísticas globales del jugador.
 * ASUME: StatsManager expone getters para cada campo de stats.json.
 */
public class StatsScreen extends BaseScreen {

    public StatsScreen(AngelatroGame game) {
        super(game);
        buildUI();
    }

    private void buildUI() {
        Table root = new Table();
        root.setFillParent(true);
        stage.addActor(root);

        StatsManager stats = game.getOrchestrator().getStatsManager();

        root.add(new Label("Estadísticas", skin)).padBottom(24).row();

        // Métrica estrella
        root.add(stat("Mejor ronda alcanzada",
            "Ronda " + (stats.getBestRound() + 1), true)).padBottom(8).row();

        root.add(stat("Partidas jugadas",
            String.valueOf(stats.getGamesPlayed()), false)).padBottom(8).row();

        root.add(stat("Rondas completadas (total)",
            String.valueOf(stats.getRoundsCompleted()), false)).padBottom(8).row();

        root.add(stat("Mejor puntuación en una ronda",
            String.valueOf(stats.getBestScore()), false)).padBottom(8).row();

        root.add(stat("Máximo de jokers en una partida",
            String.valueOf(stats.getMaxJokersHeld()), false)).padBottom(32).row();

        TextButton btnBack = new TextButton("Volver", skin);
        btnBack.addListener(new ClickListener() {
            @Override public void clicked(InputEvent e, float x, float y) {
                game.showMainMenu();
            }
        });
        root.add(btnBack).width(140).height(36);
    }

    /** Crea una fila de estadística con etiqueta y valor. */
    private Table stat(String label, String value, boolean highlight) {
        Table row = new Table();
        row.add(new Label(label + ": ", skin)).left().padRight(16);
        row.add(new Label(value, skin, highlight ? "gold" : "default")).left();
        return row;
    }
}
