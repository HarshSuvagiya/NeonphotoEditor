package com.scorpion.NeonphotoEditor.Videoneoneffect;

import cn.ezandroid.ezfilter.extra.IAdjustable;
import java.util.List;

public class MultiBuilder extends EZFilter.Builder {
    private List<EZFilter.Builder> mBuilders;
    private MultiInput mMultiInput;

    public MultiBuilder(List<EZFilter.Builder> list, MultiInput fX_MultiInput) {
        mBuilders = list;
        mMultiInput = fX_MultiInput;
    }

    public FBORender getStartPointRender(IFitView fX_IFitView) {
        mMultiInput.clearRegisteredFilters();
        for (EZFilter.Builder startPointRender : mBuilders) {
            mMultiInput.registerFilter(startPointRender.getStartPointRender(fX_IFitView));
        }
        return mMultiInput;
    }

    public float getAspectRatio(IFitView fX_IFitView) {
        return (((float) mMultiInput.getWidth()) * 1.0f) / ((float) mMultiInput.getHeight());
    }

    public MultiBuilder addFilter(FilterRender fX_FilterRender) {
        return (MultiBuilder) super.addFilter(fX_FilterRender);
    }

    public <T extends FilterRender & IAdjustable> MultiBuilder addFilter(T t, float f) {
        return (MultiBuilder) super.addFilter(t, f);
    }

    public MultiBuilder enableRecord(String str, boolean z, boolean z2) {
        return (MultiBuilder) super.enableRecord(str, z, z2);
    }
}
