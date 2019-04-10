package ru.axcheb.coloringgame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.viewport.FitViewport;

import ru.axcheb.coloringgame.ColoringGame;

import static ru.axcheb.coloringgame.screens.GameScreen.WORLD_HEIGHT;
import static ru.axcheb.coloringgame.screens.GameScreen.WORLD_WIDTH;

public class MenuScreen implements Screen {

    private Stage stage;
    private ColoringGame coloringGame;
    private Skin skin = new Skin(Gdx.files.internal("uiskin.json"));
    private TextField field;

    public MenuScreen(ColoringGame coloringGame) {
        this.coloringGame = coloringGame;
        stage = new Stage(new FitViewport(WORLD_WIDTH, WORLD_HEIGHT));

        field = createField();
        stage.addActor(field);
        stage.addActor(createPlayButton());

        Gdx.input.setInputProcessor(stage);
    }

    private TextButton createPlayButton() {
        TextButton playButton = new TextButton("Play", skin, "default");
        playButton.setBounds(WORLD_WIDTH / 2f - 50f, WORLD_HEIGHT - 150, 100f, 30f);
        Screen me = this;

        Dialog cantOpenDialog = new Dialog("", skin);
        cantOpenDialog.text("Can't open this file!");
        cantOpenDialog.button("Ok");

        playButton.addListener(new InputListener() {
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                GameScreen screen;
                try {
                    screen = new GameScreen(coloringGame, field.getText());
                    coloringGame.setScreen(screen);
                    me.dispose();
                } catch (GdxRuntimeException e) {
                    cantOpenDialog.show(stage);
                    cantOpenDialog.setPosition(Math.round((stage.getWidth() - cantOpenDialog.getWidth()) / 2), WORLD_HEIGHT - 300);
                }
            }

            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

        });
        return playButton;
    }

    private TextField createField() {
        TextField field = new TextField("bb-cnt-2019.gif", skin);
        field.setBounds(WORLD_WIDTH / 2f - 100f, WORLD_HEIGHT - 100, 200f, 30f);
        return field;
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height);
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
        stage.dispose();
    }
}
