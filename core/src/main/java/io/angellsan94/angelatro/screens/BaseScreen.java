package io.angellsan94.angelatro.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import io.angellsan94.angelatro.AngelatroGame;

/**
 * Pantalla base. Gestiona Stage, Skin y limpieza de pantalla.
 * Todas las screens concretas extienden esta clase.
 */
public abstract class BaseScreen implements Screen {

    // Paleta del juego
    protected static final Color COL_BG      = new Color(0.08f, 0.25f, 0.14f, 1f); // verde mesa
    protected static final Color COL_PANEL   = new Color(0.05f, 0.15f, 0.08f, 1f); // verde oscuro
    protected static final Color COL_BTN     = new Color(0.18f, 0.18f, 0.18f, 1f);
    protected static final Color COL_BTN_HOV = new Color(0.32f, 0.32f, 0.32f, 1f);
    protected static final Color COL_BTN_DWN = new Color(0.10f, 0.10f, 0.10f, 1f);
    protected static final Color COL_GOLD    = new Color(1.00f, 0.85f, 0.00f, 1f);
    protected static final Color COL_RED     = new Color(0.85f, 0.15f, 0.15f, 1f);

    protected final AngelatroGame game;
    protected final Stage stage;
    protected final Skin skin;

    protected BaseScreen(AngelatroGame game) {
        this.game  = game;
        this.stage = new Stage(game.getViewport(), game.getBatch());
        this.skin  = buildSkin(game.getFont());
        Gdx.input.setInputProcessor(stage);
    }

    // ── Skin programático ────────────────────────────────────────────────

    private Skin buildSkin(BitmapFont font) {
        Skin s = new Skin();
        s.add("font", font);

        Texture tUp   = solid(COL_BTN);
        Texture tOver = solid(COL_BTN_HOV);
        Texture tDown = solid(COL_BTN_DWN);
        Texture tTrans= solid(new Color(0, 0, 0, 0));

        // Guardamos en el skin para que Skin.dispose() los libere
        s.add("tex-up",    tUp);
        s.add("tex-over",  tOver);
        s.add("tex-down",  tDown);
        s.add("tex-trans", tTrans);

        // Estilo de botón principal
        TextButton.TextButtonStyle btn = new TextButton.TextButtonStyle();
        btn.font      = font;
        btn.fontColor = Color.WHITE;
        btn.up        = new TextureRegionDrawable(new TextureRegion(tUp));
        btn.over      = new TextureRegionDrawable(new TextureRegion(tOver));
        btn.down      = new TextureRegionDrawable(new TextureRegion(tDown));
        s.add("default", btn);

        // Estilo de botón peligroso (rojo) — para Hard Reset / vender
        TextButton.TextButtonStyle btnDanger = new TextButton.TextButtonStyle();
        btnDanger.font      = font;
        btnDanger.fontColor = COL_RED;
        btnDanger.up        = new TextureRegionDrawable(new TextureRegion(tUp));
        btnDanger.over      = new TextureRegionDrawable(new TextureRegion(tOver));
        btnDanger.down      = new TextureRegionDrawable(new TextureRegion(tDown));
        s.add("danger", btnDanger);

        // Estilo de botón dorado — para acciones de compra
        TextButton.TextButtonStyle btnGold = new TextButton.TextButtonStyle();
        btnGold.font      = font;
        btnGold.fontColor = COL_GOLD;
        btnGold.up        = new TextureRegionDrawable(new TextureRegion(tUp));
        btnGold.over      = new TextureRegionDrawable(new TextureRegion(tOver));
        btnGold.down      = new TextureRegionDrawable(new TextureRegion(tDown));
        s.add("gold", btnGold);

        // Estilo de label
        Label.LabelStyle lbl = new Label.LabelStyle(font, Color.WHITE);
        s.add("default", lbl);

        // Estilo de label dorado
        Label.LabelStyle lblGold = new Label.LabelStyle();
        lblGold.font      = font;
        lblGold.fontColor = COL_GOLD;
        s.add("gold", lblGold);

        // Estilo de label rojo
        Label.LabelStyle lblRed = new Label.LabelStyle();
        lblRed.font      = font;
        lblRed.fontColor = COL_RED;
        s.add("red", lblRed);

        ScrollPane.ScrollPaneStyle scrollPaneStyle = new ScrollPane.ScrollPaneStyle();
        s.add("default", scrollPaneStyle);

        Window.WindowStyle windowStyle = new Window.WindowStyle(font, Color.WHITE, null);

        s.add("default", windowStyle);

        return s;
    }

    private Texture solid(Color c) {
        Pixmap pm = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pm.setColor(c);
        pm.fill();
        Texture t = new Texture(pm);
        pm.dispose();
        return t;
    }

    // ── Ciclo de vida ────────────────────────────────────────────────────

    protected void clearScreen() {
        Gdx.gl.glClearColor(COL_BG.r, COL_BG.g, COL_BG.b, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    @Override
    public void render(float delta) {
        clearScreen();
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void show()   { Gdx.input.setInputProcessor(stage); }
    @Override
    public void resize(int w, int h) { stage.getViewport().update(w, h, true); }
    @Override
    public void pause()  {}
    @Override
    public void resume() {}
    @Override
    public void hide()   {}
    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
    }
}
