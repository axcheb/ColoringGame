package ru.axcheb.coloringgame.view;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.function.BiConsumer;

import ru.axcheb.coloringgame.model.ImageState;
import ru.axcheb.coloringgame.model.IntPair;

import static ru.axcheb.coloringgame.model.ImageState.getColor;
import static ru.axcheb.coloringgame.screens.GameScreen.WORLD_HEIGHT;
import static ru.axcheb.coloringgame.screens.GameScreen.WORLD_WIDTH;

public class ImagePanel {

    private static float BOTTOM_SIZE = 40f;
    private static float CELL_SIZE = 30f;

    private ImageState imageState;
    private SpriteBatch batch;
    private ShapeRenderer renderer;
    private BitmapFont cellNumberFont;
    private OrthographicCamera camera;
    private Viewport viewport;

    public ImagePanel(ImageState imageState) {
        this.imageState = imageState;

        batch = new SpriteBatch();
        renderer = new ShapeRenderer();
        cellNumberFont= new BitmapFont();
        cellNumberFont.setColor(Color.LIGHT_GRAY);

        camera = new OrthographicCamera();
        viewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);
        viewport.apply(true);
    }

    public void render() {
        batch.setProjectionMatrix(viewport.getCamera().combined);
        renderer.setProjectionMatrix(viewport.getCamera().combined);

        renderer.setAutoShapeType(true);
        renderer.begin(ShapeRenderer.ShapeType.Line);
        renderCell((i, j) -> {
            Integer rgb = imageState.getRgb(i, j);
            if (rgb == null) {
                renderer.set(ShapeRenderer.ShapeType.Line);
                renderer.setColor(Color.LIGHT_GRAY);
            } else {
                renderer.set(ShapeRenderer.ShapeType.Filled);
                renderer.setColor(getColor(rgb));
            }

            Vector2 cellVector = getCellPosition(i, j);
            renderer.rect(cellVector.x, cellVector.y, CELL_SIZE, CELL_SIZE);
        });
        renderer.end();

        // TODO render only if zoom > xxx

        batch.begin();
        renderCell((i, j) -> {
            Integer number = imageState.getInitialNumberByCoordinate(i, j);
            Vector2 cellVector = getCellPosition(i, j);
            cellNumberFont.draw(batch, number.toString(), cellVector.x + 10, cellVector.y + 20);
        });
        batch.end();
    }

    public Viewport getViewport() {
        return viewport;
    }

    private void renderCell(BiConsumer<Integer, Integer> rendererFn) {
        for (int i = 0; i < imageState.getWidth(); i++) {
            for (int j = 0; j < imageState.getHeight(); j++) {
                rendererFn.accept(i, j);
            }
        }
    }

    private Vector2 getCellPosition(int i, int j) {
        return new Vector2(i * CELL_SIZE, j * CELL_SIZE + BOTTOM_SIZE);
    }

    public IntPair getImageCoordinate(Vector2 position) {
        int x = (int) (position.x / CELL_SIZE);
        int y = (int) ((position.y - BOTTOM_SIZE) / CELL_SIZE);

        if (x < 0 || x >= imageState.getWidth() || y < 0 || y >= imageState.getHeight()) {
            return null;
        }

        return new IntPair(x, y);
    }

    public void update(int width, int height) {
        viewport.update(width, height);
    }

    public void dispose() {
        batch.dispose();
        renderer.dispose();
    }
}
