package com.scorpion.NeonphotoEditor.Videoneoneffect;

import android.opengl.GLES20;
import android.text.TextUtils;
import android.util.Log;
import cn.ezandroid.ezfilter.core.util.L;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.LinkedList;
import java.util.Queue;

public class GLRender implements OnTextureAcceptableListener {
    public static final String ATTRIBUTE_POSITION = "position";
    public static final String ATTRIBUTE_TEXTURE_COORD = "inputTextureCoordinate";
    public static final String DEFAULT_FRAGMENT_SHADER = "precision mediump float;\nuniform sampler2D inputImageTexture;\nvarying vec2 textureCoordinate;\nvoid main(){\n   gl_FragColor = texture2D(inputImageTexture,textureCoordinate);\n}\n";
    public static final String DEFAULT_VERTEX_SHADER = "attribute vec4 position;\nattribute vec2 inputTextureCoordinate;\nvarying vec2 textureCoordinate;\nvoid main() {\n  textureCoordinate = inputTextureCoordinate;\n   gl_Position = position;\n}\n";
    public static final String EBIRTH_TIME = "ea_BirthTime";
    public static final String EDURATION = "ea_Duration";
    public static final String EFFECT_STYLE = "effect";
    public static final String EPROGRESS = "ev_Progress";
    public static final String EU_TIME = "eu_Time";
    public static final String MS_SCALE = "mscale";
    public static final String PAUSE = "pause";
    public static final String REVERSE = "reverse";
    public static final String UNIFORM_TEXTURE = "inputImageTexture";
    public static final String UNIFORM_TEXTURE_0 = "inputImageTexture";
    public static final String VARYING_TEXTURE_COORD = "textureCoordinate";
    protected int mCurrentRotation;
    private boolean mCustomSizeSet;
    protected int mEBirthTimeHandle;
    protected int mEDurationHandle;
    protected int mEUTimeLocation;
    protected int mEffectHandle;
    protected int mFps;
    protected String mFragmentShader = "precision mediump float;\nuniform sampler2D inputImageTexture;\nvarying vec2 textureCoordinate;\nvoid main(){\n   gl_FragColor = texture2D(inputImageTexture,textureCoordinate);\n}\n";
    protected int mFragmentShaderHandle;
    private int mFrameCount;
    protected int mHeight;
    private boolean mInitialized;
    private long mLastTime;
    protected int mMsScaleHandle;
    protected int mPauseHandle;
    protected int mPositionHandle;
    protected int mProgramHandle;
    protected int mReverseHandle;
    protected final Queue<Runnable> mRunOnDraw;
    protected final Queue<Runnable> mRunOnDrawEnd;
    protected boolean mSizeChanged;
    protected int mTextureCoordHandle;
    protected int mTextureHandle;
    protected int mTextureIn;
    protected FloatBuffer[] mTextureVertices;
    protected String mVertexShader = "attribute vec4 position;\nattribute vec2 inputTextureCoordinate;\nvarying vec2 textureCoordinate;\nvoid main() {\n  textureCoordinate = inputTextureCoordinate;\n   gl_Position = position;\n}\n";
    protected int mVertexShaderHandle;
    protected int mWidth;
    protected FloatBuffer mWorldVertices;

    public void onRenderSizeChanged() {
    }

    public GLRender() {
        initWorldVertices();
        initTextureVertices();
        mRunOnDraw = new LinkedList();
        mRunOnDrawEnd = new LinkedList();
    }

