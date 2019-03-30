package ru.axcheb.coloringgame;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;

import ru.axcheb.coloringgame.model.ImageState;
import ru.axcheb.coloringgame.screens.GameScreen;

public class ColoringGame extends Game {

    private ImageState imageState;

    public ColoringGame(ImageState imageState) {
        this.imageState = imageState;
    }

    private Screen gameScreen;

    @Override
    public void create() {
        gameScreen = new GameScreen(imageState);
        setScreen(gameScreen);
    }

}
