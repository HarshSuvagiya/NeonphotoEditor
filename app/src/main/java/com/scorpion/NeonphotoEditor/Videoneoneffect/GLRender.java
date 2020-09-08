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

    /* access modifiers changed from: protected */
    public void onRenderSizeChanged() {
    }

    public GLRender() {
        initWorldVertices();
        initTextureVertices();
        this.mRunOnDraw = new LinkedList();
        this.mRunOnDrawEnd = new LinkedList();
    }

    /* access modifiers changed from: protected */
    public void initWorldVertices() {
        float[] fArr = {-1.0f, -1.0f, 1.0f, -1.0f, -1.0f, 1.0f, 1.0f, 1.0f};
        this.mWorldVertices = ByteBuffer.allocateDirect(fArr.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
        this.mWorldVertices.put(fArr).position(0);
    }

    public void setWorldVertices(FloatBuffer floatBuffer) {
        this.mWorldVertices = floatBuffer;
    }

    /* access modifiers changed from: protected */
    public void initTextureVertices() {
        this.mTextureVertices = new FloatBuffer[4];
        float[] fArr = {0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f};
        this.mTextureVertices[0] = ByteBuffer.allocateDirect(fArr.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
        this.mTextureVertices[0].put(fArr).position(0);
        float[] fArr2 = {1.0f, 0.0f, 1.0f, 1.0f, 0.0f, 0.0f, 0.0f, 1.0f};
        this.mTextureVertices[1] = ByteBuffer.allocateDirect(fArr2.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
        this.mTextureVertices[1].put(fArr2).position(0);
        float[] fArr3 = {1.0f, 1.0f, 0.0f, 1.0f, 1.0f, 0.0f, 0.0f, 0.0f};
        this.mTextureVertices[2] = ByteBuffer.allocateDirect(fArr3.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
        this.mTextureVertices[2].put(fArr3).position(0);
        float[] fArr4 = {0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f, 0.0f};
        this.mTextureVertices[3] = ByteBuffer.allocateDirect(fArr4.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
        this.mTextureVertices[3].put(fArr4).position(0);
    }

    public void setTextureVertices(FloatBuffer[] floatBufferArr) {
        this.mTextureVertices = floatBufferArr;
    }

    public int getWidth() {
        return this.mWidth;
    }

    public int getHeight() {
        return this.mHeight;
    }

    /* access modifiers changed from: protected */
    public void setWidth(int i) {
        if (!this.mCustomSizeSet && this.mWidth != i) {
            this.mWidth = i;
            this.mSizeChanged = true;
        }
    }

    /* access modifiers changed from: protected */
    public void setHeight(int i) {
        if (!this.mCustomSizeSet && this.mHeight != i) {
            this.mHeight = i;
            this.mSizeChanged = true;
        }
    }

    public int getRotate90Degrees() {
        return this.mCurrentRotation;
    }

    public boolean resetRotate() {
        if (this.mCurrentRotation % 2 == 1) {
            this.mCurrentRotation = 0;
            return true;
        }
        this.mCurrentRotation = 0;
        return false;
    }

    public void setRotate90Degrees(int i) {
        while (i < 0) {
            i += 4;
        }
        this.mCurrentRotation += i;
        this.mCurrentRotation %= 4;
    }

    public void setRenderSize(int i, int i2) {
        this.mCustomSizeSet = true;
        this.mWidth = i;
        this.mHeight = i2;
        this.mSizeChanged = true;
    }

    public void swapWidthAndHeight() {
        int i = this.mWidth;
        this.mWidth = this.mHeight;
        this.mHeight = i;
        this.mSizeChanged = true;
    }

    /* access modifiers changed from: protected */
    public void initShaderHandles() {
        this.mPositionHandle = GLES20.glGetAttribLocation(this.mProgramHandle, "position");
        this.mTextureCoordHandle = GLES20.glGetAttribLocation(this.mProgramHandle, "inputTextureCoordinate");
        this.mTextureHandle = GLES20.glGetUniformLocation(this.mProgramHandle, "inputImageTexture");
        this.mEffectHandle = GLES20.glGetUniformLocation(this.mProgramHandle, EFFECT_STYLE);
        this.mPauseHandle = GLES20.glGetUniformLocation(this.mProgramHandle, PAUSE);
        this.mMsScaleHandle = GLES20.glGetUniformLocation(this.mProgramHandle, MS_SCALE);
        this.mReverseHandle = GLES20.glGetUniformLocation(this.mProgramHandle, REVERSE);
        this.mEUTimeLocation = GLES20.glGetUniformLocation(this.mProgramHandle, EU_TIME);
        this.mEBirthTimeHandle = GLES20.glGetAttribLocation(this.mProgramHandle, EBIRTH_TIME);
        this.mEDurationHandle = GLES20.glGetAttribLocation(this.mProgramHandle, EDURATION);
    }

    /* access modifiers changed from: protected */
    public void bindShaderVertices() {
        this.mWorldVertices.position(0);
        GLES20.glVertexAttribPointer(this.mPositionHandle, 2, 5126, false, 8, this.mWorldVertices);
        GLES20.glEnableVertexAttribArray(this.mPositionHandle);
        this.mTextureVertices[this.mCurrentRotation].position(0);
        GLES20.glVertexAttribPointer(this.mTextureCoordHandle, 2, 5126, false, 8, this.mTextureVertices[this.mCurrentRotation]);
        GLES20.glEnableVertexAttribArray(this.mTextureCoordHandle);
    }

    /* access modifiers changed from: protected */
    public void bindShaderTextures() {
        GLES20.glActiveTexture(33984);
        GLES20.glBindTexture(3553, this.mTextureIn);
        GLES20.glUniform1i(this.mTextureHandle, 0);
    }

    /* access modifiers changed from: protected */
    public void bindShaderValues() {
        bindShaderVertices();
        bindShaderTextures();
    }

    public void reInit() {
        this.mInitialized = false;
    }

    /* access modifiers changed from: protected */
    public void logDraw() {
        Log.e("RenderDraw", toString() + " Fps:" + this.mFps);
    }

    public String toString() {
        return super.toString() + "[" + this.mWidth + "x" + this.mHeight + "]";
    }

    public void onDrawFrame() {
        if (!this.mInitialized) {
            initGLContext();
            this.mInitialized = true;
        }
        if (this.mSizeChanged) {
            onRenderSizeChanged();
        }
        runAll(this.mRunOnDraw);
        drawFrame();
        runAll(this.mRunOnDrawEnd);
        this.mSizeChanged = false;
        if (L.LOG_RENDER_DRAW) {
            logDraw();
        }
        calculateFps();
    }

    private void calculateFps() {
        if (this.mLastTime == 0) {
            this.mLastTime = System.currentTimeMillis();
        }
        this.mFrameCount++;
        if (System.currentTimeMillis() - this.mLastTime >= 1000) {
            this.mLastTime = System.currentTimeMillis();
            this.mFps = this.mFrameCount;
            this.mFrameCount = 0;
        }
    }

    public int getFps() {
        return this.mFps;
    }

    /* access modifiers changed from: protected */
    public void drawFrame() {
        if (this.mTextureIn != 0) {
            if (!(this.mWidth == 0 || this.mHeight == 0)) {
                GLES20.glViewport(0, 0, this.mWidth, this.mHeight);
            }
            GLES20.glUseProgram(this.mProgramHandle);
            GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
            GLES20.glClear(16640);
            bindShaderValues();
            GLES20.glDrawArrays(5, 0, 4);
        }
    }

    /* access modifiers changed from: protected */
    public String[] getShaderAttributes() {
        return new String[]{"position", "inputTextureCoordinate"};
    }

    /* access modifiers changed from: protected */
    public void initGLContext() {
        String vertexShader = getVertexShader();
        String fragmentShader = getFragmentShader();
        if (!TextUtils.isEmpty(vertexShader) && !TextUtils.isEmpty(fragmentShader)) {
            this.mVertexShaderHandle = ShaderHelper.compileShader(vertexShader, 35633);
            this.mFragmentShaderHandle = ShaderHelper.compileShader(fragmentShader, 35632);
            this.mProgramHandle = ShaderHelper.linkProgram(this.mVertexShaderHandle, this.mFragmentShaderHandle, getShaderAttributes());
        }
        initShaderHandles();
    }

    /* access modifiers changed from: protected */
    public String getVertexShader() {
        return this.mVertexShader;
    }

    /* access modifiers changed from: protected */
    public String getFragmentShader() {
        return this.mFragmentShader;
    }

    public void setVertexShader(String str) {
        this.mVertexShader = str;
    }

    public void setFragmentShader(String str) {
        this.mFragmentShader = str;
    }

    /* access modifiers changed from: protected */
    public void logDestroy() {
        Log.e("RenderDestroy", toString() + " Thread:" + Thread.currentThread().getName());
    }

    public void destroy() {
        this.mInitialized = false;
        if (this.mProgramHandle != 0) {
            GLES20.glDeleteProgram(this.mProgramHandle);
            this.mProgramHandle = 0;
        }
        if (this.mVertexShaderHandle != 0) {
            GLES20.glDeleteShader(this.mVertexShaderHandle);
            this.mVertexShaderHandle = 0;
        }
        if (this.mFragmentShaderHandle != 0) {
            GLES20.glDeleteShader(this.mFragmentShaderHandle);
            this.mFragmentShaderHandle = 0;
        }
        if (L.LOG_RENDER_DESTROY) {
            logDestroy();
        }
    }

    /* access modifiers changed from: protected */
    public void runAll(Queue<Runnable> queue) {
        synchronized (queue) {
            while (!queue.isEmpty()) {
                queue.poll().run();
            }
        }
    }

    public void runOnDraw(Runnable runnable) {
        synchronized (this.mRunOnDraw) {
            this.mRunOnDraw.add(runnable);
        }
    }

    public void runOnDrawEnd(Runnable runnable) {
        synchronized (this.mRunOnDrawEnd) {
            this.mRunOnDrawEnd.add(runnable);
        }
    }

    public void onTextureAcceptable(int i, GLRender fX_GLRender) {
        this.mTextureIn = i;
        setWidth(fX_GLRender.getWidth());
        setHeight(fX_GLRender.getHeight());
        onDrawFrame();
    }
}
