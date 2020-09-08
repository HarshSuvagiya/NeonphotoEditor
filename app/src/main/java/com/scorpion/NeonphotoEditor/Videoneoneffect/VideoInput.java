package com.scorpion.NeonphotoEditor.Videoneoneffect;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.net.Uri;
import android.opengl.GLES20;
import android.os.Handler;
import android.os.Looper;
import android.view.Surface;

import com.scorpion.NeonphotoEditor.util.Helper;
import com.scorpion.NeonphotoEditor.util.VideoEffectTimeBar;

import cn.ezandroid.ezfilter.core.environment.IGLEnvironment;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class VideoInput extends FBORender implements SurfaceTexture.OnFrameAvailableListener {
    private static final String UNIFORM_CAM_MATRIX = "u_Matrix";
    ArrayList<String> altemp = new ArrayList<>();
    int effect;
    int frame;
    HashMap<Integer, String> hmtemp = new HashMap<>();
    private IEffectTimeController iEffectTimeController;
    boolean inTouch;
    boolean isAdd = true;
    boolean isReverse = true;
    boolean loaded;
    private IMediaPlayer.OnCompletionListener mCompletionListener;
    private IMediaPlayer.OnErrorListener mErrorListener;
    private boolean mIsLoop;
    private float[] mMatrix = new float[16];
    private int mMatrixHandle;
    private IMediaPlayer mPlayer;
    private IMediaPlayer.OnPreparedListener mPreparedListener;
    private boolean mReady;
    private IGLEnvironment mRender;
    private boolean mStartWhenReady = true;
    private Surface mSurface;
    private SurfaceTexture mSurfaceTexture;
    private Uri mVideoUri;
    private float mVideoVolumeLeft = 1.0f;
    private float mVideoVolumeRight = 1.0f;
    float msScale = 1.0f;
    int pause;
    int reverseCnt = 0;
    int totalframe;
    VideoEffectTimeBar videoEffectTimeBar;

    public interface IEffectTimeController {
        float getCurrentTime();
    }

    public VideoInput(IGLEnvironment iGLEnvironment) {
        this.mRender = iGLEnvironment;
        initShader();
    }

    public VideoInput(Context context, IGLEnvironment iGLEnvironment, Uri uri) {
        this.mRender = iGLEnvironment;
        initShader();
        try {
            setVideoUri(context, uri);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public VideoInput(Context context, IGLEnvironment iGLEnvironment, Uri uri, IMediaPlayer fX_IMediaPlayer) {
        this.mRender = iGLEnvironment;
        initShader();
        try {
            setVideoUri(context, uri, fX_IMediaPlayer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initShader() {
        this.altemp.clear();
        this.frame = 0;
        this.loaded = false;
        this.totalframe = 0;
        setVertexShader("uniform mat4 u_Matrix;\nuniform float eu_Time;\nattribute vec4 position;\nattribute vec2 inputTextureCoordinate;\nattribute float ea_BirthTime;\nattribute float ea_Duration;\nvarying vec2 textureCoordinate;\nvarying float ev_Progress;\nvoid main() {\n   vec4 texPos = u_Matrix * vec4(inputTextureCoordinate, 1, 1);\n   textureCoordinate = texPos.xy;\n   float elapsedTime = eu_Time - ea_BirthTime;\n   ev_Progress = elapsedTime / ea_Duration;\n   gl_Position = position;\n}\n");
        setFragmentShader("#extension GL_OES_EGL_image_external : require\nprecision mediump float;\nuniform samplerExternalOES inputImageTexture;\nvarying vec2 textureCoordinate;\nvarying float ev_Progress;\nuniform int effect;uniform int pause;uniform float mscale;uniform float reverse;void main() {\nvec2 texture = textureCoordinate;\nvec4 base = texture2D(inputImageTexture, texture);\nfloat x = -1.0;\nfloat y = -1.0;\nfloat mixturePercent = 1.0;\nvec4 base2;\n\nif(pause == 1) {\n    if (effect == 1) {\n\n        if (texture.x > 0.5) {\n            texture.x = texture.x - 0.25;\n        } else {\n            texture.x = texture.x + 0.25;\n        }\n           base = texture2D(inputImageTexture, texture);\n\n    } else if (effect == 2) {\n\n        if (texture.y > 0.5) {\n            texture.y = texture.y - 0.25;\n        } else {\n            texture.y = texture.y + 0.25;\n        }\n           base = texture2D(inputImageTexture, texture);\n\n    } else if (effect == 3) {\n\n        if (texture.x < 0.33) {\n            texture.x = texture.x + 0.33;\n        } else if (texture.x > 0.66) {\n            texture.x = texture.x - 0.33;\n        }\n           base = texture2D(inputImageTexture, texture);\n\n    } else if (effect == 4) {\n        if (texture.x <= 0.5 && texture.y <= 0.5) {\n            texture.x = texture.x + 0.25;\n            texture.y = texture.y + 0.25;\n        } else if (texture.y > 0.5 && texture.x <= 0.5) {\n            texture.x = texture.x + 0.25;\n            texture.y = texture.y - 0.25;\n        } else if (texture.y > 0.5 && texture.x > 0.5) {\n            texture.x = texture.x - 0.25;\n            texture.y = texture.y - 0.25;\n        } else {\n            texture.x = texture.x - 0.25;\n            texture.y = texture.y + 0.25;\n        }\n           base = texture2D(inputImageTexture, texture);\n\n    } else if (effect == 5) {\n\n        if (texture.x > 0.66) {\n            texture.x = texture.x - 0.33;\n\n            if (texture.y > 0.66) {\n                texture.y = texture.y - 0.33;\n            } else if (texture.y <= 0.33) {\n                texture.y = texture.y + 0.33;\n            }\n\n           base = texture2D(inputImageTexture, texture);\n\n        } else if (texture.x > 0.33) {\n\n            if (texture.y > 0.66) {\n                texture.y = texture.y - 0.33;\n            } else if (texture.y <= 0.33) {\n                texture.y = texture.y + 0.33;\n            }\n\n           base = texture2D(inputImageTexture, texture);\n\n        } else {\n\n            texture.x = texture.x + 0.33;\n            if (texture.y > 0.66) {\n                texture.y = texture.y - 0.33;\n            } else if (texture.y <= 0.33) {\n                texture.y = texture.y + 0.33;\n            }\n\n           base = texture2D(inputImageTexture, texture);\n\n        }\n    } else if (effect == 6) {\n\n           x = ((mscale - 1.0) * 0.5 + texture.x) / mscale;\n           y = ((mscale - 1.0) * 0.5 + texture.y) / mscale;\n    } else if (effect == 7) {\n\n           x = 0.5;\n           y = 0.5;\n           vec2 newCoor = texture.xy;\n           newCoor.x = texture.x - 0.05 * mscale;\n           base2 = base;\n           if (newCoor.x >= 0.0) base2 = texture2D(inputImageTexture, newCoor.xy);\n           newCoor.x = texture.x + 0.1 * mscale;\n           vec4 base3 = base;\n           if (newCoor.x <= 1.0) base3 = texture2D(inputImageTexture, newCoor.xy);\n           base2 = vec4(mix(base2.rgb, base3.rgb, 0.5), base.a);\n    } else if (effect == 8) {\n\n       if (texture.x > mscale) {\n           x = 0.5;\n           y = 0.5;\n       }\n    } else if (effect == 9) {\n\n       if (texture.x <= 0.5 && texture.y <= 0.5) {\n           texture.x = texture.x + 0.25;\n           texture.y = texture.y + 0.25;\n           base = texture2D(inputImageTexture, texture);\n           if (mscale > 0.99) {\n               x = 0.5;\n               y = 0.5;\n           }\n       } else if (texture.y > 0.5 && texture.x <= 0.5) {\n           texture.x = texture.x + 0.25;\n           texture.y = texture.y - 0.25;\n           base = texture2D(inputImageTexture, texture);\n           if (mscale > 2.99 || mscale < 1.99) {\n               x = 0.5;\n               y = 0.5;\n           }\n       } else if (texture.y > 0.5 && texture.x > 0.5) {\n           texture.x = texture.x - 0.25;\n           texture.y = texture.y - 0.25;\n           base = texture2D(inputImageTexture, texture);\n           if (mscale < 2.99) {\n               x = 0.5;\n               y = 0.5;\n           }\n       } else {\n           texture.x = texture.x - 0.25;\n           texture.y = texture.y + 0.25;\n           base = texture2D(inputImageTexture, texture);\n           if (mscale > 1.99 || mscale < 0.99) {\n               x = 0.5;\n               y = 0.5;\n           }\n       }\n    } else if (effect == 10) {\n\n       if (reverse > 0.5) {\n               x = 0.5;\n               y = 0.5;\n       }\n    } else if (effect == 11) {\n\n       if (texture.x < 0.33) {\n           texture.x = texture.x + 0.33;\n           base = texture2D(inputImageTexture, texture.xy);\n           if (reverse > 0.5)           base = vec4(1.0 - base.r, 1.0 - base.g, 1.0 - base.b, base.a);\n       } else if (texture.x > 0.66) {\n           texture.x = texture.x - 0.33;\n           base = texture2D(inputImageTexture, texture.xy);\n           if (reverse > 0.5)           base = vec4(1.0 - base.r, 1.0 - base.g, 1.0 - base.b, base.a);\n       }\n    } else if (effect == 12) {\n\n       if (texture.x < 0.5) {\n           texture.x = 1.0 - texture.x;\n           base = texture2D(inputImageTexture, texture);\n       }\n    }\n}\n\n   if (x > 1.0 || y > 1.0 || x < 0.0 || y < 0.0) {\n\n       gl_FragColor = base;\n\n   } else {\n\n       if (effect == 6) {\n           vec4 overlay = texture2D(inputImageTexture, vec2(x, y));\n           gl_FragColor = vec4(mix(base.rgb, overlay.rgb, base.a * 0.5), base.a);\n       } else if (effect == 7) {\n           gl_FragColor = vec4(mix(base.rgb, base2.rgb, 0.5), base.a);\n       } else if (effect == 8 || effect == 9) {\n           highp float cintensity = 0.299 * base.r + 0.587 * base.g + 0.114 * base.b;\n           gl_FragColor = vec4(cintensity, cintensity, cintensity, base.a);\n       } else if (effect == 10) {\n           vec4 newColor = vec4(1.0 - base.r, 1.0 - base.g, 1.0 - base.b, base.a);\n           gl_FragColor = vec4(mix(base.rgb, newColor.rgb, base.a * mixturePercent), base.a);\n       }\n   }\n\n}\n");
    }

    public void setOnPreparedListener(IMediaPlayer.OnPreparedListener onPreparedListener) {
        this.mPreparedListener = onPreparedListener;
    }

    public void setOnCompletionListener(IMediaPlayer.OnCompletionListener onCompletionListener) {
        this.mCompletionListener = onCompletionListener;
    }

    public void setOnErrorListener(IMediaPlayer.OnErrorListener onErrorListener) {
        this.mErrorListener = onErrorListener;
    }

    public void setiEffectTimeController(IEffectTimeController iEffectTimeController2) {
        this.iEffectTimeController = iEffectTimeController2;
    }

    public void setVideoEffectTimeBar(VideoEffectTimeBar fX_VideoEffectTimeBar) {
        this.videoEffectTimeBar = fX_VideoEffectTimeBar;
    }

    public void setVideoUri(Context context, Uri uri, IMediaPlayer fX_IMediaPlayer) throws IOException {
        if (uri != null) {
            release();
            this.mVideoUri = uri;
            this.mPlayer = fX_IMediaPlayer;
            this.mPlayer.setDataSource(context, this.mVideoUri);
            this.mPlayer.setLooping(this.mIsLoop);
            this.mPlayer.setVolume(this.mVideoVolumeLeft, this.mVideoVolumeRight);
            this.mPlayer.setOnPreparedListener(new IMediaPlayer.OnPreparedListener() {
                public final void onPrepared(IMediaPlayer fX_IMediaPlayer) {
                    VideoInput.lambda$setVideoUri$0(VideoInput.this, fX_IMediaPlayer);
                }
            });
            this.mPlayer.setOnCompletionListener(new IMediaPlayer.OnCompletionListener() {
                public final void onCompletion(IMediaPlayer fX_IMediaPlayer) {
                    VideoInput.lambda$setVideoUri$1(VideoInput.this, fX_IMediaPlayer);
                }
            });
            this.mPlayer.setOnErrorListener(new IMediaPlayer.OnErrorListener() {
                public final boolean onError(IMediaPlayer fX_IMediaPlayer, int i, int i2) {
                    return VideoInput.lambda$setVideoUri$2(VideoInput.this, fX_IMediaPlayer, i, i2);
                }
            });
            reInit();
            this.mRender.requestRender();
        }
    }

    public static /* synthetic */ void lambda$setVideoUri$0(VideoInput fX_VideoInput, IMediaPlayer fX_IMediaPlayer) {
        fX_VideoInput.mReady = true;
        if (fX_VideoInput.mStartWhenReady) {
            fX_IMediaPlayer.start();
        }
        fX_VideoInput.setRenderSize(fX_VideoInput.mPlayer.getVideoWidth(), fX_VideoInput.mPlayer.getVideoHeight());
        if (fX_VideoInput.mPreparedListener != null) {
            fX_VideoInput.mPreparedListener.onPrepared(fX_IMediaPlayer);
        }
    }

    public static /* synthetic */ void lambda$setVideoUri$1(VideoInput fX_VideoInput, IMediaPlayer fX_IMediaPlayer) {
        if (fX_VideoInput.mCompletionListener != null) {
            fX_VideoInput.mCompletionListener.onCompletion(fX_IMediaPlayer);
            if (fX_VideoInput.totalframe == 0) {
                fX_VideoInput.totalframe = fX_VideoInput.frame;
            }
            fX_VideoInput.frame = 0;
            fX_VideoInput.loaded = true;
        }
    }

    public static /* synthetic */ boolean lambda$setVideoUri$2(VideoInput fX_VideoInput, IMediaPlayer fX_IMediaPlayer, int i, int i2) {
        return fX_VideoInput.mErrorListener == null || fX_VideoInput.mErrorListener.onError(fX_IMediaPlayer, i, i2);
    }

    public void setVideoUri(Context context, Uri uri) throws IOException {
        setVideoUri(context, uri, new DefaultMediaPlayer());
    }

    public IMediaPlayer getMediaPlayer() {
        return this.mPlayer;
    }

    public Uri getVideoUri() {
        return this.mVideoUri;
    }

    public void setStartWhenReady(boolean z) {
        this.mStartWhenReady = z;
    }

    public void setLoop(boolean z) {
        this.mIsLoop = z;
        if (this.mPlayer != null) {
            this.mPlayer.setLooping(this.mIsLoop);
        }
    }

    public void setEffect(int i) {
        this.effect = i;
        Helper.showLog("WWW", "Effect Set : " + i);
    }

    public void setVolume(float f, float f2) {
        this.mVideoVolumeLeft = f;
        this.mVideoVolumeRight = f2;
        if (this.mPlayer != null) {
            this.mPlayer.setVolume(this.mVideoVolumeLeft, this.mVideoVolumeRight);
        }
    }

    /* access modifiers changed from: protected */
    public void drawFrame() {
        try {
            this.mSurfaceTexture.updateTexImage();
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.drawFrame();
    }

    public void drawFrame(long j) {
        onDrawFrame();
    }

    public SurfaceTexture getSurfaceTexture() {
        onDrawFrame();
        return this.mSurfaceTexture;
    }

    /* access modifiers changed from: protected */
    public void initShaderHandles() {
        super.initShaderHandles();
        this.mMatrixHandle = GLES20.glGetUniformLocation(this.mProgramHandle, UNIFORM_CAM_MATRIX);
    }

    /* access modifiers changed from: protected */
    public void initGLContext() {
        super.initGLContext();
        this.mReady = false;
        if (this.mTextureIn != 0) {
            GLES20.glDeleteTextures(1, new int[]{this.mTextureIn}, 0);
            this.mTextureIn = 0;
        }
        int[] iArr = new int[1];
        GLES20.glGenTextures(1, iArr, 0);
        GLES20.glBindTexture(36197, iArr[0]);
        GLES20.glTexParameterf(36197, 10241, 9729.0f);
        GLES20.glTexParameterf(36197, 10240, 9729.0f);
        GLES20.glTexParameteri(36197, 10242, 33071);
        GLES20.glTexParameteri(36197, 10243, 33071);
        this.mTextureIn = iArr[0];
        if (this.mSurfaceTexture != null) {
            this.mSurfaceTexture.release();
            this.mSurfaceTexture = null;
        }
        this.mSurfaceTexture = new SurfaceTexture(this.mTextureIn);
        this.mSurfaceTexture.setOnFrameAvailableListener(this);
        this.mSurface = new Surface(this.mSurfaceTexture);
        this.mPlayer.setSurface(this.mSurface);
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            public final void run() {
                VideoInput.lambda$initGLContext$3(VideoInput.this);
            }
        });
    }

    public static /* synthetic */ void lambda$initGLContext$3(VideoInput fX_VideoInput) {
        try {
            fX_VideoInput.mPlayer.prepareAsync();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onFrameAvailable(SurfaceTexture surfaceTexture) {
        this.mRender.requestRender();
        try {
            if (!this.loaded) {
                this.altemp.add(this.pause + "");
            } else if (this.frame < this.altemp.size()) {
                String str = this.altemp.get(this.frame);
                if (this.inTouch) {
                    this.altemp.set(this.frame, str + this.pause);
                    Helper.showLog("UUU", this.frame + " update : " + str + " to " + str + this.pause);
                }
                Helper.showLog("ZZZ", this.frame + " update : " + str + " to " + str + this.pause + " : " + this.totalframe);
                String str2 = this.altemp.get(this.frame);
                this.pause = Integer.parseInt(str2.substring(str2.length() + -1));
            } else if (this.totalframe == 0) {
                this.altemp.add(this.pause + "");
            }
        } catch (Exception unused) {
        }
        this.frame++;
        Helper.showLog("FFF", this.frame + " : " + this.altemp.size());
    }

    /* access modifiers changed from: protected */
    public void bindShaderValues() {
        super.bindShaderVertices();
        Helper.freeMemory();
        GLES20.glActiveTexture(33984);
        GLES20.glBindTexture(36197, this.mTextureIn);
        GLES20.glUniform1i(this.mTextureHandle, 0);
        GLES20.glUniform1i(this.mEffectHandle, this.effect);
        if (this.inTouch) {
            GLES20.glUniform1i(this.mPauseHandle, this.pause);
        } else {
            if (this.videoEffectTimeBar.isEffect()) {
                this.pause = 1;
            } else {
                this.pause = 0;
            }
            GLES20.glUniform1i(this.mPauseHandle, this.pause);
        }
        if (this.effect > 5) {
            cycleScale();
        }
        this.mSurfaceTexture.getTransformMatrix(this.mMatrix);
        GLES20.glUniformMatrix4fv(this.mMatrixHandle, 1, false, this.mMatrix, 0);
    }

    public void startEffect() {
        this.pause = 1;
        this.inTouch = true;
    }

    public void pauseEffect() {
        this.pause = 0;
        this.inTouch = false;
    }

    public void setZoomScale(float f) {
        this.msScale = f;
        GLES20.glUniform1f(this.mMsScaleHandle, this.msScale);
    }

    public void setReverse(boolean z) {
        this.isReverse = z;
        GLES20.glUniform1f(this.mReverseHandle, z ? 1.0f : 0.0f);
    }

    public void cycleScale() {
        int i = this.effect;
        if (i == 6) {
            double d = (double) this.msScale;
            double d2 = (double) 1.0f;
            Double.isNaN(d2);
            Double.isNaN(d2);
            Double.isNaN(d);
            Double.isNaN(d);
            this.msScale = (float) (d + (d2 * 0.1d));
            if (((double) this.msScale) >= 2.0d) {
                this.msScale = 1.0f;
            }
            setZoomScale(this.msScale);
        } else if (i == 7) {
            if (this.isAdd) {
                double d3 = (double) this.msScale;
                double d4 = (double) 1.0f;
                Double.isNaN(d4);
                Double.isNaN(d4);
                Double.isNaN(d3);
                Double.isNaN(d3);
                this.msScale = (float) (d3 + (d4 * 0.075d));
                if (((double) this.msScale) >= 1.0d) {
                    this.msScale = 1.0f;
                    this.isAdd = false;
                }
            } else {
                double d5 = (double) this.msScale;
                double d6 = (double) 1.0f;
                Double.isNaN(d6);
                Double.isNaN(d6);
                Double.isNaN(d5);
                Double.isNaN(d5);
                this.msScale = (float) (d5 - (d6 * 0.075d));
                if (this.msScale <= 0.0f) {
                    this.msScale = 0.0f;
                    this.isAdd = true;
                }
            }
            setZoomScale(this.msScale);
        } else if (i == 8) {
            this.msScale += 0.015f;
            if (this.msScale > 1.0f) {
                this.msScale = 0.0f;
            }
            setZoomScale(this.msScale);
        } else if (i == 9) {
            this.msScale += 0.05f;
            if (this.msScale > 4.0f) {
                this.msScale = 0.0f;
            }
            setZoomScale(this.msScale);
        } else if (i == 10 || i == 11) {
            this.reverseCnt++;
            if (((float) this.reverseCnt) > 12.0f) {
                this.reverseCnt = 0;
                setReverse(!this.isReverse);
            }
        }
    }

    public boolean isPlaying() {
        return this.mPlayer != null && this.mPlayer.isPlaying();
    }

    public void start() {
        if (!this.mReady || this.mPlayer == null) {
            this.mStartWhenReady = true;
        } else {
            this.mPlayer.start();
        }
    }

    public void pause() {
        if (this.mPlayer != null) {
            try {
                this.mPlayer.pause();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            }
        }
    }

    public void seekTo(int i) {
        if (this.mPlayer != null) {
            try {
                this.mPlayer.seekTo(i);
            } catch (IllegalStateException e) {
                e.printStackTrace();
            }
        }
    }

    public void stop() {
        this.frame = 0;
        this.loaded = true;
        if (this.mPlayer != null) {
            try {
                this.mPlayer.stop();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            }
        }
    }

    public void resetCount() {
        this.frame = 0;
        this.loaded = true;
    }

    public void reset() {
        if (this.mPlayer != null) {
            try {
                this.mPlayer.reset();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            }
        }
    }

    public void release() {
        this.mVideoUri = null;
        if (this.mPlayer != null) {
            this.mPlayer.release();
            this.mPlayer = null;
            this.mReady = false;
        }
    }

    public void destroy() {
        super.destroy();
        if (this.mSurfaceTexture != null) {
            this.mSurfaceTexture.release();
            this.mSurfaceTexture = null;
        }
        if (this.mSurface != null) {
            this.mSurface.release();
            this.mSurface = null;
        }
        if (this.mTextureIn != 0) {
            GLES20.glDeleteTextures(1, new int[]{this.mTextureIn}, 0);
            this.mTextureIn = 0;
        }
        release();
    }
}
