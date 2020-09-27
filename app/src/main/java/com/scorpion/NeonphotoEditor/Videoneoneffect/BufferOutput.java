package com.scorpion.NeonphotoEditor.Videoneoneffect;

import android.opengl.GLES20;
import java.nio.Buffer;

public abstract class BufferOutput<T extends Buffer> extends FBORender {
    protected T mOutputBuffer;

    public abstract void bufferOutput(T t);

    public abstract T initBuffer(int i, int i2);

    public void onDraw() {
        super.onDraw();
        if (mOutputBuffer == null || mSizeChanged) {
            mOutputBuffer = initBuffer(getWidth(), getHeight());
        }
        GLES20.glReadPixels(0, 0, getWidth(), getHeight(), 6408, 5121, mOutputBuffer);
        bufferOutput(mOutputBuffer);
    }

    public void destroy() {
        super.destroy();
        if (mOutputBuffer != null) {
            mOutputBuffer.clear();
            mOutputBuffer = null;
        }
    }
}
