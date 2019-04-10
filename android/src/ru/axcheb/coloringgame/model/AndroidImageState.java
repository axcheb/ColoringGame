package ru.axcheb.coloringgame.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.GdxRuntimeException;

import java.io.InputStream;

public class AndroidImageState extends ImageState {

    @Override
    public Integer[][] loadImage(String fileName) {
        InputStream is;
        try  {
            is = Gdx.files.internal(fileName).read();
        } catch (GdxRuntimeException e) {
            is = Gdx.files.external(fileName).read();
        }

        Bitmap bitmap = BitmapFactory.decodeStream(is);

        Integer[][] colorArray = new Integer[bitmap.getWidth()][bitmap.getHeight()];
        for (int i = 0; i < bitmap.getWidth(); i ++) {
            for (int j = 0; j < bitmap.getHeight(); j ++) {
                colorArray[i][bitmap.getHeight() - 1 - j] = bitmap.getPixel(i, j);
            }
        }

        return colorArray;
    }
}
