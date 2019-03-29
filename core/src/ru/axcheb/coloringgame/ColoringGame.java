package ru.axcheb.coloringgame;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;

import ru.axcheb.coloringgame.model.LoadedImage;
import ru.axcheb.coloringgame.screens.GameScreen;

public class ColoringGame extends Game {

    private LoadedImage loadedImage;

    public ColoringGame(LoadedImage loadedImage) {
        this.loadedImage = loadedImage;
    }

    private Screen gameScreen;

    @Override
    public void create() {
        gameScreen = new GameScreen(loadedImage);
        setScreen(gameScreen);
    }

}
