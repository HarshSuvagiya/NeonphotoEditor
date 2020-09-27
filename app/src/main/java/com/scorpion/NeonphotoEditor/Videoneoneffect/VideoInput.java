package com.scorpion.NeonphotoEditor.Videoneoneffect;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.net.Uri;
import android.opengl.GLES20;
import android.os.Handler;
import android.os.Looper;
import android.view.Surface;

import com.scorpion.NeonphotoEditor.Util.Helper;
import com.scorpion.NeonphotoEditor.Util.VideoEffectTimeBar;

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
        mRender = iGLEnvironment;
        initShader();
    }

    public VideoInput(Context context, IGLEnvironment iGLEnvironment, Uri uri) {
        mRender = iGLEnvironment;
        initShader();
        try {
            setVideoUri(context, uri);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public VideoInput(Context context, IGLEnvironment iGLEnvironment, Uri uri, IMediaPlayer IMediaPlayer) {
        mRender = iGLEnvironment;
        initShader();
        try {
            setVideoUri(context, uri, IMediaPlayer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initShader() {
        altemp.clear();
        frame = 0;
        loaded = false;
        totalframe = 0;
        setVertexShader("uniform mat4 u_Matrix;\nuniform float eu_Time;\nattribute vec4 position;\nattribute vec2 inputTextureCoordinate;\nattribute float ea_BirthTime;\nattribute float ea_Duration;\nvarying vec2 textureCoordinate;\nvarying float ev_Progress;\nvoid main() {\n   vec4 texPos = u_Matrix * vec4(inputTextureCoordinate, 1, 1);\n   textureCoordinate = texPos.xy;\n   float elapsedTime = eu_Time - ea_BirthTime;\n   ev_Progress = elapsedTime / ea_Duration;\n   gl_Position = position;\n}\n");
        setFragmentShader("#extension GL_OES_EGL_image_external : require\nprecision mediump float;\nuniform samplerExternalOES inputImageTexture;\nvarying vec2 textureCoordinate;\nvarying float ev_Progress;\nuniform int effect;uniform int pause;uniform float mscale;uniform float reverse;void main() {\nvec2 texture = textureCoordinate;\nvec4 base = texture2D(inputImageTexture, texture);\nfloat x = -1.0;\nfloat y = -1.0;\nfloat mixturePercent = 1.0;\nvec4 base2;\n\nif(pause == 1) {\n    if (effect == 1) {\n\n        if (texture.x > 0.5) {\n            texture.x = texture.x - 0.25;\n        } else {\n            texture.x = texture.x + 0.25;\n        }\n           base = texture2D(inputImageTexture, texture);\n\n    } else if (effect == 2) {\n\n        if (texture.y > 0.5) {\n            texture.y = texture.y - 0.25;\n        } else {\n            texture.y = texture.y + 0.25;\n        }\n           base = texture2D(inputImageTexture, texture);\n\n    } else if (effect == 3) {\n\n        if (texture.x < 0.33) {\n            texture.x = texture.x + 0.33;\n        } else if (texture.x > 0.66) {\n            texture.x = texture.x - 0.33;\n        }\n           base = texture2D(inputImageTexture, texture);\n\n    } else if (effect == 4) {\n        if (texture.x <= 0.5 && texture.y <= 0.5) {\n            texture.x = texture.x + 0.25;\n            texture.y = texture.y + 0.25;\n        } else if (texture.y > 0.5 && texture.x <= 0.5) {\n            texture.x = texture.x + 0.25;\n            texture.y = texture.y - 0.25;\n        } else if (texture.y > 0.5 && texture.x > 0.5) {\n            texture.x = texture.x - 0.25;\n            texture.y = texture.y - 0.25;\n        } else {\n            texture.x = texture.x - 0.25;\n            texture.y = texture.y + 0.25;\n        }\n           base = texture2D(inputImageTexture, texture);\n\n    } else if (effect == 5) {\n\n        if (texture.x > 0.66) {\n            texture.x = texture.x - 0.33;\n\n            if (texture.y > 0.66) {\n                texture.y = texture.y - 0.33;\n            } else if (texture.y <= 0.33) {\n                texture.y = texture.y + 0.33;\n            }\n\n           base = texture2D(inputImageTexture, texture);\n\n        } else if (texture.x > 0.33) {\n\n            if (texture.y > 0.66) {\n                texture.y = texture.y - 0.33;\n            } else if (texture.y <= 0.33) {\n                texture.y = texture.y + 0.33;\n            }\n\n           base = texture2D(inputImageTexture, texture);\n\n        } else {\n\n            texture.x = texture.x + 0.33;\n            if (texture.y > 0.66) {\n                texture.y = texture.y - 0.33;\n            } else if (texture.y <= 0.33) {\n                texture.y = texture.y + 0.33;\n            }\n\n           base = texture2D(inputImageTexture, texture);\n\n        }\n    } else if (effect == 6) {\n\n           x = ((mscale - 1.0) * 0.5 + texture.x) / mscale;\n           y = ((mscale - 1.0) * 0.5 + texture.y) / mscale;\n    } else if (effect == 7) {\n\n           x = 0.5;\n           y = 0.5;\n           vec2 newCoor = texture.xy;\n           newCoor.x = texture.x - 0.05 * mscale;\n           base2 = base;\n           if (newCoor.x >= 0.0) base2 = texture2D(inputImageTexture, newCoor.xy);\n           newCoor.x = texture.x + 0.1 * mscale;\n           vec4 base3 = base;\n           if (newCoor.x <= 1.0) base3 = texture2D(inputImageTexture, newCoor.xy);\n           base2 = vec4(mix(base2.rgb, base3.rgb, 0.5), base.a);\n    } else if (effect == 8) {\n\n       if (texture.x > mscale) {\n           x = 0.5;\n           y = 0.5;\n       }\n    } else if (effect == 9) {\n\n       if (texture.x <= 0.5 && texture.y <= 0.5) {\n           texture.x = texture.x + 0.25;\n           texture.y = texture.y + 0.25;\n           base = texture2D(inputImageTexture, texture);\n           if (mscale > 0.99) {\n               x = 0.5;\n               y = 0.5;\n           }\n       } else if (texture.y > 0.5 && texture.x <= 0.5) {\n           texture.x = texture.x + 0.25;\n           texture.y = texture.y - 0.25;\n           base = texture2D(inputImageTexture, texture);\n           if (mscale > 2.99 || mscale < 1.99) {\n               x = 0.5;\n               y = 0.5;\n           }\n       } else if (texture.y > 0.5 && texture.x > 0.5) {\n           texture.x = texture.x - 0.25;\n           texture.y = texture.y - 0.25;\n           base = texture2D(inputImageTexture, texture);\n           if (mscale < 2.99) {\n               x = 0.5;\n               y = 0.5;\n           }\n       } else {\n           texture.x = texture.x - 0.25;\n           texture.y = texture.y + 0.25;\n           base = texture2D(inputImageTexture, texture);\n           if (mscale > 1.99 || mscale < 0.99) {\n               x = 0.5;\n               y = 0.5;\n           }\n       }\n    } else if (effect == 10) {\n\n       if (reverse > 0.5) {\n               x = 0.5;\n               y = 0.5;\n       }\n    } else if (effect == 11) {\n\n       if (texture.x < 0.33) {\n           texture.x = texture.x + 0.33;\n           base = texture2D(inputImageTexture, texture.xy);\n           if (reverse > 0.5)           base = vec4(1.0 - base.r, 1.0 - base.g, 1.0 - base.b, base.a);\n       } else if (texture.x > 0.66) {\n           texture.x = texture.x - 0.33;\n           base = texture2D(inputImageTexture, texture.xy);\n           if (reverse > 0.5)           base = vec4(1.0 - base.r, 1.0 - base.g, 1.0 - base.b, base.a);\n       }\n    } else if (effect == 12) {\n\n       if (texture.x < 0.5) {\n           texture.x = 1.0 - texture.x;\n           base = texture2D(inputImageTexture, texture);\n       }\n    }\n}\n\n   if (x > 1.0 || y > 1.0 || x < 0.0 || y < 0.0) {\n\n       gl_FragColor = base;\n\n   } else {\n\n       if (effect == 6) {\n           vec4 overlay = texture2D(inputImageTexture, vec2(x, y));\n           gl_FragColor = vec4(mix(base.rgb, overlay.rgb, base.a * 0.5), base.a);\n       } else if (effect == 7) {\n           gl_FragColor = vec4(mix(base.rgb, base2.rgb, 0.5), base.a);\n       } else if (effect == 8 || effect == 9) {\n           highp float cintensity = 0.299 * base.r + 0.587 * base.g + 0.114 * base.b;\n           gl_FragColor = vec4(cintensity, cintensity, cintensity, base.a);\n       } else if (effect == 10) {\n           vec4 newColor = vec4(1.0 - base.r, 1.0 - base.g, 1.0 - base.b, base.a);\n           gl_FragColor = vec4(mix(base.rgb, newColor.rgb, base.a * mixturePercent), base.a);\n       }\n   }\n\n}\n");
    }

    public void setOnPreparedListener(IMediaPlayer.OnPreparedListener onPreparedListener) {
        mPreparedListener = onPreparedListener;
    }

    public void setOnCompletionListener(IMediaPlayer.OnCompletionListener onCompletionListener) {
        mCompletionListener = onCompletionListener;
    }

    public void setOnErrorListener(IMediaPlayer.OnErrorListener onErrorListener) {
        mErrorListener = onErrorListener;
    }

    public void setiEffectTimeController(IEffectTimeController iEffectTimeController2) {
        iEffectTimeController = iEffectTimeController2;
    }

    public void setVideoEffectTimeBar(VideoEffectTimeBar VideoEffectTimeBar) {
        videoEffectTimeBar = VideoEffectTimeBar;
    }

    public void setVideoUri(Context context, Uri uri, IMediaPlayer IMediaPlayer) throws IOException {
        if (uri != null) {
            release();
            mVideoUri = uri;
            mPlayer = IMediaPlayer;
            mPlayer.setDataSource(context, mVideoUri);
            mPlayer.setLooping(mIsLoop);
            mPlayer.setVolume(mVideoVolumeLeft, mVideoVolumeRight);
            mPlayer.setOnPreparedListener(new IMediaPlayer.OnPreparedListener() {
                public final void onPrepared(IMediaPlayer IMediaPlayer) {
                    VideoInput.lambda$setVideoUri$0(VideoInput.this, IMediaPlayer);
                }
            });
            mPlayer.setOnCompletionListener(new IMediaPlayer.OnCompletionListener() {
                public final void onCompletion(IMediaPlayer IMediaPlayer) {
                    VideoInput.lambda$setVideoUri$1(VideoInput.this, IMediaPlayer);
                }
            });
            mPlayer.setOnErrorListener(new IMediaPlayer.OnErrorListener() {
                public final boolean onError(IMediaPlayer IMediaPlayer, int i, int i2) {
                    return VideoInput.lambda$setVideoUri$2(VideoInput.this, IMediaPlayer, i, i2);
                }
            });
            reInit();
            mRender.requestRender();
        }
    }

    public static  void lambda$setVideoUri$0(VideoInput VideoInput, IMediaPlayer IMediaPlayer) {
        VideoInput.mReady = true;
        if (VideoInput.mStartWhenReady) {
            IMediaPlayer.start();
        }
        VideoInput.setRenderSize(VideoInput.mPlayer.getVideoWidth(), VideoInput.mPlayer.getVideoHeight());
        if (VideoInput.mPreparedListener != null) {
            VideoInput.mPreparedListener.onPrepared(IMediaPlayer);
        }
    }

    public static /* synthetic */ void lambda$setVideoUri$1(VideoInput VideoInput, IMediaPlayer IMediaPlayer) {
        if (VideoInput.mCompletionListener != null) {
            VideoInput.mCompletionListener.onCompletion(IMediaPlayer);
            if (VideoInput.totalframe == 0) {
                VideoInput.totalframe = VideoInput.frame;
            }
            VideoInput.frame = 0;
            VideoInput.loaded = true;
        }
    }

    public static boolean lambda$setVideoUri$2(VideoInput VideoInput, IMediaPlayer IMediaPlayer, int i, int i2) {
        return VideoInput.mErrorListener == null || VideoInput.mErrorListener.onError(IMediaPlayer, i, i2);
    }

    public void setVideoUri(Context context, Uri uri) throws IOException {
        setVideoUri(context, uri, new DefaultMediaPlayer());
    }

    public IMediaPlayer getMediaPlayer() {
        return mPlayer;
    }

    public Uri getVideoUri() {
        return mVideoUri;
    }

    public void setStartWhenReady(boolean z) {
        mStartWhenReady = z;
    }

    public void setLoop(boolean z) {
        mIsLoop = z;
        if (mPlayer != null) {
            mPlayer.setLooping(mIsLoop);
        }
    }

    public void setEffect(int i) {
        effect = i;
        Helper.showLog("WWW", "Effect Set : " + i);
    }

    public void setVolume(float f, float f2) {
        mVideoVolumeLeft = f;
        mVideoVolumeRight = f2;
        if (mPlayer != null) {
            mPlayer.setVolume(mVideoVolumeLeft, mVideoVolumeRight);
        }
    }
    
    public void drawFrame() {
        try {
            mSurfaceTexture.updateTexImage();
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
        return mSurfaceTexture;
    }
    
    public void initShaderHandles() {
        super.initShaderHandles();
        mMatrixHandle = GLES20.glGetUniformLocation(mProgramHandle, UNIFORM_CAM_MATRIX);
    }
    
    public void initGLContext() {
        super.initGLContext();
        mReady = false;
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
        mSurfaceTexture.setOnFrameAvailableListener(this);
        mSurface = new Surface(mSurfaceTexture);
        mPlayer.setSurface(mSurface);
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            public final void run() {
                VideoInput.lambda$initGLContext$3(VideoInput.this);
            }
        });
    }

    public static  void lambda$initGLContext$3(VideoInput VideoInput) {
        try {
            VideoInput.mPlayer.prepareAsync();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onFrameAvailable(SurfaceTexture surfaceTexture) {
        mRender.requestRender();
        try {
            if (!loaded) {
                altemp.add(pause + "");
            } else if (frame < altemp.size()) {
                String str = altemp.get(frame);
                if (inTouch) {
                    altemp.set(frame, str + pause);
                    Helper.showLog("UUU", frame + " update : " + str + " to " + str + pause);
                }
                Helper.showLog("ZZZ", frame + " update : " + str + " to " + str + pause + " : " + totalframe);
                String str2 = altemp.get(frame);
                pause = Integer.parseInt(str2.substring(str2.length() + -1));
            } else if (totalframe == 0) {
                altemp.add(pause + "");
            }
        } catch (Exception unused) {
        }
        frame++;
        Helper.showLog("FFF", frame + " : " + altemp.size());
    }
    
    public void bindShaderValues() {
        super.bindShaderVertices();
        Helper.freeMemory();
        GLES20.glActiveTexture(33984);
        GLES20.glBindTexture(36197, mTextureIn);
        GLES20.glUniform1i(mTextureHandle, 0);
        GLES20.glUniform1i(mEffectHandle, effect);
        if (inTouch) {
            GLES20.glUniform1i(mPauseHandle, pause);
        } else {
            if (videoEffectTimeBar.isEffect()) {
                pause = 1;
            } else {
                pause = 0;
            }
            GLES20.glUniform1i(mPauseHandle, pause);
        }
        if (effect > 5) {
            cycleScale();
        }
        mSurfaceTexture.getTransformMatrix(mMatrix);
        GLES20.glUniformMatrix4fv(mMatrixHandle, 1, false, mMatrix, 0);
    }

    public void startEffect() {
        pause = 1;
        inTouch = true;
    }

    public void pauseEffect() {
        pause = 0;
        inTouch = false;
    }

    public void setZoomScale(float f) {
        msScale = f;
        GLES20.glUniform1f(mMsScaleHandle, msScale);
    }

    public void setReverse(boolean z) {
        isReverse = z;
        GLES20.glUniform1f(mReverseHandle, z ? 1.0f : 0.0f);
    }

    public void cycleScale() {
        int i = effect;
        if (i == 6) {
            double d = (double) msScale;
            double d2 = (double) 1.0f;
            Double.isNaN(d2);
            Double.isNaN(d2);
            Double.isNaN(d);
            Double.isNaN(d);
            msScale = (float) (d + (d2 * 0.1d));
            if (((double) msScale) >= 2.0d) {
                msScale = 1.0f;
            }
            setZoomScale(msScale);
        } else if (i == 7) {
            if (isAdd) {
                double d3 = (double) msScale;
                double d4 = (double) 1.0f;
                Double.isNaN(d4);
                Double.isNaN(d4);
                Double.isNaN(d3);
                Double.isNaN(d3);
                msScale = (float) (d3 + (d4 * 0.075d));
                if (((double) msScale) >= 1.0d) {
                    msScale = 1.0f;
                    isAdd = false;
                }
            } else {
                double d5 = (double) msScale;
                double d6 = (double) 1.0f;
                Double.isNaN(d6);
                Double.isNaN(d6);
                Double.isNaN(d5);
                Double.isNaN(d5);
                msScale = (float) (d5 - (d6 * 0.075d));
                if (msScale <= 0.0f) {
                    msScale = 0.0f;
                    isAdd = true;
                }
            }
            setZoomScale(msScale);
        } else if (i == 8) {
            msScale += 0.015f;
            if (msScale > 1.0f) {
                msScale = 0.0f;
            }
            setZoomScale(msScale);
        } else if (i == 9) {
            msScale += 0.05f;
            if (msScale > 4.0f) {
                msScale = 0.0f;
            }
            setZoomScale(msScale);
        } else if (i == 10 || i == 11) {
            reverseCnt++;
            if (((float) reverseCnt) > 12.0f) {
                reverseCnt = 0;
                setReverse(!isReverse);
            }
        }
    }

    public boolean isPlaying() {
        return mPlayer != null && mPlayer.isPlaying();
    }

    public void start() {
        if (!mReady || mPlayer == null) {
            mStartWhenReady = true;
        } else {
            mPlayer.start();
        }
    }

    public void pause() {
        if (mPlayer != null) {
            try {
                mPlayer.pause();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            }
        }
    }

    public void seekTo(int i) {
        if (mPlayer != null) {
            try {
                mPlayer.seekTo(i);
            } catch (IllegalStateException e) {
                e.printStackTrace();
            }
        }
    }

    public void stop() {
        frame = 0;
        loaded = true;
        if (mPlayer != null) {
            try {
                mPlayer.stop();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            }
        }
    }

    public void resetCount() {
        frame = 0;
        loaded = true;
    }

    public void reset() {
        if (mPlayer != null) {
            try {
                mPlayer.reset();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            }
        }
    }

    public void release() {
        mVideoUri = null;
        if (mPlayer != null) {
            mPlayer.release();
            mPlayer = null;
            mReady = false;
        }
    }

    public void destroy() {
        super.destroy();
        if (mSurfaceTexture != null) {
            mSurfaceTexture.release();
            mSurfaceTexture = null;
        }
        if (mSurface != null) {
            mSurface.release();
            mSurface = null;
        }
        if (mTextureIn != 0) {
            GLES20.glDeleteTextures(1, new int[]{mTextureIn}, 0);
            mTextureIn = 0;
        }
        release();
    }
}
