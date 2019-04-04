package ru.axcheb.coloringgame.view;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.List;
import java.util.Map;

import ru.axcheb.coloringgame.model.ImageState;

import static ru.axcheb.coloringgame.model.ImageState.getColor;
import static ru.axcheb.coloringgame.screens.GameScreen.WORLD_HEIGHT;
import static ru.axcheb.coloringgame.screens.GameScreen.WORLD_WIDTH;

public class ColorButtonsPanel {

    private static float NORMAL_WIDTH = 40f;
    private static float NORMAL_HEIGHT = 40f;
    private static float SELECTED_HEIGHT = 60f;

    public static int COLOR_START_NUMBER = 1;

    private Viewport viewport;
    private SpriteBatch batch;
    private ShapeRenderer renderer;
    private BitmapFont cellNumberFont;

    private ImageState imageState;

    public ColorButtonsPanel(ImageState imageState) {
        this.imageState = imageState;
        batch = new SpriteBatch();
        renderer = new ShapeRenderer();
        cellNumberFont = new BitmapFont();
        cellNumberFont.setColor(Color.LIGHT_GRAY);
        viewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT/*, camera*/);
        viewport.apply(true);
    }

    public void render() {
        batch.setProjectionMatrix(viewport.getCamera().combined);
        renderer.setProjectionMatrix(viewport.getCamera().combined);

        renderBoosterButtons();
        renderColorButtons();
    }

    private void renderBoosterButtons() {

        // TODO Бомба. Выбирается как цвет, закрашивает область 10x10px нужными цветами.
    }

    private void renderColorButtons() {
        renderer.begin(ShapeRenderer.ShapeType.Filled);
        int xPosition = 0;
        Map<Integer, Integer> map = imageState.getColorMap();

        List<Integer> usedColors = imageState.getUsedColors();
        for (Integer color : usedColors) {
            renderer.setColor(getColor(color));
            Integer colorNumber = map.get(color);
            float height = NORMAL_HEIGHT;
            if (imageState.getSelectedColorNumber() != null && imageState.getSelectedColorNumber().equals(colorNumber)) {
                height = SELECTED_HEIGHT;
            }
            renderer.rect(xPosition, 0, NORMAL_WIDTH, height);
            xPosition += 40;
        }
        renderer.end();

        batch.begin();
        xPosition = 0;
        for (int i = 0; i < usedColors.size(); i ++) {
            cellNumberFont.draw(batch, String.valueOf(i + COLOR_START_NUMBER), xPosition + 10, 20);
            xPosition += 40;
        }

        batch.end();
    }

    public Viewport getViewport() {
        return viewport;
    }

    public Integer getButtonNumber(Vector2 position) {
        if (position.y > NORMAL_HEIGHT) {
            return null;
        }

        int colorNumber = (int) (position.x / NORMAL_WIDTH);
        if (colorNumber < imageState.getColorMap().size()) {
            return colorNumber;
        }

        return null;
    }

    public void update(int width, int height) {
        viewport.update(width, height);
    }

    public void dispose() {
        cellNumberFont.dispose();
        batch.dispose();
        renderer.dispose();
    }

}
