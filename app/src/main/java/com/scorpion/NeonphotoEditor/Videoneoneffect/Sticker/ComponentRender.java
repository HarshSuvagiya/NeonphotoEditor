package com.scorpion.NeonphotoEditor.Videoneoneffect.Sticker;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PointF;
import android.opengl.GLES20;

import com.scorpion.NeonphotoEditor.Util.VideoEffectTimeBar;

import cn.ezandroid.ezfilter.core.cache.IBitmapCache;
import cn.ezandroid.ezfilter.core.cache.LruBitmapCache;
import cn.ezandroid.ezfilter.core.util.BitmapUtil;
import cn.ezandroid.ezfilter.extra.sticker.model.Component;
import cn.ezandroid.ezfilter.extra.sticker.model.ScreenAnchor;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class ComponentRender {
    VideoEffectTimeBar effectTimeBar;
    boolean isTouch = false;
    private IBitmapCache mBitmapCache;
    private Component mComponent;
    private Context mContext;
    boolean mIsPause = true;
    private int mLastIndex = -1;
    private FloatBuffer mRenderVertices;
    private ScreenAnchor mScreenAnchor;
    private long mStartTime = -1;
    private int mTexture;

    public ComponentRender(Context context, Component component) {
        mContext = context;
        mComponent = component;
        mRenderVertices = ByteBuffer.allocateDirect(32).order(ByteOrder.nativeOrder()).asFloatBuffer();
    }

    public void setBitmapCache(IBitmapCache iBitmapCache) {
        mBitmapCache = iBitmapCache;
    }

    public void setScreenAnchor(ScreenAnchor screenAnchor) {
        mScreenAnchor = screenAnchor;
    }

    public void updateRenderVertices(int i, int i2) {
        double d;
        PointF leftAnchorPoint = mScreenAnchor.getLeftAnchorPoint();
        PointF rightAnchorPoint = mScreenAnchor.getRightAnchorPoint();
        PointF leftAnchorPoint2 = mComponent.textureAnchor.getLeftAnchorPoint();
        PointF rightAnchorPoint2 = mComponent.textureAnchor.getRightAnchorPoint();
        PointF pointF = new PointF(leftAnchorPoint.x, leftAnchorPoint.y);
        PointF pointF2 = new PointF(rightAnchorPoint.x, rightAnchorPoint.y);
        float distanceOf = distanceOf(pointF, pointF2) / distanceOf(leftAnchorPoint2, rightAnchorPoint2);
        leftAnchorPoint2.x *= distanceOf;
        leftAnchorPoint2.y *= distanceOf;
        rightAnchorPoint2.x *= distanceOf;
        rightAnchorPoint2.y *= distanceOf;
        float f = ((float) mComponent.width) * distanceOf;
        float f2 = ((float) mComponent.height) * distanceOf;
        PointF pointF3 = new PointF(pointF.x - leftAnchorPoint2.x, pointF.y + leftAnchorPoint2.y);
        PointF pointF4 = new PointF(pointF3.x, pointF3.y - f2);
        PointF pointF5 = new PointF(pointF3.x + f, pointF3.y);
        PointF pointF6 = new PointF(pointF5.x, pointF4.y);
        if (mScreenAnchor.roll == 2.14748365E9f) {
            PointF pointF7 = new PointF(pointF3.x + rightAnchorPoint2.x, pointF3.y - rightAnchorPoint2.y);
            float distanceOf2 = distanceOf(pointF, pointF7);
            float distanceOf3 = distanceOf(pointF, pointF2);
            float distanceOf4 = distanceOf(pointF7, pointF2);
            d = Math.acos((double) ((((distanceOf2 * distanceOf2) + (distanceOf3 * distanceOf3)) - (distanceOf4 * distanceOf4)) / ((distanceOf2 * 2.0f) * distanceOf3)));
            if (pointF2.x < pointF7.x && pointF2.y < (pointF.y * 2.0f) - pointF7.y) {
                d = -d;
            }
        } else {
            double d2 = (double) mScreenAnchor.roll;
            Double.isNaN(d2);
            d = ((180.0d - d2) / 180.0d) * 3.14d;
        }
        PointF rotateVertices = getRotateVertices(pointF3, pointF, d);
        PointF rotateVertices2 = getRotateVertices(pointF4, pointF, d);
        PointF rotateVertices3 = getRotateVertices(pointF5, pointF, d);
        PointF rotateVertices4 = getRotateVertices(pointF6, pointF, d);
        float f3 = (float) i;
        float f4 = (float) i2;
        PointF transVerticesToOpenGL = transVerticesToOpenGL(rotateVertices, f3, f4);
        PointF transVerticesToOpenGL2 = transVerticesToOpenGL(rotateVertices2, f3, f4);
        PointF transVerticesToOpenGL3 = transVerticesToOpenGL(rotateVertices3, f3, f4);
        PointF transVerticesToOpenGL4 = transVerticesToOpenGL(rotateVertices4, f3, f4);
        float[] fArr = {transVerticesToOpenGL4.x, transVerticesToOpenGL4.y, transVerticesToOpenGL2.x, transVerticesToOpenGL2.y, transVerticesToOpenGL3.x, transVerticesToOpenGL3.y, transVerticesToOpenGL.x, transVerticesToOpenGL.y};
        mRenderVertices.clear();
        mRenderVertices.put(fArr);
    }

    public void setmIsPause(boolean z) {
        mIsPause = z;
    }

    public void setTouch(boolean z) {
        isTouch = z;
    }

    public void setEffectTimeBar(VideoEffectTimeBar VideoEffectTimeBar1) {
        effectTimeBar = VideoEffectTimeBar1;
    }

    public void onDraw(int i, int i2, int i3, FloatBuffer floatBuffer) {
        mRenderVertices.position(0);
        floatBuffer.position(0);
        if (mStartTime == -1) {
            mStartTime = System.currentTimeMillis();
        }
        int round = Math.round(((((float) (mComponent.length - 1)) * 1.0f) / ((float) mComponent.duration)) * ((float) ((System.currentTimeMillis() - mStartTime) % ((long) mComponent.duration))));
        if (mBitmapCache == null) {
            mBitmapCache = new LruBitmapCache((int) (Runtime.getRuntime().maxMemory() / 4));
        }
        String str = mComponent.resources.get(round);
        Bitmap bitmap = mBitmapCache.get(str);
        if (bitmap == null || bitmap.isRecycled()) {
            bitmap = BitmapUtil.loadBitmap(mContext, str);
            if (bitmap != null && !bitmap.isRecycled()) {
                mBitmapCache.put(str, bitmap);
            } else {
                return;
            }
        }
        if (!effectTimeBar.isEffect() && !isTouch) {
            bitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        }
        if (mLastIndex != round) {
            if (mTexture != 0) {
                GLES20.glDeleteTextures(1, new int[]{mTexture}, 0);
                mTexture = 0;
            }
            mTexture = BitmapUtil.bindBitmap(bitmap);
        }
        GLES20.glActiveTexture(33986);
        GLES20.glBindTexture(3553, mTexture);
        GLES20.glUniform1i(i, 2);
        GLES20.glVertexAttribPointer(i2, 2, 5126, false, 0, mRenderVertices);
        GLES20.glVertexAttribPointer(i3, 2, 5126, false, 0, floatBuffer);
        GLES20.glDrawArrays(5, 0, 4);
        GLES20.glBindTexture(3553, 0);
        mLastIndex = round;
    }

    public void destroy() {
        if (mTexture != 0) {
            GLES20.glDeleteTextures(1, new int[]{mTexture}, 0);
            mTexture = 0;
        }
    }

    private PointF getRotateVertices(PointF pointF, PointF pointF2, double d) {
        double d2 = (double) (pointF.x - pointF2.x);
        double cos = Math.cos(d);
        Double.isNaN(d2);
        double d3 = d2 * cos;
        double d4 = (double) (pointF.y - pointF2.y);
        double sin = Math.sin(d);
        Double.isNaN(d4);
        double d5 = d3 - (d4 * sin);
        double d6 = (double) pointF2.x;
        Double.isNaN(d6);
        double d7 = (double) (pointF.x - pointF2.x);
        double sin2 = Math.sin(d);
        Double.isNaN(d7);
        double d8 = d7 * sin2;
        double d9 = (double) (pointF.y - pointF2.y);
        double cos2 = Math.cos(d);
        Double.isNaN(d9);
        double d10 = (double) pointF2.y;
        Double.isNaN(d10);
        return new PointF((float) (d5 + d6), (float) (d8 + (d9 * cos2) + d10));
    }

    private PointF transVerticesToOpenGL(PointF pointF, float f, float f2) {
        float f3 = f / 2.0f;
        float f4 = f2 / 2.0f;
        return new PointF((pointF.x - f3) / f3, (pointF.y - f4) / f4);
    }

    private float distanceOf(PointF pointF, PointF pointF2) {
        return (float) Math.sqrt((double) (((pointF.x - pointF2.x) * (pointF.x - pointF2.x)) + ((pointF.y - pointF2.y) * (pointF.y - pointF2.y))));
    }
}
