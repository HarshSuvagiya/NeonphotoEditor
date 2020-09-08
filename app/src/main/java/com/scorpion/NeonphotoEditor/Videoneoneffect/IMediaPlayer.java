package com.scorpion.NeonphotoEditor.Videoneoneffect;

import android.content.Context;
import android.net.Uri;
import android.view.Surface;
import java.io.IOException;

public interface IMediaPlayer {

    public interface OnCompletionListener {
        void onCompletion(IMediaPlayer fX_IMediaPlayer);
    }

    public interface OnErrorListener {
        boolean onError(IMediaPlayer fX_IMediaPlayer, int i, int i2);
    }

    public interface OnPreparedListener {
        void onPrepared(IMediaPlayer fX_IMediaPlayer);
    }

    int getCurrentPosition();

    int getDuration();

    int getVideoHeight();

    int getVideoWidth();

    boolean isLooping();

    boolean isPlaying();

    void pause() throws IllegalStateException;

    void prepareAsync() throws IllegalStateException;

    void release();

    void reset();

    void seekTo(int i) throws IllegalStateException;

    void setDataSource(Context context, Uri uri) throws IOException, IllegalArgumentException, SecurityException, IllegalStateException;

    void setLooping(boolean z);

    void setOnCompletionListener(OnCompletionListener onCompletionListener);

    void setOnErrorListener(OnErrorListener onErrorListener);

    void setOnPreparedListener(OnPreparedListener onPreparedListener);

    void setSurface(Surface surface);

    void setVolume(float f, float f2);

    void start() throws IllegalStateException;

    void stop() throws IllegalStateException;
}
