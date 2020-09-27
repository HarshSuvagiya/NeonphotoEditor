package com.scorpion.NeonphotoEditor.Videoneoneffect;

import cn.ezandroid.ezfilter.extra.IAdjustable;

public class SplitBuilder extends EZFilter.Builder {
    private EZFilter.Builder mOriginalBuilder;
    private SplitInput mSplitInput;

    public SplitBuilder(EZFilter.Builder builder, SplitInput fX_SplitInput) {
        mOriginalBuilder = builder;
        mSplitInput = fX_SplitInput;
    }

    public FBORender getStartPointRender(IFitView fX_IFitView) {
        mSplitInput.setRootRender(mOriginalBuilder.getStartPointRender(fX_IFitView));
        return mSplitInput;
    }

    public float getAspectRatio(IFitView fX_IFitView) {
        return mOriginalBuilder.getAspectRatio(fX_IFitView);
    }

    public SplitBuilder addFilter(FilterRender fX_FilterRender) {
        return (SplitBuilder) super.addFilter(fX_FilterRender);
    }

    public <T extends FilterRender & IAdjustable> SplitBuilder addFilter(T t, float f) {
        return (SplitBuilder) super.addFilter(t, f);
    }

    public SplitBuilder enableRecord(String str, boolean z, boolean z2) {
        return (SplitBuilder) super.enableRecord(str, z, z2);
    }
}
