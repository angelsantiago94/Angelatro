package io.angellsan94.angelatro.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import io.angellsan94.angelatro.logic.model.Card;
import io.angellsan94.angelatro.logic.model.Rank;
import io.angellsan94.angelatro.logic.model.Suit;

/**
 * Componente visual para mostrar una carta.
 * <p>
 * Muestra un rectángulo con el rango y palo de la carta en color.
 * </p>
 *
 * @author angellsan94
 * @version 1.0
 * @since 1.0
 */
public class CardView {

    private static final float CARD_WIDTH = 60;
    private static final float CARD_HEIGHT = 90;
    private static final float CORNER_RADIUS = 5;

    private final Card card;
    private final Rectangle bounds;
    private boolean selected;

    /**
     * Constructor de CardView.
     *
     * @param card la carta a mostrar
     * @param x    posición x
     * @param y    posición y
     */
    public CardView(Card card, float x, float y) {
        this.card = card;
        this.bounds = new Rectangle(x, y, CARD_WIDTH, CARD_HEIGHT);
        this.selected = false;
    }

    /**
     * Dibuja la carta.
     *
     * @param spriteBatch el SpriteBatch para dibujar texto
     * @param font       la fuente para dibujar texto
     * @param shapeRenderer el ShapeRenderer para dibujar formas
     */
    public void draw(SpriteBatch spriteBatch, BitmapFont font, ShapeRenderer shapeRenderer) {
        // Dibujar rectángulo de fondo
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(selected ? Color.YELLOW : Color.WHITE);
        shapeRenderer.rect(bounds.x, bounds.y, bounds.width, bounds.height);
        shapeRenderer.end();

        // Dibujar borde
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.BLACK);
        shapeRenderer.rect(bounds.x, bounds.y, bounds.width, bounds.height);
        shapeRenderer.end();

        // Dibujar texto del rango y palo
        spriteBatch.begin();
        font.setColor(getSuitColor());
        font.draw(spriteBatch, card.rank().name(), bounds.x + 5, bounds.y + bounds.height - 10);
        font.draw(spriteBatch, card.suit().name(), bounds.x + 5, bounds.y + 25);
        spriteBatch.end();
    }

    /**
     * Obtiene el color del palo.
     *
     * @return el color correspondiente al palo
     */
    private Color getSuitColor() {
        return switch (card.suit()) {
            case HEARTS, DIAMONDS -> Color.RED;
            case CLUBS, SPADES -> Color.BLACK;
        };
    }

    /**
     * Verifica si un punto está dentro de la carta.
     *
     * @param x coordenada x
     * @param y coordenada y
     * @return true si el punto está dentro
     */
    public boolean contains(float x, float y) {
        return bounds.contains(x, y);
    }

    /**
     * Marca la carta como seleccionada o no.
     *
     * @param selected true para seleccionar
     */
    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    /**
     * Verifica si la carta está seleccionada.
     *
     * @return true si está seleccionada
     */
    public boolean isSelected() {
        return selected;
    }

    /**
     * Obtiene los límites de la carta.
     *
     * @return el Rectangle de límites
     */
    public Rectangle getBounds() {
        return bounds;
    }

    /**
     * Obtiene la carta asociada.
     *
     * @return la carta
     */
    public Card getCard() {
        return card;
    }
}
