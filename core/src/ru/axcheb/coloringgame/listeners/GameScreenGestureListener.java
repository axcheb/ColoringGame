package ru.axcheb.coloringgame.listeners;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;

public class GameScreenGestureListener implements GestureDetector.GestureListener {

    private Viewport viewport;

    public GameScreenGestureListener(Viewport viewport) {
        this.viewport = viewport;
    }

    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {
        Gdx.app.log("INFO","touchDown");
        Gdx.app.log("INFO",x + " " + y + " " + pointer + " " + button);

        Vector2 unProject = viewport.unproject(new Vector2(x, y));
        Gdx.app.log("INFO",unProject.x + " " + unProject.y);

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
        OrthographicCamera camera = (OrthographicCamera) viewport.getCamera();
        Gdx.app.log("INFO", "Zoom performed");
        camera.zoom = (initialDistance / distance) * camera.zoom;
        viewport.apply();
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
