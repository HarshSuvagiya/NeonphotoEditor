package com.scorpion.NeonphotoEditor;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import cn.ezandroid.ezfilter.media.record.ISupportRecord;
import com.airbnb.lottie.LottieAnimationView;
import com.netcompss.ffmpeg4android.CommandExecutor;
import com.netcompss.ffmpeg4android.FfResponse;
import com.netcompss.ffmpeg4android.GeneralUtils;
import com.scorpion.NeonphotoEditor.Adapters.VideoStickerAdapter;
import com.scorpion.NeonphotoEditor.Adapters.ExtraUsedAdapter;
import com.scorpion.NeonphotoEditor.Videoneoneffect.EZFilter;
import com.scorpion.NeonphotoEditor.Videoneoneffect.GLRender;
import com.scorpion.NeonphotoEditor.Videoneoneffect.IMediaPlayer;
import com.scorpion.NeonphotoEditor.Videoneoneffect.RenderPipeline;
import com.scorpion.NeonphotoEditor.Videoneoneffect.TextureFitView;
import com.scorpion.NeonphotoEditor.Videoneoneffect.VideoInput;
import com.scorpion.NeonphotoEditor.Videoneoneffect.VideoRender.StickerRender;
import com.scorpion.NeonphotoEditor.Videoneoneffect.Sticker.IStickerTimeController;
import com.scorpion.NeonphotoEditor.Videoneoneffect.Sticker.StickerHelper;
import com.scorpion.NeonphotoEditor.Util.Constant;
import com.scorpion.NeonphotoEditor.Util.Helper;
import com.scorpion.NeonphotoEditor.Util.RecyclerTouchListener;
import com.scorpion.NeonphotoEditor.Util.SetLayparam;
import com.scorpion.NeonphotoEditor.Util.VideoEffectTimeBar;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class NeonVideoEffectEditor extends Activity {
    ArrayList<StickerRender> alrender = new ArrayList<>();
    ArrayList<String> alused = new ArrayList<>();
    Context context;
    long duration;
    long end;
    TextView header;
    int height;
    String inputpath;
    ImageView ivback;
    ImageView ivdelete;
    ImageView ivdone;
    ImageView ivedit;
    ImageView ivplay;
    ImageView ivsicon;
    LottieAnimationView lav;
    LinearLayout lcontainer;
    LinearLayout leffectbar;
    LinearLayout lop;
    LinearLayout lpbar;
    LinearLayout lsize;
    RenderPipeline mRenderPipeline;
    TextureFitView mRenderView;
    StickerRender mStickerRender;
    ISupportRecord mSupportRecord;
    Timer mTimer;
    boolean mTouchingSeekBar;
    boolean mTouchingTextureView;
    VideoInput mVideoInput;
    File outfile;
    RecyclerView rcvs;
    RecyclerView rcvu;
    StickerHelper sHelper;
    IStickerTimeController sTimeControl;
    VideoStickerAdapter sadapter;
    SeekBar sbsize;
    long start;
    int sticker;
    ExtraUsedAdapter uadapter;
    StickerRender usedRender;
    int usticker = -1;
    VideoEffectTimeBar vseek;
    int width;

    public void nothing(View view) {
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_sticker_preview);
        getWindow().setFlags(1024, 1024);
        this.context = this;

        this.header = (TextView) findViewById(R.id.my_header_text);
        this.ivdone = (ImageView) findViewById(R.id.ivoption);
        this.ivback = (ImageView) findViewById(R.id.ivback);
        this.ivplay = (ImageView) findViewById(R.id.ivplay);
        this.mRenderView = (TextureFitView) findViewById(R.id.render_view);
        this.lpbar = (LinearLayout) findViewById(R.id.lpbar);
        this.leffectbar = (LinearLayout) findViewById(R.id.leffectbar);
        this.vseek = (VideoEffectTimeBar) findViewById(R.id.vseek);
        this.rcvs = (RecyclerView) findViewById(R.id.rcvsticker);
        this.lsize = (LinearLayout) findViewById(R.id.linearSize);
        this.rcvu = (RecyclerView) findViewById(R.id.rcvused);
        this.sbsize = (SeekBar) findViewById(R.id.seeksize);
        this.lop = (LinearLayout) findViewById(R.id.linearop);
        this.ivedit = (ImageView) findViewById(R.id.ivedit);
        this.ivsicon = (ImageView) findViewById(R.id.ivsicon);
        this.ivdelete = (ImageView) findViewById(R.id.ivdelete);
        this.lav = (LottieAnimationView) findViewById(R.id.animfront);
        this.lcontainer = (LinearLayout) findViewById(R.id.lcontainer);
        this.width = Helper.getWidth(this.context);
        this.height = Helper.getHeight(this.context);
        forUI();
        init();
    }

    private void forUI() {
        SetLayparam.setMargins(this.context, this.lcontainer, 60, 60, 60, 0);
        SetLayparam.setMargins(this.context, this.lop, 60, 45, 60, 45);
        SetLayparam.setHeightAsBoth(this.context, this.ivplay, 120);
        SetLayparam.setHeightWidth(this.context, this.leffectbar, 808, 184);
        SetLayparam.setPadding(this.context, this.leffectbar, 20);
        SetLayparam.setHeightWidth(this.context, this.ivedit, 226, CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE);
        SetLayparam.setHeight(this.context, this.rcvs, CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE);
        SetLayparam.setHeight(this.context, this.rcvu, CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE);
        SetLayparam.setHeight(this.context, this.lsize, 92);
        SetLayparam.setPadding(this.context, this.lsize, 30, 0, 23, 0);
        SetLayparam.setMargins(this.context, this.lsize, 0, 23, 0, 20);
        SetLayparam.setHeightAsBoth(this.context, this.ivsicon, 90);
        SetLayparam.setHeightAsBoth(this.context, this.ivdelete, 90);
    }

    private void init() {
        this.header.setText(R.string.app_name);
        this.ivdone.setVisibility(0);
        this.header.setTypeface(Typeface.createFromAsset(getAssets(), "Poppins-Bold.ttf"));
        this.alused.clear();
        setAdapter();
        setListeners();
        this.sTimeControl = new IStickerTimeController() {
            public float getCurrentTime() {
                return ((float) NeonVideoEffectEditor.this.mVideoInput.getMediaPlayer().getCurrentPosition()) / 1000.0f;
            }
        };
        this.inputpath = getIntent().getStringExtra("path");
        this.duration = Long.parseLong(getIntent().getStringExtra("duration"));
        this.vseek.setMaxValue(this.duration);
        this.vseek.beginPreView(this.inputpath);
        this.vseek.setCanSeek(true);
        this.outfile = new File(Helper.getTempFolder(this.context), "woa.mp4");
        if (this.outfile.exists()) {
            this.outfile.delete();
        }
        new Thread() {
            public void run() {
                NeonVideoEffectEditor.this.mRenderPipeline = EZFilter.input(Uri.parse(NeonVideoEffectEditor.this.inputpath)).setLoop(false).enableRecord(NeonVideoEffectEditor.this.outfile.getAbsolutePath(), true, false).setPreparedListener(new IMediaPlayer.OnPreparedListener() {
                    public void onPrepared(IMediaPlayer IMediaPlayer1) {
                        NeonVideoEffectEditor.this.mVideoInput.seekTo(1);
                        NeonVideoEffectEditor.this.pauseVideo();
                    }
                }).setCompletionListener(new IMediaPlayer.OnCompletionListener() {
                    public void onCompletion(IMediaPlayer IMediaPlayer1) {
                        if (!NeonVideoEffectEditor.this.mTouchingSeekBar && !NeonVideoEffectEditor.this.mTouchingTextureView) {
                            NeonVideoEffectEditor.this.mVideoInput.seekTo(0);
                            NeonVideoEffectEditor.this.ivplay.setImageResource(R.drawable.play_edit);
                            NeonVideoEffectEditor.this.runOnUiThread(new Runnable() {
                                public void run() {
                                    if (NeonVideoEffectEditor.this.mSupportRecord.isRecording()) {
                                        Helper.showLog("SSS", "Saving Stopped");
                                        NeonVideoEffectEditor.this.stopRecording();
                                    }
                                }
                            });
                        }
                    }
                }).setVideoEffectTimeBar(NeonVideoEffectEditor.this.vseek).into(NeonVideoEffectEditor.this.mRenderView);
                NeonVideoEffectEditor.this.mVideoInput = (VideoInput) NeonVideoEffectEditor.this.mRenderPipeline.getStartPointRender();
                for (GLRender next : NeonVideoEffectEditor.this.mRenderPipeline.getEndPointRenders()) {
                    if (next instanceof ISupportRecord) {
                        NeonVideoEffectEditor.this.mSupportRecord = (ISupportRecord) next;
                    }
                }
            }
        }.start();
    }

    private void setAdapter() {
        this.rcvs.setLayoutManager(new LinearLayoutManager(this.context, 0, false));
        this.rcvu.setLayoutManager(new LinearLayoutManager(this.context, 0, false));
        this.sadapter = new VideoStickerAdapter(this.context, Constant.hslist);
        this.rcvs.setAdapter(this.sadapter);
        this.uadapter = new ExtraUsedAdapter(this.context, this.alused);
        this.rcvu.setAdapter(this.uadapter);
        this.rcvs.addOnItemTouchListener(new RecyclerTouchListener(this.context, this.rcvs, new RecyclerTouchListener.ClickListener() {
            public void onLongClick(View view, int i) {
            }

            public void onClick(View view, int i) {
                NeonVideoEffectEditor.this.setSticker(i);
            }
        }));
        this.rcvu.addOnItemTouchListener(new RecyclerTouchListener(this.context, this.rcvu, new RecyclerTouchListener.ClickListener() {
            public void onLongClick(View view, int i) {
            }

            public void onClick(View view, int i) {
                NeonVideoEffectEditor.this.setUsedSticker(i);
            }
        }));
    }

    @SuppressLint({"ClickableViewAccessibility"})
    private void setListeners() {
        this.mRenderView.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case 0:
                        NeonVideoEffectEditor.this.mTouchingTextureView = true;
                        NeonVideoEffectEditor.this.preTouchOperation();
                        NeonVideoEffectEditor.this.mStickerRender = NeonVideoEffectEditor.this.sHelper.getParticleRenderer(NeonVideoEffectEditor.this.sticker);
                        NeonVideoEffectEditor.this.startVideo();
                        NeonVideoEffectEditor.this.mStickerRender.setEffectTimeBar(NeonVideoEffectEditor.this.vseek);
                        NeonVideoEffectEditor.this.mStickerRender.start();
                        NeonVideoEffectEditor.this.alrender.add(NeonVideoEffectEditor.this.mStickerRender);
                        NeonVideoEffectEditor.this.mStickerRender.setPosition(Math.round(((motionEvent.getX() * ((float) NeonVideoEffectEditor.this.mStickerRender.getWidth())) * 1.0f) / ((float) NeonVideoEffectEditor.this.mRenderView.getWidth())), Math.round(((motionEvent.getY() * ((float) NeonVideoEffectEditor.this.mStickerRender.getHeight())) * 1.0f) / ((float) NeonVideoEffectEditor.this.mRenderView.getHeight())));
                        NeonVideoEffectEditor.this.start = (long) NeonVideoEffectEditor.this.mVideoInput.getMediaPlayer().getCurrentPosition();
                        NeonVideoEffectEditor.this.vseek.setTouch(true, NeonVideoEffectEditor.this.start);
                        if (!NeonVideoEffectEditor.this.mRenderPipeline.getFilterRenders().contains(NeonVideoEffectEditor.this.mStickerRender)) {
                            NeonVideoEffectEditor.this.mRenderPipeline.addFilterRender(NeonVideoEffectEditor.this.mStickerRender);
                            break;
                        }
                        break;
                    case 1:
                    case 3:
                        NeonVideoEffectEditor.this.mTouchingTextureView = false;
                        if (NeonVideoEffectEditor.this.mStickerRender != null) {
                            NeonVideoEffectEditor.this.mStickerRender.setPosition(-2000, -2000);
                            new Handler().postDelayed(new Runnable() {
                                public void run() {
                                    NeonVideoEffectEditor.this.mStickerRender.pause();
                                    NeonVideoEffectEditor.this.mStickerRender = null;
                                }
                            }, 200);
                        }
                        NeonVideoEffectEditor.this.end = (long) NeonVideoEffectEditor.this.mVideoInput.getMediaPlayer().getCurrentPosition();
                        NeonVideoEffectEditor.this.vseek.setTouch(false, NeonVideoEffectEditor.this.end);
                        break;
                    case 2:
                        if (NeonVideoEffectEditor.this.mStickerRender != null) {
                            NeonVideoEffectEditor.this.mStickerRender.setPosition(Math.round(((motionEvent.getX() * ((float) NeonVideoEffectEditor.this.mStickerRender.getWidth())) * 1.0f) / ((float) NeonVideoEffectEditor.this.mRenderView.getWidth())), Math.round(((motionEvent.getY() * ((float) NeonVideoEffectEditor.this.mStickerRender.getHeight())) * 1.0f) / ((float) NeonVideoEffectEditor.this.mRenderView.getHeight())));
                            break;
                        }
                        break;
                }
                return true;
            }
        });
        this.vseek.setSeekBarChangeListener(new VideoEffectTimeBar.SeekBarChangeListener() {
            public void seekBarValueChanged(int i) {
                NeonVideoEffectEditor.this.mVideoInput.getMediaPlayer().seekTo(i);
            }

            public void onSeeking(boolean z) {
                if (z) {
                    NeonVideoEffectEditor.this.pauseVideo();
                } else {
                    NeonVideoEffectEditor.this.startVideo();
                }
            }
        });
    }

    public void preTouchOperation() {
        if (this.sHelper == null) {
            this.sHelper = new StickerHelper(this.context, this.sTimeControl);
        }
        if (this.mStickerRender != null) {
            this.mStickerRender.pause();
        }
        int size = this.alused.size();
        this.alused.add(size, Constant.hslist.get(Integer.valueOf(this.sticker)));
        this.uadapter.notifyItemInserted(size);
    }

    public void setSticker(int i) {
        if (this.mStickerRender != null) {
            this.mStickerRender.pause();
        }
        Helper.freeMemory();
        this.sticker = i;
        this.sadapter.setSelected(this.sticker);
        this.sHelper = new StickerHelper(this.context, this.sTimeControl);
    }

    public void setUsedSticker(int i) {
        Helper.freeMemory();
        this.usticker = i;
        this.uadapter.setSelected(this.usticker);
        this.usedRender = null;
        this.usedRender = this.alrender.get(this.usticker);
        if (this.usedRender != null) {
            this.sbsize.setMax(this.usedRender.getOriginalSize());
        } else {
            this.sbsize.setMax(Constant.NORMAL_STICKER_SIZE);
        }
        this.sbsize.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                int i2 = i + 200;
                if (NeonVideoEffectEditor.this.usedRender != null) {
                    NeonVideoEffectEditor.this.usedRender.setSize(i2);
                }
            }
        });
        this.sbsize.setProgress(this.usedRender.getSize() - 200);
    }

    public void play(View view) {
        if (this.mVideoInput.isPlaying()) {
            pauseVideo();
        } else {
            startVideo();
        }
    }

    public void size(View view) {
        if (this.lsize.getVisibility() == View.VISIBLE) {
            this.ivedit.setImageResource(R.drawable.edit_unpress);
            this.lsize.setVisibility(View.INVISIBLE);
            this.rcvu.setVisibility(View.GONE);
            this.rcvs.setVisibility(View.VISIBLE);
            return;
        }
        this.ivedit.setImageResource(R.drawable.stickers_unpress);
        this.rcvs.setVisibility(View.GONE);
        this.lsize.setVisibility(View.VISIBLE);
        this.rcvu.setVisibility(View.VISIBLE);
    }

    public void delete(View view) {
        if (this.usticker >= 0) {
            this.mRenderPipeline.removeFilterRender(this.alrender.get(this.usticker));
            this.alrender.remove(this.usticker);
            this.vseek.removeEffectRange(this.usticker);
            this.alused.remove(this.usticker);
            this.uadapter.notifyItemRemoved(this.usticker);
            this.usticker = -1;
            this.usedRender = null;
        }
    }

    public void option(View view) {
         if (!this.mSupportRecord.isRecording()) {
            this.lpbar.setVisibility(View.VISIBLE);
            this.ivback.setVisibility(View.INVISIBLE);
            this.ivdone.setVisibility(View.INVISIBLE);
            if (this.mSupportRecord != null) {
                Helper.showLog("SSS", "Saving Start");
                this.mVideoInput.getMediaPlayer().setVolume(0.0f, 0.0f);
                stopVideo();
                startRecording();
            }
        }
    }

    public void startRecording() {
        Helper.freeMemory();
        Log.i("RRR", "startRecording:");
        if (this.mSupportRecord != null) {
            this.mSupportRecord.startRecording();
        }
    }

    public void stopRecording() {
        Helper.freeMemory();
        Log.i("RRR", "stopRecording: ");
        if (this.mSupportRecord != null) {
            this.mSupportRecord.stopRecording();
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    NeonVideoEffectEditor.this.mixAudio();
                }
            }, 2000);
        }
    }

    public void mixAudio() {
        String outputFolder = Helper.getOutputFolder(this.context);
        final File file = new File(outputFolder, new Random().nextInt(1000) + "" + System.currentTimeMillis() + "_" + getResources().getString(R.string.app_name) + ".mp4");
        if (file.exists()) {
            file.delete();
        }
        GeneralUtils.deleteLicFile(this.context);
        new CommandExecutor(this.context, new String[]{"ffmpeg", "-i", this.outfile.getAbsolutePath(), "-i", this.inputpath, "-c", "copy", "-map", "0:v:0", "-map", "1:a:0", "-shortest", file.getAbsolutePath()}, new FfResponse() {
            public void onSuccess() {
                Helper.showLog("SSS", "Mixing Audio Success");
                NeonVideoEffectEditor.this.resultReady(file);
            }

            public void onError(String str) {
                Helper.showLog("SSS", "Mixing Audio Failed : " + str);
                try {
                    Helper.copyFile(NeonVideoEffectEditor.this.outfile.getAbsolutePath(), file.getAbsolutePath());
                    NeonVideoEffectEditor.this.resultReady(file);
                } catch (Exception unused) {
                    Helper.show(NeonVideoEffectEditor.this.context, "Error while saving video !!");
                    NeonVideoEffectEditor.this.lpbar.setVisibility(View.GONE);
                    NeonVideoEffectEditor.this.ivback.setVisibility(View.VISIBLE);
                    NeonVideoEffectEditor.this.ivdone.setVisibility(View.VISIBLE);
                }
            }

            public void onStop() {
                Helper.showLog("SSS", "Mixing Audio Stopped");
            }

            public void onStart() {
                Helper.showLog("SSS", "Mixing Audio Start");
            }
        }).execute(new Void[0]);
    }

    public void resultReady(final File file) {
        MediaScannerConnection.scanFile(this.context, new String[]{file.getAbsolutePath()}, new String[]{"mp4"}, new MediaScannerConnection.OnScanCompletedListener() {
            public void onScanCompleted(String str, Uri uri) {
                Helper.deleteFolder(new File(Helper.getTempFolder(NeonVideoEffectEditor.this.context)));
                Helper.deleteFolder(NeonVideoEffectEditor.this.outfile);
                NeonVideoEffectEditor.this.runOnUiThread(new Runnable() {
                    public void run() {
                        NeonVideoEffectEditor.this.lpbar.setVisibility(View.GONE);
                        NeonVideoEffectEditor.this.ivback.setVisibility(View.VISIBLE);
                        NeonVideoEffectEditor.this.ivdone.setVisibility(View.VISIBLE);
                        Intent intent = new Intent(NeonVideoEffectEditor.this.context, NeonVideoPreview.class);
                        intent.putExtra("vpath", file.getAbsolutePath());
                        intent.putExtra("fromedit", true);
                        NeonVideoEffectEditor.this.startActivity(intent);
                    }
                });
            }
        });
    }

    private void startTimer() {
        if (this.mTimer != null) {
            this.mTimer.cancel();
        }
        this.mTimer = new Timer();
        this.mTimer.schedule(new TimerTask() {
            public void run() {
                try {
                    NeonVideoEffectEditor.this.vseek.videoPlayingProgress(NeonVideoEffectEditor.this.mVideoInput.getMediaPlayer().getCurrentPosition());
                } catch (Exception unused) {
                }
            }
        }, 0, 50);
    }

    private void cancelTimer() {
        if (this.mTimer != null) {
            this.mTimer.cancel();
        }
        this.mTimer = null;
    }

    public void startVideo() {
        this.ivplay.setImageResource(R.drawable.pause_edit);
        this.mVideoInput.start();
        startTimer();
    }

    public void pauseVideo() {
        this.ivplay.setImageResource(R.drawable.play_edit);
        this.mVideoInput.pause();
        cancelTimer();
    }

    public void stopVideo() {
        pauseVideo();
        this.mVideoInput.resetCount();
        this.mVideoInput.seekTo(0);
        startVideo();
    }

    private void releaseVideo() {
        this.mVideoInput.release();
        cancelTimer();
    }


    public void onResume() {
        super.onResume();
    }


    public void onPause() {
        super.onPause();
        pauseVideo();
    }


    public void onDestroy() {
        super.onDestroy();
        releaseVideo();
    }

    public int getWidth(int i) {
        return (this.width * i) / 1080;
    }

    public int getHeight(int i) {
        return (this.height * i) / 1920;
    }

    public void back(View view) {
        onBackPressed();
    }

    public void onBackPressed() {
        if (!this.mSupportRecord.isRecording()) {
            super.onBackPressed();
            finish();
        }
    }

}
