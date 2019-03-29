package ru.axcheb.coloringgame.model;

import java.util.Map;

public interface LoadedImage {

    void loadImage(String fileName);

    Map<Integer, Integer> getColorMap();

    Integer getNumberByColor(Integer color);

    Integer getColor(Integer i, Integer j);

    Integer getNumberByCoordinate(Integer i, Integer j);

    int getWidth();

    int getHeight();

}
