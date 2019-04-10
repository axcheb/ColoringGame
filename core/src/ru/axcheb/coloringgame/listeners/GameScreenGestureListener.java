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
    private float imageZoom;
    private OrthographicCamera imagePanelcamera;

    public GameScreenGestureListener(ImagePanel imagePanel, ColorButtonsPanel colorButtonsPanel, ImageState imageState) {
        this.imagePanel = imagePanel;
        this.colorButtonsPanel = colorButtonsPanel;
        this.imageState = imageState;
        imagePanelcamera = (OrthographicCamera) imagePanel.getViewport().getCamera();
        imageZoom = imagePanelcamera.zoom;
    }

    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {
        imageZoom = imagePanelcamera.zoom;
        colorCellOnTouch(x, y);
        return true;
    }

    private void colorCellOnTouch(float x, float y) {
        Vector2 unProject = colorButtonsPanel.getViewport().unproject(new Vector2(x, y));
        Integer colorNumber = colorButtonsPanel.getButtonNumber(unProject);
        if (colorNumber != null) {
            imageState.selectColor(colorNumber);
        } else if (colorButtonsPanel.isBombButton(unProject)) {
            imageState.selectBomb();
        } else {
            unProject = imagePanel.getViewport().unproject(new Vector2(x, y));
            IntPair imgCoordinate = imagePanel.getImageCoordinate(unProject);
            if (imgCoordinate != null) {
                imageState.colorCell(imgCoordinate);
            }
        }
    }

    @Override
    public boolean tap(float x, float y, int count, int button) {
        if (count == 2) {
            boostColor(x, y);
        }
        return true;
    }

    private void boostColor(float x, float y) {
        Vector2 unProject = imagePanel.getViewport().unproject(new Vector2(x, y));
        IntPair imgCoordinate = imagePanel.getImageCoordinate(unProject);
        if (imgCoordinate != null) {
            imageState.boost(imgCoordinate);
        }
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
        Gdx.app.log("INFO", "pan");
        Vector2 unProject = imagePanel.getViewport().unproject(new Vector2(x, y));
        IntPair imgCoordinate = imagePanel.getImageCoordinate(unProject);
        // We can move camera if color not picked or coordinate out of image (imgCoordinate == null) or if selected color != touched image color.
        boolean canTranslate = (imageState.getSelectedColorNumber() == null ||
                imgCoordinate == null ||
                !imageState.getSelectedColorNumber().equals(imageState.getInitialNumberByCoordinate(imgCoordinate.getX(), imgCoordinate.getY()))
        );

        if (canTranslate) {
            imagePanelcamera.translate(- deltaX, deltaY, 0);
        } else {
            colorCellOnPan(x, y, deltaX, deltaY);
        }
        return true;
    }

    private void colorCellOnPan(float x, float y, float deltaX, float deltaY) {
        Vector2 unProject = imagePanel.getViewport().unproject(new Vector2(x + deltaX, y + deltaY));
        IntPair imgCoordinate = imagePanel.getImageCoordinate(unProject);
        if (imgCoordinate != null) {
            imageState.colorCell(imgCoordinate);
        }
    }

    @Override
    public boolean panStop(float x, float y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean zoom(float initialDistance, float distance) {
        imagePanelcamera.zoom = (initialDistance / distance) * imageZoom;
        imagePanel.getViewport().apply();
        return true;
    }

    @Override
    public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
        return false;
    }

    @Override
    public void pinchStop() {
    }

}
