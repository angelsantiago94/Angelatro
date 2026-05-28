package io.angellsan94.angelatro.screens;

import java.util.List;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import io.angellsan94.angelatro.AngelatroGame;
import io.angellsan94.angelatro.logic.game.GameOrchestrator;
import io.angellsan94.angelatro.logic.game.HandType;
import io.angellsan94.angelatro.logic.jokers.Joker;
import io.angellsan94.angelatro.ui.JokerActor;

/**
 * Tienda entre rondas.
 * Muestra 2 jokers y 2 mejoras de mano para comprar.
 * Permite vender jokers activos.
 */
public class ShopScreen extends BaseScreen {

    private Label lblMoney;
    private Table offersTable;
    private Table activeJokersTable;

    public ShopScreen(AngelatroGame game) {
        super(game);
        buildUI();
    }

    private void buildUI() {
        Table root = new Table();
        root.setFillParent(true);
        //root.top().pad(16);
        stage.addActor(root);

        // ── Cabecera ─────────────────────────────────────────────────────
        Table header = new Table();
        int round = game.getOrchestrator().getRoundManager().getRound();
        header.add(new Label("Tienda — Ronda " + round, skin)).expandX().left();
        lblMoney = new Label("$" + game.getOrchestrator().getWallet().getAmount(), skin, "gold");
        header.add(lblMoney).right();
        root.add(header).fillX().padBottom(16).row();

        // ── Ofertas (jokers + mejoras de mano) ───────────────────────────
        offersTable = new Table();
        buildOffers();
        root.add(offersTable).fillX().padBottom(20).row();

        // ── Jokers activos (para vender) ─────────────────────────────────
        root.add(new Label("Jokers activos", skin)).left().padBottom(6).row();
        activeJokersTable = new Table();
        buildActiveJokers();
        ScrollPane scroll = new ScrollPane(activeJokersTable, skin);
        root.add(scroll).fillX().height(90).padBottom(20).row();

        // ── Botón siguiente ronda ─────────────────────────────────────────
        TextButton btnNext = new TextButton("Siguiente ronda →", skin, "gold");
        btnNext.addListener(new ClickListener() {
            @Override public void clicked(InputEvent e, float x, float y) {
                onNextRound();
            }
        });
        root.add(btnNext).width(220).height(44);
    }

    // ── Construcción de ofertas ──────────────────────────────────────────

    private void buildOffers() {
        offersTable.clearChildren();
        offersTable.add(new Label("Jokers disponibles", skin)).colspan(2).padBottom(8).row();

        GameOrchestrator o = game.getOrchestrator();
        List<Joker> jokers = o.getCurrentShopJokers();

        for (Joker j : jokers) {
            offersTable.add(buildJokerOffer(j)).padRight(20);
        }
        offersTable.row().padBottom(16);

        offersTable.add(new Label("Mejoras de mano  (6$)", skin)).colspan(2).padBottom(8).row();
        List<HandType> upgrades = o.getCurrentShopHandUpgrades();
        for (HandType ht : upgrades) {
            offersTable.add(buildHandUpgradeOffer(ht)).padRight(20);
        }
    }

    private Table buildJokerOffer(Joker joker) {
        Table card = new Table();
        card.pad(8);

        card.add(new JokerActor(joker, skin, game.getFont())).padBottom(4).row();
        card.add(new Label(joker.getRarity().name(), skin)).left().padBottom(4).row();

        boolean canAfford  = game.getOrchestrator().getWallet().getAmount() >= joker.getPrice();
        boolean alreadyHas = game.getOrchestrator().getJokerManager()
            .getActiveJokers().contains(joker);
        boolean full       = game.getOrchestrator().getJokerManager().getActiveJokers().size() >= 6;

        TextButton btnBuy = new TextButton("Comprar $" + joker.getPrice(), skin,
            (canAfford && !alreadyHas && !full) ? "gold" : "default");
        btnBuy.setDisabled(!canAfford || alreadyHas || full);
        btnBuy.addListener(new ClickListener() {
            @Override public void clicked(InputEvent e, float x, float y) {
                if (!btnBuy.isDisabled()) {
                    game.getOrchestrator().buyJoker(joker);
                    refreshMoney();
                    buildOffers();     // redibuja ofertas
                    buildActiveJokers();
                }
            }
        });
        card.add(btnBuy).fillX();
        return card;
    }

    private Table buildHandUpgradeOffer(HandType handType) {
        Table card = new Table();
        card.pad(8);

        card.add(new Label(handType.name(), skin)).left().padBottom(4).row();
        int price = game.getOrchestrator().getShopGenerator().getHandUpgradePrice();
        boolean canAfford = game.getOrchestrator().getWallet().getAmount() >= price;

        TextButton btnUpgrade = new TextButton("Mejorar $" + price, skin, canAfford ? "gold" : "default");
        btnUpgrade.setDisabled(!canAfford);
        btnUpgrade.addListener(new ClickListener() {
            @Override public void clicked(InputEvent e, float x, float y) {
                if (!btnUpgrade.isDisabled()) {
                    game.getOrchestrator().buyHandUpgrade(handType);
                    refreshMoney();
                    buildOffers();
                }
            }
        });
        card.add(btnUpgrade).fillX();
        return card;
    }

    // ── Jokers activos ───────────────────────────────────────────────────

    private void buildActiveJokers() {
        activeJokersTable.clearChildren();
        List<Joker> active = game.getOrchestrator().getJokerManager().getActiveJokers();

        if (active.isEmpty()) {
            activeJokersTable.add(new Label("Sin jokers activos", skin)).padLeft(8);
            return;
        }

        for (Joker j : active) {
            activeJokersTable.add(new JokerActor(j, skin, game.getFont())).padRight(12);
            int sellPrice = j.getPrice() / 2;
            TextButton btnSell = new TextButton("Vender $" + sellPrice, skin, "danger");
            btnSell.addListener(new ClickListener() {
                @Override public void clicked(InputEvent e, float x, float y) {
                    game.getOrchestrator().sellJoker(j);
                    refreshMoney();
                    buildActiveJokers();
                    buildOffers(); // actualizar estado de botones de compra
                }
            });
            activeJokersTable.add(btnSell).height(32).padRight(20);
        }
    }

    // ── Acciones ─────────────────────────────────────────────────────────

    private void onNextRound() {
        game.getOrchestrator().completeRound(true);
        game.getOrchestrator().drawCards();    // rellena la mano con el nuevo mazo
        game.getOrchestrator().exitShop();     // guarda partida
        game.showGame();
    }

    private void refreshMoney() {
        lblMoney.setText("$" + game.getOrchestrator().getWallet().getAmount());
    }
}

