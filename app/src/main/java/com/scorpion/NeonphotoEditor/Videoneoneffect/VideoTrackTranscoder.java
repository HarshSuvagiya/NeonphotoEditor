package com.scorpion.NeonphotoEditor.Videoneoneffect;

import android.media.MediaCodec;
import android.media.MediaCrypto;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.view.Surface;
import cn.ezandroid.ezfilter.media.transcode.IVideoRender;
import cn.ezandroid.ezfilter.media.transcode.QueuedMuxer;
import cn.ezandroid.ezfilter.media.transcode.TrackTranscoder;
import cn.ezandroid.ezfilter.media.util.CodecUtil;
import java.io.IOException;

public class VideoTrackTranscoder implements TrackTranscoder {
    private static final int DRAIN_STATE_CONSUMED = 2;
    private static final int DRAIN_STATE_NONE = 0;
    private static final int DRAIN_STATE_SHOULD_RETRY_IMMEDIATELY = 1;
    private MediaFormat mActualOutputFormat;
    private final MediaCodec.BufferInfo mBufferInfo;
    private MediaCodec mDecoder;
    private OutputSurface mDecoderOutputSurfaceWrapper;
    private boolean mDecoderStarted;
    private MediaCodec mEncoder;
    private InputSurface mEncoderInputSurfaceWrapper;
    private boolean mEncoderStarted;
    private final MediaExtractor mExtractor;
    private boolean mIsDecoderEOS;
    private boolean mIsEncoderEOS;
    private boolean mIsExtractorEOS;
    private final QueuedMuxer mMuxer;
    private final MediaFormat mOutputFormat;
    private IVideoRender mSurfaceRender;
    private final int mTrackIndex;

    public VideoTrackTranscoder(MediaExtractor mediaExtractor, int i, MediaFormat mediaFormat, QueuedMuxer queuedMuxer) {
        this(mediaExtractor, i, mediaFormat, queuedMuxer, (IVideoRender) null);
    }

    public VideoTrackTranscoder(MediaExtractor mediaExtractor, int i, MediaFormat mediaFormat, QueuedMuxer queuedMuxer, IVideoRender iVideoRender) {
        this.mBufferInfo = new MediaCodec.BufferInfo();
        this.mExtractor = mediaExtractor;
        this.mTrackIndex = i;
        this.mOutputFormat = mediaFormat;
        this.mMuxer = queuedMuxer;
        if (iVideoRender != null) {
            this.mSurfaceRender = iVideoRender;
        } else {
            this.mSurfaceRender = new DefaultVideoRender();
        }
    }

    public void setup() throws IOException {
        this.mExtractor.selectTrack(this.mTrackIndex);
        this.mEncoder = MediaCodec.createEncoderByType(this.mOutputFormat.getString("mime"));
        this.mEncoder.configure(this.mOutputFormat, (Surface) null, (MediaCrypto) null, 1);
        this.mEncoderInputSurfaceWrapper = new InputSurface(this.mEncoder.createInputSurface());
        this.mEncoder.start();
        this.mEncoderStarted = true;
        MediaFormat trackFormat = this.mExtractor.getTrackFormat(this.mTrackIndex);
        this.mDecoderOutputSurfaceWrapper = new OutputSurface(this.mSurfaceRender);
        this.mDecoder = MediaCodec.createDecoderByType(trackFormat.getString("mime"));
        this.mDecoder.configure(trackFormat, this.mDecoderOutputSurfaceWrapper.getSurface(), (MediaCrypto) null, 0);
        this.mDecoder.start();
        this.mDecoderStarted = true;
    }

    public boolean stepPipeline() {
        int drainDecoder;
        boolean z = false;
        while (drainEncoder() != 0) {
            z = true;
        }
        do {
            drainDecoder = drainDecoder();
            if (drainDecoder != 0) {
                z = true;
                continue;
            }
        } while (drainDecoder == 1);
        while (drainExtractor() != 0) {
            z = true;
        }
        return z;
    }

    public boolean isFinished() {
        return this.mIsEncoderEOS;
    }

