package com.scorpion.NeonphotoEditor.Videoneoneffect;

import android.media.MediaPlayer;

public class DefaultMediaPlayer extends MediaPlayer implements IMediaPlayer {
    public void setOnErrorListener(IMediaPlayer.OnErrorListener onErrorListener) {
    }

    public void setOnPreparedListener(IMediaPlayer.OnPreparedListener onPreparedListener) {

        setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        private final IMediaPlayer.OnPreparedListener f$1;

            {
                f$1 = onPreparedListener;
            }

            public final void onPrepared(MediaPlayer mediaPlayer) {
                f$1.onPrepared(DefaultMediaPlayer.this);
            }

        });
    }

    public void setOnCompletionListener(IMediaPlayer.OnCompletionListener onCompletionListener) {

        setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            private final IMediaPlayer.OnCompletionListener f$1;

            {
                f$1 = onCompletionListener;
            }

            public final void onCompletion(MediaPlayer mediaPlayer) {
                f$1.onCompletion(DefaultMediaPlayer.this);
            }
        });
    }
}
