package com.scorpion.NeonphotoEditor.Videoneoneffect.VideoRender;

import android.content.Context;
import android.graphics.PointF;

import com.scorpion.NeonphotoEditor.Videoneoneffect.Sticker.ComponentConvert;
import com.scorpion.NeonphotoEditor.Videoneoneffect.Sticker.IStickerTimeController;
import com.scorpion.NeonphotoEditor.Util.Constant;

import cn.ezandroid.ezfilter.extra.sticker.model.AnchorPoint;
import cn.ezandroid.ezfilter.extra.sticker.model.Component;
import cn.ezandroid.ezfilter.extra.sticker.model.ScreenAnchor;
import cn.ezandroid.ezfilter.extra.sticker.model.Sticker;
import cn.ezandroid.ezfilter.extra.sticker.model.TextureAnchor;
import java.util.ArrayList;
import java.util.List;

public class HeartsRender extends StickerRender {
    private List<PointF> mPositionHistories = new ArrayList();

    public HeartsRender(Context context, IStickerTimeController IStickerTimeController1) {
        super(context, IStickerTimeController1);
        setOriginalSize(Constant.NORMAL_STICKER_SIZE);
        int originalSize = getOriginalSize();
        Sticker sticker = new Sticker();
        sticker.components = new ArrayList();
        Component component = new Component();
        component.duration = 1800;
        component.src = Constant.HEARTS_FOLDER;
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
        return mPositionHistories;
    }

    public void onDraw() {
        super.onDraw();
        if (!mIsPause) {
            mPositionHistories.add(new PointF(mScreenAnchor.leftAnchor.x + ((float) (mSticker.components.get(0).width / 2)), mScreenAnchor.leftAnchor.y));
            return;
        }
        float currentTime = mTimeController.getCurrentTime();
        if (currentTime < mStartTime || mEndTime <= mStartTime || mPositionHistories.isEmpty()) {
            setPosition(-2000, -2000);
            return;
        }
        int round = Math.round((((float) (mPositionHistories.size() - 1)) * (currentTime - mStartTime)) / (mEndTime - mStartTime));
        List<PointF> list = mPositionHistories;
        if (round >= mPositionHistories.size()) {
            round = mPositionHistories.size() - 1;
        }
        PointF pointF = list.get(round);
        setPosition(Math.round(pointF.x), Math.round(pointF.y));
    }
}
