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
        this.context = context2;
        this.iStickerTimeController = IStickerTimeController1;
    }

    public StickerRender getParticleRenderer(int i) {
        switch (i) {
            case 0:
                return new AnnasRender(this.context, this.iStickerTimeController);
            case 1:
                return new BeatingRender(this.context, this.iStickerTimeController);
            case 2:
                return new ColorRoundRender(this.context, this.iStickerTimeController);
            case 3:
                return new FlamingoRender(this.context, this.iStickerTimeController);
            case 4:
                return new HalfRoundRender(this.context, this.iStickerTimeController);
            case 5:
                return new HeartsRender(this.context, this.iStickerTimeController);
            case 6:
                return new NimbuzzRender(this.context, this.iStickerTimeController);
            case 7:
                return new RoundRender(this.context, this.iStickerTimeController);
            case 8:
                return new SquareRender(this.context, this.iStickerTimeController);
            case 9:
                return new TreeRender(this.context, this.iStickerTimeController);
            case 10:
                return new MusicRender(this.context, this.iStickerTimeController);
            default:
                return new AnnasRender(this.context, this.iStickerTimeController);
        }
    }
}
