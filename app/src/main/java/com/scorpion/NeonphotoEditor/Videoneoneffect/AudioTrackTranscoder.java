package com.scorpion.NeonphotoEditor.Videoneoneffect;

import android.media.MediaCodec;
import android.media.MediaCrypto;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.view.Surface;
import cn.ezandroid.ezfilter.media.transcode.QueuedMuxer;
import cn.ezandroid.ezfilter.media.transcode.TrackTranscoder;
import cn.ezandroid.ezfilter.media.util.CodecUtil;
import java.io.IOException;

public class AudioTrackTranscoder implements TrackTranscoder {
    private static final int DRAIN_STATE_CONSUMED = 2;
    private static final int DRAIN_STATE_NONE = 0;
    private static final int DRAIN_STATE_SHOULD_RETRY_IMMEDIATELY = 1;
    private MediaFormat mActualOutputFormat;
    private AudioChannel mAudioChannel;
    private final MediaCodec.BufferInfo mBufferInfo = new MediaCodec.BufferInfo();
    private MediaCodec mDecoder;
    private boolean mDecoderStarted;
    private MediaCodec mEncoder;
    private boolean mEncoderStarted;
    private final MediaExtractor mExtractor;
    private boolean mIsDecoderEOS;
    private boolean mIsEncoderEOS;
    private boolean mIsExtractorEOS;
    private final QueuedMuxer mMuxer;
    private final MediaFormat mOutputFormat;
    private final int mTrackIndex;

    public AudioTrackTranscoder(MediaExtractor mediaExtractor, int i, MediaFormat mediaFormat, QueuedMuxer queuedMuxer) {
        mExtractor = mediaExtractor;
        mTrackIndex = i;
        mOutputFormat = mediaFormat;
        mMuxer = queuedMuxer;
    }

    public void setup() throws IOException {
        mExtractor.selectTrack(mTrackIndex);
        mEncoder = MediaCodec.createEncoderByType(mOutputFormat.getString("mime"));
        mEncoder.configure(mOutputFormat, (Surface) null, (MediaCrypto) null, 1);
        mEncoder.start();
        mEncoderStarted = true;
        MediaFormat trackFormat = mExtractor.getTrackFormat(mTrackIndex);
        mDecoder = MediaCodec.createDecoderByType(trackFormat.getString("mime"));
        mDecoder.configure(trackFormat, (Surface) null, (MediaCrypto) null, 0);
        mDecoder.start();
        mDecoderStarted = true;
        mAudioChannel = new AudioChannel(mDecoder, mEncoder, mOutputFormat);
    }

    public boolean stepPipeline() {
        int drainDecoder;
        boolean z = false;
        while (drainEncoder(0) != 0) {
            z = true;
        }
        do {
            drainDecoder = drainDecoder(0);
            if (drainDecoder != 0) {
                z = true;
                continue;
            }
        } while (drainDecoder == 1);
        while (mAudioChannel.feedEncoder(0)) {
            z = true;
        }
        while (drainExtractor(0) != 0) {
            z = true;
        }
        return z;
    }

    public boolean isFinished() {
        return mIsEncoderEOS;
    }

    public void release() {
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

    private int drainExtractor(long j) {
        int dequeueInputBuffer;
        if (mIsExtractorEOS) {
            return 0;
        }
        int sampleTrackIndex = mExtractor.getSampleTrackIndex();
        if ((sampleTrackIndex >= 0 && sampleTrackIndex != mTrackIndex) || (dequeueInputBuffer = mDecoder.dequeueInputBuffer(j)) < 0) {
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

    private int drainDecoder(long j) {
        if (mIsDecoderEOS) {
            return 0;
        }
        int dequeueOutputBuffer = mDecoder.dequeueOutputBuffer(mBufferInfo, j);
        switch (dequeueOutputBuffer) {
            case -3:
                break;
            case -2:
                mAudioChannel.setActualDecodedFormat(mDecoder.getOutputFormat());
                break;
            case -1:
                return 0;
            default:
                if ((mBufferInfo.flags & 4) != 0) {
                    mIsDecoderEOS = true;
                    mAudioChannel.drainDecoderBufferAndQueue(-1, 0);
                    return 2;
                } else if (mBufferInfo.size <= 0) {
                    return 2;
                } else {
                    mAudioChannel.drainDecoderBufferAndQueue(dequeueOutputBuffer, mBufferInfo.presentationTimeUs);
                    return 2;
                }
        }
        return 1;
    }

    private int drainEncoder(long j) {
        if (mIsEncoderEOS) {
            return 0;
        }
        int dequeueOutputBuffer = mEncoder.dequeueOutputBuffer(mBufferInfo, j);
        switch (dequeueOutputBuffer) {
            case -3:
                return 1;
            case -2:
                if (mActualOutputFormat == null) {
                    mActualOutputFormat = mEncoder.getOutputFormat();
                    mMuxer.setOutputFormat(QueuedMuxer.SampleType.AUDIO, mActualOutputFormat);
                    return 1;
                }
                throw new RuntimeException("Audio output format changed twice.");
            case -1:
                return 0;
            default:
                if (mActualOutputFormat != null) {
                    if ((mBufferInfo.flags & 4) != 0) {
                        mIsEncoderEOS = true;
                        mBufferInfo.set(0, 0, 0, mBufferInfo.flags);
                    }
                    if ((mBufferInfo.flags & 2) != 0) {
                        mEncoder.releaseOutputBuffer(dequeueOutputBuffer, false);
                        return 1;
                    }
                    mMuxer.writeSampleData(QueuedMuxer.SampleType.AUDIO, CodecUtil.getOutputBuffer(mEncoder, dequeueOutputBuffer), mBufferInfo);
                    mEncoder.releaseOutputBuffer(dequeueOutputBuffer, false);
                    return 2;
                }
                throw new RuntimeException("Could not determine actual output format.");
        }
    }
}
