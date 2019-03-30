package ru.axcheb.coloringgame.listeners;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;

import ru.axcheb.coloringgame.model.ImageState;
import ru.axcheb.coloringgame.model.IntPair;
import ru.axcheb.coloringgame.view.ColorButtonsPanel;
import ru.axcheb.coloringgame.view.ImagePanel;

public class GameScreenGestureListener implements GestureDetector.GestureListener {

    private ImagePanel imagePanel;
    private ColorButtonsPanel colorButtonsPanel;
    private ImageState imageState;

    public GameScreenGestureListener(ImagePanel imagePanel, ColorButtonsPanel colorButtonsPanel, ImageState imageState) {
        this.imagePanel = imagePanel;
        this.colorButtonsPanel = colorButtonsPanel;
        this.imageState = imageState;
    }

    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {
        Vector2 unProject = colorButtonsPanel.getViewport().unproject(new Vector2(x, y));
        Integer colorNumber = colorButtonsPanel.getButtonNumber(unProject);
        if (colorNumber != null) {
            imageState.selectColor(colorNumber);
        } else {
            unProject = imagePanel.getViewport().unproject(new Vector2(x, y));
            IntPair imgCoordinate = imagePanel.getImageCoordinate(unProject);
            if (imgCoordinate != null) {
                imageState.setImageColor(imgCoordinate);
            }
        }

        return false;
    }

    @Override
    public boolean tap(float x, float y, int count, int button) {
        return false;
    }

    @Override
    public boolean longPress(float x, float y) {
        return false;
    }

    @Override
    public boolean fling(float velocityX, float velocityY, int button) {
        return false;
    }

    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY) {
        return false;
    }

    @Override
    public boolean panStop(float x, float y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean zoom(float initialDistance, float distance) {
        OrthographicCamera camera = (OrthographicCamera) imagePanel.getViewport().getCamera();
        Gdx.app.log("INFO", "Zoom performed");
        camera.zoom = (initialDistance / distance) * camera.zoom;
        imagePanel.getViewport().apply();
        return true;
    }

    @Override
    public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
        return false;
    }

    @Override
    public void pinchStop() {
        Gdx.app.log("INFO", "pinchStop");
    }

}
