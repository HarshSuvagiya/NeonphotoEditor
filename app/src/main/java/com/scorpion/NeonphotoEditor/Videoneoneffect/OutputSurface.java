package com.scorpion.NeonphotoEditor.Videoneoneffect;

import android.graphics.SurfaceTexture;
import android.view.Surface;
import cn.ezandroid.ezfilter.media.transcode.IVideoRender;

public class OutputSurface implements SurfaceTexture.OnFrameAvailableListener {
    private boolean mFrameAvailable;
    private final Object mFrameSyncObject = new Object();
    private Surface mSurface;
    private SurfaceTexture mSurfaceTexture;
    private IVideoRender mTextureRender;

    public OutputSurface(IVideoRender iVideoRender) {
        this.mTextureRender = iVideoRender;
        setup();
    }

    private void setup() {
        this.mSurfaceTexture = this.mTextureRender.getSurfaceTexture();
        this.mSurfaceTexture.setOnFrameAvailableListener(this);
        this.mSurface = new Surface(this.mSurfaceTexture);
    }

    public void release() {
        this.mSurface.release();
        this.mTextureRender = null;
        this.mSurface = null;
        this.mSurfaceTexture = null;
    }

    public Surface getSurface() {
        return this.mSurface;
    }

    public void awaitNewImage() {
        synchronized (this.mFrameSyncObject) {
            while (!this.mFrameAvailable) {
                try {
                    this.mFrameSyncObject.wait(10000);
                    if (!this.mFrameAvailable) {
                        throw new RuntimeException("Surface frame wait timed out");
                    }
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            this.mFrameAvailable = false;
        }
        this.mSurfaceTexture.updateTexImage();
    }

    public void drawImage(long j) {
        this.mTextureRender.drawFrame(j);
    }

    public void onFrameAvailable(SurfaceTexture surfaceTexture) {
        synchronized (this.mFrameSyncObject) {
            if (!this.mFrameAvailable) {
                this.mFrameAvailable = true;
                this.mFrameSyncObject.notifyAll();
            } else {
                throw new RuntimeException("mFrameAvailable already set, frame could be dropped");
            }
        }
    }
}
