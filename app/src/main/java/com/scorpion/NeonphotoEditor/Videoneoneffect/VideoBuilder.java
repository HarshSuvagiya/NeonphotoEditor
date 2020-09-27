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
        mVideo = uri;
    }

    public VideoBuilder setMediaPlayer(IMediaPlayer IMediaPlayer1) {
        mMediaPlayer = IMediaPlayer1;
        return this;
    }

    public VideoBuilder setLoop(boolean z) {
        mVideoLoop = z;
        return this;
    }

    public VideoBuilder setVolume(float f) {
        mVideoVolume = f;
        return this;
    }

    public VideoBuilder setStartWhenReady(boolean z) {
        mStartWhenReady = z;
        return this;
    }

    public VideoBuilder setPreparedListener(IMediaPlayer.OnPreparedListener onPreparedListener) {
        mPreparedListener = onPreparedListener;
        return this;
    }

    public VideoBuilder setCompletionListener(IMediaPlayer.OnCompletionListener onCompletionListener) {
        mCompletionListener = onCompletionListener;
        return this;
    }

    public VideoBuilder setErrorListener(IMediaPlayer.OnErrorListener onErrorListener) {
        mErrorListener = onErrorListener;
        return this;
    }

    public VideoBuilder setVideoEffectTimeBar(VideoEffectTimeBar VideoEffectTimeBar1) {
        videoEffectTimeBar = VideoEffectTimeBar1;
        return this;
    }

    public void output(String str) {
        try {
            new OffscreenVideo(mVideo.getPath()).save(str);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void output(String str, int i, int i2) throws IOException {
        OffscreenVideo OffscreenVideo1 = new OffscreenVideo(mVideo.getPath());
        try {
            for (FilterRender addFilterRender : mFilterRenders) {
                OffscreenVideo1.addFilterRender(addFilterRender);
            }
            OffscreenVideo1.save(str, i, i2);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public FBORender getStartPointRender(IFitView IFitView1) {
        if (mVideoInput == null) {
            mVideoInput = new VideoInput(IFitView1.getContext(), IFitView1, mVideo, mMediaPlayer);
            mVideoInput.setStartWhenReady(mStartWhenReady);
            mVideoInput.setLoop(mVideoLoop);
            mVideoInput.setVolume(mVideoVolume, mVideoVolume);
            mVideoInput.setOnPreparedListener(mPreparedListener);
            mVideoInput.setOnCompletionListener(mCompletionListener);
            mVideoInput.setOnErrorListener(mErrorListener);
            mVideoInput.setVideoEffectTimeBar(videoEffectTimeBar);
        }
        return mVideoInput;
    }

    public float getAspectRatio(IFitView IFitView1) {
        MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
        try {
            String scheme = mVideo.getScheme();
            if (scheme == null || (!scheme.equals("http") && !scheme.equals("https"))) {
                mediaMetadataRetriever.setDataSource(IFitView1.getContext(), mVideo);
            } else {
                mediaMetadataRetriever.setDataSource(mVideo.toString(), new HashMap());
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
