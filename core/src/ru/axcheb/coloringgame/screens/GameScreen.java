package ru.axcheb.coloringgame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.input.GestureDetector;

import ru.axcheb.coloringgame.listeners.GameScreenGestureListener;
import ru.axcheb.coloringgame.listeners.GameScreenKeyListener;
import ru.axcheb.coloringgame.model.ImageState;
import ru.axcheb.coloringgame.view.ColorButtonsPanel;
import ru.axcheb.coloringgame.view.ImagePanel;

public class GameScreen implements Screen {

    public static float WORLD_WIDTH = 720f;
    public static float WORLD_HEIGHT = 1280f;

    private ColorButtonsPanel colorButtonsPanel;
    private ImagePanel imagePanel;

    public GameScreen(ImageState imageState) {
        colorButtonsPanel = new ColorButtonsPanel(imageState);
        imagePanel = new ImagePanel(imageState);

        Gdx.input.setInputProcessor(new GestureDetector(new GameScreenGestureListener(imagePanel, colorButtonsPanel, imageState)));

        imageState.init("bb-cnt-2019.gif");
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {

        GameScreenKeyListener.handleKeyPress((OrthographicCamera) imagePanel.getViewport().getCamera());
        imagePanel.getViewport().apply();

        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        imagePanel.render();
        colorButtonsPanel.render();
    }

    @Override
    public void resize(int width, int height) {
        colorButtonsPanel.update(width, height);
        imagePanel.update(width, height);
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
        colorButtonsPanel.dispose();
        imagePanel.dispose();
    }

}
