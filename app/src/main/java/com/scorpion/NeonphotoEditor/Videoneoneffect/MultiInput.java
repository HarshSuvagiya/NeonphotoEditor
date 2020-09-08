package com.scorpion.NeonphotoEditor.Videoneoneffect;

import android.opengl.GLES20;
import java.util.ArrayList;
import java.util.List;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class MultiInput extends FBORender {
    protected List<FBORender> mEndPointRenders;
    protected int[] mMultiTexture;
    protected int[] mMultiTextureHandle;
    protected int mNumOfInputs;
    protected List<RenderPipeline> mRenderPipelines;
    protected List<FBORender> mStartPointRenders;

    public MultiInput(int i) {
        this.mNumOfInputs = i;
        int i2 = i - 1;
        this.mMultiTextureHandle = new int[i2];
        this.mMultiTexture = new int[i2];
        this.mStartPointRenders = new ArrayList(i);
        this.mEndPointRenders = new ArrayList(i);
        this.mRenderPipelines = new ArrayList(i);
    }

    public synchronized void onTextureAcceptable(int i, GLRender fX_GLRender) {
        int lastIndexOf = this.mEndPointRenders.lastIndexOf(fX_GLRender);
        if (lastIndexOf <= 0) {
            this.mTextureIn = i;
        } else {
            this.mMultiTexture[lastIndexOf - 1] = i;
        }
    }

    /* access modifiers changed from: protected */
    public void initShaderHandles() {
        super.initShaderHandles();
        for (int i = 0; i < this.mNumOfInputs - 1; i++) {
            int[] iArr = this.mMultiTextureHandle;
            int i2 = this.mProgramHandle;
            iArr[i] = GLES20.glGetUniformLocation(i2, "inputImageTexture" + (i + 2));
        }
    }

    /* access modifiers changed from: protected */
    public void bindShaderValues() {
        super.bindShaderValues();
        for (int i = 0; i < this.mNumOfInputs - 1; i++) {
            if (this.mMultiTexture[i] != 0) {
                GLES20.glActiveTexture(33985 + i);
                GLES20.glBindTexture(3553, this.mMultiTexture[i]);
                GLES20.glUniform1i(this.mMultiTextureHandle[i], i + 1);
            }
        }
    }

    public void clearRegisteredFilters() {
        for (FBORender removeTarget : this.mEndPointRenders) {
            removeTarget.removeTarget(this);
        }
        this.mStartPointRenders.clear();
        this.mEndPointRenders.clear();
        this.mRenderPipelines.clear();
    }

    public void registerFilter(FBORender fX_FBORender) {
        if (!this.mStartPointRenders.contains(fX_FBORender)) {
            FBORender fX_FBORender2 = new FBORender();
            fX_FBORender2.addTarget(this);
            this.mStartPointRenders.add(fX_FBORender);
            this.mEndPointRenders.add(fX_FBORender2);
            RenderPipeline fX_RenderPipeline = new RenderPipeline();
            fX_RenderPipeline.onSurfaceCreated((GL10) null, (EGLConfig) null);
            fX_RenderPipeline.onSurfaceChanged((GL10) null, fX_FBORender.getWidth(), fX_FBORender.getHeight());
            fX_RenderPipeline.setStartPointRender(fX_FBORender);
            fX_RenderPipeline.addEndPointRender(fX_FBORender2);
            fX_RenderPipeline.startRender();
            this.mRenderPipelines.add(fX_RenderPipeline);
        }
    }

    public List<FBORender> getStartPointRenders() {
        return this.mStartPointRenders;
    }

    public List<FBORender> getEndPointRenders() {
        return this.mEndPointRenders;
    }

    public List<RenderPipeline> getRenderPipelines() {
        return this.mRenderPipelines;
    }

    public void onDrawFrame() {
        for (RenderPipeline onDrawFrame : this.mRenderPipelines) {
            onDrawFrame.onDrawFrame((GL10) null);
        }
        super.onDrawFrame();
    }

    public void destroy() {
        super.destroy();
        for (RenderPipeline onSurfaceDestroyed : this.mRenderPipelines) {
            onSurfaceDestroyed.onSurfaceDestroyed();
        }
    }
}
