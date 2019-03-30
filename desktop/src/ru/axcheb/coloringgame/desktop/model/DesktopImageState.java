package ru.axcheb.coloringgame.desktop.model;

import com.badlogic.gdx.Gdx;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import ru.axcheb.coloringgame.model.ImageState;

public class DesktopImageState extends ImageState {

    @Override
    public Integer[][] loadImage(String fileName) {
        File initialImage = Gdx.files.internal(fileName).file();

        try {
            BufferedImage bufferedImage = ImageIO.read(initialImage);
            int height = bufferedImage.getHeight();
            int width = bufferedImage.getWidth();
            Integer[][] colorArray = new Integer[width][height];
            for (int i = 0; i < width; i ++) {
                for (int j = height - 1; j >= 0; j --) {
                    colorArray[i][j] = bufferedImage.getRGB(i, j);
                }
            }
            return colorArray;
        } catch (IOException e) {
            throw new IllegalStateException("Can't open file " + fileName, e);
        }
    }

}
