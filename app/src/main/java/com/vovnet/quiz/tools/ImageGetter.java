package com.vovnet.quiz.tools;

import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.text.Html;

/**
 * Created by Vladimir on 05.02.2018.
 */

public class ImageGetter implements Html.ImageGetter {

    private float scale = 1;

    public ImageGetter(float scale) {
        this.scale = scale;
    }

    @Override
    public Drawable getDrawable(String s) {
        Drawable bmp = null;
        try {
            bmp = new BitmapDrawable(FileHelper.getBitmapByName(s, scale));
            bmp.setBounds(0, 0, bmp.getIntrinsicWidth(), bmp.getIntrinsicHeight());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return bmp;
    }


}
