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
                RecordableRender.this.setVideoEncoder((MediaVideoEncoder) mediaEncoder);
            }
        }

        public void onStopped(MediaEncoder mediaEncoder) {
            if (mediaEncoder instanceof MediaVideoEncoder) {
                RecordableRender.this.setVideoEncoder((MediaVideoEncoder) null);
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
        this.mOutputPath = str;
        this.mRecordVideo = z;
        this.mRecordAudio = z2;
    }

    public void onTextureAcceptable(int i, GLRender GLRender1) {
        super.onTextureAcceptable(i, GLRender1);
        try {
            if (this.mVideoEncoder != null && i != this.mVideoEncoder.getInputTextureId()) {
                this.mVideoEncoder.setInputTextureId(i);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setRecordListener(IRecordListener iRecordListener) {
        this.mRecordListener = iRecordListener;
    }

    public void setAudioExtraEncoder(IAudioExtraEncoder iAudioExtraEncoder) {
        this.mAudioExtraEncoder = iAudioExtraEncoder;
    }

    public void setVideoEncoder(MediaVideoEncoder mediaVideoEncoder) {
        this.mVideoEncoder = mediaVideoEncoder;
    }

    public void setRecordOutputPath(String str) {
        this.mOutputPath = str;
    }

    public void enableRecordAudio(boolean z) {
        this.mRecordAudio = z;
    }

    public void enableRecordVideo(boolean z) {
        this.mRecordVideo = z;
    }

    public void setRecordSize(int i, int i2) {
        this.mRecordWidth = i;
        this.mRecordHeight = i2;
    }

    public boolean isRecording() {
        return this.mMuxerWrapper != null && this.mMuxerWrapper.isStarted();
    }

    public boolean startRecording() {
        try {
            this.mMuxerWrapper = new MediaMuxerWrapper(this.mOutputPath);
            if (this.mRecordVideo) {
                new MediaVideoEncoder(this.mMuxerWrapper, this.mMediaEncoderListener, this.mRecordWidth <= 0 ? getWidth() : this.mRecordWidth, this.mRecordHeight <= 0 ? getHeight() : this.mRecordHeight);
            }
            if (this.mRecordAudio) {
                new MediaAudioEncoder(this.mMuxerWrapper, this.mMediaEncoderListener).setAudioExtraEncoder(this.mAudioExtraEncoder);
            }
            this.mMuxerWrapper.setRecordListener(this.mRecordListener);
            this.mMuxerWrapper.prepare();
            this.mMuxerWrapper.startRecording();
        } catch (Exception e) {
            e.printStackTrace();
            Helper.showLog("EEE", "startRecording : " + e.toString());
        }
        return false;
    }

    public void stopRecording() {
        if (this.mMuxerWrapper != null) {
            this.mMuxerWrapper.stopRecording();
            this.mMuxerWrapper = null;
        }
    }


    public void drawFrame() {
        super.drawFrame();
        try {
            if (this.mVideoEncoder != null) {
                this.mVideoEncoder.frameAvailableSoon();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
