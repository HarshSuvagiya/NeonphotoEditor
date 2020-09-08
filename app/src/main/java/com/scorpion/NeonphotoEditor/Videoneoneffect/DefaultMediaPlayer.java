package com.scorpion.NeonphotoEditor.Videoneoneffect;

import android.media.MediaPlayer;

public class DefaultMediaPlayer extends MediaPlayer implements IMediaPlayer {
    public void setOnErrorListener(IMediaPlayer.OnErrorListener onErrorListener) {
    }

    public void setOnPreparedListener(IMediaPlayer.OnPreparedListener onPreparedListener) {
//        final FX_IMediaPlayer.OnPreparedListener onPreparedListener1 = new FX_IMediaPlayer.OnPreparedListener() {
//            @Override
//            public void onPrepared(FX_IMediaPlayer fX_IMediaPlayer) {
//
//            }
//        };
//        setOnPreparedListener(new FX_IMediaPlayer.OnPreparedListener() {
//            FX_IMediaPlayer.OnPreparedListener onPreparedListener1=onPreparedListener;
//            @Override
//            public void onPrepared(FX_IMediaPlayer fX_IMediaPlayer) {
//                onPreparedListener1.onPrepared(fX_IMediaPlayer);
//            }
//        });

        setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        private final IMediaPlayer.OnPreparedListener f$1;

            {
                this.f$1 = onPreparedListener;
            }

            public final void onPrepared(MediaPlayer mediaPlayer) {
                this.f$1.onPrepared(DefaultMediaPlayer.this);
            }
//            public void onPrepared(MediaPlayer mp) {
//                mp.start();
//            }
        });
    }

    public void setOnCompletionListener(IMediaPlayer.OnCompletionListener onCompletionListener) {
//        setOnCompletionListener(new FX_IMediaPlayer.OnCompletionListener() {
//            FX_IMediaPlayer.OnCompletionListener onPreparedListener1=onCompletionListener;
//            @Override
//            public void onCompletion(FX_IMediaPlayer fX_IMediaPlayer) {
//                onPreparedListener1.onCompletion(fX_IMediaPlayer);
//            }
//        });
        setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            private final IMediaPlayer.OnCompletionListener f$1;

            {
                this.f$1 = onCompletionListener;
            }

            public final void onCompletion(MediaPlayer mediaPlayer) {
                this.f$1.onCompletion(DefaultMediaPlayer.this);
            }
        });
    }
}
