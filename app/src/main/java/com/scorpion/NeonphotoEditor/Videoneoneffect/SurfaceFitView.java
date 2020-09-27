package com.scorpion.NeonphotoEditor.Videoneoneffect;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import cn.ezandroid.ezfilter.core.environment.GLSurfaceView;

public class SurfaceFitView extends GLSurfaceView implements IFitView {
    private FitViewHelper mHelper;
    private RenderPipeline mPipeline;

    public SurfaceFitView(Context context) {
        this(context, (AttributeSet) null);
        init();
    }

    public SurfaceFitView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init();
    }

    private void init() {
        setDebugFlags(3);
        setEGLContextClientVersion(2);
        this.mHelper = new FitViewHelper();
        this.mPipeline = new RenderPipeline();
        setRenderer(this.mPipeline);
        setRenderMode(0);
    }

    public void initRenderPipeline(FBORender FBORender1) {
        if (FBORender1 != null) {
            this.mPipeline.setStartPointRender(FBORender1);
        }
    }

    public RenderPipeline getRenderPipeline() {
        return this.mPipeline;
    }

    public void setScaleType(FitViewHelper.ScaleType scaleType) {
        this.mHelper.setScaleType(scaleType);
    }

    public boolean setAspectRatio(float f, int i, int i2) {
        boolean aspectRatio = this.mHelper.setAspectRatio(f, i, i2);
        if (this.mPipeline != null) {
            this.mPipeline.setRenderSize(getPreviewWidth(), getPreviewHeight());
        }
        return aspectRatio;
    }

    public boolean setRotate90Degrees(int i) {
        boolean rotate90Degrees = this.mHelper.setRotate90Degrees(i);
        if (this.mPipeline != null) {
            this.mPipeline.setRotate90Degrees(this.mHelper.getRotation90Degrees());
            this.mPipeline.setRenderSize(getPreviewWidth(), getPreviewHeight());
        }
        return rotate90Degrees;
    }

    public float getAspectRatio() {
        return this.mHelper.getAspectRatio();
    }

    public int getRotation90Degrees() {
        return this.mHelper.getRotation90Degrees();
    }

    public int getPreviewWidth() {
        return this.mHelper.getPreviewWidth();
    }

    public int getPreviewHeight() {
        return this.mHelper.getPreviewHeight();
    }

    public void onMeasure(int i, int i2) {
        this.mHelper.calculatePreviewSize(View.MeasureSpec.getSize(i), View.MeasureSpec.getSize(i2));
        super.onMeasure(View.MeasureSpec.makeMeasureSpec(this.mHelper.getPreviewWidth(), 1073741824), View.MeasureSpec.makeMeasureSpec(this.mHelper.getPreviewHeight(), 1073741824));
    }
}
