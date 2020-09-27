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
        mBufferInfo = new MediaCodec.BufferInfo();
        mExtractor = mediaExtractor;
        mTrackIndex = i;
        mOutputFormat = mediaFormat;
        mMuxer = queuedMuxer;
        if (iVideoRender != null) {
            mSurfaceRender = iVideoRender;
        } else {
            mSurfaceRender = new DefaultVideoRender();
        }
    }

    public void setup() throws IOException {
        mExtractor.selectTrack(mTrackIndex);
        mEncoder = MediaCodec.createEncoderByType(mOutputFormat.getString("mime"));
        mEncoder.configure(mOutputFormat, (Surface) null, (MediaCrypto) null, 1);
        mEncoderInputSurfaceWrapper = new InputSurface(mEncoder.createInputSurface());
        mEncoder.start();
        mEncoderStarted = true;
        MediaFormat trackFormat = mExtractor.getTrackFormat(mTrackIndex);
        mDecoderOutputSurfaceWrapper = new OutputSurface(mSurfaceRender);
        mDecoder = MediaCodec.createDecoderByType(trackFormat.getString("mime"));
        mDecoder.configure(trackFormat, mDecoderOutputSurfaceWrapper.getSurface(), (MediaCrypto) null, 0);
        mDecoder.start();
        mDecoderStarted = true;
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
        return mIsEncoderEOS;
    }

    public void release() {
        if (mDecoderOutputSurfaceWrapper != null) {
            mDecoderOutputSurfaceWrapper.release();
            mDecoderOutputSurfaceWrapper = null;
        }
        if (mEncoderInputSurfaceWrapper != null) {
            mEncoderInputSurfaceWrapper.release();
            mEncoderInputSurfaceWrapper = null;
        }
        if (mDecoder != null) {
            if (mDecoderStarted) {
                mDecoder.stop();
            }
            mDecoder.release();
            mDecoder = null;
        }
        if (mEncoder != null) {
            if (mEncoderStarted) {
                mEncoder.stop();
            }
            mEncoder.release();
            mEncoder = null;
        }
    }

    private int drainExtractor() {
        int dequeueInputBuffer;
        if (mIsExtractorEOS) {
            return 0;
        }
        int sampleTrackIndex = mExtractor.getSampleTrackIndex();
        if ((sampleTrackIndex >= 0 && sampleTrackIndex != mTrackIndex) || (dequeueInputBuffer = mDecoder.dequeueInputBuffer(0)) < 0) {
            return 0;
        }
        if (sampleTrackIndex < 0) {
            mIsExtractorEOS = true;
            mDecoder.queueInputBuffer(dequeueInputBuffer, 0, 0, 0, 4);
            return 0;
        }
        mDecoder.queueInputBuffer(dequeueInputBuffer, 0, mExtractor.readSampleData(CodecUtil.getInputBuffer(mDecoder, dequeueInputBuffer), 0), mExtractor.getSampleTime(), (mExtractor.getSampleFlags() & 1) != 0 ? 1 : 0);
        mExtractor.advance();
        return 2;
    }

    private int drainDecoder() {
        int i;
        boolean z = false;
        if (mIsDecoderEOS) {
            return 0;
        }
        try {
            i = mDecoder.dequeueOutputBuffer(mBufferInfo, 0);
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
                if ((mBufferInfo.flags & 4) != 0) {
                    mEncoder.signalEndOfInputStream();
                    mIsDecoderEOS = true;
                    mBufferInfo.size = 0;
                }
                if (mBufferInfo.size > 0) {
                    z = true;
                }
                mDecoder.releaseOutputBuffer(i, z);
                if (!z) {
                    return 2;
                }
                mDecoderOutputSurfaceWrapper.awaitNewImage();
                mDecoderOutputSurfaceWrapper.drawImage(mBufferInfo.presentationTimeUs * 1000);
                mEncoderInputSurfaceWrapper.setPresentationTime(mBufferInfo.presentationTimeUs * 1000);
                mEncoderInputSurfaceWrapper.swapBuffers();
                return 2;
        }
    }

    private int drainEncoder() {
        int i;
        if (mIsEncoderEOS) {
            return 0;
        }
        try {
            i = mEncoder.dequeueOutputBuffer(mBufferInfo, 0);
        } catch (Exception e) {
            e.printStackTrace();
            i = -1;
        }
        switch (i) {
            case -3:
                return 1;
            case -2:
                if (mActualOutputFormat == null) {
                    mActualOutputFormat = mEncoder.getOutputFormat();
                    mMuxer.setOutputFormat(QueuedMuxer.SampleType.VIDEO, mActualOutputFormat);
                    return 1;
                }
                throw new RuntimeException("Video output format changed twice.");
            case -1:
                return 0;
            default:
                if (mActualOutputFormat != null) {
                    if ((mBufferInfo.flags & 4) != 0) {
                        mIsEncoderEOS = true;
                        mBufferInfo.set(0, 0, 0, mBufferInfo.flags);
                    }
                    if ((mBufferInfo.flags & 2) != 0) {
                        mEncoder.releaseOutputBuffer(i, false);
                        return 1;
                    }
                    mMuxer.writeSampleData(QueuedMuxer.SampleType.VIDEO, CodecUtil.getOutputBuffer(mEncoder, i), mBufferInfo);
                    mEncoder.releaseOutputBuffer(i, false);
                    return 2;
                }
                throw new RuntimeException("Could not determine actual output format.");
        }
    }
}
