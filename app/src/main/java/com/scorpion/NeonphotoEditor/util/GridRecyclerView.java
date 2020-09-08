package com.scorpion.NeonphotoEditor.util;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.GridLayoutAnimationController;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class GridRecyclerView extends RecyclerView {
    public GridRecyclerView(Context context) {
        super(context);
    }

    public GridRecyclerView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public GridRecyclerView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    public void attachLayoutAnimationParameters(View view, ViewGroup.LayoutParams layoutParams, int i, int i2) {
        RecyclerView.LayoutManager layoutManager = getLayoutManager();
        if (getAdapter() == null || !(layoutManager instanceof GridLayoutManager)) {
            super.attachLayoutAnimationParameters(view, layoutParams, i, i2);
            return;
        }
        GridLayoutAnimationController.AnimationParameters animationParameters = (GridLayoutAnimationController.AnimationParameters) layoutParams.layoutAnimationParameters;
        if (animationParameters == null) {
            animationParameters = new GridLayoutAnimationController.AnimationParameters();
            layoutParams.layoutAnimationParameters = animationParameters;
        }
        animationParameters.count = i2;
        animationParameters.index = i;
        int spanCount = ((GridLayoutManager) layoutManager).getSpanCount();
        animationParameters.columnsCount = spanCount;
        animationParameters.rowsCount = i2 / spanCount;
        int i3 = (i2 - 1) - i;
        animationParameters.column = (spanCount - 1) - (i3 % spanCount);
        animationParameters.row = (animationParameters.rowsCount - 1) - (i3 / spanCount);
    }
}
