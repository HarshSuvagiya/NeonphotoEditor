package com.scorpion.NeonphotoEditor.Videoneoneffect;

import android.media.MediaCodec;
import android.media.MediaFormat;
import cn.ezandroid.ezfilter.media.util.CodecUtil;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.ShortBuffer;
import java.util.ArrayDeque;
import java.util.Queue;

class AudioChannel {
    public static final int BUFFER_INDEX_END_OF_STREAM = -1;
    private static final int BYTES_PER_SHORT = 2;
    private static final long MICROSECS_PER_SEC = 1000000;
    private MediaFormat mActualDecodedFormat;
    private final MediaCodec mDecoder;
    private final Queue<AudioBuffer> mEmptyBuffers = new ArrayDeque();
    private final MediaFormat mEncodeFormat;
    private final MediaCodec mEncoder;
    private final Queue<AudioBuffer> mFilledBuffers = new ArrayDeque();
    private int mInputChannelCount;
    private int mInputSampleRate;
    private int mOutputChannelCount;
    private final AudioBuffer mOverflowBuffer = new AudioBuffer();
    private AudioRemixer mRemixer;

    private static class AudioBuffer {
        int bufferIndex;
        ShortBuffer data;
        long presentationTimeUs;

        private AudioBuffer() {
        }
    }

    public AudioChannel(MediaCodec mediaCodec, MediaCodec mediaCodec2, MediaFormat mediaFormat) {
        this.mDecoder = mediaCodec;
        this.mEncoder = mediaCodec2;
        this.mEncodeFormat = mediaFormat;
    }

    public void setActualDecodedFormat(MediaFormat mediaFormat) {
        this.mActualDecodedFormat = mediaFormat;
        this.mInputSampleRate = this.mActualDecodedFormat.getInteger("sample-rate");
        if (this.mInputSampleRate == this.mEncodeFormat.getInteger("sample-rate")) {
            this.mInputChannelCount = this.mActualDecodedFormat.getInteger("channel-count");
            this.mOutputChannelCount = this.mEncodeFormat.getInteger("channel-count");
            if (this.mInputChannelCount != 1 && this.mInputChannelCount != 2) {
                throw new UnsupportedOperationException("Input channel count (" + this.mInputChannelCount + ") not supported.");
            } else if (this.mOutputChannelCount == 1 || this.mOutputChannelCount == 2) {
                if (this.mInputChannelCount > this.mOutputChannelCount) {
                    this.mRemixer = AudioRemixer.DOWNMIX;
                } else if (this.mInputChannelCount < this.mOutputChannelCount) {
                    this.mRemixer = AudioRemixer.UPMIX;
                } else {
                    this.mRemixer = AudioRemixer.PASSTHROUGH;
                }
                this.mOverflowBuffer.presentationTimeUs = 0;
            } else {
                throw new UnsupportedOperationException("Output channel count (" + this.mOutputChannelCount + ") not supported.");
            }
        } else {
            throw new UnsupportedOperationException("Audio sample rate conversion not supported yet.");
        }
    }

    public void drainDecoderBufferAndQueue(int i, long j) {
        ByteBuffer byteBuffer;
        if (this.mActualDecodedFormat != null) {
            ShortBuffer shortBuffer = null;
            if (i == -1) {
                byteBuffer = null;
            } else {
                byteBuffer = CodecUtil.getOutputBuffer(this.mDecoder, i);
            }
            AudioBuffer poll = this.mEmptyBuffers.poll();
            if (poll == null) {
                poll = new AudioBuffer();
            }
            poll.bufferIndex = i;
            poll.presentationTimeUs = j;
            if (byteBuffer != null) {
                shortBuffer = byteBuffer.asShortBuffer();
            }
            poll.data = shortBuffer;
            if (this.mOverflowBuffer.data == null) {
                this.mOverflowBuffer.data = ByteBuffer.allocateDirect(byteBuffer.capacity()).order(ByteOrder.nativeOrder()).asShortBuffer();
                this.mOverflowBuffer.data.clear().flip();
            }
            this.mFilledBuffers.add(poll);
            return;
        }
        throw new RuntimeException("Buffer received before format!");
    }

    public boolean feedEncoder(long j) {
        int dequeueInputBuffer;
        boolean z = this.mOverflowBuffer.data != null && this.mOverflowBuffer.data.hasRemaining();
        if ((this.mFilledBuffers.isEmpty() && !z) || (dequeueInputBuffer = this.mEncoder.dequeueInputBuffer(j)) < 0) {
            return false;
        }
        ShortBuffer asShortBuffer = CodecUtil.getInputBuffer(this.mEncoder, dequeueInputBuffer).asShortBuffer();
        if (z) {
            this.mEncoder.queueInputBuffer(dequeueInputBuffer, 0, asShortBuffer.position() * 2, drainOverflow(asShortBuffer), 0);
            return true;
        }
        AudioBuffer poll = this.mFilledBuffers.poll();
        if (poll.bufferIndex == -1) {
            this.mEncoder.queueInputBuffer(dequeueInputBuffer, 0, 0, 0, 4);
            return false;
        }
        this.mEncoder.queueInputBuffer(dequeueInputBuffer, 0, asShortBuffer.position() * 2, remixAndMaybeFillOverflow(poll, asShortBuffer), 0);
        this.mDecoder.releaseOutputBuffer(poll.bufferIndex, false);
        this.mEmptyBuffers.add(poll);
        return true;
    }

    private static long sampleCountToDurationUs(int i, int i2, int i3) {
        return (((long) i) / (((long) i2) * MICROSECS_PER_SEC)) / ((long) i3);
    }

    private long drainOverflow(ShortBuffer shortBuffer) {
        ShortBuffer shortBuffer2 = this.mOverflowBuffer.data;
        int limit = shortBuffer2.limit();
        int remaining = shortBuffer2.remaining();
        long sampleCountToDurationUs = this.mOverflowBuffer.presentationTimeUs + sampleCountToDurationUs(shortBuffer2.position(), this.mInputSampleRate, this.mOutputChannelCount);
        shortBuffer.clear();
        shortBuffer2.limit(shortBuffer.capacity());
        shortBuffer.put(shortBuffer2);
        if (remaining >= shortBuffer.capacity()) {
            shortBuffer2.clear().limit(0);
        } else {
            shortBuffer2.limit(limit);
        }
        return sampleCountToDurationUs;
    }

    private long remixAndMaybeFillOverflow(AudioBuffer audioBuffer, ShortBuffer shortBuffer) {
        ShortBuffer shortBuffer2 = audioBuffer.data;
        ShortBuffer shortBuffer3 = this.mOverflowBuffer.data;
        shortBuffer.clear();
        shortBuffer2.clear();
        if (shortBuffer2.remaining() > shortBuffer.remaining()) {
            shortBuffer2.limit(shortBuffer.capacity());
            this.mRemixer.remix(shortBuffer2, shortBuffer);
            shortBuffer2.limit(shortBuffer2.capacity());
            long sampleCountToDurationUs = sampleCountToDurationUs(shortBuffer2.position(), this.mInputSampleRate, this.mInputChannelCount);
            this.mRemixer.remix(shortBuffer2, shortBuffer3);
            shortBuffer3.flip();
            this.mOverflowBuffer.presentationTimeUs = audioBuffer.presentationTimeUs + sampleCountToDurationUs;
        } else {
            this.mRemixer.remix(shortBuffer2, shortBuffer);
        }
        return audioBuffer.presentationTimeUs;
    }
}
