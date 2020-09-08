package com.scorpion.NeonphotoEditor.Videoneoneffect;

import android.opengl.GLES20;
import java.nio.Buffer;

public abstract class BufferOutput<T extends Buffer> extends FBORender {
    protected T mOutputBuffer;

    public abstract void bufferOutput(T t);

    public abstract T initBuffer(int i, int i2);

    /* access modifiers changed from: protected */
    public void onDraw() {
        super.onDraw();
        if (this.mOutputBuffer == null || this.mSizeChanged) {
            this.mOutputBuffer = initBuffer(getWidth(), getHeight());
        }
        GLES20.glReadPixels(0, 0, getWidth(), getHeight(), 6408, 5121, this.mOutputBuffer);
        bufferOutput(this.mOutputBuffer);
    }

    public void destroy() {
        super.destroy();
        if (this.mOutputBuffer != null) {
            this.mOutputBuffer.clear();
            this.mOutputBuffer = null;
        }
    }
}