    public void release() {
        if (this.mDecoderOutputSurfaceWrapper != null) {
            this.mDecoderOutputSurfaceWrapper.release();
            this.mDecoderOutputSurfaceWrapper = null;
        }
        if (this.mEncoderInputSurfaceWrapper != null) {
            this.mEncoderInputSurfaceWrapper.release();
            this.mEncoderInputSurfaceWrapper = null;
        }
        if (this.mDecoder != null) {
            if (this.mDecoderStarted) {
                this.mDecoder.stop();
            }
            this.mDecoder.release();
            this.mDecoder = null;
        }
        if (this.mEncoder != null) {
            if (this.mEncoderStarted) {
                this.mEncoder.stop();
            }
            this.mEncoder.release();
            this.mEncoder = null;
        }
    }

    private int drainExtractor() {
        int dequeueInputBuffer;
        if (this.mIsExtractorEOS) {
            return 0;
        }
        int sampleTrackIndex = this.mExtractor.getSampleTrackIndex();
        if ((sampleTrackIndex >= 0 && sampleTrackIndex != this.mTrackIndex) || (dequeueInputBuffer = this.mDecoder.dequeueInputBuffer(0)) < 0) {
            return 0;
        }
        if (sampleTrackIndex < 0) {
            this.mIsExtractorEOS = true;
            this.mDecoder.queueInputBuffer(dequeueInputBuffer, 0, 0, 0, 4);
            return 0;
        }
        this.mDecoder.queueInputBuffer(dequeueInputBuffer, 0, this.mExtractor.readSampleData(CodecUtil.getInputBuffer(this.mDecoder, dequeueInputBuffer), 0), this.mExtractor.getSampleTime(), (this.mExtractor.getSampleFlags() & 1) != 0 ? 1 : 0);
        this.mExtractor.advance();
        return 2;
    }

    private int drainDecoder() {
        int i;
        boolean z = false;
        if (this.mIsDecoderEOS) {
            return 0;
        }
        try {
            i = this.mDecoder.dequeueOutputBuffer(this.mBufferInfo, 0);
        } catch (Exception e) {
            e.printStackTrace();
            i = -1;
        }
        switch (i) {
            case -3:
            case -2:
                return 1;
            case -1:
                return 0;
            default:
                if ((this.mBufferInfo.flags & 4) != 0) {
                    this.mEncoder.signalEndOfInputStream();
                    this.mIsDecoderEOS = true;
                    this.mBufferInfo.size = 0;
                }
                if (this.mBufferInfo.size > 0) {
                    z = true;
                }
                this.mDecoder.releaseOutputBuffer(i, z);
                if (!z) {
                    return 2;
                }
                this.mDecoderOutputSurfaceWrapper.awaitNewImage();
                this.mDecoderOutputSurfaceWrapper.drawImage(this.mBufferInfo.presentationTimeUs * 1000);
                this.mEncoderInputSurfaceWrapper.setPresentationTime(this.mBufferInfo.presentationTimeUs * 1000);
                this.mEncoderInputSurfaceWrapper.swapBuffers();
                return 2;
        }
    }

    private int drainEncoder() {
        int i;
        if (this.mIsEncoderEOS) {
            return 0;
        }
        try {
            i = this.mEncoder.dequeueOutputBuffer(this.mBufferInfo, 0);
        } catch (Exception e) {
            e.printStackTrace();
            i = -1;
        }
        switch (i) {
            case -3:
                return 1;
            case -2:
                if (this.mActualOutputFormat == null) {
                    this.mActualOutputFormat = this.mEncoder.getOutputFormat();
                    this.mMuxer.setOutputFormat(QueuedMuxer.SampleType.VIDEO, this.mActualOutputFormat);
                    return 1;
                }
                throw new RuntimeException("Video output format changed twice.");
            case -1:
                return 0;
            default:
                if (this.mActualOutputFormat != null) {
                    if ((this.mBufferInfo.flags & 4) != 0) {
                        this.mIsEncoderEOS = true;
                        this.mBufferInfo.set(0, 0, 0, this.mBufferInfo.flags);
                    }
                    if ((this.mBufferInfo.flags & 2) != 0) {
                        this.mEncoder.releaseOutputBuffer(i, false);
                        return 1;
                    }
                    this.mMuxer.writeSampleData(QueuedMuxer.SampleType.VIDEO, CodecUtil.getOutputBuffer(this.mEncoder, i), this.mBufferInfo);
                    this.mEncoder.releaseOutputBuffer(i, false);
                    return 2;
                }
                throw new RuntimeException("Could not determine actual output format.");
        }
    }
}
