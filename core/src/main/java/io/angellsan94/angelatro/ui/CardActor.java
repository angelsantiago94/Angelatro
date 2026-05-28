package io.angellsan94.angelatro.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import io.angellsan94.angelatro.logic.model.Card;
import io.angellsan94.angelatro.logic.model.Suit;


/**
 * Actor de Scene2D que representa una carta visualmente.
 * Gestiona su propio estado de selección y dibuja fondo + texto.
 */
public class CardActor extends Actor {

    public static final float CARD_W = 195f;
    public static final float CARD_H = 273f;

    private static Texture texNormal;
    private static Texture texSelected;
    private static Texture texBorder;

    private final Card card;
    private boolean selected;
    private final BitmapFont font;
    private final GlyphLayout layout;

    // ── Texturas compartidas ─────────────────────────────────────────────

    /** Inicializa las texturas compartidas. Llama una sola vez en AngelatroGame.create(). */
    public static void initTextures() {
        texNormal   = solidTexture(0.96f, 0.94f, 0.91f, 1f);  // crema
        texSelected = solidTexture(1.00f, 0.85f, 0.00f, 1f);  // dorado
        texBorder   = solidTexture(0.20f, 0.20f, 0.20f, 1f);  // gris oscuro
    }

    /** Libera las texturas compartidas. Llama una sola vez en AngelatroGame.dispose(). */
    public static void disposeTextures() {
        if (texNormal   != null) texNormal.dispose();
        if (texSelected != null) texSelected.dispose();
        if (texBorder   != null) texBorder.dispose();
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

    public CardActor(Card card, BitmapFont font) {
        this.card   = card;
        this.font   = font;
        this.layout = new GlyphLayout();
        setSize(CARD_W, CARD_H);

        addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                selected = !selected;
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

        // Fondo según selección
        Texture bg = selected ? texSelected : texNormal;
        batch.setColor(1f, 1f, 1f, parentAlpha);
        batch.draw(bg, x + 4, y + 4, w - 8, h - 8);

        // Texto del rango
        Color textColor = isRed() ? Color.RED : Color.BLACK;
        font.setColor(textColor.r, textColor.g, textColor.b, parentAlpha);

        String rankStr = rankSymbol();
        layout.setText(font, rankStr);
        font.draw(batch, rankStr, x + 8, y + h - 8);

        // Símbolo del palo centrado
        String suitStr = suitSymbol();
        layout.setText(font, suitStr);
        font.draw(batch, suitStr,
            x + (w - layout.width) / 2f,
            y + h / 2f + layout.height / 2f);

        // Rango en esquina inferior (invertido visualmente)
        font.draw(batch, rankStr, x + 8, y + layout.height + 8);
    }

    // ── Helpers ──────────────────────────────────────────────────────────

    private boolean isRed() {
        return card.suit() == Suit.HEARTS || card.suit() == Suit.DIAMONDS;
    }

    private String rankSymbol() {
        return switch (card.rank()) {
            case AS   -> "A";
            case REY  -> "K";
            case REINA -> "Q";
            case JOTA  -> "J";
            default    -> String.valueOf(card.rank().getValueInChips()); // 2-10
        };
    }

    private String suitSymbol() {
        return switch (card.suit()) {
            case HEARTS   -> "C";
            case DIAMONDS -> "D";
            case CLUBS    -> "T";
            case SPADES   -> "P";
        };
    }

    // ── API pública ──────────────────────────────────────────────────────

    public Card getCard()        { return card; }
    public boolean isSelected()  { return selected; }
    public void setSelected(boolean v) { selected = v; }
    public void deselect()       { selected = false; }
}

