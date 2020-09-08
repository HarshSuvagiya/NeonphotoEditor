package com.scorpion.NeonphotoEditor.autocutimage;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextUtils;

public class ImageUtils {
    public static Bitmap tfResizeBilinear(Bitmap bitmap, int i, int i2) {
        if (bitmap == null) {
            return null;
        }
        Bitmap createBitmap = Bitmap.createBitmap(i, i2, Bitmap.Config.ARGB_8888);
        new Canvas(createBitmap).drawBitmap(bitmap, new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight()), new Rect(0, 0, i, i2), (Paint) null);
        return createBitmap;
    }


    public static Bitmap scaleBitmap(Bitmap bitmap, int i, int i2) {
        if (bitmap == null) {
            return null;
        }
        if (i <= 0 || i2 <= 0) {
            return bitmap;
        }
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        if (width > i) {
            if (height <= i2) {
                return createClippedBitmap(bitmap, (bitmap.getWidth() - i) / 2, 0, i, height);
            }
            float f = ((float) i) / ((float) width);
            float f2 = ((float) i2) / ((float) height);
            if (f > f2) {
                Bitmap m153a = m153a(bitmap, f, width, height);
                if (m153a != null) {
                    return createClippedBitmap(m153a, 0, (m153a.getHeight() - i2) / 2, i, i2);
                }
                return bitmap;
            }
            Bitmap m153a2 = m153a(bitmap, f2, width, height);
            if (m153a2 != null) {
                return createClippedBitmap(m153a2, (m153a2.getWidth() - i) / 2, 0, i, i2);
            }
            return bitmap;
        } else if (width > i) {
            return bitmap;
        } else {
            if (height > i2) {
                return createClippedBitmap(bitmap, 0, (bitmap.getHeight() - i2) / 2, width, i2);
            }
            Bitmap createBitmap = Bitmap.createBitmap(i, i2, bitmap.getConfig());
            new Canvas(createBitmap).drawBitmap(bitmap, new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight()), new Rect(0, 0, i, i2), new Paint(1));
            return createBitmap;
        }
    }

    public static Bitmap createClippedBitmap(Bitmap bitmap, int i, int i2, int i3, int i4) {
        if (bitmap == null) {
            return null;
        }
        return Bitmap.createBitmap(bitmap, i, i2, i3, i4);
    }

    private static Bitmap m153a(Bitmap bitmap, float f, int i, int i2) {
        if (bitmap == null) {
            return null;
        }
        Matrix matrix = new Matrix();
        matrix.postScale(f, f);
        return Bitmap.createBitmap(bitmap, 0, 0, i, i2, matrix, true);
    }
}
