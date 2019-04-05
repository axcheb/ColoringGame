package ru.axcheb.coloringgame.model;

import com.badlogic.gdx.graphics.Color;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class ImageState {

    private Integer[][] initialColorArray;
    private Integer[][] colorArray;

    private Map<Integer, Integer> colorToNumberMap;
    private List<Integer> usedColors;
    private Integer selectedColorNumber;
    private boolean isBomb;

    private int cellsCount;
    private int colloredCellsCount;

    //TODO history

    abstract public Integer[][] loadImage(String fileName);

    public void init(String fileName) {
        initialColorArray = loadImage(fileName);
        colorArray = new Integer[getWidth()][getHeight()];
        selectedColorNumber = null;

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
        colloredCellsCount = 0;
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
        selectedColorNumber = colorNumber;
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

    public boolean colorCell(IntPair pair) {
        if (pair.getX() < 0 || pair.getX() >= getWidth() || pair.getY() < 0 || pair.getY() >= getHeight()) {
            return false;
        }

        Integer rgb = initialColorArray[pair.getX()][pair.getY()];
        if (getInitialNumberByRgb(rgb).equals(selectedColorNumber)) {
            setInitialColor(pair);
            return true;
        } else if (isBomb) {
            for (IntPair bombPair : nearby(pair, 4)) { // 9x9
                setInitialColor(bombPair);
            }
            return true;
        }

        return false;
    }

    private void setInitialColor(IntPair pair) {
        if (colorArray[pair.getX()][pair.getY()] == null) {
            colorArray[pair.getX()][pair.getY()] = initialColorArray[pair.getX()][pair.getY()];
            colloredCellsCount ++;
        }
    }

    public void boost(IntPair pair) {
        if (selectedColorNumber == null) {
            return;
        }

        for (IntPair cell : pair.nearby()) {
            if (colorCell(cell)) {
                boost(cell);
            }
        }
    }

    public int getCellsCount() {
        return cellsCount;
    }

    public int getColloredCellsCount() {
        return colloredCellsCount;
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
