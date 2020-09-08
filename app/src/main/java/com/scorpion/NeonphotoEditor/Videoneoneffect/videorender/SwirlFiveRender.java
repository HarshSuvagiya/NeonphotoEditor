package com.scorpion.NeonphotoEditor.Videoneoneffect.videorender;

import android.content.Context;
import android.graphics.PointF;

import com.scorpion.NeonphotoEditor.Videoneoneffect.sticker.ComponentConvert;
import com.scorpion.NeonphotoEditor.Videoneoneffect.sticker.IStickerTimeController;
import com.scorpion.NeonphotoEditor.util.Constant;

import cn.ezandroid.ezfilter.extra.sticker.model.AnchorPoint;
import cn.ezandroid.ezfilter.extra.sticker.model.Component;
import cn.ezandroid.ezfilter.extra.sticker.model.ScreenAnchor;
import cn.ezandroid.ezfilter.extra.sticker.model.Sticker;
import cn.ezandroid.ezfilter.extra.sticker.model.TextureAnchor;
import java.util.ArrayList;
import java.util.List;

public class SwirlFiveRender extends StickerRender {
    private List<PointF> mPositionHistories = new ArrayList();

    public SwirlFiveRender(Context context, IStickerTimeController fX_IStickerTimeController) {
        super(context, fX_IStickerTimeController);
        setOriginalSize(Constant.SWIRL_STICKER_SIZE);
        int originalSize = getOriginalSize();
        Sticker sticker = new Sticker();
        sticker.components = new ArrayList();
        Component component = new Component();
        component.duration = 1500;
        component.src = Constant.SWIRL5_FOLDER;
        component.width = originalSize;
        component.height = originalSize;
        TextureAnchor textureAnchor = new TextureAnchor();
        textureAnchor.leftAnchor = new AnchorPoint(-3, 0, 0);
        textureAnchor.rightAnchor = new AnchorPoint(-4, 0, 0);
        textureAnchor.width = component.width;
        textureAnchor.height = component.height;
        component.textureAnchor = textureAnchor;
        sticker.components.add(component);
        ComponentConvert.convert(context, component, "file:///android_asset/" + Constant.STICKER_FOLDER + "/");
        setSticker(sticker);
        ScreenAnchor screenAnchor = new ScreenAnchor();
        screenAnchor.leftAnchor = new AnchorPoint(-1, 0, 0);
        screenAnchor.rightAnchor = new AnchorPoint(-1, 0, 0);
        setScreenAnchor(screenAnchor);
    }

    public List<PointF> getPositionHistories() {
        return this.mPositionHistories;
    }

    /* access modifiers changed from: protected */
    public void onDraw() {
        super.onDraw();
        if (!this.mIsPause) {
            this.mPositionHistories.add(new PointF(this.mScreenAnchor.leftAnchor.x + ((float) (this.mSticker.components.get(0).width / 2)), this.mScreenAnchor.leftAnchor.y));
            return;
        }
        float currentTime = this.mTimeController.getCurrentTime();
        if (currentTime < this.mStartTime || this.mEndTime <= this.mStartTime || this.mPositionHistories.isEmpty()) {
            setPosition(-2000, -2000);
            return;
        }
        int round = Math.round((((float) (this.mPositionHistories.size() - 1)) * (currentTime - this.mStartTime)) / (this.mEndTime - this.mStartTime));
        List<PointF> list = this.mPositionHistories;
        if (round >= this.mPositionHistories.size()) {
            round = this.mPositionHistories.size() - 1;
        }
        PointF pointF = list.get(round);
        setPosition(Math.round(pointF.x), Math.round(pointF.y));
    }
}
