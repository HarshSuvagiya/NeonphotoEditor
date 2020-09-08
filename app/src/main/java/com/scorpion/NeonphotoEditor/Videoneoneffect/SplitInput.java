package com.scorpion.NeonphotoEditor.Videoneoneffect;

import android.opengl.GLES20;
import java.util.ArrayList;
import java.util.List;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class SplitInput extends FBORender {
    protected List<CropRender> mCropRenders;
    protected List<FBORender> mEndPointRenders = new ArrayList(this.mNumOfInputs);
    protected int[] mMultiTexture = new int[(this.mNumOfInputs - 1)];
    protected int[] mMultiTextureHandle = new int[(this.mNumOfInputs - 1)];
    protected int mNumOfInputs;
    protected List<RenderPipeline> mRenderPipelines = new ArrayList(this.mNumOfInputs);
    protected FBORender mRootRender;
    protected List<FBORender> mStartPointRenders = new ArrayList(this.mNumOfInputs);

    public SplitInput(List<CropRender> list) {
        this.mCropRenders = list;
        this.mNumOfInputs = list.size();
    }

    public void setRootRender(FBORender fX_FBORender) {
        if (fX_FBORender != null && this.mRootRender != fX_FBORender) {
            if (this.mRootRender != null) {
                synchronized (this.mRootRender.getTargets()) {
                    for (OnTextureAcceptableListener addTarget : this.mRootRender.getTargets()) {
                        fX_FBORender.addTarget(addTarget);
                    }
                }
                this.mRootRender.clearTargets();
                FBORender fX_FBORender2 = this.mRootRender;
                fX_FBORender2.getClass();
                runOnDraw(new Runnable() {
                    public final void run() {
                        destroy();
                    }
                });
                this.mRootRender = fX_FBORender;
                return;
            }
            this.mRootRender = fX_FBORender;
            clearRegisteredFilters();
            for (CropRender registerFilter : this.mCropRenders) {
                registerFilter(registerFilter);
            }
        }
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
        this.mRootRender.clearTargets();
        this.mStartPointRenders.clear();
        for (FBORender removeTarget : this.mEndPointRenders) {
            removeTarget.removeTarget(this);
        }
        this.mEndPointRenders.clear();
        this.mRenderPipelines.clear();
    }

    public void registerFilter(FBORender fX_FBORender) {
        if (!this.mStartPointRenders.contains(fX_FBORender)) {
            this.mRootRender.addTarget(fX_FBORender);
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

    public FBORender getRootRender() {
        return this.mRootRender;
    }

    public void onDrawFrame() {
        if (this.mRootRender != null) {
            this.mRootRender.onDrawFrame();
        }
        super.onDrawFrame();
    }

    public void destroy() {
        super.destroy();
        if (this.mRootRender != null) {
            this.mRootRender.destroy();
        }
        for (RenderPipeline onSurfaceDestroyed : this.mRenderPipelines) {
            onSurfaceDestroyed.onSurfaceDestroyed();
        }
    }
}
