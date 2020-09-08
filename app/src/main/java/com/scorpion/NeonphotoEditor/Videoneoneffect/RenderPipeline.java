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

        /* renamed from: fx.neonsketch.videoeffect.ezutil.FX_RenderPipeline$OnFilterRendersChangedListener$-CC  reason: invalid class name */
        public final /* synthetic */ class CC {
            public static void $default$onFilterRendersChanged(OnFilterRendersChangedListener onFilterRendersChangedListener) {
            }
        }

        void onFilterRendersChanged();
    }

    public interface OnSurfaceListener {

        /* renamed from: fx.neonsketch.videoeffect.ezutil.FX_RenderPipeline$OnSurfaceListener$-CC  reason: invalid class name */
        public final /* synthetic */ class CC {
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
            Log.e("FX_RenderPipeline", this + " onSurfaceCreated");
        }
        synchronized (this.mOnSurfaceListeners) {
            for (OnSurfaceListener onSurfaceCreated : this.mOnSurfaceListeners) {
                onSurfaceCreated.onSurfaceCreated(gl10, eGLConfig);
            }
        }
    }

    public void onSurfaceChanged(GL10 gl10, int i, int i2) {
        if (L.LOG_PIPELINE_CHANGE) {
            Log.e("FX_RenderPipeline", this + " onSurfaceChanged:" + i + "x" + i2);
        }
        setRenderSize(i, i2);
        synchronized (this.mOnSurfaceListeners) {
            for (OnSurfaceListener onSurfaceChanged : this.mOnSurfaceListeners) {
                onSurfaceChanged.onSurfaceChanged(gl10, i, i2);
            }
        }
    }

    public void setBackgroundRed(float f) {
        this.mBackgroundRed = f;
    }

    public void setBackgroundGreen(float f) {
        this.mBackgroundGreen = f;
    }

    public void setBackgroundBlue(float f) {
        this.mBackgroundBlue = f;
    }

    public void setBackgroundAlpha(float f) {
        this.mBackgroundAlpha = f;
    }

    public void onDrawFrame(GL10 gl10) {
        if (L.LOG_PIPELINE_DRAW) {
            Log.e("FX_RenderPipeline", this + " onDrawFrame:" + this.mWidth + "x" + this.mHeight + " " + isRendering());
        }
        if (gl10 != null) {
            GLES20.glClearColor(this.mBackgroundRed, this.mBackgroundGreen, this.mBackgroundBlue, this.mBackgroundAlpha);
            GLES20.glClear(16640);
        }
        if (isRendering()) {
            if (this.mStartPointRender != null) {
                this.mStartPointRender.onDrawFrame();
            }
            synchronized (this.mRendersToDestroy) {
                for (GLRender next : this.mRendersToDestroy) {
                    if (next != null) {
                        next.destroy();
                    }
                }
                this.mRendersToDestroy.clear();
            }
        }
    }

    public void onSurfaceDestroyed() {
        if (L.LOG_PIPELINE_DESTROY) {
            Log.e("FX_RenderPipeline", this + " onSurfaceDestroyed " + Thread.currentThread().getName());
        }
        if (this.mStartPointRender != null) {
            this.mStartPointRender.clearTargets();
            this.mStartPointRender.destroy();
        }
        this.mStartPointRender = null;
        synchronized (this.mFilterRenders) {
            for (FBORender destroy : this.mFilterRenders) {
                destroy.destroy();
            }
            this.mFilterRenders.clear();
        }
        synchronized (this.mOutputs) {
            for (BufferOutput destroy2 : this.mOutputs) {
                destroy2.destroy();
            }
            this.mOutputs.clear();
        }
        synchronized (this.mEndPointRenders) {
            for (GLRender destroy3 : this.mEndPointRenders) {
                destroy3.destroy();
            }
            this.mEndPointRenders.clear();
        }
        synchronized (this.mOnSurfaceListeners) {
            for (OnSurfaceListener onSurfaceDestroyed : this.mOnSurfaceListeners) {
                onSurfaceDestroyed.onSurfaceDestroyed();
            }
        }
    }

    public synchronized void clean() {
        this.mCurrentRotation = 0;
        if (this.mStartPointRender != null) {
            this.mStartPointRender.clearTargets();
            addRenderToDestroy(this.mStartPointRender);
        }
        this.mStartPointRender = null;
        synchronized (this.mFilterRenders) {
            for (FBORender addRenderToDestroy : this.mFilterRenders) {
                addRenderToDestroy(addRenderToDestroy);
            }
            this.mFilterRenders.clear();
        }
        synchronized (this.mOutputs) {
            for (BufferOutput addRenderToDestroy2 : this.mOutputs) {
                addRenderToDestroy(addRenderToDestroy2);
            }
            this.mOutputs.clear();
        }
        synchronized (this.mEndPointRenders) {
            for (GLRender addRenderToDestroy3 : this.mEndPointRenders) {
                addRenderToDestroy(addRenderToDestroy3);
            }
            this.mEndPointRenders.clear();
        }
        synchronized (this.mRendersChangedListeners) {
            for (OnFilterRendersChangedListener onFilterRendersChanged : this.mRendersChangedListeners) {
                onFilterRendersChanged.onFilterRendersChanged();
            }
        }
    }

    public void addRenderToDestroy(GLRender fX_GLRender) {
        synchronized (this.mRendersToDestroy) {
            this.mRendersToDestroy.add(fX_GLRender);
        }
    }

    public void addOnFilterRendersChangedListener(OnFilterRendersChangedListener onFilterRendersChangedListener) {
        synchronized (this.mRendersChangedListeners) {
            if (!this.mRendersChangedListeners.contains(onFilterRendersChangedListener)) {
                this.mRendersChangedListeners.add(onFilterRendersChangedListener);
            }
        }
    }

    public void removeOnFilterRendersChangedListener(OnFilterRendersChangedListener onFilterRendersChangedListener) {
        synchronized (this.mRendersChangedListeners) {
            if (this.mRendersChangedListeners.contains(onFilterRendersChangedListener)) {
                this.mRendersChangedListeners.remove(onFilterRendersChangedListener);
            }
        }
    }

    public void clearOnFilterRendersChangedListener() {
        synchronized (this.mRendersChangedListeners) {
            this.mRendersChangedListeners.clear();
        }
    }

    public void addOnSurfaceListener(OnSurfaceListener onSurfaceListener) {
        synchronized (this.mOnSurfaceListeners) {
            if (!this.mOnSurfaceListeners.contains(onSurfaceListener)) {
                this.mOnSurfaceListeners.add(onSurfaceListener);
            }
        }
    }

    public void removeOnSurfaceListener(OnSurfaceListener onSurfaceListener) {
        synchronized (this.mOnSurfaceListeners) {
            if (this.mOnSurfaceListeners.contains(onSurfaceListener)) {
                this.mOnSurfaceListeners.remove(onSurfaceListener);
            }
        }
    }

    public void clearOnSurfaceListener() {
        synchronized (this.mOnSurfaceListeners) {
            this.mOnSurfaceListeners.clear();
        }
    }

    public int getHeight() {
        return this.mHeight;
    }

    public int getWidth() {
        return this.mWidth;
    }

    public void setRotate90Degrees(int i) {
        this.mCurrentRotation = i;
        synchronized (this.mEndPointRenders) {
            for (GLRender next : this.mEndPointRenders) {
                next.resetRotate();
                next.setRotate90Degrees(i);
            }
        }
        synchronized (this.mOutputs) {
            for (BufferOutput rotate90Degrees : this.mOutputs) {
                rotate90Degrees.setRotate90Degrees(i);
            }
        }
    }

    public void setRenderSize(int i, int i2) {
        this.mWidth = i;
        this.mHeight = i2;
        updateEndPointRender();
    }

    private void updateEndPointRender() {
        synchronized (this.mEndPointRenders) {
            for (GLRender renderSize : this.mEndPointRenders) {
                renderSize.setRenderSize(this.mWidth, this.mHeight);
            }
        }
    }

    public boolean isRendering() {
        return this.mIsRendering;
    }

    public void setRendering(boolean z) {
        this.mIsRendering = z;
    }

    public void startRender() {
        this.mIsRendering = true;
    }

    public void pauseRender() {
        this.mIsRendering = false;
    }

    public FBORender getStartPointRender() {
        return this.mStartPointRender;
    }

    public List<FBORender> getFilterRenders() {
        return this.mFilterRenders;
    }

    public List<GLRender> getEndPointRenders() {
        return this.mEndPointRenders;
    }

    public synchronized void setStartPointRender(FBORender fX_FBORender) {
        if (fX_FBORender != null) {
            if (this.mStartPointRender != fX_FBORender) {
                if (this.mStartPointRender != null) {
                    synchronized (this.mStartPointRender.getTargets()) {
                        for (OnTextureAcceptableListener addTarget : this.mStartPointRender.getTargets()) {
                            fX_FBORender.addTarget(addTarget);
                        }
                    }
                    this.mStartPointRender.clearTargets();
                    addRenderToDestroy(this.mStartPointRender);
                    this.mStartPointRender = fX_FBORender;
                } else {
                    this.mStartPointRender = fX_FBORender;
                    synchronized (this.mEndPointRenders) {
                        for (GLRender addTarget2 : this.mEndPointRenders) {
                            this.mStartPointRender.addTarget(addTarget2);
                        }
                    }
                }
            }
        }
    }

    public synchronized void addEndPointRender(GLRender fX_GLRender) {
        synchronized (this.mEndPointRenders) {
            if (fX_GLRender != null) {
                try {
                    if (!this.mEndPointRenders.contains(fX_GLRender) && this.mStartPointRender != null) {
                        fX_GLRender.resetRotate();
                        fX_GLRender.setRotate90Degrees(this.mCurrentRotation);
                        if (this.mFilterRenders.isEmpty()) {
                            this.mStartPointRender.addTarget(fX_GLRender);
                        } else {
                            this.mFilterRenders.get(this.mFilterRenders.size() - 1).addTarget(fX_GLRender);
                        }
                        this.mEndPointRenders.add(fX_GLRender);
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

    public synchronized void removeEndPointRender(GLRender fX_GLRender) {
        synchronized (this.mEndPointRenders) {
            if (fX_GLRender != null) {
                try {
                    if (this.mEndPointRenders.contains(fX_GLRender) && this.mStartPointRender != null) {
                        this.mEndPointRenders.remove(fX_GLRender);
                        if (this.mFilterRenders.isEmpty()) {
                            this.mStartPointRender.removeTarget(fX_GLRender);
                        } else {
                            this.mFilterRenders.get(this.mFilterRenders.size() - 1).removeTarget(fX_GLRender);
                        }
                        addRenderToDestroy(fX_GLRender);
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
        synchronized (this.mEndPointRenders) {
            if (this.mStartPointRender != null && !this.mEndPointRenders.isEmpty()) {
                if (this.mFilterRenders.isEmpty()) {
                    for (GLRender removeTarget : this.mEndPointRenders) {
                        this.mStartPointRender.removeTarget(removeTarget);
                    }
                } else {
                    FBORender fX_FBORender = this.mFilterRenders.get(this.mFilterRenders.size() - 1);
                    for (GLRender removeTarget2 : this.mEndPointRenders) {
                        fX_FBORender.removeTarget(removeTarget2);
                    }
                }
                for (GLRender addRenderToDestroy : this.mEndPointRenders) {
                    addRenderToDestroy(addRenderToDestroy);
                }
                this.mEndPointRenders.clear();
            }
        }
    }

    public synchronized void addOutput(FBORender fX_FBORender, BufferOutput fX_BufferOutput) {
        synchronized (this.mOutputs) {
            if (fX_BufferOutput != null) {
                try {
                    if (!this.mOutputs.contains(fX_BufferOutput) && this.mStartPointRender != null) {
                        fX_BufferOutput.clearTargets();
                        fX_BufferOutput.resetRotate();
                        fX_BufferOutput.setRotate90Degrees(this.mCurrentRotation);
                        fX_FBORender.addTarget(fX_BufferOutput);
                        this.mOutputs.add(fX_BufferOutput);
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
        synchronized (this.mOutputs) {
            if (fX_FBORender != null) {
                try {
                    if (this.mOutputs.contains(fX_BufferOutput) && this.mStartPointRender != null) {
                        this.mOutputs.remove(fX_BufferOutput);
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
        synchronized (this.mFilterRenders) {
            if (fX_FBORender != null) {
                try {
                    if (!this.mFilterRenders.contains(fX_FBORender) && this.mStartPointRender != null) {
                        fX_FBORender.clearTargets();
                        if (this.mFilterRenders.isEmpty()) {
                            this.mStartPointRender.addTarget(fX_FBORender);
                            synchronized (this.mEndPointRenders) {
                                for (GLRender next : this.mEndPointRenders) {
                                    this.mStartPointRender.removeTarget(next);
                                    fX_FBORender.addTarget(next);
                                }
                            }
                        } else if (i == 0) {
                            FBORender fX_FBORender2 = this.mFilterRenders.get(0);
                            this.mStartPointRender.removeTarget(fX_FBORender2);
                            fX_FBORender.addTarget(fX_FBORender2);
                            this.mStartPointRender.addTarget(fX_FBORender);
                        } else if (i > this.mFilterRenders.size() - 1) {
                            FBORender fX_FBORender3 = this.mFilterRenders.get(this.mFilterRenders.size() - 1);
                            fX_FBORender3.addTarget(fX_FBORender);
                            synchronized (this.mEndPointRenders) {
                                for (GLRender next2 : this.mEndPointRenders) {
                                    fX_FBORender3.removeTarget(next2);
                                    fX_FBORender.addTarget(next2);
                                }
                            }
                        } else {
                            FBORender fX_FBORender4 = this.mFilterRenders.get(i - 1);
                            FBORender fX_FBORender5 = this.mFilterRenders.get(i);
                            fX_FBORender4.removeTarget(fX_FBORender5);
                            fX_FBORender4.addTarget(fX_FBORender);
                            fX_FBORender.addTarget(fX_FBORender5);
                        }
                        if (i > this.mFilterRenders.size() - 1) {
                            this.mFilterRenders.add(fX_FBORender);
                        } else {
                            this.mFilterRenders.add(i, fX_FBORender);
                        }
                        synchronized (this.mRendersChangedListeners) {
                            for (OnFilterRendersChangedListener onFilterRendersChanged : this.mRendersChangedListeners) {
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
        addFilterRender(this.mFilterRenders.size(), fX_FBORender);
    }

    public synchronized void removeFilterRender(FBORender fX_FBORender) {
        synchronized (this.mFilterRenders) {
            if (fX_FBORender != null) {
                try {
                    if (this.mFilterRenders.contains(fX_FBORender) && this.mStartPointRender != null) {
                        int indexOf = this.mFilterRenders.indexOf(fX_FBORender);
                        this.mFilterRenders.remove(fX_FBORender);
                        if (this.mFilterRenders.isEmpty()) {
                            this.mStartPointRender.removeTarget(fX_FBORender);
                            synchronized (this.mEndPointRenders) {
                                for (GLRender next : this.mEndPointRenders) {
                                    fX_FBORender.removeTarget(next);
                                    this.mStartPointRender.addTarget(next);
                                }
                            }
                        } else if (indexOf == 0) {
                            FBORender fX_FBORender2 = this.mFilterRenders.get(0);
                            this.mStartPointRender.removeTarget(fX_FBORender);
                            fX_FBORender.removeTarget(fX_FBORender2);
                            this.mStartPointRender.addTarget(fX_FBORender2);
                        } else if (indexOf > this.mFilterRenders.size() - 1) {
                            FBORender fX_FBORender3 = this.mFilterRenders.get(this.mFilterRenders.size() - 1);
                            fX_FBORender3.removeTarget(fX_FBORender);
                            synchronized (this.mEndPointRenders) {
                                for (GLRender next2 : this.mEndPointRenders) {
                                    fX_FBORender.removeTarget(next2);
                                    fX_FBORender3.addTarget(next2);
                                }
                            }
                        } else {
                            FBORender fX_FBORender4 = this.mFilterRenders.get(indexOf - 1);
                            FBORender fX_FBORender5 = this.mFilterRenders.get(indexOf);
                            fX_FBORender4.removeTarget(fX_FBORender);
                            fX_FBORender.removeTarget(fX_FBORender5);
                            fX_FBORender4.addTarget(fX_FBORender5);
                        }
                        addRenderToDestroy(fX_FBORender);
                        synchronized (this.mRendersChangedListeners) {
                            for (OnFilterRendersChangedListener onFilterRendersChanged : this.mRendersChangedListeners) {
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
        synchronized (this.mFilterRenders) {
            if (this.mStartPointRender != null && !this.mFilterRenders.isEmpty()) {
                if (this.mFilterRenders.size() == 1) {
                    FBORender fX_FBORender = this.mFilterRenders.get(0);
                    this.mStartPointRender.removeTarget(fX_FBORender);
                    synchronized (this.mEndPointRenders) {
                        for (GLRender next : this.mEndPointRenders) {
                            fX_FBORender.removeTarget(next);
                            this.mStartPointRender.addTarget(next);
                        }
                    }
                } else {
                    FBORender fX_FBORender2 = this.mFilterRenders.get(this.mFilterRenders.size() - 1);
                    this.mStartPointRender.removeTarget(this.mFilterRenders.get(0));
                    synchronized (this.mEndPointRenders) {
                        for (GLRender next2 : this.mEndPointRenders) {
                            fX_FBORender2.removeTarget(next2);
                            this.mStartPointRender.addTarget(next2);
                        }
                    }
                }
                for (FBORender addRenderToDestroy : this.mFilterRenders) {
                    addRenderToDestroy(addRenderToDestroy);
                }
                this.mFilterRenders.clear();
                synchronized (this.mRendersChangedListeners) {
                    for (OnFilterRendersChangedListener onFilterRendersChanged : this.mRendersChangedListeners) {
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
//        output(bitmapOutputCallback, this.mWidth, this.mHeight, z);
//    }
//
//    public void output(FX_BitmapOutput.BitmapOutputCallback bitmapOutputCallback, int i, int i2, boolean z) {
//        FX_FBORender fX_FBORender;
//        if (!z || this.mFilterRenders.isEmpty()) {
//            fX_FBORender = this.mStartPointRender;
//        } else {
//            fX_FBORender = this.mFilterRenders.get(this.mFilterRenders.size() - 1);
//        }
//        FX_BitmapOutput fX_BitmapOutput = new FX_BitmapOutput();
//        fX_BitmapOutput.setRenderSize(i, i2);
//        fX_BitmapOutput.setBitmapOutputCallback(new FX_BitmapOutput.BitmapOutputCallback(bitmapOutputCallback, fX_FBORender, fX_BitmapOutput) {
//            private final /* synthetic */ FX_BitmapOutput.BitmapOutputCallback f$1;
//            private final /* synthetic */ FX_FBORender f$2;
//            private final /* synthetic */ FX_BitmapOutput f$3;
//
//            {
//                this.f$1 = r2;
//                this.f$2 = r3;
//                this.f$3 = r4;
//            }
//
//            public final void bitmapOutput(Bitmap bitmap) {
//                FX_RenderPipeline.lambda$output$0(FX_RenderPipeline.this, this.f$1, this.f$2, this.f$3, bitmap);
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
