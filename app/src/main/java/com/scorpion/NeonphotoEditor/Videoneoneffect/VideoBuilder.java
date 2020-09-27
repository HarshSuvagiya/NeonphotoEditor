package com.scorpion.NeonphotoEditor.Videoneoneffect;

import android.media.MediaMetadataRetriever;
import android.net.Uri;

import com.scorpion.NeonphotoEditor.Util.VideoEffectTimeBar;

import cn.ezandroid.ezfilter.core.util.NumberUtil;
import cn.ezandroid.ezfilter.extra.IAdjustable;
import java.io.IOException;
import java.util.HashMap;

public class VideoBuilder extends EZFilter.Builder {
    private IMediaPlayer.OnCompletionListener mCompletionListener;
    private IMediaPlayer.OnErrorListener mErrorListener;
    private IMediaPlayer mMediaPlayer = new DefaultMediaPlayer();
    private IMediaPlayer.OnPreparedListener mPreparedListener;
    private boolean mStartWhenReady = true;
    private Uri mVideo;
    private VideoInput mVideoInput;
    private boolean mVideoLoop = true;
    private float mVideoVolume = 1.0f;
    private VideoEffectTimeBar videoEffectTimeBar;

    public VideoBuilder(Uri uri) {
        this.mVideo = uri;
    }

    public VideoBuilder setMediaPlayer(IMediaPlayer IMediaPlayer1) {
        this.mMediaPlayer = IMediaPlayer1;
        return this;
    }

    public VideoBuilder setLoop(boolean z) {
        this.mVideoLoop = z;
        return this;
    }

    public VideoBuilder setVolume(float f) {
        this.mVideoVolume = f;
        return this;
    }

    public VideoBuilder setStartWhenReady(boolean z) {
        this.mStartWhenReady = z;
        return this;
    }

    public VideoBuilder setPreparedListener(IMediaPlayer.OnPreparedListener onPreparedListener) {
        this.mPreparedListener = onPreparedListener;
        return this;
    }

    public VideoBuilder setCompletionListener(IMediaPlayer.OnCompletionListener onCompletionListener) {
        this.mCompletionListener = onCompletionListener;
        return this;
    }

    public VideoBuilder setErrorListener(IMediaPlayer.OnErrorListener onErrorListener) {
        this.mErrorListener = onErrorListener;
        return this;
    }

    public VideoBuilder setVideoEffectTimeBar(VideoEffectTimeBar VideoEffectTimeBar1) {
        this.videoEffectTimeBar = VideoEffectTimeBar1;
        return this;
    }

    public void output(String str) {
        try {
            new OffscreenVideo(this.mVideo.getPath()).save(str);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void output(String str, int i, int i2) throws IOException {
        OffscreenVideo OffscreenVideo1 = new OffscreenVideo(this.mVideo.getPath());
        try {
            for (FilterRender addFilterRender : this.mFilterRenders) {
                OffscreenVideo1.addFilterRender(addFilterRender);
            }
            OffscreenVideo1.save(str, i, i2);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public FBORender getStartPointRender(IFitView IFitView1) {
        if (this.mVideoInput == null) {
            this.mVideoInput = new VideoInput(IFitView1.getContext(), IFitView1, this.mVideo, this.mMediaPlayer);
            this.mVideoInput.setStartWhenReady(this.mStartWhenReady);
            this.mVideoInput.setLoop(this.mVideoLoop);
            this.mVideoInput.setVolume(this.mVideoVolume, this.mVideoVolume);
            this.mVideoInput.setOnPreparedListener(this.mPreparedListener);
            this.mVideoInput.setOnCompletionListener(this.mCompletionListener);
            this.mVideoInput.setOnErrorListener(this.mErrorListener);
            this.mVideoInput.setVideoEffectTimeBar(this.videoEffectTimeBar);
        }
        return this.mVideoInput;
    }

    public float getAspectRatio(IFitView IFitView1) {
        MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
        try {
            String scheme = this.mVideo.getScheme();
            if (scheme == null || (!scheme.equals("http") && !scheme.equals("https"))) {
                mediaMetadataRetriever.setDataSource(IFitView1.getContext(), this.mVideo);
            } else {
                mediaMetadataRetriever.setDataSource(this.mVideo.toString(), new HashMap());
            }
            String extractMetadata = mediaMetadataRetriever.extractMetadata(18);
            String extractMetadata2 = mediaMetadataRetriever.extractMetadata(19);
            if ((NumberUtil.parseInt(mediaMetadataRetriever.extractMetadata(24)) / 90) % 2 != 0) {
                return (((float) NumberUtil.parseInt(extractMetadata2)) * 1.0f) / ((float) NumberUtil.parseInt(extractMetadata));
            }
            float parseInt = (((float) NumberUtil.parseInt(extractMetadata)) * 1.0f) / ((float) NumberUtil.parseInt(extractMetadata2));
            mediaMetadataRetriever.release();
            return parseInt;
        } catch (Exception e) {
            e.printStackTrace();
            return 1.0f;
        } finally {
            mediaMetadataRetriever.release();
        }
    }

    public VideoBuilder addFilter(FilterRender fX_FilterRender) {
        return (VideoBuilder) super.addFilter(fX_FilterRender);
    }

    public <T extends FilterRender & IAdjustable> VideoBuilder addFilter(T t, float f) {
        return (VideoBuilder) super.addFilter(t, f);
    }

    public VideoBuilder enableRecord(String str, boolean z, boolean z2) {
        return (VideoBuilder) super.enableRecord(str, z, z2);
    }
}
