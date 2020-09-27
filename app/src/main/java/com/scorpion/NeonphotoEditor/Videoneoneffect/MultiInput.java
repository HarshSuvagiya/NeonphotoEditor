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
        mNumOfInputs = i;
        int i2 = i - 1;
        mMultiTextureHandle = new int[i2];
        mMultiTexture = new int[i2];
        mStartPointRenders = new ArrayList(i);
        mEndPointRenders = new ArrayList(i);
        mRenderPipelines = new ArrayList(i);
    }

    public synchronized void onTextureAcceptable(int i, GLRender fX_GLRender) {
        int lastIndexOf = mEndPointRenders.lastIndexOf(fX_GLRender);
        if (lastIndexOf <= 0) {
            mTextureIn = i;
        } else {
            mMultiTexture[lastIndexOf - 1] = i;
        }
    }

    public void initShaderHandles() {
        super.initShaderHandles();
        for (int i = 0; i < mNumOfInputs - 1; i++) {
            int[] iArr = mMultiTextureHandle;
            int i2 = mProgramHandle;
            iArr[i] = GLES20.glGetUniformLocation(i2, "inputImageTexture" + (i + 2));
        }
    }

    public void bindShaderValues() {
        super.bindShaderValues();
        for (int i = 0; i < mNumOfInputs - 1; i++) {
            if (mMultiTexture[i] != 0) {
                GLES20.glActiveTexture(33985 + i);
                GLES20.glBindTexture(3553, mMultiTexture[i]);
                GLES20.glUniform1i(mMultiTextureHandle[i], i + 1);
            }
        }
    }

    public void clearRegisteredFilters() {
        for (FBORender removeTarget : mEndPointRenders) {
            removeTarget.removeTarget(this);
        }
        mStartPointRenders.clear();
        mEndPointRenders.clear();
        mRenderPipelines.clear();
    }

    public void registerFilter(FBORender fX_FBORender) {
        if (!mStartPointRenders.contains(fX_FBORender)) {
            FBORender fX_FBORender2 = new FBORender();
            fX_FBORender2.addTarget(this);
            mStartPointRenders.add(fX_FBORender);
            mEndPointRenders.add(fX_FBORender2);
            RenderPipeline fX_RenderPipeline = new RenderPipeline();
            fX_RenderPipeline.onSurfaceCreated((GL10) null, (EGLConfig) null);
            fX_RenderPipeline.onSurfaceChanged((GL10) null, fX_FBORender.getWidth(), fX_FBORender.getHeight());
            fX_RenderPipeline.setStartPointRender(fX_FBORender);
            fX_RenderPipeline.addEndPointRender(fX_FBORender2);
            fX_RenderPipeline.startRender();
            mRenderPipelines.add(fX_RenderPipeline);
        }
    }

    public List<FBORender> getStartPointRenders() {
        return mStartPointRenders;
    }

    public List<FBORender> getEndPointRenders() {
        return mEndPointRenders;
    }

    public List<RenderPipeline> getRenderPipelines() {
        return mRenderPipelines;
    }

    public void onDrawFrame() {
        for (RenderPipeline onDrawFrame : mRenderPipelines) {
            onDrawFrame.onDrawFrame((GL10) null);
        }
        super.onDrawFrame();
    }

    public void destroy() {
        super.destroy();
        for (RenderPipeline onSurfaceDestroyed : mRenderPipelines) {
            onSurfaceDestroyed.onSurfaceDestroyed();
        }
    }
}
