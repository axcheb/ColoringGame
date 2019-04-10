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

    private static final float BOTTOM_SIZE = 40f;
    private static final float CELL_SIZE = 30f;

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


        translateImageToCenter();
    }

    private void translateImageToCenter() {
        float imageWidth = imageState.getWidth() * CELL_SIZE;
        float deltaX = 0;
        float deltaY = 0;
        if (imageWidth > WORLD_WIDTH) {
            deltaX = (imageWidth - WORLD_WIDTH) / 2f;
        }

        float imageHeight = imageState.getHeight() * CELL_SIZE;
        if (imageHeight > WORLD_HEIGHT) {
            deltaY = (imageHeight - WORLD_HEIGHT) / 2f;
        }

        float zoom = Math.max(imageWidth / WORLD_WIDTH, imageHeight / WORLD_HEIGHT);

        camera.translate(deltaX, deltaY, 0);
        camera.zoom = zoom;
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

        batch.begin();
        renderCell((i, j) -> {
            if (imageState.getRgb(i, j) == null) {
                Integer number = imageState.getInitialNumberByCoordinate(i, j);
                Vector2 cellVector = getCellPosition(i, j);
                cellNumberFont.draw(batch, String.valueOf(number + ColorButtonsPanel.COLOR_START_NUMBER), cellVector.x + 10, cellVector.y + 20);
            }
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
        cellNumberFont.dispose();
        batch.dispose();
        renderer.dispose();
    }
}
