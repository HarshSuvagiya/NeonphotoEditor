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
        mDecoder = mediaCodec;
        mEncoder = mediaCodec2;
        mEncodeFormat = mediaFormat;
    }

    public void setActualDecodedFormat(MediaFormat mediaFormat) {
        mActualDecodedFormat = mediaFormat;
        mInputSampleRate = mActualDecodedFormat.getInteger("sample-rate");
        if (mInputSampleRate == mEncodeFormat.getInteger("sample-rate")) {
            mInputChannelCount = mActualDecodedFormat.getInteger("channel-count");
            mOutputChannelCount = mEncodeFormat.getInteger("channel-count");
            if (mInputChannelCount != 1 && mInputChannelCount != 2) {
                throw new UnsupportedOperationException("Input channel count (" + mInputChannelCount + ") not supported.");
            } else if (mOutputChannelCount == 1 || mOutputChannelCount == 2) {
                if (mInputChannelCount > mOutputChannelCount) {
                    mRemixer = AudioRemixer.DOWNMIX;
                } else if (mInputChannelCount < mOutputChannelCount) {
                    mRemixer = AudioRemixer.UPMIX;
                } else {
                    mRemixer = AudioRemixer.PASSTHROUGH;
                }
                mOverflowBuffer.presentationTimeUs = 0;
            } else {
                throw new UnsupportedOperationException("Output channel count (" + mOutputChannelCount + ") not supported.");
            }
        } else {
            throw new UnsupportedOperationException("Audio sample rate conversion not supported yet.");
        }
    }

    public void drainDecoderBufferAndQueue(int i, long j) {
        ByteBuffer byteBuffer;
        if (mActualDecodedFormat != null) {
            ShortBuffer shortBuffer = null;
            if (i == -1) {
                byteBuffer = null;
            } else {
                byteBuffer = CodecUtil.getOutputBuffer(mDecoder, i);
            }
            AudioBuffer poll = mEmptyBuffers.poll();
            if (poll == null) {
                poll = new AudioBuffer();
            }
            poll.bufferIndex = i;
            poll.presentationTimeUs = j;
            if (byteBuffer != null) {
                shortBuffer = byteBuffer.asShortBuffer();
            }
            poll.data = shortBuffer;
            if (mOverflowBuffer.data == null) {
                mOverflowBuffer.data = ByteBuffer.allocateDirect(byteBuffer.capacity()).order(ByteOrder.nativeOrder()).asShortBuffer();
                mOverflowBuffer.data.clear().flip();
            }
            mFilledBuffers.add(poll);
            return;
        }
        throw new RuntimeException("Buffer received before format!");
    }

    public boolean feedEncoder(long j) {
        int dequeueInputBuffer;
        boolean z = mOverflowBuffer.data != null && mOverflowBuffer.data.hasRemaining();
        if ((mFilledBuffers.isEmpty() && !z) || (dequeueInputBuffer = mEncoder.dequeueInputBuffer(j)) < 0) {
            return false;
        }
        ShortBuffer asShortBuffer = CodecUtil.getInputBuffer(mEncoder, dequeueInputBuffer).asShortBuffer();
        if (z) {
            mEncoder.queueInputBuffer(dequeueInputBuffer, 0, asShortBuffer.position() * 2, drainOverflow(asShortBuffer), 0);
            return true;
        }
        AudioBuffer poll = mFilledBuffers.poll();
        if (poll.bufferIndex == -1) {
            mEncoder.queueInputBuffer(dequeueInputBuffer, 0, 0, 0, 4);
            return false;
        }
        mEncoder.queueInputBuffer(dequeueInputBuffer, 0, asShortBuffer.position() * 2, remixAndMaybeFillOverflow(poll, asShortBuffer), 0);
        mDecoder.releaseOutputBuffer(poll.bufferIndex, false);
        mEmptyBuffers.add(poll);
        return true;
    }

    private static long sampleCountToDurationUs(int i, int i2, int i3) {
        return (((long) i) / (((long) i2) * MICROSECS_PER_SEC)) / ((long) i3);
    }

    private long drainOverflow(ShortBuffer shortBuffer) {
        ShortBuffer shortBuffer2 = mOverflowBuffer.data;
        int limit = shortBuffer2.limit();
        int remaining = shortBuffer2.remaining();
        long sampleCountToDurationUs = mOverflowBuffer.presentationTimeUs + sampleCountToDurationUs(shortBuffer2.position(), mInputSampleRate, mOutputChannelCount);
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
        ShortBuffer shortBuffer3 = mOverflowBuffer.data;
        shortBuffer.clear();
        shortBuffer2.clear();
        if (shortBuffer2.remaining() > shortBuffer.remaining()) {
            shortBuffer2.limit(shortBuffer.capacity());
            mRemixer.remix(shortBuffer2, shortBuffer);
            shortBuffer2.limit(shortBuffer2.capacity());
            long sampleCountToDurationUs = sampleCountToDurationUs(shortBuffer2.position(), mInputSampleRate, mInputChannelCount);
            mRemixer.remix(shortBuffer2, shortBuffer3);
            shortBuffer3.flip();
            mOverflowBuffer.presentationTimeUs = audioBuffer.presentationTimeUs + sampleCountToDurationUs;
        } else {
            mRemixer.remix(shortBuffer2, shortBuffer);
        }
        return audioBuffer.presentationTimeUs;
    }
}
