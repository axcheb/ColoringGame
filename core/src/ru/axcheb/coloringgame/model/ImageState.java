package ru.axcheb.coloringgame.model;

import com.badlogic.gdx.graphics.Color;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public abstract class ImageState {

    private Integer[][] initialColorArray;
    private Integer[][] colorArray;

    private Map<Integer, Integer> colorToNumberMap;
    private Integer selectedColorNumber;
    //TODO history

    abstract public Integer[][] loadImage(String fileName);

    public void init(String fileName) {
        initialColorArray = loadImage(fileName);
        colorArray = new Integer[getWidth()][getHeight()];
        selectedColorNumber = null;

        Set<Integer> colors = new HashSet<>();
        for (int i = 0; i < getWidth(); i ++) {
            colors.addAll(Arrays.asList(initialColorArray[i]));
        }

        colorToNumberMap = new HashMap<>();
        int i = 0;
        for (Integer color : colors) {
            colorToNumberMap.put(color, i);
            i ++;
        }
    }

    /**
     * Map colorRgb to color number.
     * @return Map colorRgb(key)->colorNumber(value)
     */
    public Map<Integer, Integer> getColorMap() {
        return colorToNumberMap;
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
    }

    public Integer getSelectedColorNumber() {
        return selectedColorNumber;
    }

    public void setImageColor(IntPair pair) {
        Integer rgb = initialColorArray[pair.getX()][pair.getY()];
        if (getInitialNumberByRgb(rgb).equals(selectedColorNumber)) {
            colorArray[pair.getX()][pair.getY()] = initialColorArray[pair.getX()][pair.getY()];
        }
    }

    public static Color getColor(int rgb) {
        Color color = new Color();
        Color.rgb888ToColor(color, rgb);
        return color;
    }
}
