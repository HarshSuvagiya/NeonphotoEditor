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
            this.mSurface = surface;
            this.mEgl = new EGLEnvironment(EGL14.eglGetCurrentContext(), false);
            this.mInputSurface = this.mEgl.createFromSurface(surface);
            this.mInputSurface.makeCurrent();
            return;
        }
        throw new NullPointerException();
    }

    public void swapBuffers() {
        this.mInputSurface.swap();
    }

    public void setPresentationTime(long j) {
        this.mEgl.setPresentationTime(j, this.mInputSurface);
    }

    public void release() {
        this.mInputSurface.release();
        this.mEgl.release();
        this.mSurface.release();
        this.mSurface = null;
    }
}
