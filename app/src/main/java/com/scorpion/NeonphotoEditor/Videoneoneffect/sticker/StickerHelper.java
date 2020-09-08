package com.scorpion.NeonphotoEditor.Videoneoneffect.sticker;

import android.content.Context;

import com.scorpion.NeonphotoEditor.Videoneoneffect.videorender.BalloonRender;
import com.scorpion.NeonphotoEditor.Videoneoneffect.videorender.BellRender;
import com.scorpion.NeonphotoEditor.Videoneoneffect.videorender.BottleOpenRender;
import com.scorpion.NeonphotoEditor.Videoneoneffect.videorender.CallRender;
import com.scorpion.NeonphotoEditor.Videoneoneffect.videorender.FireworkRender;
import com.scorpion.NeonphotoEditor.Videoneoneffect.videorender.FlyingHeartRender;
import com.scorpion.NeonphotoEditor.Videoneoneffect.videorender.GiftRender;
import com.scorpion.NeonphotoEditor.Videoneoneffect.videorender.GuitarRender;
import com.scorpion.NeonphotoEditor.Videoneoneffect.videorender.HeartRender;
import com.scorpion.NeonphotoEditor.Videoneoneffect.videorender.HolidayRender;
import com.scorpion.NeonphotoEditor.Videoneoneffect.videorender.MagicHatRender;
import com.scorpion.NeonphotoEditor.Videoneoneffect.videorender.MegaStarRender;
import com.scorpion.NeonphotoEditor.Videoneoneffect.videorender.MusicRender;
import com.scorpion.NeonphotoEditor.Videoneoneffect.videorender.PartyRender;
import com.scorpion.NeonphotoEditor.Videoneoneffect.videorender.PizzaRender;
import com.scorpion.NeonphotoEditor.Videoneoneffect.videorender.RocketRender;
import com.scorpion.NeonphotoEditor.Videoneoneffect.videorender.SantaRender;
import com.scorpion.NeonphotoEditor.Videoneoneffect.videorender.StickerRender;
import com.scorpion.NeonphotoEditor.Videoneoneffect.videorender.SwirlFiveRender;
import com.scorpion.NeonphotoEditor.Videoneoneffect.videorender.SwirlFourRender;
import com.scorpion.NeonphotoEditor.Videoneoneffect.videorender.SwirlOneRender;
import com.scorpion.NeonphotoEditor.Videoneoneffect.videorender.SwirlSixRender;
import com.scorpion.NeonphotoEditor.Videoneoneffect.videorender.SwirlThreeRender;
import com.scorpion.NeonphotoEditor.Videoneoneffect.videorender.SwirlTwoRender;
import com.scorpion.NeonphotoEditor.Videoneoneffect.videorender.TeacupRender;
import com.scorpion.NeonphotoEditor.Videoneoneffect.videorender.ToyRender;
import com.scorpion.NeonphotoEditor.Videoneoneffect.videorender.WitchHatRender;
import com.scorpion.NeonphotoEditor.Videoneoneffect.videorender.XmasRender;

public class StickerHelper {
    Context context;
    IStickerTimeController iStickerTimeController;

    public StickerHelper(Context context2, IStickerTimeController fX_IStickerTimeController) {
        this.context = context2;
        this.iStickerTimeController = fX_IStickerTimeController;
    }

    public StickerRender getParticleRenderer(int i) {
        switch (i) {
            case 0:
                return new SwirlOneRender(this.context, this.iStickerTimeController);
            case 1:
                return new SwirlTwoRender(this.context, this.iStickerTimeController);
            case 2:
                return new SwirlThreeRender(this.context, this.iStickerTimeController);
            case 3:
                return new SwirlFourRender(this.context, this.iStickerTimeController);
            case 4:
                return new SwirlFiveRender(this.context, this.iStickerTimeController);
            case 5:
                return new SwirlSixRender(this.context, this.iStickerTimeController);
            case 6:
                return new BalloonRender(this.context, this.iStickerTimeController);
            case 7:
                return new BellRender(this.context, this.iStickerTimeController);
            case 8:
                return new BottleOpenRender(this.context, this.iStickerTimeController);
            case 9:
                return new FlyingHeartRender(this.context, this.iStickerTimeController);
            case 10:
                return new GiftRender(this.context, this.iStickerTimeController);
            case 11:
                return new GuitarRender(this.context, this.iStickerTimeController);
            case 12:
                return new HeartRender(this.context, this.iStickerTimeController);
            case 13:
                return new HolidayRender(this.context, this.iStickerTimeController);
            case 14:
                return new MagicHatRender(this.context, this.iStickerTimeController);
            case 15:
                return new MegaStarRender(this.context, this.iStickerTimeController);
            case 16:
                return new MusicRender(this.context, this.iStickerTimeController);
            case 17:
                return new PartyRender(this.context, this.iStickerTimeController);
            case 18:
                return new PizzaRender(this.context, this.iStickerTimeController);
            case 19:
                return new RocketRender(this.context, this.iStickerTimeController);
            case 20:
                return new SantaRender(this.context, this.iStickerTimeController);
            case 21:
                return new ToyRender(this.context, this.iStickerTimeController);
            case 22:
                return new WitchHatRender(this.context, this.iStickerTimeController);
            case 23:
                return new XmasRender(this.context, this.iStickerTimeController);
            case 24:
                return new CallRender(this.context, this.iStickerTimeController);
            case 25:
                return new FireworkRender(this.context, this.iStickerTimeController);
            case 26:
                return new TeacupRender(this.context, this.iStickerTimeController);
            default:
                return new BalloonRender(this.context, this.iStickerTimeController);
        }
    }
}
