package ru.axcheb.coloringgame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.FitViewport;

import ru.axcheb.coloringgame.ColoringGame;

import static ru.axcheb.coloringgame.screens.GameScreen.WORLD_HEIGHT;
import static ru.axcheb.coloringgame.screens.GameScreen.WORLD_WIDTH;

public class MenuScreen implements Screen {

    private Stage stage;
    private ColoringGame coloringGame;
    private Skin skin = new Skin(Gdx.files.internal("uiskin.json"));

    public MenuScreen(ColoringGame coloringGame) {
        this.coloringGame = coloringGame;
        stage = new Stage(new FitViewport(WORLD_WIDTH, WORLD_HEIGHT));
//        Label title = new Label("Title", new Label.LabelStyle(new BitmapFont(Gdx.files.internal("default.fnt")), Color.RED));
//        title.setAlignment(Align.center);
//        title.setY(Gdx.graphics.getHeight()*2/3);
//        title.setWidth(Gdx.graphics.getWidth());
//        stage.addActor(title);

        stage.addActor(createPlayButton());
        stage.addActor(createHistoryButton());
        stage.addActor(createChooseButton());

        Gdx.input.setInputProcessor(stage);
    }

    private TextButton createPlayButton() {
        TextButton playButton = new TextButton("Play", skin, "default");
        playButton.setBounds(WORLD_WIDTH / 2f - 50f, WORLD_HEIGHT - 100, 100f, 30f);
        Screen me = this;
        playButton.addListener(new InputListener() {
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                coloringGame.setScreen(new GameScreen(coloringGame));
                me.dispose();
            }

            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

        });
        return playButton;
    }

    private TextButton createHistoryButton() {
        TextButton historyButton = new TextButton("History", skin, "default");
        historyButton.setBounds(WORLD_WIDTH / 2f - 50f, WORLD_HEIGHT - 150, 100f, 30f);
        return historyButton;
    }

    private TextButton createChooseButton() {
        TextButton chooseButton = new TextButton("Choose file", skin, "default");
        chooseButton.setBounds(WORLD_WIDTH / 2f - 50f, WORLD_HEIGHT - 200, 100f, 30f);
        return chooseButton;
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
