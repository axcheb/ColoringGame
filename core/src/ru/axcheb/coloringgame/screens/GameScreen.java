package ru.axcheb.coloringgame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.Map;
import java.util.function.BiConsumer;

import ru.axcheb.coloringgame.listeners.GameScreenGestureListener;
import ru.axcheb.coloringgame.listeners.GameScreenKeyListener;
import ru.axcheb.coloringgame.model.LoadedImage;

public class GameScreen implements Screen {

    private static int BOTTOM_SIZE = 40;
    private static int CELL_SIZE = 30;

    private OrthographicCamera camera;
    private Viewport viewport;
    private Viewport viewport2;
    private Stage stage;
    private SpriteBatch batch;
    private Texture imgTexture;

    private ShapeRenderer renderer;
    private BitmapFont font;

    /** Font for number of cell. */
    private BitmapFont cellNumberFont;


    private LoadedImage loadedImage;
    private int height;
    private int width;

    public GameScreen(LoadedImage loadedImage) {
        camera = new OrthographicCamera();
//        viewport = new FitViewport(1280, 720, camera);
        viewport = new FitViewport(720, 1280, camera);
        viewport.apply(true);

//        viewport2 = new FitViewport(1280, 720/*, camera*/);
        viewport2 = new FitViewport(720, 1280/*, camera*/);
        viewport2.apply(true);


        Gdx.input.setInputProcessor(new GestureDetector(new GameScreenGestureListener(viewport)));


        this.loadedImage = loadedImage;
        loadedImage.loadImage("bb-cnt-2019.gif");
        height = loadedImage.getHeight();
        width = loadedImage.getWidth();
    }

    @Override
    public void show() {
        batch = new SpriteBatch();

        font = new BitmapFont();
        font.setColor(Color.RED);

        cellNumberFont= new BitmapFont();
        cellNumberFont.setColor(Color.LIGHT_GRAY);

        renderer = new ShapeRenderer();
    }

    @Override
    public void render(float delta) {
        GameScreenKeyListener.handleKeyPress(camera);
        viewport.apply();

        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        renderCellGrid();
        renderColorButtons();

        batch.getProjectionMatrix().setToOrtho2D(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.begin();
        font.draw(batch, "fps: " + Gdx.graphics.getFramesPerSecond(), Gdx.graphics.getWidth() - 50, Gdx.graphics.getHeight() - 50);
        batch.end();
    }

    private void renderCellGrid() {
        batch.setProjectionMatrix(viewport.getCamera().combined);
        renderer.setProjectionMatrix(viewport.getCamera().combined);

//        renderer.begin(ShapeRenderer.ShapeType.Line);
        renderer.begin(ShapeRenderer.ShapeType.Filled);
        renderer.setColor(Color.LIGHT_GRAY);
        renderCell((i, j) -> {
            Vector2 cellVector = getCellPosition(i, j);

            // TODO create method to create Color object
            Color color = new Color();
            Color.rgb888ToColor(color, loadedImage.getColor(i, j));
            renderer.setColor(color);
            renderer.rect(cellVector.x, cellVector.y, CELL_SIZE, CELL_SIZE);
        });
        renderer.end();

        // TODO render only if zoom > xxx

        batch.begin();
        renderCell((i, j) -> {
            Integer number = loadedImage.getNumberByCoordinate(i, j);
            Vector2 cellVector = getCellPosition(i, j);
            cellNumberFont.draw(batch, number.toString(), cellVector.x + 10, cellVector.y + 20);
        });
        batch.end();
    }

    private void renderColorButtons() {
        batch.setProjectionMatrix(viewport2.getCamera().combined);
        renderer.setProjectionMatrix(viewport2.getCamera().combined);

        renderer.begin(ShapeRenderer.ShapeType.Filled);
        int xPosition = 0;
        for (Map.Entry<Integer, Integer> entity : loadedImage.getColorMap().entrySet()) {

            // TODO create method to create Color object
            Color color = new Color();
            Color.rgb888ToColor(color, entity.getKey());
            renderer.setColor(new Color(color));
            renderer.rect(xPosition,0, 40, 40);
            xPosition += 40;
        }
        renderer.end();

        batch.begin();
        xPosition = 0;
        for (Map.Entry<Integer, Integer> entity : loadedImage.getColorMap().entrySet()) {
            Integer colorNumber = entity.getValue();
            cellNumberFont.draw(batch, colorNumber.toString(), xPosition + 10, 20);
            xPosition += 40;
        }
        batch.end();
    }

    private void renderCell(BiConsumer<Integer, Integer> rendererFn) {
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                rendererFn.accept(i, j);
            }
        }
    }

    private Vector2 getCellPosition(int i, int j) {
        return new Vector2(i * CELL_SIZE, j * CELL_SIZE + BOTTOM_SIZE);
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
        viewport2.update(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        batch.dispose();
        imgTexture.dispose();
        //TODO dispose
    }

}
