package com.scorpion.NeonphotoEditor.Videoneoneffect;

import android.graphics.Bitmap;
import android.opengl.GLES20;
import android.util.Log;
import cn.ezandroid.ezfilter.core.environment.Renderer;
import cn.ezandroid.ezfilter.core.util.L;
import java.util.ArrayList;
import java.util.List;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class RenderPipeline implements Renderer {
    private float mBackgroundAlpha = 1.0f;
    private float mBackgroundBlue;
    private float mBackgroundGreen;
    private float mBackgroundRed;
    private int mCurrentRotation;
    private final List<GLRender> mEndPointRenders = new ArrayList();
    private final List<FBORender> mFilterRenders = new ArrayList();
    private int mHeight;
    private volatile boolean mIsRendering;
    private final List<OnSurfaceListener> mOnSurfaceListeners = new ArrayList();
    private final List<BufferOutput> mOutputs = new ArrayList();
    private final List<OnFilterRendersChangedListener> mRendersChangedListeners = new ArrayList();
    private final List<GLRender> mRendersToDestroy = new ArrayList();
    private FBORender mStartPointRender;
    private int mWidth;

    public interface OnFilterRendersChangedListener {

             public final class CC {
            public static void $default$onFilterRendersChanged(OnFilterRendersChangedListener onFilterRendersChangedListener) {
            }
        }

        void onFilterRendersChanged();
    }

    public interface OnSurfaceListener {

             public final  class CC {
            public static void $default$onSurfaceChanged(OnSurfaceListener onSurfaceListener, GL10 gl10, int i, int i2) {
            }

            public static void $default$onSurfaceCreated(OnSurfaceListener onSurfaceListener, GL10 gl10, EGLConfig eGLConfig) {
            }

            public static void $default$onSurfaceDestroyed(OnSurfaceListener onSurfaceListener) {
            }
        }

        void onSurfaceChanged(GL10 gl10, int i, int i2);

        void onSurfaceCreated(GL10 gl10, EGLConfig eGLConfig);

        void onSurfaceDestroyed();
    }

    public void onSurfaceCreated(GL10 gl10, EGLConfig eGLConfig) {
        if (L.LOG_PIPELINE_CREATE) {
            Log.e("RenderPipeline", this + " onSurfaceCreated");
        }
        synchronized (mOnSurfaceListeners) {
            for (OnSurfaceListener onSurfaceCreated : mOnSurfaceListeners) {
                onSurfaceCreated.onSurfaceCreated(gl10, eGLConfig);
            }
        }
    }

    public void onSurfaceChanged(GL10 gl10, int i, int i2) {
        if (L.LOG_PIPELINE_CHANGE) {
            Log.e("RenderPipeline", this + " onSurfaceChanged:" + i + "x" + i2);
        }
        setRenderSize(i, i2);
        synchronized (mOnSurfaceListeners) {
            for (OnSurfaceListener onSurfaceChanged : mOnSurfaceListeners) {
                onSurfaceChanged.onSurfaceChanged(gl10, i, i2);
            }
        }
    }

    public void setBackgroundRed(float f) {
        mBackgroundRed = f;
    }

    public void setBackgroundGreen(float f) {
        mBackgroundGreen = f;
    }

    public void setBackgroundBlue(float f) {
        mBackgroundBlue = f;
    }

    public void setBackgroundAlpha(float f) {
        mBackgroundAlpha = f;
    }

    public void onDrawFrame(GL10 gl10) {
        if (L.LOG_PIPELINE_DRAW) {
            Log.e("RenderPipeline", this + " onDrawFrame:" + mWidth + "x" + mHeight + " " + isRendering());
        }
        if (gl10 != null) {
            GLES20.glClearColor(mBackgroundRed, mBackgroundGreen, mBackgroundBlue, mBackgroundAlpha);
            GLES20.glClear(16640);
        }
        if (isRendering()) {
            if (mStartPointRender != null) {
                mStartPointRender.onDrawFrame();
            }
            synchronized (mRendersToDestroy) {
                for (GLRender next : mRendersToDestroy) {
                    if (next != null) {
                        next.destroy();
                    }
                }
                mRendersToDestroy.clear();
            }
        }
    }

    public void onSurfaceDestroyed() {
        if (L.LOG_PIPELINE_DESTROY) {
            Log.e("RenderPipeline", this + " onSurfaceDestroyed " + Thread.currentThread().getName());
        }
        if (mStartPointRender != null) {
            mStartPointRender.clearTargets();
            mStartPointRender.destroy();
        }
        mStartPointRender = null;
        synchronized (mFilterRenders) {
            for (FBORender destroy : mFilterRenders) {
                destroy.destroy();
            }
            mFilterRenders.clear();
        }
        synchronized (mOutputs) {
            for (BufferOutput destroy2 : mOutputs) {
                destroy2.destroy();
            }
            mOutputs.clear();
        }
        synchronized (mEndPointRenders) {
            for (GLRender destroy3 : mEndPointRenders) {
                destroy3.destroy();
            }
            mEndPointRenders.clear();
        }
        synchronized (mOnSurfaceListeners) {
            for (OnSurfaceListener onSurfaceDestroyed : mOnSurfaceListeners) {
                onSurfaceDestroyed.onSurfaceDestroyed();
            }
        }
    }

    public synchronized void clean() {
        mCurrentRotation = 0;
        if (mStartPointRender != null) {
            mStartPointRender.clearTargets();
            addRenderToDestroy(mStartPointRender);
        }
        mStartPointRender = null;
        synchronized (mFilterRenders) {
            for (FBORender addRenderToDestroy : mFilterRenders) {
                addRenderToDestroy(addRenderToDestroy);
            }
            mFilterRenders.clear();
        }
        synchronized (mOutputs) {
            for (BufferOutput addRenderToDestroy2 : mOutputs) {
                addRenderToDestroy(addRenderToDestroy2);
            }
            mOutputs.clear();
        }
        synchronized (mEndPointRenders) {
            for (GLRender addRenderToDestroy3 : mEndPointRenders) {
                addRenderToDestroy(addRenderToDestroy3);
            }
            mEndPointRenders.clear();
        }
        synchronized (mRendersChangedListeners) {
            for (OnFilterRendersChangedListener onFilterRendersChanged : mRendersChangedListeners) {
                onFilterRendersChanged.onFilterRendersChanged();
            }
        }
    }

    public void addRenderToDestroy(GLRender GLRender1) {
        synchronized (mRendersToDestroy) {
            mRendersToDestroy.add(GLRender1);
        }
    }

    public void addOnFilterRendersChangedListener(OnFilterRendersChangedListener onFilterRendersChangedListener) {
        synchronized (mRendersChangedListeners) {
            if (!mRendersChangedListeners.contains(onFilterRendersChangedListener)) {
                mRendersChangedListeners.add(onFilterRendersChangedListener);
            }
        }
    }

    public void removeOnFilterRendersChangedListener(OnFilterRendersChangedListener onFilterRendersChangedListener) {
        synchronized (mRendersChangedListeners) {
            if (mRendersChangedListeners.contains(onFilterRendersChangedListener)) {
                mRendersChangedListeners.remove(onFilterRendersChangedListener);
            }
        }
    }

    public void clearOnFilterRendersChangedListener() {
        synchronized (mRendersChangedListeners) {
            mRendersChangedListeners.clear();
        }
    }

    public void addOnSurfaceListener(OnSurfaceListener onSurfaceListener) {
        synchronized (mOnSurfaceListeners) {
            if (!mOnSurfaceListeners.contains(onSurfaceListener)) {
                mOnSurfaceListeners.add(onSurfaceListener);
            }
        }
    }

    public void removeOnSurfaceListener(OnSurfaceListener onSurfaceListener) {
        synchronized (mOnSurfaceListeners) {
            if (mOnSurfaceListeners.contains(onSurfaceListener)) {
                mOnSurfaceListeners.remove(onSurfaceListener);
            }
        }
    }

    public void clearOnSurfaceListener() {
        synchronized (mOnSurfaceListeners) {
            mOnSurfaceListeners.clear();
        }
    }

    public int getHeight() {
        return mHeight;
    }

    public int getWidth() {
        return mWidth;
    }

    public void setRotate90Degrees(int i) {
        mCurrentRotation = i;
        synchronized (mEndPointRenders) {
            for (GLRender next : mEndPointRenders) {
                next.resetRotate();
                next.setRotate90Degrees(i);
            }
        }
        synchronized (mOutputs) {
            for (BufferOutput rotate90Degrees : mOutputs) {
                rotate90Degrees.setRotate90Degrees(i);
            }
        }
    }

    public void setRenderSize(int i, int i2) {
        mWidth = i;
        mHeight = i2;
        updateEndPointRender();
    }

    private void updateEndPointRender() {
        synchronized (mEndPointRenders) {
            for (GLRender renderSize : mEndPointRenders) {
                renderSize.setRenderSize(mWidth, mHeight);
            }
        }
    }

    public boolean isRendering() {
        return mIsRendering;
    }

    public void setRendering(boolean z) {
        mIsRendering = z;
    }

    public void startRender() {
        mIsRendering = true;
    }

    public void pauseRender() {
        mIsRendering = false;
    }

    public FBORender getStartPointRender() {
        return mStartPointRender;
    }

    public List<FBORender> getFilterRenders() {
        return mFilterRenders;
    }

    public List<GLRender> getEndPointRenders() {
        return mEndPointRenders;
    }

    public synchronized void setStartPointRender(FBORender FBORender1) {
        if (FBORender1 != null) {
            if (mStartPointRender != FBORender1) {
                if (mStartPointRender != null) {
                    synchronized (mStartPointRender.getTargets()) {
                        for (OnTextureAcceptableListener addTarget : mStartPointRender.getTargets()) {
                            FBORender1.addTarget(addTarget);
                        }
                    }
                    mStartPointRender.clearTargets();
                    addRenderToDestroy(mStartPointRender);
                    mStartPointRender = FBORender1;
                } else {
                    mStartPointRender = FBORender1;
                    synchronized (mEndPointRenders) {
                        for (GLRender addTarget2 : mEndPointRenders) {
                            mStartPointRender.addTarget(addTarget2);
                        }
                    }
                }
            }
        }
    }

    public synchronized void addEndPointRender(GLRender GLRender1) {
        synchronized (mEndPointRenders) {
            if (GLRender1 != null) {
                try {
                    if (!mEndPointRenders.contains(GLRender1) && mStartPointRender != null) {
                        GLRender1.resetRotate();
                        GLRender1.setRotate90Degrees(mCurrentRotation);
                        if (mFilterRenders.isEmpty()) {
                            mStartPointRender.addTarget(GLRender1);
                        } else {
                            mFilterRenders.get(mFilterRenders.size() - 1).addTarget(GLRender1);
                        }
                        mEndPointRenders.add(GLRender1);
                        updateEndPointRender();
                    }
                } catch (Throwable th) {
                    while (true) {
                    }

                }
            }
        }
        return;
    }

    public synchronized void removeEndPointRender(GLRender GLRender1) {
        synchronized (mEndPointRenders) {
            if (GLRender1 != null) {
                try {
                    if (mEndPointRenders.contains(GLRender1) && mStartPointRender != null) {
                        mEndPointRenders.remove(GLRender1);
                        if (mFilterRenders.isEmpty()) {
                            mStartPointRender.removeTarget(GLRender1);
                        } else {
                            mFilterRenders.get(mFilterRenders.size() - 1).removeTarget(GLRender1);
                        }
                        addRenderToDestroy(GLRender1);
                    }
                } catch (Throwable th) {
                    while (true) {
                    }

                }
            }
        }
        return;
    }

    public synchronized void clearEndPointRenders() {
        synchronized (mEndPointRenders) {
            if (mStartPointRender != null && !mEndPointRenders.isEmpty()) {
                if (mFilterRenders.isEmpty()) {
                    for (GLRender removeTarget : mEndPointRenders) {
                        mStartPointRender.removeTarget(removeTarget);
                    }
                } else {
                    FBORender fX_FBORender = mFilterRenders.get(mFilterRenders.size() - 1);
                    for (GLRender removeTarget2 : mEndPointRenders) {
                        fX_FBORender.removeTarget(removeTarget2);
                    }
                }
                for (GLRender addRenderToDestroy : mEndPointRenders) {
                    addRenderToDestroy(addRenderToDestroy);
                }
                mEndPointRenders.clear();
            }
        }
    }

    public synchronized void addOutput(FBORender fX_FBORender, BufferOutput fX_BufferOutput) {
        synchronized (mOutputs) {
            if (fX_BufferOutput != null) {
                try {
                    if (!mOutputs.contains(fX_BufferOutput) && mStartPointRender != null) {
                        fX_BufferOutput.clearTargets();
                        fX_BufferOutput.resetRotate();
                        fX_BufferOutput.setRotate90Degrees(mCurrentRotation);
                        fX_FBORender.addTarget(fX_BufferOutput);
                        mOutputs.add(fX_BufferOutput);
                    }
                } catch (Throwable th) {
                    while (true) {
                    }

                }
            }
        }
        return;
    }

    public synchronized void removeOutput(FBORender fX_FBORender, BufferOutput fX_BufferOutput) {
        synchronized (mOutputs) {
            if (fX_FBORender != null) {
                try {
                    if (mOutputs.contains(fX_BufferOutput) && mStartPointRender != null) {
                        mOutputs.remove(fX_BufferOutput);
                        fX_FBORender.removeTarget(fX_BufferOutput);
                        addRenderToDestroy(fX_BufferOutput);
                    }
                } catch (Throwable th) {
                    while (true) {
                    }

                }
            }
        }
        return;
    }

    public synchronized void addFilterRender(int i, FBORender fX_FBORender) {
        synchronized (mFilterRenders) {
            if (fX_FBORender != null) {
                try {
                    if (!mFilterRenders.contains(fX_FBORender) && mStartPointRender != null) {
                        fX_FBORender.clearTargets();
                        if (mFilterRenders.isEmpty()) {
                            mStartPointRender.addTarget(fX_FBORender);
                            synchronized (mEndPointRenders) {
                                for (GLRender next : mEndPointRenders) {
                                    mStartPointRender.removeTarget(next);
                                    fX_FBORender.addTarget(next);
                                }
                            }
                        } else if (i == 0) {
                            FBORender fX_FBORender2 = mFilterRenders.get(0);
                            mStartPointRender.removeTarget(fX_FBORender2);
                            fX_FBORender.addTarget(fX_FBORender2);
                            mStartPointRender.addTarget(fX_FBORender);
                        } else if (i > mFilterRenders.size() - 1) {
                            FBORender fX_FBORender3 = mFilterRenders.get(mFilterRenders.size() - 1);
                            fX_FBORender3.addTarget(fX_FBORender);
                            synchronized (mEndPointRenders) {
                                for (GLRender next2 : mEndPointRenders) {
                                    fX_FBORender3.removeTarget(next2);
                                    fX_FBORender.addTarget(next2);
                                }
                            }
                        } else {
                            FBORender fX_FBORender4 = mFilterRenders.get(i - 1);
                            FBORender fX_FBORender5 = mFilterRenders.get(i);
                            fX_FBORender4.removeTarget(fX_FBORender5);
                            fX_FBORender4.addTarget(fX_FBORender);
                            fX_FBORender.addTarget(fX_FBORender5);
                        }
                        if (i > mFilterRenders.size() - 1) {
                            mFilterRenders.add(fX_FBORender);
                        } else {
                            mFilterRenders.add(i, fX_FBORender);
                        }
                        synchronized (mRendersChangedListeners) {
                            for (OnFilterRendersChangedListener onFilterRendersChanged : mRendersChangedListeners) {
                                onFilterRendersChanged.onFilterRendersChanged();
                            }
                        }
                    }
                } catch (Throwable th) {
                    while (true) {
                    }

                }
            }
        }
        return;
    }

    public synchronized void addFilterRender(FBORender fX_FBORender) {
        addFilterRender(mFilterRenders.size(), fX_FBORender);
    }

    public synchronized void removeFilterRender(FBORender fX_FBORender) {
        synchronized (mFilterRenders) {
            if (fX_FBORender != null) {
                try {
                    if (mFilterRenders.contains(fX_FBORender) && mStartPointRender != null) {
                        int indexOf = mFilterRenders.indexOf(fX_FBORender);
                        mFilterRenders.remove(fX_FBORender);
                        if (mFilterRenders.isEmpty()) {
                            mStartPointRender.removeTarget(fX_FBORender);
                            synchronized (mEndPointRenders) {
                                for (GLRender next : mEndPointRenders) {
                                    fX_FBORender.removeTarget(next);
                                    mStartPointRender.addTarget(next);
                                }
                            }
                        } else if (indexOf == 0) {
                            FBORender fX_FBORender2 = mFilterRenders.get(0);
                            mStartPointRender.removeTarget(fX_FBORender);
                            fX_FBORender.removeTarget(fX_FBORender2);
                            mStartPointRender.addTarget(fX_FBORender2);
                        } else if (indexOf > mFilterRenders.size() - 1) {
                            FBORender fX_FBORender3 = mFilterRenders.get(mFilterRenders.size() - 1);
                            fX_FBORender3.removeTarget(fX_FBORender);
                            synchronized (mEndPointRenders) {
                                for (GLRender next2 : mEndPointRenders) {
                                    fX_FBORender.removeTarget(next2);
                                    fX_FBORender3.addTarget(next2);
                                }
                            }
                        } else {
                            FBORender fX_FBORender4 = mFilterRenders.get(indexOf - 1);
                            FBORender fX_FBORender5 = mFilterRenders.get(indexOf);
                            fX_FBORender4.removeTarget(fX_FBORender);
                            fX_FBORender.removeTarget(fX_FBORender5);
                            fX_FBORender4.addTarget(fX_FBORender5);
                        }
                        addRenderToDestroy(fX_FBORender);
                        synchronized (mRendersChangedListeners) {
                            for (OnFilterRendersChangedListener onFilterRendersChanged : mRendersChangedListeners) {
                                onFilterRendersChanged.onFilterRendersChanged();
                            }
                        }
                    }
                } catch (Throwable th) {
                    while (true) {
                    }

                }
            }
        }
        return;
    }

    public synchronized void clearFilterRenders() {
        synchronized (mFilterRenders) {
            if (mStartPointRender != null && !mFilterRenders.isEmpty()) {
                if (mFilterRenders.size() == 1) {
                    FBORender fX_FBORender = mFilterRenders.get(0);
                    mStartPointRender.removeTarget(fX_FBORender);
                    synchronized (mEndPointRenders) {
                        for (GLRender next : mEndPointRenders) {
                            fX_FBORender.removeTarget(next);
                            mStartPointRender.addTarget(next);
                        }
                    }
                } else {
                    FBORender fX_FBORender2 = mFilterRenders.get(mFilterRenders.size() - 1);
                    mStartPointRender.removeTarget(mFilterRenders.get(0));
                    synchronized (mEndPointRenders) {
                        for (GLRender next2 : mEndPointRenders) {
                            fX_FBORender2.removeTarget(next2);
                            mStartPointRender.addTarget(next2);
                        }
                    }
                }
                for (FBORender addRenderToDestroy : mFilterRenders) {
                    addRenderToDestroy(addRenderToDestroy);
                }
                mFilterRenders.clear();
                synchronized (mRendersChangedListeners) {
                    for (OnFilterRendersChangedListener onFilterRendersChanged : mRendersChangedListeners) {
                        onFilterRendersChanged.onFilterRendersChanged();
                    }
                }
            }
        }
    }

//    public void output(FX_BitmapOutput.BitmapOutputCallback bitmapOutputCallback) {
//        output(bitmapOutputCallback, true);
//    }
//
//    public void output(FX_BitmapOutput.BitmapOutputCallback bitmapOutputCallback, boolean z) {
//        output(bitmapOutputCallback, mWidth, mHeight, z);
//    }
//
//    public void output(FX_BitmapOutput.BitmapOutputCallback bitmapOutputCallback, int i, int i2, boolean z) {
//        FX_FBORender fX_FBORender;
//        if (!z || mFilterRenders.isEmpty()) {
//            fX_FBORender = mStartPointRender;
//        } else {
//            fX_FBORender = mFilterRenders.get(mFilterRenders.size() - 1);
//        }
//        FX_BitmapOutput fX_BitmapOutput = new FX_BitmapOutput();
//        fX_BitmapOutput.setRenderSize(i, i2);
//        fX_BitmapOutput.setBitmapOutputCallback(new FX_BitmapOutput.BitmapOutputCallback(bitmapOutputCallback, fX_FBORender, fX_BitmapOutput) {
//            private final /* synthetic */ FX_BitmapOutput.BitmapOutputCallback f$1;
//            private final /* synthetic */ FX_FBORender f$2;
//            private final /* synthetic */ FX_BitmapOutput f$3;
//
//            {
//                f$1 = r2;
//                f$2 = r3;
//                f$3 = r4;
//            }
//
//            public final void bitmapOutput(Bitmap bitmap) {
//                FX_RenderPipeline.lambda$output$0(FX_RenderPipeline.this, f$1, f$2, f$3, bitmap);
//            }
//        });
//        addOutput(fX_FBORender, fX_BitmapOutput);
//    }

    public static /* synthetic */ void lambda$output$0(RenderPipeline fX_RenderPipeline, BitmapOutput.BitmapOutputCallback bitmapOutputCallback, FBORender fX_FBORender, BitmapOutput fX_BitmapOutput, Bitmap bitmap) {
        if (bitmapOutputCallback != null) {
            bitmapOutputCallback.bitmapOutput(bitmap);
        }
        fX_RenderPipeline.removeOutput(fX_FBORender, fX_BitmapOutput);
    }
}
