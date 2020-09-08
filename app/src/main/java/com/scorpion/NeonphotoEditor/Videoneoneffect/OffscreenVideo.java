package com.scorpion.NeonphotoEditor.Videoneoneffect;

import android.annotation.TargetApi;
import android.graphics.SurfaceTexture;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.media.MediaMetadataRetriever;
import android.media.MediaMuxer;
import cn.ezandroid.ezfilter.media.transcode.IVideoRender;
import cn.ezandroid.ezfilter.media.transcode.QueuedMuxer;
import java.io.IOException;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

@TargetApi(18)
public class OffscreenVideo {
    private MediaExtractor mExtractor;
    private int mHeight;
    /* access modifiers changed from: private */
    public VideoFBORender mOffscreenRender;
    private RenderPipeline mPipeline;
    private MediaUtil.Track mTrack;
    private String mVideoPath;
    /* access modifiers changed from: private */
    public IVideoRenderListener mVideoRenderListener;
    private int mWidth;

    public interface IVideoRenderListener {
        void onFrameDraw(long j);
    }

    public OffscreenVideo(String str) throws IOException {
        this.mVideoPath = str;
        initRenderSize();
    }

    private void initRenderSize() throws IOException {
        this.mExtractor = new MediaExtractor();
        this.mExtractor.setDataSource(this.mVideoPath);
        this.mTrack = MediaUtil.getFirstTrack(this.mExtractor);
        if (this.mTrack == null) {
            return;
        }
        if (this.mTrack.videoTrackFormat != null) {
            int integer = this.mTrack.videoTrackFormat.getInteger("width");
            int integer2 = this.mTrack.videoTrackFormat.getInteger("height");
            int i = 0;
            if (this.mTrack.videoTrackFormat.containsKey("rotation-degrees")) {
                i = this.mTrack.videoTrackFormat.getInteger("rotation-degrees");
            } else {
                MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
                try {
                    mediaMetadataRetriever.setDataSource(this.mVideoPath);
                    int parseInt = Integer.parseInt(mediaMetadataRetriever.extractMetadata(24));
                    mediaMetadataRetriever.release();
                    i = parseInt;
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                    mediaMetadataRetriever.release();
                    int i2 = integer ^ integer2;
                    integer2 ^= i2;
                    integer = i2 ^ integer2;
                    this.mWidth = integer;
                    this.mHeight = integer2;
                } catch (RuntimeException e2) {
                    try {
                        e2.printStackTrace();
                        mediaMetadataRetriever.release();
                        int i22 = integer ^ integer2;
                        integer2 ^= i22;
                        integer = i22 ^ integer2;
                        this.mWidth = integer;
                        this.mHeight = integer2;
                    } catch (Exception e3) {
                        e3.printStackTrace();
                        return;
                    } catch (Throwable th) {
                        mediaMetadataRetriever.release();
                        throw th;
                    }
                }
            }
            if (i == 90 || i == 270) {
                int i222 = integer ^ integer2;
                integer2 ^= i222;
                integer = i222 ^ integer2;
            }
            this.mWidth = integer;
            this.mHeight = integer2;
        }
    }

    private void initPipeline() {
        if (this.mPipeline == null) {
            this.mOffscreenRender = new VideoFBORender();
            this.mOffscreenRender.setRenderSize(this.mWidth, this.mHeight);
            this.mPipeline = new RenderPipeline();
            this.mPipeline.onSurfaceCreated((GL10) null, (EGLConfig) null);
            this.mPipeline.setStartPointRender(this.mOffscreenRender);
            this.mPipeline.addEndPointRender(new GLRender());
        }
    }

    public void setVideoRenderListener(IVideoRenderListener iVideoRenderListener) {
        this.mVideoRenderListener = iVideoRenderListener;
    }

    public void addFilterRender(FBORender fX_FBORender) {
        initPipeline();
        this.mPipeline.addFilterRender(fX_FBORender);
    }

    public int getWidth() {
        return this.mWidth;
    }

    public int getHeight() {
        return this.mHeight;
    }

    private int getInteger(String str, int i) {
        try {
            return this.mTrack.audioTrackFormat.getInteger(str);
        } catch (Exception unused) {
            return i;
        }
    }

    private VideoTrackTranscoder initVideoTrack(MediaFormat mediaFormat, QueuedMuxer queuedMuxer) {
        return new VideoTrackTranscoder(this.mExtractor, this.mTrack.videoTrackIndex, mediaFormat, queuedMuxer, new IVideoRender() {
            public void drawFrame(long j) {
                if (OffscreenVideo.this.mVideoRenderListener != null) {
                    OffscreenVideo.this.mVideoRenderListener.onFrameDraw(j);
                }
                OffscreenVideo.this.mOffscreenRender.drawFrame(j);
            }

            public SurfaceTexture getSurfaceTexture() {
                return OffscreenVideo.this.mOffscreenRender.getSurfaceTexture();
            }
        });
    }

    private AudioTrackTranscoder initAudioTrack(MediaFormat mediaFormat, QueuedMuxer queuedMuxer) {
        return new AudioTrackTranscoder(this.mExtractor, this.mTrack.audioTrackIndex, mediaFormat, queuedMuxer);
    }

    public void save(String str) throws IOException {
        save(str, this.mWidth, this.mHeight);
    }

    public void save(String str, int i, int i2) throws IOException {
        if (this.mTrack != null && this.mTrack.videoTrackFormat != null) {
            initPipeline();
            this.mPipeline.onSurfaceChanged((GL10) null, i, i2);
            this.mPipeline.startRender();
            MediaFormat createVideoFormat = MediaUtil.createVideoFormat(i, i2, MediaUtil.getMetadata(this.mVideoPath).bitrate, 2130708361);
            MediaMuxer mediaMuxer = new MediaMuxer(str, 0);
            QueuedMuxer queuedMuxer = new QueuedMuxer(mediaMuxer);
            if (this.mTrack.audioTrackFormat != null) {
                MediaFormat createAudioFormat = MediaUtil.createAudioFormat(getInteger("sample-rate", 44100), getInteger("channel-mask", 12), getInteger("channel-count", 2));
                queuedMuxer.setTrackCount(0);
                VideoTrackTranscoder initVideoTrack = initVideoTrack(createVideoFormat, queuedMuxer);
                AudioTrackTranscoder initAudioTrack = initAudioTrack(createAudioFormat, queuedMuxer);
                initVideoTrack.setup();
                initAudioTrack.setup();
                while (true) {
                    if (initVideoTrack.isFinished() && initAudioTrack.isFinished()) {
                        break;
                    }
                    if (!(initVideoTrack.stepPipeline() || initAudioTrack.stepPipeline())) {
                        try {
                            Thread.sleep(10);
                        } catch (InterruptedException unused) {
                        }
                    }
                }
                this.mPipeline.onSurfaceDestroyed();
                initVideoTrack.release();
                initAudioTrack.release();
            } else {
                queuedMuxer.setTrackCount(1);
                VideoTrackTranscoder initVideoTrack2 = initVideoTrack(createVideoFormat, queuedMuxer);
                initVideoTrack2.setup();
                while (!initVideoTrack2.isFinished()) {
                    if (!initVideoTrack2.stepPipeline()) {
                        try {
                            Thread.sleep(10);
                        } catch (InterruptedException unused2) {
                        }
                    }
                }
                this.mPipeline.onSurfaceDestroyed();
                initVideoTrack2.release();
            }
            mediaMuxer.stop();
            mediaMuxer.release();
            this.mExtractor.release();
        }
    }
}