    public void initWorldVertices() {
        float[] fArr = {-1.0f, -1.0f, 1.0f, -1.0f, -1.0f, 1.0f, 1.0f, 1.0f};
        mWorldVertices = ByteBuffer.allocateDirect(fArr.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
        mWorldVertices.put(fArr).position(0);
    }

    public void setWorldVertices(FloatBuffer floatBuffer) {
        mWorldVertices = floatBuffer;
    }

    public void initTextureVertices() {
        mTextureVertices = new FloatBuffer[4];
        float[] fArr = {0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f};
        mTextureVertices[0] = ByteBuffer.allocateDirect(fArr.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
        mTextureVertices[0].put(fArr).position(0);
        float[] fArr2 = {1.0f, 0.0f, 1.0f, 1.0f, 0.0f, 0.0f, 0.0f, 1.0f};
        mTextureVertices[1] = ByteBuffer.allocateDirect(fArr2.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
        mTextureVertices[1].put(fArr2).position(0);
        float[] fArr3 = {1.0f, 1.0f, 0.0f, 1.0f, 1.0f, 0.0f, 0.0f, 0.0f};
        mTextureVertices[2] = ByteBuffer.allocateDirect(fArr3.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
        mTextureVertices[2].put(fArr3).position(0);
        float[] fArr4 = {0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f, 0.0f};
        mTextureVertices[3] = ByteBuffer.allocateDirect(fArr4.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
        mTextureVertices[3].put(fArr4).position(0);
    }

    public void setTextureVertices(FloatBuffer[] floatBufferArr) {
        mTextureVertices = floatBufferArr;
    }

    public int getWidth() {
        return mWidth;
    }

    public int getHeight() {
        return mHeight;
    }

    public void setWidth(int i) {
        if (!mCustomSizeSet && mWidth != i) {
            mWidth = i;
            mSizeChanged = true;
        }
    }

    public void setHeight(int i) {
        if (!mCustomSizeSet && mHeight != i) {
            mHeight = i;
            mSizeChanged = true;
        }
    }

    public int getRotate90Degrees() {
        return mCurrentRotation;
    }

    public boolean resetRotate() {
        if (mCurrentRotation % 2 == 1) {
            mCurrentRotation = 0;
            return true;
        }
        mCurrentRotation = 0;
        return false;
    }

    public void setRotate90Degrees(int i) {
        while (i < 0) {
            i += 4;
        }
        mCurrentRotation += i;
        mCurrentRotation %= 4;
    }

    public void setRenderSize(int i, int i2) {
        mCustomSizeSet = true;
        mWidth = i;
        mHeight = i2;
        mSizeChanged = true;
    }

    public void swapWidthAndHeight() {
        int i = mWidth;
        mWidth = mHeight;
        mHeight = i;
        mSizeChanged = true;
    }

    public void initShaderHandles() {
        mPositionHandle = GLES20.glGetAttribLocation(mProgramHandle, "position");
        mTextureCoordHandle = GLES20.glGetAttribLocation(mProgramHandle, "inputTextureCoordinate");
        mTextureHandle = GLES20.glGetUniformLocation(mProgramHandle, "inputImageTexture");
        mEffectHandle = GLES20.glGetUniformLocation(mProgramHandle, EFFECT_STYLE);
        mPauseHandle = GLES20.glGetUniformLocation(mProgramHandle, PAUSE);
        mMsScaleHandle = GLES20.glGetUniformLocation(mProgramHandle, MS_SCALE);
        mReverseHandle = GLES20.glGetUniformLocation(mProgramHandle, REVERSE);
        mEUTimeLocation = GLES20.glGetUniformLocation(mProgramHandle, EU_TIME);
        mEBirthTimeHandle = GLES20.glGetAttribLocation(mProgramHandle, EBIRTH_TIME);
        mEDurationHandle = GLES20.glGetAttribLocation(mProgramHandle, EDURATION);
    }

    public void bindShaderVertices() {
        mWorldVertices.position(0);
        GLES20.glVertexAttribPointer(mPositionHandle, 2, 5126, false, 8, mWorldVertices);
        GLES20.glEnableVertexAttribArray(mPositionHandle);
        mTextureVertices[mCurrentRotation].position(0);
        GLES20.glVertexAttribPointer(mTextureCoordHandle, 2, 5126, false, 8, mTextureVertices[mCurrentRotation]);
        GLES20.glEnableVertexAttribArray(mTextureCoordHandle);
    }

    public void bindShaderTextures() {
        GLES20.glActiveTexture(33984);
        GLES20.glBindTexture(3553, mTextureIn);
        GLES20.glUniform1i(mTextureHandle, 0);
    }

    public void bindShaderValues() {
        bindShaderVertices();
        bindShaderTextures();
    }

    public void reInit() {
        mInitialized = false;
    }

    public void logDraw() {
        Log.e("RenderDraw", toString() + " Fps:" + mFps);
    }

    public String toString() {
        return super.toString() + "[" + mWidth + "x" + mHeight + "]";
    }

    public void onDrawFrame() {
        if (!mInitialized) {
            initGLContext();
            mInitialized = true;
        }
        if (mSizeChanged) {
            onRenderSizeChanged();
        }
        runAll(mRunOnDraw);
        drawFrame();
        runAll(mRunOnDrawEnd);
        mSizeChanged = false;
        if (L.LOG_RENDER_DRAW) {
            logDraw();
        }
        calculateFps();
    }

    private void calculateFps() {
        if (mLastTime == 0) {
            mLastTime = System.currentTimeMillis();
        }
        mFrameCount++;
        if (System.currentTimeMillis() - mLastTime >= 1000) {
            mLastTime = System.currentTimeMillis();
            mFps = mFrameCount;
            mFrameCount = 0;
        }
    }

    public int getFps() {
        return mFps;
    }


    public void drawFrame() {
        if (mTextureIn != 0) {
            if (!(mWidth == 0 || mHeight == 0)) {
                GLES20.glViewport(0, 0, mWidth, mHeight);
            }
            GLES20.glUseProgram(mProgramHandle);
            GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
            GLES20.glClear(16640);
            bindShaderValues();
            GLES20.glDrawArrays(5, 0, 4);
        }
    }


    public String[] getShaderAttributes() {
        return new String[]{"position", "inputTextureCoordinate"};
    }


    public void initGLContext() {
        String vertexShader = getVertexShader();
        String fragmentShader = getFragmentShader();
        if (!TextUtils.isEmpty(vertexShader) && !TextUtils.isEmpty(fragmentShader)) {
            mVertexShaderHandle = ShaderHelper.compileShader(vertexShader, 35633);
            mFragmentShaderHandle = ShaderHelper.compileShader(fragmentShader, 35632);
            mProgramHandle = ShaderHelper.linkProgram(mVertexShaderHandle, mFragmentShaderHandle, getShaderAttributes());
        }
        initShaderHandles();
    }


    public String getVertexShader() {
        return mVertexShader;
    }


    public String getFragmentShader() {
        return mFragmentShader;
    }

    public void setVertexShader(String str) {
        mVertexShader = str;
    }

    public void setFragmentShader(String str) {
        mFragmentShader = str;
    }


    public void logDestroy() {
        Log.e("RenderDestroy", toString() + " Thread:" + Thread.currentThread().getName());
    }

    public void destroy() {
        mInitialized = false;
        if (mProgramHandle != 0) {
            GLES20.glDeleteProgram(mProgramHandle);
            mProgramHandle = 0;
        }
        if (mVertexShaderHandle != 0) {
            GLES20.glDeleteShader(mVertexShaderHandle);
            mVertexShaderHandle = 0;
        }
        if (mFragmentShaderHandle != 0) {
            GLES20.glDeleteShader(mFragmentShaderHandle);
            mFragmentShaderHandle = 0;
        }
        if (L.LOG_RENDER_DESTROY) {
            logDestroy();
        }
    }


    public void runAll(Queue<Runnable> queue) {
        synchronized (queue) {
            while (!queue.isEmpty()) {
                queue.poll().run();
            }
        }
    }

    public void runOnDraw(Runnable runnable) {
        synchronized (mRunOnDraw) {
            mRunOnDraw.add(runnable);
        }
    }

    public void runOnDrawEnd(Runnable runnable) {
        synchronized (mRunOnDrawEnd) {
            mRunOnDrawEnd.add(runnable);
        }
    }

    public void onTextureAcceptable(int i, GLRender GLRender1) {
        mTextureIn = i;
        setWidth(GLRender1.getWidth());
        setHeight(GLRender1.getHeight());
        onDrawFrame();
    }
}
