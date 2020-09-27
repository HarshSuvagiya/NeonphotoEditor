package com.scorpion.NeonphotoEditor.Videoneoneffect;

import android.opengl.EGL14;
import android.view.Surface;
import cn.ezandroid.ezfilter.core.environment.EGLEnvironment;

class InputSurface {
    private EGLEnvironment mEgl;
    private EGLEnvironment.EglSurface mInputSurface;
    private Surface mSurface;

    public InputSurface(Surface surface) {
        if (surface != null) {
            mSurface = surface;
            mEgl = new EGLEnvironment(EGL14.eglGetCurrentContext(), false);
            mInputSurface = mEgl.createFromSurface(surface);
            mInputSurface.makeCurrent();
            return;
        }
        throw new NullPointerException();
    }

    public void swapBuffers() {
        mInputSurface.swap();
    }

    public void setPresentationTime(long j) {
        mEgl.setPresentationTime(j, mInputSurface);
    }

    public void release() {
        mInputSurface.release();
        mEgl.release();
        mSurface.release();
        mSurface = null;
    }
}
