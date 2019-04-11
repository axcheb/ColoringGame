package ru.axcheb.coloringgame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.FitViewport;

import ru.axcheb.coloringgame.ColoringGame;
import ru.axcheb.coloringgame.listeners.GameScreenGestureListener;
import ru.axcheb.coloringgame.listeners.GameScreenKeyListener;
import ru.axcheb.coloringgame.model.ImageState;
import ru.axcheb.coloringgame.view.ColorButtonsPanel;
import ru.axcheb.coloringgame.view.CounterPanel;
import ru.axcheb.coloringgame.view.ImagePanel;

import static ru.axcheb.coloringgame.model.GameMode.*;

public class GameScreen implements Screen {

    public static float WORLD_WIDTH = Gdx.graphics.getWidth();
    public static float WORLD_HEIGHT = Gdx.graphics.getHeight();

    private static final float HISTORY_STEP = 5f / 60f; // 5 times in second
    private float historyStepDelta = HISTORY_STEP;

    private ColorButtonsPanel colorButtonsPanel;
    private ImagePanel imagePanel;
    private CounterPanel counterPanel;
    private ColoringGame coloringGame;
    private Dialog completedDialog;
    private Stage stage;
    private Skin skin = new Skin(Gdx.files.internal("uiskin.json"));

    private static final String EXIT_ACTION = "exit";
    private static final String HISTORY_ACTION = "history";

    public GameScreen(ColoringGame coloringGame, String filename) {
        ImageState imageState = coloringGame.getImageState();
        imageState.init(filename);
        this.coloringGame = coloringGame;
        stage = new Stage(new FitViewport(WORLD_WIDTH, WORLD_HEIGHT));
        colorButtonsPanel = new ColorButtonsPanel(imageState);
        imagePanel = new ImagePanel(imageState);
        counterPanel = new CounterPanel(imageState);

        completedDialog = createCompletedDialog();
        imageState.addModeChangeListener(mode -> {
            if (COMPLETED_MODE.equals(mode) ||
                    HISTORY_COMPLETED_MODE.equals(mode)) {
                completedDialog.show(stage);
            }
        });

        InputMultiplexer inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(stage);
        inputMultiplexer.addProcessor(new GameScreenKeyListener((OrthographicCamera) imagePanel.getViewport().getCamera(), coloringGame));
        inputMultiplexer.addProcessor(new GestureDetector(new GameScreenGestureListener(imagePanel, colorButtonsPanel, imageState)));
        Gdx.input.setInputProcessor(inputMultiplexer);
    }

    private Dialog createCompletedDialog() {
        Screen me = this;
        Dialog dialog = new Dialog("", skin) {
            @Override
            protected void result(Object object) {
                if (EXIT_ACTION.equals(object)) {
                    coloringGame.setScreen(new MenuScreen(coloringGame));
                    me.dispose();
                } else if (HISTORY_ACTION.equals(object)) {
                    ImageState imageState = coloringGame.getImageState();
                    imageState.switchToHistoryMode();
                }

            }
        };
        dialog.text("The game is completed!");
        dialog.button("Exit", EXIT_ACTION);
        dialog.button("Show history", HISTORY_ACTION);
        return dialog;
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        imagePanel.getViewport().apply();
        colorButtonsPanel.getViewport().apply();

        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        ImageState imageState = coloringGame.getImageState();
        if (HISTORY_MODE == imageState.getGameMode()) {

            if (historyStepDelta > 0) {
                historyStepDelta -= delta;
            } else {
                historyStepDelta = HISTORY_STEP;
                imageState.nextHistoryStep();
            }
        }

        imagePanel.render();
        colorButtonsPanel.render();
        counterPanel.render();

        stage.act();
        stage.draw();
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
        counterPanel.dispose();
        stage.dispose();
    }

}
