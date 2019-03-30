package ru.axcheb.coloringgame.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.badlogic.gdx.Gdx;

public class AndroidImageState extends ImageState {

    @Override
    public Integer[][] loadImage(String fileName) {
        Bitmap bitmap = BitmapFactory.decodeStream(Gdx.files.internal(fileName).read());
        Integer[][] colorArray = new Integer[bitmap.getWidth()][bitmap.getHeight()];
        for (int i = 0; i < bitmap.getWidth(); i ++) {
            for (int j = bitmap.getHeight() - 1; j >= 0; j --) {
                colorArray[i][j] = bitmap.getPixel(i, j);
            }
        }

        return colorArray;
    }
}
