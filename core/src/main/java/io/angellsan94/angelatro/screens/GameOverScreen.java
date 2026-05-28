package io.angellsan94.angelatro.screens;


import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import io.angellsan94.angelatro.AngelatroGame;

/**
 * Pantalla de Game Over. Se muestra cuando el jugador no alcanza
 * la puntuación objetivo. Destaca la ronda alcanzada como métrica estrella.
 */
public class GameOverScreen extends BaseScreen {

    public GameOverScreen(AngelatroGame game) {
        super(game);
        buildUI();
    }

    private void buildUI() {
        Table root = new Table();
        root.setFillParent(true);
        stage.addActor(root);

        int round        = game.getOrchestrator().getRoundManager().getRound() + 1;
        int score        = game.getOrchestrator().getCurrentScore();
        // ASUME: StatsManager expone getBestRound() y getLastRoundScore()
        boolean isRecord = game.getOrchestrator().isNewRecord();

        root.add(new Label("PARTIDA TERMINADA", skin)).padBottom(8).row();

        // Ronda alcanzada — métrica estrella
        Label lblRound = new Label("Ronda " + round, skin, "gold");
        root.add(lblRound).padBottom(4).row();

        if (isRecord) {
            root.add(new Label("¡Nuevo récord personal!", skin, "gold")).padBottom(12).row();
        }

        root.add(new Label("Puntuación última ronda: " + score, skin)).padBottom(4).row();

        // ASUME: StatsManager.getTotalScore() acumula el score de todas las partidas
        // Si no existe este campo, omite esta línea.
        // root.add(new Label("Puntuación total: " + ..., skin)).padBottom(20).row();
        root.add(new Label("", skin)).padBottom(20).row(); // spacer

        TextButton btnMenu = new TextButton("Menú principal", skin);
        btnMenu.addListener(new ClickListener() {
            @Override public void clicked(InputEvent e, float x, float y) {
                game.showMainMenu();
            }
        });
        root.add(btnMenu).width(200).height(44);
    }
}
