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
        this.mContext = context;
        this.mTimeController = IStickerTimeController1;
    }

    public Sticker getSticker() {
        return this.mSticker;
    }

    public void setSticker(Sticker sticker) {
        this.mSticker = sticker;
        this.mComponentRenders.clear();
        for (Component Component1 : this.mSticker.components) {
            this.mComponentRenders.add(new ComponentRender(this.mContext, Component1));
        }
    }

    public void setScreenAnchor(ScreenAnchor screenAnchor) {
        this.mScreenAnchor = screenAnchor;
    }

    public void destroy() {
        super.destroy();
        for (ComponentRender destroy : this.mComponentRenders) {
            destroy.destroy();
        }
    }

    public void setBitmapCache(IBitmapCache iBitmapCache) {
        super.setBitmapCache(iBitmapCache);
        for (ComponentRender bitmapCache : this.mComponentRenders) {
            bitmapCache.setBitmapCache(iBitmapCache);
        }
    }

    public void onRenderSizeChanged() {
        super.onRenderSizeChanged();
        if (this.mScreenAnchor != null) {
            this.mScreenAnchor.width = this.mWidth;
            this.mScreenAnchor.height = this.mHeight;
        }
    }

    public void start() {
        this.mIsPause = false;
        this.mIsTouch = true;
        this.mStartTime = this.mTimeController.getCurrentTime();
    }

    public void pause() {
        this.mIsPause = true;
        this.mIsTouch = false;
        this.mEndTime = this.mTimeController.getCurrentTime();
    }

    public void setPosition(int i, int i2) {
        Helper.showLog("X : " + i + " : Y : " + i2);
        if (!this.mSticker.components.isEmpty()) {
            this.mScreenAnchor.leftAnchor.x = (float) (i - (this.mSticker.components.get(0).width / 2));
            float f = (float) i2;
            this.mScreenAnchor.leftAnchor.y = f;
            this.mScreenAnchor.rightAnchor.x = (float) (i + (this.mSticker.components.get(0).width / 2));
            this.mScreenAnchor.rightAnchor.y = f;
        }
    }

    public void setEffectTimeBar(VideoEffectTimeBar VideoEffectTimeBar1) {
        this.effectTimeBar = VideoEffectTimeBar1;
    }

    public void setSize(int i) {
        for (Component next : this.mSticker.components) {
            next.width = i;
            next.height = i;
        }
    }

    public int getSize() {
        int i = 0;
        for (Component component : this.mSticker.components) {
            i = component.width;
        }
        return i;
    }

    public int getOriginalSize() {
        return this.originalSize;
    }

    public void setOriginalSize(int i) {
        this.originalSize = i;
    }

    public void onDraw() {
        super.onDraw();
        GLES20.glEnable(3042);
        GLES20.glBlendFunc(1, 771);
        for (ComponentRender next : this.mComponentRenders) {
            next.setmIsPause(this.mIsPause);
            next.setTouch(this.mIsTouch);
            next.setEffectTimeBar(this.effectTimeBar);
            next.setScreenAnchor(this.mScreenAnchor);
            next.updateRenderVertices(getWidth(), getHeight());
            next.onDraw(this.mTextureHandle, this.mPositionHandle, this.mTextureCoordHandle, this.mTextureVertices[2]);
        }
        GLES20.glDisable(3042);
    }
}
