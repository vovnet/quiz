package com.vovnet.quiz.tools;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

/**
 * Created by Vladimir on 05.02.2018.
 */

class FileHelper {
    static Bitmap getBitmapByName(String name, float scale) throws Exception {
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            throw new Exception("SD карта не доступна!");
        }
        //String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + name;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = false;
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        options.inDither = true;
        Bitmap pic = BitmapFactory.decodeFile(name, options);

        int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
        int screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;

        float ratio = Math.min(
                (float) screenWidth / pic.getWidth(),
                (float) screenHeight / pic.getHeight()
        );

        int width = Math.round(ratio * pic.getWidth() * scale);
        int height = Math.round(ratio * pic.getHeight() * scale);

        return Bitmap.createScaledBitmap(pic, width, height, true);
    }
}
