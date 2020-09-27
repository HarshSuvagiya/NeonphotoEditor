package com.scorpion.NeonphotoEditor.Videoneoneffect;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import cn.ezandroid.ezfilter.core.environment.GLTextureView;

public class TextureFitView extends GLTextureView implements IFitView {
    private FitViewHelper mHelper;
    private RenderPipeline mPipeline;

    public TextureFitView(Context context) {
        this(context, (AttributeSet) null);
        init();
    }

    public TextureFitView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init();
    }

    private void init() {
        setDebugFlags(3);
        setEGLContextClientVersion(2);
        mHelper = new FitViewHelper();
        mPipeline = new RenderPipeline();
        setRenderer(mPipeline);
        setRenderMode(0);
    }

    public void initRenderPipeline(FBORender FBORender1) {
        if (FBORender1 != null) {
            mPipeline.setStartPointRender(FBORender1);
        }
    }

    public RenderPipeline getRenderPipeline() {
        return mPipeline;
    }

    public void setScaleType(FitViewHelper.ScaleType scaleType) {
        mHelper.setScaleType(scaleType);
    }

    public boolean setAspectRatio(float f, int i, int i2) {
        boolean aspectRatio = mHelper.setAspectRatio(f, i, i2);
        if (mPipeline != null) {
            mPipeline.setRenderSize(getPreviewWidth(), getPreviewHeight());
        }
        return aspectRatio;
    }

    public boolean setRotate90Degrees(int i) {
        boolean rotate90Degrees = mHelper.setRotate90Degrees(i);
        if (mPipeline != null) {
            mPipeline.setRotate90Degrees(mHelper.getRotation90Degrees());
            mPipeline.setRenderSize(getPreviewWidth(), getPreviewHeight());
        }
        return rotate90Degrees;
    }

    public float getAspectRatio() {
        return mHelper.getAspectRatio();
    }

    public int getRotation90Degrees() {
        return mHelper.getRotation90Degrees();
    }

    public int getPreviewWidth() {
        return mHelper.getPreviewWidth();
    }

    public int getPreviewHeight() {
        return mHelper.getPreviewHeight();
    }

    public void onMeasure(int i, int i2) {
        mHelper.calculatePreviewSize(View.MeasureSpec.getSize(i), View.MeasureSpec.getSize(i2));
        super.onMeasure(View.MeasureSpec.makeMeasureSpec(mHelper.getPreviewWidth(), 1073741824), View.MeasureSpec.makeMeasureSpec(mHelper.getPreviewHeight(), 1073741824));
    }
}
