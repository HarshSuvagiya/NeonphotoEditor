package com.scorpion.NeonphotoEditor.Videoneoneffect.VideoRender;

import android.content.Context;
import android.opengl.GLES20;

import com.scorpion.NeonphotoEditor.Videoneoneffect.FilterRender;
import com.scorpion.NeonphotoEditor.Videoneoneffect.Sticker.ComponentRender;
import com.scorpion.NeonphotoEditor.Videoneoneffect.Sticker.IStickerTimeController;
import com.scorpion.NeonphotoEditor.Util.Constant;
import com.scorpion.NeonphotoEditor.Util.Helper;
import com.scorpion.NeonphotoEditor.Util.VideoEffectTimeBar;

import cn.ezandroid.ezfilter.core.cache.IBitmapCache;
import cn.ezandroid.ezfilter.extra.sticker.model.Component;
import cn.ezandroid.ezfilter.extra.sticker.model.ScreenAnchor;
import cn.ezandroid.ezfilter.extra.sticker.model.Sticker;
import java.util.ArrayList;
import java.util.List;

public class StickerRender extends FilterRender {
    VideoEffectTimeBar effectTimeBar;
    protected List<ComponentRender> mComponentRenders = new ArrayList();
    protected Context mContext;
    float mEndTime;
    boolean mIsPause = true;
    boolean mIsTouch = false;
    protected ScreenAnchor mScreenAnchor;
    float mStartTime;
    protected Sticker mSticker;
    IStickerTimeController mTimeController;
    int originalSize = Constant.NORMAL_STICKER_SIZE;

    public StickerRender(Context context, IStickerTimeController IStickerTimeController1) {
        mContext = context;
        mTimeController = IStickerTimeController1;
    }

    public Sticker getSticker() {
        return mSticker;
    }

    public void setSticker(Sticker sticker) {
        mSticker = sticker;
        mComponentRenders.clear();
        for (Component Component1 : mSticker.components) {
            mComponentRenders.add(new ComponentRender(mContext, Component1));
        }
    }

    public void setScreenAnchor(ScreenAnchor screenAnchor) {
        mScreenAnchor = screenAnchor;
    }

    public void destroy() {
        super.destroy();
        for (ComponentRender destroy : mComponentRenders) {
            destroy.destroy();
        }
    }

    public void setBitmapCache(IBitmapCache iBitmapCache) {
        super.setBitmapCache(iBitmapCache);
        for (ComponentRender bitmapCache : mComponentRenders) {
            bitmapCache.setBitmapCache(iBitmapCache);
        }
    }

    public void onRenderSizeChanged() {
        super.onRenderSizeChanged();
        if (mScreenAnchor != null) {
            mScreenAnchor.width = mWidth;
            mScreenAnchor.height = mHeight;
        }
    }

    public void start() {
        mIsPause = false;
        mIsTouch = true;
        mStartTime = mTimeController.getCurrentTime();
    }

    public void pause() {
        mIsPause = true;
        mIsTouch = false;
        mEndTime = mTimeController.getCurrentTime();
    }

    public void setPosition(int i, int i2) {
        Helper.showLog("X : " + i + " : Y : " + i2);
        if (!mSticker.components.isEmpty()) {
            mScreenAnchor.leftAnchor.x = (float) (i - (mSticker.components.get(0).width / 2));
            float f = (float) i2;
            mScreenAnchor.leftAnchor.y = f;
            mScreenAnchor.rightAnchor.x = (float) (i + (mSticker.components.get(0).width / 2));
            mScreenAnchor.rightAnchor.y = f;
        }
    }

    public void setEffectTimeBar(VideoEffectTimeBar VideoEffectTimeBar1) {
        effectTimeBar = VideoEffectTimeBar1;
    }

    public void setSize(int i) {
        for (Component next : mSticker.components) {
            next.width = i;
            next.height = i;
        }
    }

    public int getSize() {
        int i = 0;
        for (Component component : mSticker.components) {
            i = component.width;
        }
        return i;
    }

    public int getOriginalSize() {
        return originalSize;
    }

    public void setOriginalSize(int i) {
        originalSize = i;
    }

    public void onDraw() {
        super.onDraw();
        GLES20.glEnable(3042);
        GLES20.glBlendFunc(1, 771);
        for (ComponentRender next : mComponentRenders) {
            next.setmIsPause(mIsPause);
            next.setTouch(mIsTouch);
            next.setEffectTimeBar(effectTimeBar);
            next.setScreenAnchor(mScreenAnchor);
            next.updateRenderVertices(getWidth(), getHeight());
            next.onDraw(mTextureHandle, mPositionHandle, mTextureCoordHandle, mTextureVertices[2]);
        }
        GLES20.glDisable(3042);
    }
}
