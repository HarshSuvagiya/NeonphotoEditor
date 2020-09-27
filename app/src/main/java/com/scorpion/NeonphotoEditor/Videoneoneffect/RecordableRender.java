package com.scorpion.NeonphotoEditor.Videoneoneffect;

import com.scorpion.NeonphotoEditor.Util.Helper;

import cn.ezandroid.ezfilter.media.record.IAudioExtraEncoder;
import cn.ezandroid.ezfilter.media.record.IRecordListener;
import cn.ezandroid.ezfilter.media.record.ISupportRecord;
import cn.ezandroid.ezfilter.media.record.MediaAudioEncoder;
import cn.ezandroid.ezfilter.media.record.MediaEncoder;
import cn.ezandroid.ezfilter.media.record.MediaMuxerWrapper;
import cn.ezandroid.ezfilter.media.record.MediaVideoEncoder;

public class RecordableRender extends FBORender implements ISupportRecord {
    private IAudioExtraEncoder mAudioExtraEncoder;
    private final MediaEncoder.MediaEncoderListener mMediaEncoderListener = new MediaEncoder.MediaEncoderListener() {
        public void onPrepared(MediaEncoder mediaEncoder) {
            if (mediaEncoder instanceof MediaVideoEncoder) {
                setVideoEncoder((MediaVideoEncoder) mediaEncoder);
            }
        }

        public void onStopped(MediaEncoder mediaEncoder) {
            if (mediaEncoder instanceof MediaVideoEncoder) {
                setVideoEncoder((MediaVideoEncoder) null);
            }
        }

        @Override
        public void onInterrupted(MediaEncoder encoder) {

        }
    };
    private MediaMuxerWrapper mMuxerWrapper;
    private String mOutputPath;
    private boolean mRecordAudio;
    private int mRecordHeight;
    private IRecordListener mRecordListener;
    private boolean mRecordVideo;
    private int mRecordWidth;
    private MediaVideoEncoder mVideoEncoder;

    public RecordableRender(String str, boolean z, boolean z2) {
        mOutputPath = str;
        mRecordVideo = z;
        mRecordAudio = z2;
    }

    public void onTextureAcceptable(int i, GLRender GLRender1) {
        super.onTextureAcceptable(i, GLRender1);
        try {
            if (mVideoEncoder != null && i != mVideoEncoder.getInputTextureId()) {
                mVideoEncoder.setInputTextureId(i);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setRecordListener(IRecordListener iRecordListener) {
        mRecordListener = iRecordListener;
    }

    public void setAudioExtraEncoder(IAudioExtraEncoder iAudioExtraEncoder) {
        mAudioExtraEncoder = iAudioExtraEncoder;
    }

    public void setVideoEncoder(MediaVideoEncoder mediaVideoEncoder) {
        mVideoEncoder = mediaVideoEncoder;
    }

    public void setRecordOutputPath(String str) {
        mOutputPath = str;
    }

    public void enableRecordAudio(boolean z) {
        mRecordAudio = z;
    }

    public void enableRecordVideo(boolean z) {
        mRecordVideo = z;
    }

    public void setRecordSize(int i, int i2) {
        mRecordWidth = i;
        mRecordHeight = i2;
    }

    public boolean isRecording() {
        return mMuxerWrapper != null && mMuxerWrapper.isStarted();
    }

    public boolean startRecording() {
        try {
            mMuxerWrapper = new MediaMuxerWrapper(mOutputPath);
            if (mRecordVideo) {
                new MediaVideoEncoder(mMuxerWrapper, mMediaEncoderListener, mRecordWidth <= 0 ? getWidth() : mRecordWidth, mRecordHeight <= 0 ? getHeight() : mRecordHeight);
            }
            if (mRecordAudio) {
                new MediaAudioEncoder(mMuxerWrapper, mMediaEncoderListener).setAudioExtraEncoder(mAudioExtraEncoder);
            }
            mMuxerWrapper.setRecordListener(mRecordListener);
            mMuxerWrapper.prepare();
            mMuxerWrapper.startRecording();
        } catch (Exception e) {
            e.printStackTrace();
            Helper.showLog("EEE", "startRecording : " + e.toString());
        }
        return false;
    }

    public void stopRecording() {
        if (mMuxerWrapper != null) {
            mMuxerWrapper.stopRecording();
            mMuxerWrapper = null;
        }
    }


    public void drawFrame() {
        super.drawFrame();
        try {
            if (mVideoEncoder != null) {
                mVideoEncoder.frameAvailableSoon();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
