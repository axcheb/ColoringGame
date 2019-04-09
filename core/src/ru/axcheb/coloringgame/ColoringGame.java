package ru.axcheb.coloringgame;

import com.badlogic.gdx.Game;

import ru.axcheb.coloringgame.model.ImageState;
import ru.axcheb.coloringgame.screens.MenuScreen;

public class ColoringGame extends Game {

    private ImageState imageState;

    public ColoringGame(ImageState imageState) {
        this.imageState = imageState;
    }

    @Override
    public void create() {
        setScreen(new MenuScreen(this));
    }

    public ImageState getImageState() {
        return imageState;
    }
}
