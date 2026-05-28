package io.angellsan94.angelatro;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;

import io.angellsan94.angelatro.logic.game.GameOrchestrator;
import io.angellsan94.angelatro.screens.CollectionScreen;
import io.angellsan94.angelatro.screens.DeckSelectionScreen;
import io.angellsan94.angelatro.screens.GameOverScreen;
import io.angellsan94.angelatro.screens.GameScreen;
import io.angellsan94.angelatro.screens.MainMenuScreen;
import io.angellsan94.angelatro.screens.ShopScreen;
import io.angellsan94.angelatro.screens.StatsScreen;
import io.angellsan94.angelatro.ui.CardActor;
import io.angellsan94.angelatro.ui.JokerActor;

/**
 * Clase principal de LibGDX. Punto de entrada de la aplicación.
 *
 * Responsabilidades:
 *   - Crear y compartir recursos globales (batch, font, viewport).
 *   - Mantener el GameOrchestrator como estado de partida.
 *   - Centralizar la navegación entre pantallas.
 */
public class AngelatroGame extends Game {

    public static final int WIDTH  = 1920;
    public static final int HEIGHT = 1080;

    // Recursos compartidos entre pantallas (se crean una sola vez)
    private SpriteBatch  batch;
    private BitmapFont   font;
    private FitViewport  viewport;

    // Estado de juego compartido
    private GameOrchestrator orchestrator;

    // ── Ciclo de vida LibGDX ─────────────────────────────────────────────

    @Override
    public void create() {
        batch      = new SpriteBatch();
        font       =  new BitmapFont(Gdx.files.internal("fonts/font-export.fnt"));
        font.getData().setScale(1.5f);
        viewport   = new FitViewport(WIDTH, HEIGHT);
        orchestrator = new GameOrchestrator();

        CardActor.initTextures();   // texturas compartidas de cartas
        JokerActor.initTextures();  // texturas compartidas de jokers

        showMainMenu();
    }

    @Override
    public void render() {
        super.render();             // delega en la pantalla activa
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        if (getScreen() != null) getScreen().resize(width, height);
    }

    @Override
    public void dispose() {
        if (getScreen() != null) getScreen().dispose();
        CardActor.disposeTextures();
        JokerActor.disposeTextures();
        batch.dispose();
        font.dispose();
    }

    // ── Navegación ───────────────────────────────────────────────────────

    public void showMainMenu() {
        setScreen(new MainMenuScreen(this));
    }

    public void showDeckSelection() {
        setScreen(new DeckSelectionScreen(this));
    }

    /** Inicia la pantalla de juego. El orquestador ya debe tener una partida activa. */
    public void showGame() {
        setScreen(new GameScreen(this));
    }

    /** Prepara el contenido de la tienda y navega a ella. */
    public void showShop() {
        orchestrator.prepareShop();      // genera jokers y mejoras
        setScreen(new ShopScreen(this));
    }

    public void showGameOver() {
        setScreen(new GameOverScreen(this));
    }

    public void showCollection() {
        setScreen(new CollectionScreen(this));
    }

    public void showStats() {
        setScreen(new StatsScreen(this));
    }

    // ── Getters de recursos ──────────────────────────────────────────────

    public SpriteBatch      getBatch()        { return batch; }
    public BitmapFont       getFont()         { return font; }
    public FitViewport      getViewport()     { return viewport; }
    public GameOrchestrator getOrchestrator() { return orchestrator; }
}

