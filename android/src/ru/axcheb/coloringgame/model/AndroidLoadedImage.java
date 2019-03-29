package ru.axcheb.coloringgame.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.badlogic.gdx.Gdx;


import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import ru.axcheb.coloringgame.model.LoadedImage;


public class AndroidLoadedImage implements LoadedImage {

    private Map<Integer, Integer> colorToNumberMap;
    private Bitmap bitmap;


    @Override
    public void loadImage(String fileName) {
        Set<Integer> colors = new HashSet<>();

        bitmap = BitmapFactory.decodeStream(Gdx.files.internal(fileName).read());

        for (int i = 0; i < getWidth(); i ++) {
            for (int j = 0; j < getHeight(); j ++) {
                colors.add(bitmap.getPixel(i, j));
            }
        }

        colorToNumberMap = new HashMap<>();
        int i = 0;
        for (Integer color : colors) {
            colorToNumberMap.put(color, i);
            i ++;
        }
    }

    public Map<Integer, Integer> getColorMap() {
        return colorToNumberMap;
    }

    public Integer getNumberByColor(Integer color) {
        return colorToNumberMap.get(color);
    }

    public Integer getColor(Integer i, Integer j) {
        return bitmap.getPixel(i, getHeight() - 1 - j);
    }

    public Integer getNumberByCoordinate(Integer i, Integer j) {
        return getNumberByColor(getColor(i, j));
    }

    public int getWidth() {
        return bitmap.getWidth();
    }

    public int getHeight() {
        return bitmap.getHeight();
    }
}
