package com.scorpion.NeonphotoEditor.Videoneoneffect;

import android.graphics.Bitmap;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public class BitmapOutput extends BufferOutput<IntBuffer> {
    private BitmapOutputCallback mCallback;
    private Bitmap.Config mConfig = Bitmap.Config.ARGB_8888;

    public interface BitmapOutputCallback {
        void bitmapOutput(Bitmap bitmap);
    }

    public void initTextureVertices() {
        this.mTextureVertices = new FloatBuffer[4];
        float[] fArr = {0.0f, 1.0f, 1.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f};
        this.mTextureVertices[0] = ByteBuffer.allocateDirect(fArr.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
        this.mTextureVertices[0].put(fArr).position(0);
        float[] fArr2 = {0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f, 1.0f, 1.0f};
        this.mTextureVertices[1] = ByteBuffer.allocateDirect(fArr2.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
        this.mTextureVertices[1].put(fArr2).position(0);
        float[] fArr3 = {1.0f, 0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f, 1.0f};
        this.mTextureVertices[2] = ByteBuffer.allocateDirect(fArr3.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
        this.mTextureVertices[2].put(fArr3).position(0);
        float[] fArr4 = {1.0f, 1.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f};
        this.mTextureVertices[3] = ByteBuffer.allocateDirect(fArr4.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
        this.mTextureVertices[3].put(fArr4).position(0);
    }

    public IntBuffer initBuffer(int i, int i2) {
        IntBuffer wrap = IntBuffer.wrap(new int[(i * i2)]);
        wrap.position(0);
        return wrap;
    }

    public void bufferOutput(IntBuffer intBuffer) {
        int width = getWidth();
        int height = getHeight();
        if (width > 0 && height > 0) {
            try {
                int[] array = intBuffer.array();
                Bitmap createBitmap = Bitmap.createBitmap(width, height, this.mConfig);
                createBitmap.copyPixelsFromBuffer(IntBuffer.wrap(array));
                if (this.mCallback != null) {
                    this.mCallback.bitmapOutput(createBitmap);
                }
            } catch (OutOfMemoryError e) {
                e.printStackTrace();
                if (this.mCallback != null) {
                    this.mCallback.bitmapOutput((Bitmap) null);
                }
            }
        } else if (this.mCallback != null) {
            this.mCallback.bitmapOutput((Bitmap) null);
        }
    }

    public void setBitmapConfig(Bitmap.Config config) {
        this.mConfig = config;
    }

    public void setBitmapOutputCallback(BitmapOutputCallback bitmapOutputCallback) {
        this.mCallback = bitmapOutputCallback;
    }
}
