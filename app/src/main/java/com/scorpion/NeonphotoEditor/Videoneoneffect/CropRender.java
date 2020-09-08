package com.scorpion.NeonphotoEditor.Videoneoneffect;

import android.graphics.RectF;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class CropRender extends FBORender {
    private RectF mRegion = new RectF(0.0f, 0.0f, 1.0f, 1.0f);

    public void setCropRegion(RectF rectF) {
        RectF rectF2 = rectF;
        this.mRegion = rectF2;
        FloatBuffer[] floatBufferArr = new FloatBuffer[4];
        float f = rectF2.left;
        float f2 = rectF2.right;
        float f3 = rectF2.top;
        float f4 = rectF2.bottom;
        float[] fArr = {f, f3, f2, f3, f, f4, f2, f4};
        floatBufferArr[0] = ByteBuffer.allocateDirect(fArr.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
        floatBufferArr[0].put(fArr).position(0);
        float[] fArr2 = {f2, f3, f2, f4, f, f3, f, f4};
        floatBufferArr[1] = ByteBuffer.allocateDirect(fArr2.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
        floatBufferArr[1].put(fArr2).position(0);
        float[] fArr3 = {f2, f4, f, f4, f2, f3, f, f3};
        floatBufferArr[2] = ByteBuffer.allocateDirect(fArr3.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
        floatBufferArr[2].put(fArr3).position(0);
        float[] fArr4 = {f, f4, f, f3, f2, f4, f2, f3};
        floatBufferArr[3] = ByteBuffer.allocateDirect(fArr4.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
        floatBufferArr[3].put(fArr4).position(0);
        setTextureVertices(floatBufferArr);
    }

    public void onTextureAcceptable(int i, GLRender fX_GLRender) {
        this.mTextureIn = i;
        setWidth(Math.round(((float) fX_GLRender.getWidth()) * this.mRegion.width()));
        setHeight(Math.round(((float) fX_GLRender.getHeight()) * this.mRegion.height()));
        onDrawFrame();
    }
}
