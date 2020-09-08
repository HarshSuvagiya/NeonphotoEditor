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
        this.mExtractor = mediaExtractor;
        this.mTrackIndex = i;
        this.mOutputFormat = mediaFormat;
        this.mMuxer = queuedMuxer;
    }

    public void setup() throws IOException {
        this.mExtractor.selectTrack(this.mTrackIndex);
        this.mEncoder = MediaCodec.createEncoderByType(this.mOutputFormat.getString("mime"));
        this.mEncoder.configure(this.mOutputFormat, (Surface) null, (MediaCrypto) null, 1);
        this.mEncoder.start();
        this.mEncoderStarted = true;
        MediaFormat trackFormat = this.mExtractor.getTrackFormat(this.mTrackIndex);
        this.mDecoder = MediaCodec.createDecoderByType(trackFormat.getString("mime"));
        this.mDecoder.configure(trackFormat, (Surface) null, (MediaCrypto) null, 0);
        this.mDecoder.start();
        this.mDecoderStarted = true;
        this.mAudioChannel = new AudioChannel(this.mDecoder, this.mEncoder, this.mOutputFormat);
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
        while (this.mAudioChannel.feedEncoder(0)) {
            z = true;
        }
        while (drainExtractor(0) != 0) {
            z = true;
        }
        return z;
    }

    public boolean isFinished() {
        return this.mIsEncoderEOS;
    }

    public void release() {
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

    private int drainExtractor(long j) {
        int dequeueInputBuffer;
        if (this.mIsExtractorEOS) {
            return 0;
        }
        int sampleTrackIndex = this.mExtractor.getSampleTrackIndex();
        if ((sampleTrackIndex >= 0 && sampleTrackIndex != this.mTrackIndex) || (dequeueInputBuffer = this.mDecoder.dequeueInputBuffer(j)) < 0) {
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

    private int drainDecoder(long j) {
        if (this.mIsDecoderEOS) {
            return 0;
        }
        int dequeueOutputBuffer = this.mDecoder.dequeueOutputBuffer(this.mBufferInfo, j);
        switch (dequeueOutputBuffer) {
            case -3:
                break;
            case -2:
                this.mAudioChannel.setActualDecodedFormat(this.mDecoder.getOutputFormat());
                break;
            case -1:
                return 0;
            default:
                if ((this.mBufferInfo.flags & 4) != 0) {
                    this.mIsDecoderEOS = true;
                    this.mAudioChannel.drainDecoderBufferAndQueue(-1, 0);
                    return 2;
                } else if (this.mBufferInfo.size <= 0) {
                    return 2;
                } else {
                    this.mAudioChannel.drainDecoderBufferAndQueue(dequeueOutputBuffer, this.mBufferInfo.presentationTimeUs);
                    return 2;
                }
        }
        return 1;
    }

    private int drainEncoder(long j) {
        if (this.mIsEncoderEOS) {
            return 0;
        }
        int dequeueOutputBuffer = this.mEncoder.dequeueOutputBuffer(this.mBufferInfo, j);
        switch (dequeueOutputBuffer) {
            case -3:
                return 1;
            case -2:
                if (this.mActualOutputFormat == null) {
                    this.mActualOutputFormat = this.mEncoder.getOutputFormat();
                    this.mMuxer.setOutputFormat(QueuedMuxer.SampleType.AUDIO, this.mActualOutputFormat);
                    return 1;
                }
                throw new RuntimeException("Audio output format changed twice.");
            case -1:
                return 0;
            default:
                if (this.mActualOutputFormat != null) {
                    if ((this.mBufferInfo.flags & 4) != 0) {
                        this.mIsEncoderEOS = true;
                        this.mBufferInfo.set(0, 0, 0, this.mBufferInfo.flags);
                    }
                    if ((this.mBufferInfo.flags & 2) != 0) {
                        this.mEncoder.releaseOutputBuffer(dequeueOutputBuffer, false);
                        return 1;
                    }
                    this.mMuxer.writeSampleData(QueuedMuxer.SampleType.AUDIO, CodecUtil.getOutputBuffer(this.mEncoder, dequeueOutputBuffer), this.mBufferInfo);
                    this.mEncoder.releaseOutputBuffer(dequeueOutputBuffer, false);
                    return 2;
                }
                throw new RuntimeException("Could not determine actual output format.");
        }
    }
}
