package ru.axcheb.coloringgame.desktop.model;

import com.badlogic.gdx.Gdx;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.imageio.ImageIO;

import ru.axcheb.coloringgame.model.LoadedImage;

public class DesktopLoadedImage implements LoadedImage {

    // bb-cnt-2019.gif

    private BufferedImage bufferedImage;
    private Map<Integer, Integer> colorToNumberMap;

    @Override
    public void loadImage(String fileName) {
        Set<Integer> colors = new HashSet<>();
        File initialImage = Gdx.files.internal(fileName).file();


        try {
            bufferedImage = ImageIO.read(initialImage);

        } catch (IOException e) {
            e.printStackTrace(); //TODO
        }

        int height = bufferedImage.getHeight();
        int width = bufferedImage.getWidth();
        for (int i = 0; i < height; i ++) {
            for (int j = 0; j < width; j ++) {
                colors.add(bufferedImage.getRGB(j, i));
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
        return bufferedImage.getRGB(i, getHeight() - 1 - j);
    }

    public Integer getNumberByCoordinate(Integer i, Integer j) {
        return getNumberByColor(getColor(i, j));
    }

    public int getWidth() {
        return bufferedImage.getWidth();
    }

    public int getHeight() {
        return bufferedImage.getHeight();
    }
}
