package com.scorpion.NeonphotoEditor.Videoneoneffect;

import android.opengl.GLES20;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.List;

public class FBORender extends GLRender {
    protected int[] mDepthRenderBuffer;
    protected int[] mFrameBuffer;
    protected final List<OnTextureAcceptableListener> mTargets = new ArrayList();
    protected int[] mTextureOut;

    public void destroy() {
        super.destroy();
        if (this.mFrameBuffer != null) {
            GLES20.glDeleteFramebuffers(1, this.mFrameBuffer, 0);
            this.mFrameBuffer = null;
        }
        if (this.mDepthRenderBuffer != null) {
            GLES20.glDeleteRenderbuffers(1, this.mDepthRenderBuffer, 0);
            this.mDepthRenderBuffer = null;
        }
        if (this.mTextureOut != null) {
            GLES20.glDeleteTextures(1, this.mTextureOut, 0);
            this.mTextureOut = null;
        }
    }

    public String toString() {
        return super.toString() + " Targets:" + this.mTargets.size();
    }

    /* access modifiers changed from: protected */
    public void drawFrame() {
        if (this.mTextureOut == null) {
            if (this.mWidth != 0 && this.mHeight != 0) {
                initFBO();
            } else {
                return;
            }
        }
        GLES20.glBindFramebuffer(36160, this.mFrameBuffer[0]);
        onDraw();
        GLES20.glBindFramebuffer(36160, 0);
        synchronized (this.mTargets) {
            for (OnTextureAcceptableListener next : this.mTargets) {
                if (!(next == null || this.mTextureOut == null || this.mTextureOut.length <= 0)) {
                    next.onTextureAcceptable(this.mTextureOut[0], this);
                }
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onDraw() {
        super.drawFrame();
    }

    /* access modifiers changed from: protected */
    public void onRenderSizeChanged() {
        initFBO();
    }

    private void initFBO() {
        if (this.mTextureOut != null) {
            GLES20.glDeleteTextures(1, this.mTextureOut, 0);
            this.mTextureOut = null;
        }
        this.mTextureOut = new int[1];
        GLES20.glGenTextures(1, this.mTextureOut, 0);
        GLES20.glActiveTexture(33984);
        GLES20.glBindTexture(3553, this.mTextureOut[0]);
        GLES20.glTexParameteri(3553, 10240, 9729);
        GLES20.glTexParameteri(3553, 10241, 9729);
        GLES20.glTexParameteri(3553, 10242, 33071);
        GLES20.glTexParameteri(3553, 10243, 33071);
        GLES20.glTexImage2D(3553, 0, 6408, getWidth(), getHeight(), 0, 6408, 5121, (Buffer) null);
        if (this.mFrameBuffer != null) {
            GLES20.glDeleteFramebuffers(1, this.mFrameBuffer, 0);
            this.mFrameBuffer = null;
        }
        if (this.mDepthRenderBuffer != null) {
            GLES20.glDeleteRenderbuffers(1, this.mDepthRenderBuffer, 0);
            this.mDepthRenderBuffer = null;
        }
        this.mFrameBuffer = new int[1];
        this.mDepthRenderBuffer = new int[1];
        GLES20.glGenFramebuffers(1, this.mFrameBuffer, 0);
        GLES20.glGenRenderbuffers(1, this.mDepthRenderBuffer, 0);
        GLES20.glBindFramebuffer(36160, this.mFrameBuffer[0]);
        GLES20.glFramebufferTexture2D(36160, 36064, 3553, this.mTextureOut[0], 0);
        GLES20.glBindRenderbuffer(36161, this.mDepthRenderBuffer[0]);
        GLES20.glRenderbufferStorage(36161, 33189, getWidth(), getHeight());
        GLES20.glFramebufferRenderbuffer(36160, 36096, 36161, this.mDepthRenderBuffer[0]);
    }

    public List<OnTextureAcceptableListener> getTargets() {
        return this.mTargets;
    }

    public void addTarget(OnTextureAcceptableListener fX_OnTextureAcceptableListener) {
        synchronized (this.mTargets) {
            if (!this.mTargets.contains(fX_OnTextureAcceptableListener) && fX_OnTextureAcceptableListener != null) {
                this.mTargets.add(fX_OnTextureAcceptableListener);
            }
        }
    }

    public void removeTarget(OnTextureAcceptableListener fX_OnTextureAcceptableListener) {
        synchronized (this.mTargets) {
            this.mTargets.remove(fX_OnTextureAcceptableListener);
        }
    }

    public void clearTargets() {
        synchronized (this.mTargets) {
            this.mTargets.clear();
        }
    }
}
