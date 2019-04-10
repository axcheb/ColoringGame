package ru.axcheb.coloringgame.model;

import com.badlogic.gdx.graphics.Color;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

import static ru.axcheb.coloringgame.model.GameMode.*;

public abstract class ImageState {

    private GameMode gameMode;

    private List<Consumer<GameMode>> modeChangeListeners = new ArrayList<>();

    private Integer[][] initialColorArray;
    private Integer[][] colorArray;

    private Map<Integer, Integer> colorToNumberMap;
    private List<Integer> usedColors;
    private Integer selectedColorNumber;
    private boolean isBomb;

    private int cellsCount;
    private int coloredCellsCount;
    private int historyStep;

    /**
     * History.
     * Internal list - batch.
     * External list - list of batches.
     */
    private List<List<IntPair>> history;

    abstract public Integer[][] loadImage(String fileName);

    public void init(String fileName) {
        gameMode = PLAYING_MODE;
        initialColorArray = loadImage(fileName);
        colorArray = new Integer[getWidth()][getHeight()];
        selectedColorNumber = null;
        history = new ArrayList<>();

        Set<Integer> colorsSet = new HashSet<>();
        for (int i = 0; i < getWidth(); i ++) {
            colorsSet.addAll(Arrays.asList(initialColorArray[i]));
        }

        colorToNumberMap = new HashMap<>();
        usedColors = new ArrayList<>(colorsSet);
        usedColors.sort(Comparator.naturalOrder());

        int i = 0;
        for (Integer color : usedColors) {
            colorToNumberMap.put(color, i);
            i ++;
        }

        cellsCount = getHeight() * getWidth();
        coloredCellsCount = 0;
    }

    /**
     * Map colorRgb to color number.
     * @return Map colorRgb(key)->colorNumber(value)
     */
    public Map<Integer, Integer> getColorMap() {
        return colorToNumberMap;
    }

    public List<Integer> getUsedColors() {
        return usedColors;
    }

    public Integer getInitialNumberByRgb(Integer rgb) {
        return colorToNumberMap.get(rgb);
    }

    public Integer getInitialRgb(int x, int y) {
        return initialColorArray[x][y];
    }

    public Integer getRgb(int x, int y) {
        return colorArray[x][y];
    }

    public Integer getInitialNumberByCoordinate(int i, int j) {
        return getInitialNumberByRgb(getInitialRgb(i, j));
    }

    public int getWidth() {
        return initialColorArray.length;
    }

    public int getHeight() {
        return initialColorArray[0].length;
    }

    public void selectColor(int colorNumber) {
        selectedColorNumber = (selectedColorNumber == null || colorNumber != selectedColorNumber) ? colorNumber : null;
        isBomb = false;
    }

    public Integer getSelectedColorNumber() {
        return selectedColorNumber;
    }

    public void selectBomb() {
        isBomb = true;
        selectedColorNumber = null;
    }

    public boolean isBomb() {
        return isBomb;
    }

    public List<IntPair> colorCell(IntPair pair) {
        if (gameMode != PLAYING_MODE) {
            return Collections.emptyList();
        }
        List<IntPair> result = colorCell0(pair);
        if (!result.isEmpty()) {
            history.add(result);
        }
        return result;
    }

    private List<IntPair> colorCell0(IntPair pair) {
        if (pair.getX() < 0 || pair.getX() >= getWidth() || pair.getY() < 0 || pair.getY() >= getHeight()) {
            return Collections.emptyList();
        }

        Integer rgb = initialColorArray[pair.getX()][pair.getY()];
        if (getInitialNumberByRgb(rgb).equals(selectedColorNumber)) {
            IntPair colored = setInitialColor(pair);
            return (colored == null) ? Collections.emptyList() : Collections.singletonList(colored);
        } else if (isBomb) {
            List<IntPair> result = new ArrayList<>();
            for (IntPair bombPair : nearby(pair, 4)) { // 9x9
                IntPair colored = setInitialColor(bombPair);
                if (colored != null) {
                    result.add(colored);
                }
            }
            return result;
        }
        return Collections.emptyList();
    }

    private IntPair setInitialColor(IntPair pair) {
        if (colorArray[pair.getX()][pair.getY()] == null) {
            colorArray[pair.getX()][pair.getY()] = initialColorArray[pair.getX()][pair.getY()];
            coloredCellsCount ++;
            if (isCompleted()) {
                changeGameMode(COMPLETED_MODE);
            }
            return pair;
        }
        return null;
    }

    public List<IntPair> boost(IntPair pair) {
        if (gameMode != PLAYING_MODE) {
            return Collections.emptyList();
        }
        List<IntPair> result = boost0(pair);
        if (!result.isEmpty()) {
            history.add(result);
        }
        return result;
    }

    private List<IntPair> boost0(IntPair pair) {
        if (selectedColorNumber == null) {
            return Collections.emptyList();
        }

        List<IntPair> result = new ArrayList<>();
        for (IntPair cell : pair.nearby()) {
            List<IntPair> colored = colorCell0(cell);
            result.addAll(colored);
            if (!colored.isEmpty()) {
                result.addAll(boost0(cell));
            }
        }
        return result;
    }

    private void changeGameMode(GameMode mode) {
        gameMode = mode;
        modeChangeListeners.forEach(action -> action.accept(mode));
    }

    public void switchToHistoryMode() {
        changeGameMode(HISTORY_MODE);
        colorArray = new Integer[getWidth()][getHeight()];
        historyStep = 0;
        coloredCellsCount = 0;
    }

    public void nextHistoryStep() {
        if (historyStep == history.size()) {
            return;
        }
        List<IntPair> cells = history.get(historyStep);
        cells.forEach(pair -> {
            colorArray[pair.getX()][pair.getY()] = initialColorArray[pair.getX()][pair.getY()];
            coloredCellsCount ++;
        });

        if (isCompleted()) {
            changeGameMode(HISTORY_COMPLETED_MODE);
        }

        historyStep ++;
    }

    public void addModeChangeListener(Consumer<GameMode> listener) {
        modeChangeListeners.add(listener);
    }

    public int getCellsCount() {
        return cellsCount;
    }

    public int getColoredCellsCount() {
        return coloredCellsCount;
    }

    public boolean isCompleted() {
        return coloredCellsCount == cellsCount;
    }

    public GameMode getGameMode() {
        return gameMode;
    }

    public static Color getColor(int rgb) {
        Color color = new Color();
        Color.rgb888ToColor(color, rgb);
        return color;
    }

    public List<IntPair> nearby(IntPair pair, int near) {
        int x = pair.getX();
        int y = pair.getY();

        int minX = x - near;
        int maxX = x + near + 1;
        int minY = y - near;
        int maxY = y + near + 1;
        minX = (minX < 0) ? 0 : minX;
        minY = (minY < 0) ? 0 : minY;
        maxX = (maxX < getWidth()) ? maxX : getWidth();
        maxY = (maxY < getHeight()) ? maxY : getHeight();

        List<IntPair> result = new ArrayList<>();
        for (int i = minX; i < maxX; i ++) {
            for (int j = minY; j < maxY; j ++) {
                result.add(new IntPair(i, j));
            }
        }
        return result;
    }
}
