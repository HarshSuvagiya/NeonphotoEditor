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
        if (mFrameBuffer != null) {
            GLES20.glDeleteFramebuffers(1, mFrameBuffer, 0);
            mFrameBuffer = null;
        }
        if (mDepthRenderBuffer != null) {
            GLES20.glDeleteRenderbuffers(1, mDepthRenderBuffer, 0);
            mDepthRenderBuffer = null;
        }
        if (mTextureOut != null) {
            GLES20.glDeleteTextures(1, mTextureOut, 0);
            mTextureOut = null;
        }
    }

    public String toString() {
        return super.toString() + " Targets:" + mTargets.size();
    }

    public void drawFrame() {
        if (mTextureOut == null) {
            if (mWidth != 0 && mHeight != 0) {
                initFBO();
            } else {
                return;
            }
        }
        GLES20.glBindFramebuffer(36160, mFrameBuffer[0]);
        onDraw();
        GLES20.glBindFramebuffer(36160, 0);
        synchronized (mTargets) {
            for (OnTextureAcceptableListener next : mTargets) {
                if (!(next == null || mTextureOut == null || mTextureOut.length <= 0)) {
                    next.onTextureAcceptable(mTextureOut[0], this);
                }
            }
        }
    }

    public void onDraw() {
        super.drawFrame();
    }

    public void onRenderSizeChanged() {
        initFBO();
    }

    private void initFBO() {
        if (mTextureOut != null) {
            GLES20.glDeleteTextures(1, mTextureOut, 0);
            mTextureOut = null;
        }
        mTextureOut = new int[1];
        GLES20.glGenTextures(1, mTextureOut, 0);
        GLES20.glActiveTexture(33984);
        GLES20.glBindTexture(3553, mTextureOut[0]);
        GLES20.glTexParameteri(3553, 10240, 9729);
        GLES20.glTexParameteri(3553, 10241, 9729);
        GLES20.glTexParameteri(3553, 10242, 33071);
        GLES20.glTexParameteri(3553, 10243, 33071);
        GLES20.glTexImage2D(3553, 0, 6408, getWidth(), getHeight(), 0, 6408, 5121, (Buffer) null);
        if (mFrameBuffer != null) {
            GLES20.glDeleteFramebuffers(1, mFrameBuffer, 0);
            mFrameBuffer = null;
        }
        if (mDepthRenderBuffer != null) {
            GLES20.glDeleteRenderbuffers(1, mDepthRenderBuffer, 0);
            mDepthRenderBuffer = null;
        }
        mFrameBuffer = new int[1];
        mDepthRenderBuffer = new int[1];
        GLES20.glGenFramebuffers(1, mFrameBuffer, 0);
        GLES20.glGenRenderbuffers(1, mDepthRenderBuffer, 0);
        GLES20.glBindFramebuffer(36160, mFrameBuffer[0]);
        GLES20.glFramebufferTexture2D(36160, 36064, 3553, mTextureOut[0], 0);
        GLES20.glBindRenderbuffer(36161, mDepthRenderBuffer[0]);
        GLES20.glRenderbufferStorage(36161, 33189, getWidth(), getHeight());
        GLES20.glFramebufferRenderbuffer(36160, 36096, 36161, mDepthRenderBuffer[0]);
    }

    public List<OnTextureAcceptableListener> getTargets() {
        return mTargets;
    }

    public void addTarget(OnTextureAcceptableListener OnTextureAcceptableListener1) {
        synchronized (mTargets) {
            if (!mTargets.contains(OnTextureAcceptableListener1) && OnTextureAcceptableListener1 != null) {
                mTargets.add(OnTextureAcceptableListener1);
            }
        }
    }

    public void removeTarget(OnTextureAcceptableListener OnTextureAcceptableListener1) {
        synchronized (mTargets) {
            mTargets.remove(OnTextureAcceptableListener1);
        }
    }

    public void clearTargets() {
        synchronized (mTargets) {
            mTargets.clear();
        }
    }
}
