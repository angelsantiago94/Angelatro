package io.angellsan94.angelatro.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import io.angellsan94.angelatro.logic.jokers.Joker;

/**
 * Actor de Scene2D que representa un joker visualmente.
 * Muestra un recuadro blanco con una "J" y muestra tooltip con nombre y efecto al pasar el ratón.
 */
public class JokerActor extends Actor {

    public static final float JOKER_W = 120f;
    public static final float JOKER_H = 168f;

    private static Texture texNormal;
    private static Texture texHover;
    private static Texture texBorder;

    private final Joker joker;
    private final BitmapFont font;

    private final Skin skin;
    private final GlyphLayout layout;
    private boolean isHovered;
    private Table tooltipTable;

    // ── Texturas compartidas ─────────────────────────────────────────────

    /** Inicializa las texturas compartidas. Llama una sola vez en AngelatroGame.create(). */
    public static void initTextures() {
        texNormal = solidTexture(0.96f, 0.94f, 0.91f, 1f);  // crema
        texHover  = solidTexture(1.00f, 0.95f, 0.80f, 1f);  // amarillo claro
        texBorder = solidTexture(0.20f, 0.20f, 0.20f, 1f);  // gris oscuro
    }

    /** Libera las texturas compartidas. Llama una sola vez en AngelatroGame.dispose(). */
    public static void disposeTextures() {
        if (texNormal != null) texNormal.dispose();
        if (texHover  != null) texHover.dispose();
        if (texBorder != null) texBorder.dispose();
    }

    private static Texture solidTexture(float r, float g, float b, float a) {
        Pixmap pm = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pm.setColor(r, g, b, a);
        pm.fill();
        Texture t = new Texture(pm);
        pm.dispose();
        return t;
    }

    // ── Constructor ──────────────────────────────────────────────────────

    public JokerActor(Joker joker, Skin skin, BitmapFont font) {
        this.joker = joker;
        this.skin = skin;
        this.font = font;
        this.layout = new GlyphLayout();
        setSize(JOKER_W, JOKER_H);

        addListener(new ClickListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                isHovered = true;
                showTooltip();
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                isHovered = false;
                hideTooltip();
            }
        });
    }

    // ── Dibujo ───────────────────────────────────────────────────────────

    @Override
    public void draw(Batch batch, float parentAlpha) {
        float x = getX(), y = getY(), w = getWidth(), h = getHeight();

        // Borde (2px en cada lado)
        batch.setColor(0.2f, 0.2f, 0.2f, parentAlpha);
        batch.draw(texBorder, x, y, w, h);

        // Fondo según hover
        Texture bg = isHovered ? texHover : texNormal;
        batch.setColor(1f, 1f, 1f, parentAlpha);
        batch.draw(bg, x + 4, y + 4, w - 8, h - 8);

        // Texto "J" centrado
        font.setColor(Color.BLACK.r, Color.BLACK.g, Color.BLACK.b, parentAlpha);
        String jStr = "J";
        layout.setText(font, jStr);
        font.draw(batch, jStr,
            x + (w - layout.width) / 2f,
            y + h / 2f + layout.height / 2f);
    }

    // ── Tooltip ─────────────────────────────────────────────────────────

    private void showTooltip() {
        if (tooltipTable == null && getStage() != null) {
            tooltipTable = new Table(skin);
            tooltipTable.setBackground(skin.getDrawable("tex-up"));
            tooltipTable.pad(10);

            Label tooltipLabel = new Label(joker.getName() + "\n" + joker.getDescription(), skin);
            tooltipLabel.setWrap(true);
            tooltipTable.add(tooltipLabel).width(250);

            tooltipTable.pack();
            getStage().addActor(tooltipTable);
        }

        if (tooltipTable != null) {
            // Obtener coordenadas absolutas del joker en el stage
            com.badlogic.gdx.math.Vector2 stagePos = localToStageCoordinates(new com.badlogic.gdx.math.Vector2(0, 0));

            // Posicionar tooltip debajo del joker
            float tooltipX = stagePos.x;
            float tooltipY = stagePos.y - tooltipTable.getHeight() - 5;
            tooltipTable.setPosition(tooltipX, tooltipY);
            tooltipTable.setVisible(true);
            // Asegurar que el tooltip aparezca por encima de todo
            tooltipTable.toFront();
        }
    }

    private void hideTooltip() {
        if (tooltipTable != null) {
            tooltipTable.setVisible(false);
        }
    }

    // ── API pública ──────────────────────────────────────────────────────

    public Joker getJoker() {
        return joker;
    }

    public void dispose() {
        hideTooltip();
        if (tooltipTable != null) {
            tooltipTable.remove();
            tooltipTable = null;
        }
    }
}
