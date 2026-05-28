package io.angellsan94.angelatro.screens;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import java.util.List;

import io.angellsan94.angelatro.AngelatroGame;
import io.angellsan94.angelatro.logic.jokers.Joker;
import io.angellsan94.angelatro.logic.jokers.JokerRepository;
import io.angellsan94.angelatro.logic.model.DeckType;
import io.angellsan94.angelatro.ui.JokerActor;

/**
 * Pantalla de Colección. Muestra jokers y mazos desbloqueados.
 * Los bloqueados aparecen en silueta con texto "??".
 */
public class CollectionScreen extends BaseScreen {

    private Table contentTable;
    private boolean showingJokers = true;

    public CollectionScreen(AngelatroGame game) {
        super(game);
        buildUI();
    }

    private void buildUI() {
        Table root = new Table();
        root.setFillParent(true);
        root.top().pad(16);
        stage.addActor(root);

        // Cabecera con pestañas
        Table tabs = new Table();

        TextButton tabJokers = new TextButton("Jokers", skin);
        tabJokers.addListener(new ClickListener() {
            @Override public void clicked(InputEvent e, float x, float y) {
                showingJokers = true;
                buildContent();
            }
        });

        TextButton tabDecks = new TextButton("Mazos", skin);
        tabDecks.addListener(new ClickListener() {
            @Override public void clicked(InputEvent e, float x, float y) {
                showingJokers = false;
                buildContent();
            }
        });

        tabs.add(tabJokers).width(120).height(36).padRight(8);
        tabs.add(tabDecks).width(120).height(36);
        root.add(tabs).padBottom(16).row();

        // Contenido con scroll
        contentTable = new Table();
        contentTable.top().left();
        ScrollPane scroll = new ScrollPane(contentTable, skin);
        root.add(scroll).expand().fill().padBottom(16).row();

        // Volver
        TextButton btnBack = new TextButton("Volver", skin);
        btnBack.addListener(new ClickListener() {
            @Override public void clicked(InputEvent e, float x, float y) {
                game.showMainMenu();
            }
        });
        root.add(btnBack).width(140).height(36);

        buildContent();
    }

    private void buildContent() {
        contentTable.clearChildren();
        if (showingJokers) buildJokerGrid();
        else               buildDeckList();
    }

    private void buildJokerGrid() {
        List<Joker> allJokers = JokerRepository.getAllJokers();

        int cols = 4;
        int col  = 0;
        for (Joker joker : allJokers) {
            // TODO boolean isUnlocked = unlocked.contains(joker.getId());
            boolean isUnlocked = true;
            Table jokerCard = buildJokerCard(joker, isUnlocked);
            contentTable.add(jokerCard).width(150).height(90).pad(6);
            if (++col >= cols) { contentTable.row(); col = 0; }
        }
    }

    private Table buildJokerCard(Joker joker, boolean unlocked) {
        Table card = new Table();
        card.pad(6);
        if (unlocked) {
            card.add(new JokerActor(joker, skin, game.getFont())).row();
            card.add(new Label(joker.getRarity().name(), skin)).row();
        } else {
            card.add(new Label("??", skin)).row();
            card.add(new Label("Bloqueado", skin)).row();
        }
        return card;
    }

    private void buildDeckList() {
       //TODO List<String> unlocked = game.getOrchestrator().getUnlockService().getUnlockedDeckIds();

        for (DeckType dt : DeckType.values()) {
            //TODO boolean isUnlocked = unlocked.contains(dt.getId());
            boolean isUnlocked = true;
            Table row = new Table();
            row.pad(8);

            if (isUnlocked) {
                row.add(new Label(dt.name(), skin, "gold")).left().padRight(16);
                row.add(new Label("$" + dt.getInitialMoney()
                    + "  +" + dt.getBonusChips() + " chips"
                    + "  +" + dt.getBonusMult() + "x", skin)).left();
            } else {
                row.add(new Label("?? (bloqueado)", skin)).left().padRight(16);
                row.add(new Label("Condición de desbloqueo pendiente", skin)).left();
            }
            contentTable.add(row).left().padBottom(6).row();
        }
    }
}
