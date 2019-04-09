package ru.axcheb.coloringgame.view;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import ru.axcheb.coloringgame.model.ImageState;

import static ru.axcheb.coloringgame.screens.GameScreen.WORLD_HEIGHT;
import static ru.axcheb.coloringgame.screens.GameScreen.WORLD_WIDTH;

public class CounterPanel {

    private SpriteBatch batch;
    private Viewport viewport;
    private ImageState imageState;
    private BitmapFont font;
    private ShapeRenderer renderer;

    public CounterPanel(ImageState imageState) {
        this.imageState = imageState;

        font= new BitmapFont();
        font.setColor(Color.BLACK);

        batch = new SpriteBatch();
        renderer = new ShapeRenderer();
        viewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT);
        viewport.apply(true);
    }

    public void render() {
        batch.setProjectionMatrix(viewport.getCamera().combined);
        renderer.begin(ShapeRenderer.ShapeType.Filled);
        renderer.setColor(Color.LIGHT_GRAY);
        renderer.arc(WORLD_WIDTH / 2f - 30f, WORLD_HEIGHT - 25f, 20f, 90f, 180f);
        renderer.arc(WORLD_WIDTH / 2f + 40f, WORLD_HEIGHT - 25f, 20f, 270f, 180f);
        renderer.rect(WORLD_WIDTH / 2f - 30f, WORLD_HEIGHT - 45f, 80f, 40f);
        renderer.end();

        batch.begin();
        if (imageState.isCompleted()) {
            font.draw(batch, "Completed!", WORLD_WIDTH / 2f - 30f, WORLD_HEIGHT - 20f);
        } else {
            font.draw(batch, imageState.getColoredCellsCount() + "/" + imageState.getCellsCount() + "px", WORLD_WIDTH / 2f - 30f, WORLD_HEIGHT - 20f);
        }
        batch.end();
    }

    public void dispose() {
        font.dispose();
        batch.dispose();
        renderer.dispose();
    }
}
