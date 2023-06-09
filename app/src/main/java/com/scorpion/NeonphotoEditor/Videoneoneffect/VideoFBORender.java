package com.scorpion.NeonphotoEditor.Videoneoneffect;

import android.graphics.SurfaceTexture;
import android.opengl.GLES20;
import cn.ezandroid.ezfilter.media.transcode.IVideoRender;

public class VideoFBORender extends FBORender implements IVideoRender {
    private static final String UNIFORM_CAM_MATRIX = "u_Matrix";
    private float[] mMatrix = new float[16];
    private int mMatrixHandle;
    private SurfaceTexture mSurfaceTexture;

    public VideoFBORender() {
        setVertexShader("uniform mat4 u_Matrix;\nattribute vec4 position;\nattribute vec2 inputTextureCoordinate;\nvarying vec2 textureCoordinate;\nvoid main() {\n   vec4 texPos = u_Matrix * vec4(inputTextureCoordinate, 1, 1);\n   textureCoordinate = texPos.xy;\n   gl_Position = position;\n}\n");
        setFragmentShader("#extension GL_OES_EGL_image_external : require\nprecision mediump float;\nuniform samplerExternalOES inputImageTexture;\nvarying vec2 textureCoordinate;\nvoid main() {\n   gl_FragColor = texture2D(inputImageTexture, textureCoordinate);\n}\n");
    }

    public void drawFrame(long j) {
        onDrawFrame();
    }

    public void initShaderHandles() {
        super.initShaderHandles();
        mMatrixHandle = GLES20.glGetUniformLocation(mProgramHandle, UNIFORM_CAM_MATRIX);
    }

    public void initGLContext() {
        super.initGLContext();
        if (mTextureIn != 0) {
            GLES20.glDeleteTextures(1, new int[]{mTextureIn}, 0);
            mTextureIn = 0;
        }
        int[] iArr = new int[1];
        GLES20.glGenTextures(1, iArr, 0);
        GLES20.glBindTexture(36197, iArr[0]);
        GLES20.glTexParameterf(36197, 10241, 9729.0f);
        GLES20.glTexParameterf(36197, 10240, 9729.0f);
        GLES20.glTexParameteri(36197, 10242, 33071);
        GLES20.glTexParameteri(36197, 10243, 33071);
        mTextureIn = iArr[0];
        if (mSurfaceTexture != null) {
            mSurfaceTexture.release();
            mSurfaceTexture = null;
        }
        mSurfaceTexture = new SurfaceTexture(mTextureIn);
    }

    public void bindShaderValues() {
        super.bindShaderVertices();
        GLES20.glActiveTexture(33984);
        GLES20.glBindTexture(36197, mTextureIn);
        GLES20.glUniform1i(mTextureHandle, 0);
        mSurfaceTexture.getTransformMatrix(mMatrix);
        GLES20.glUniformMatrix4fv(mMatrixHandle, 1, false, mMatrix, 0);
    }

    public SurfaceTexture getSurfaceTexture() {
        onDrawFrame();
        return mSurfaceTexture;
    }

    public void destroy() {
        super.destroy();
        if (mSurfaceTexture != null) {
            mSurfaceTexture.release();
            mSurfaceTexture = null;
        }
        if (mTextureIn != 0) {
            GLES20.glDeleteTextures(1, new int[]{mTextureIn}, 0);
            mTextureIn = 0;
        }
    }
}
