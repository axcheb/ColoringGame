package ru.axcheb.coloringgame.listeners;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;

import ru.axcheb.coloringgame.ColoringGame;
import ru.axcheb.coloringgame.screens.GameScreen;
import ru.axcheb.coloringgame.screens.MenuScreen;

public class GameScreenKeyListener implements InputProcessor {

    private OrthographicCamera camera;
    private ColoringGame coloringGame;

    public GameScreenKeyListener(OrthographicCamera camera, ColoringGame coloringGame) {
        this.camera = camera;
        this.coloringGame = coloringGame;
        Gdx.input.setCatchBackKey(true);
    }

    @Override
    public boolean keyDown(int keycode) {
        switch (keycode) {
            case Input.Keys.A:
                camera.zoom += 0.02;
                return true;
            case Input.Keys.Q:
                camera.zoom -= 0.02;
                return true;
            case Input.Keys.LEFT:
                camera.translate(-3, 0, 0);
                return true;
            case Input.Keys.RIGHT:
                camera.translate(3, 0, 0);
                return true;
            case Input.Keys.DOWN:
                camera.translate(0, -3, 0);
                return true;
            case Input.Keys.UP:
                camera.translate(0, 3, 0);
                return true;
            case Input.Keys.BACK:
                Screen screen = coloringGame.getScreen();
                if (screen instanceof GameScreen) {
                    coloringGame.setScreen(new MenuScreen(coloringGame));
                    return true;
                }
                return false;
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
