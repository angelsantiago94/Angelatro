package io.angellsan94.angelatro;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * Clase principal del juego Angelatro.
 * <p>
 * Extiende Game de LibGDX y gestiona el ciclo de vida del juego,
 * incluyendo la configuración del viewport, carga de recursos y navegación entre pantallas.
 * </p>
 *
 * @author angellsan94
 * @version 1.0
 * @since 1.0
 */
public class AngelatroGame extends Game {

    private static final int SCREEN_WIDTH = 1920;
    private static final int SCREEN_HEIGHT = 1080;

    private SpriteBatch spriteBatch;
    private BitmapFont font;
    private Viewport viewport;
    private Skin skin;

    @Override
    public void create() {
        // Configurar viewport con ajuste de pantalla
        viewport = new FitViewport(SCREEN_WIDTH, SCREEN_HEIGHT);

        // Cargar recursos globales
        spriteBatch = new SpriteBatch();
        font = new BitmapFont();
        font.getData().setScale(3.5f);

        // Crear Skin programático para Scene2D
        skin = new Skin();
        skin.add("default", font);

        // Crear colores
        skin.add("white", Color.WHITE);
        skin.add("black", Color.BLACK);
        skin.add("red", Color.RED);
        skin.add("green", Color.GREEN);
        skin.add("blue", Color.BLUE);
        skin.add("gray", Color.GRAY);
        skin.add("yellow", Color.YELLOW);

        // Crear estilo de TextButton
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = font;
        textButtonStyle.fontColor = Color.WHITE;
        textButtonStyle.downFontColor = Color.WHITE;
        skin.add("default", textButtonStyle);

        // Crear estilo de Label
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = font;
        labelStyle.fontColor = Color.WHITE;
        skin.add("default", labelStyle);


        // Navegar a pantalla principal
        setScreen(new io.angellsan94.angelatro.screens.MainMenuScreen(this));
    }

    @Override
    public void dispose() {
        // Liberar recursos globales
        if (spriteBatch != null) {
            spriteBatch.dispose();
        }
        if (font != null) {
            font.dispose();
        }
        if (skin != null) {
            skin.dispose();
        }
        if (screen != null) {
            screen.dispose();
        }
    }

    /**
     * Obtiene el SpriteBatch global.
     *
     * @return el SpriteBatch
     */
    public SpriteBatch getSpriteBatch() {
        return spriteBatch;
    }

    /**
     * Obtiene la fuente global.
     *
     * @return la BitmapFont
     */
    public BitmapFont getFont() {
        return font;
    }

    /**
     * Obtiene el viewport global.
     *
     * @return el Viewport
     */
    public Viewport getViewport() {
        return viewport;
    }

    /**
     * Obtiene el Skin global para Scene2D.
     *
     * @return el Skin
     */
    public Skin getSkin() {
        return skin;
    }
}
