package com.scorpion.NeonphotoEditor.Videoneoneffect;

import android.content.Context;
import cn.ezandroid.ezfilter.core.environment.IGLEnvironment;

public interface IFitView extends IGLEnvironment {
    Context getContext();

    int getPreviewHeight();

    int getPreviewWidth();

    RenderPipeline getRenderPipeline();

    void initRenderPipeline(FBORender fX_FBORender);

    void requestLayout();

    boolean setAspectRatio(float f, int i, int i2);

    boolean setRotate90Degrees(int i);

    void setScaleType(FitViewHelper.ScaleType scaleType);
}
