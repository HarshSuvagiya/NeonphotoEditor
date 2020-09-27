package com.scorpion.NeonphotoEditor.Videoneoneffect.Sticker;

import android.content.Context;

import com.scorpion.NeonphotoEditor.Videoneoneffect.VideoRender.AnnasRender;
import com.scorpion.NeonphotoEditor.Videoneoneffect.VideoRender.BeatingRender;
import com.scorpion.NeonphotoEditor.Videoneoneffect.VideoRender.ColorRoundRender;
import com.scorpion.NeonphotoEditor.Videoneoneffect.VideoRender.FlamingoRender;
import com.scorpion.NeonphotoEditor.Videoneoneffect.VideoRender.HalfRoundRender;
import com.scorpion.NeonphotoEditor.Videoneoneffect.VideoRender.HeartsRender;
import com.scorpion.NeonphotoEditor.Videoneoneffect.VideoRender.NimbuzzRender;
import com.scorpion.NeonphotoEditor.Videoneoneffect.VideoRender.RoundRender;
import com.scorpion.NeonphotoEditor.Videoneoneffect.VideoRender.SquareRender;
import com.scorpion.NeonphotoEditor.Videoneoneffect.VideoRender.TreeRender;
import com.scorpion.NeonphotoEditor.Videoneoneffect.VideoRender.MusicRender;
import com.scorpion.NeonphotoEditor.Videoneoneffect.VideoRender.StickerRender;

public class StickerHelper {
    Context context;
    IStickerTimeController iStickerTimeController;

    public StickerHelper(Context context2, IStickerTimeController IStickerTimeController1) {
        context = context2;
        iStickerTimeController = IStickerTimeController1;
    }

    public StickerRender getParticleRenderer(int i) {
        switch (i) {
            case 0:
                return new AnnasRender(context, iStickerTimeController);
            case 1:
                return new BeatingRender(context, iStickerTimeController);
            case 2:
                return new ColorRoundRender(context, iStickerTimeController);
            case 3:
                return new FlamingoRender(context, iStickerTimeController);
            case 4:
                return new HalfRoundRender(context, iStickerTimeController);
            case 5:
                return new HeartsRender(context, iStickerTimeController);
            case 6:
                return new NimbuzzRender(context, iStickerTimeController);
            case 7:
                return new RoundRender(context, iStickerTimeController);
            case 8:
                return new SquareRender(context, iStickerTimeController);
            case 9:
                return new TreeRender(context, iStickerTimeController);
            case 10:
                return new MusicRender(context, iStickerTimeController);
            default:
                return new AnnasRender(context, iStickerTimeController);
        }
    }
}
