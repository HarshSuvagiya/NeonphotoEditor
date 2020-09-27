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
        mTextureRender = iVideoRender;
        setup();
    }

    private void setup() {
        mSurfaceTexture = mTextureRender.getSurfaceTexture();
        mSurfaceTexture.setOnFrameAvailableListener(this);
        mSurface = new Surface(mSurfaceTexture);
    }

    public void release() {
        mSurface.release();
        mTextureRender = null;
        mSurface = null;
        mSurfaceTexture = null;
    }

    public Surface getSurface() {
        return mSurface;
    }

    public void awaitNewImage() {
        synchronized (mFrameSyncObject) {
            while (!mFrameAvailable) {
                try {
                    mFrameSyncObject.wait(10000);
                    if (!mFrameAvailable) {
                        throw new RuntimeException("Surface frame wait timed out");
                    }
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            mFrameAvailable = false;
        }
        mSurfaceTexture.updateTexImage();
    }

    public void drawImage(long j) {
        mTextureRender.drawFrame(j);
    }

    public void onFrameAvailable(SurfaceTexture surfaceTexture) {
        synchronized (mFrameSyncObject) {
            if (!mFrameAvailable) {
                mFrameAvailable = true;
                mFrameSyncObject.notifyAll();
            } else {
                throw new RuntimeException("mFrameAvailable already set, frame could be dropped");
            }
        }
    }
}
