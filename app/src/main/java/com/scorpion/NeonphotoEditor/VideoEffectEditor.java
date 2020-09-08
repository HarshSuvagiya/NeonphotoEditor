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
import com.scorpion.NeonphotoEditor.adapter.StickerAdapter;
import com.scorpion.NeonphotoEditor.adapter.UsedAdapter;
import com.scorpion.NeonphotoEditor.Videoneoneffect.EZFilter;
import com.scorpion.NeonphotoEditor.Videoneoneffect.GLRender;
import com.scorpion.NeonphotoEditor.Videoneoneffect.IMediaPlayer;
import com.scorpion.NeonphotoEditor.Videoneoneffect.RenderPipeline;
import com.scorpion.NeonphotoEditor.Videoneoneffect.TextureFitView;
import com.scorpion.NeonphotoEditor.Videoneoneffect.VideoInput;
import com.scorpion.NeonphotoEditor.Videoneoneffect.videorender.StickerRender;
import com.scorpion.NeonphotoEditor.Videoneoneffect.sticker.IStickerTimeController;
import com.scorpion.NeonphotoEditor.Videoneoneffect.sticker.StickerHelper;
import com.scorpion.NeonphotoEditor.util.Constant;
import com.scorpion.NeonphotoEditor.util.Helper;
import com.scorpion.NeonphotoEditor.util.RecyclerTouchListener;
import com.scorpion.NeonphotoEditor.util.Layparam;
import com.scorpion.NeonphotoEditor.util.VideoEffectTimeBar;
import com.theartofdev.edmodo.cropper.CropImage;
import io.github.inflationx.viewpump.ViewPumpContextWrapper;
import java.io.File;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class VideoEffectEditor extends Activity {
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
    StickerAdapter sadapter;
    SeekBar sbsize;
    long start;
    int sticker;
    UsedAdapter uadapter;
    StickerRender usedRender;
    int usticker = -1;
    VideoEffectTimeBar vseek;
    int width;

    public void nothing(View view) {
    }

//    public void attachBaseContext(Context context2) {
//        super.attachBaseContext(ViewPumpContextWrapper.wrap(context2));
//    }

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
        Layparam.setMargins(this.context, this.lcontainer, 60, 60, 60, 0);
        Layparam.setMargins(this.context, this.lop, 60, 45, 60, 45);
        Layparam.setHeightAsBoth(this.context, this.ivplay, 120);
        Layparam.setHeightWidth(this.context, this.leffectbar, 808, 184);
        Layparam.setPadding(this.context, this.leffectbar, 20);
        Layparam.setHeightWidth(this.context, this.ivedit, 226, CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE);
        Layparam.setHeight(this.context, this.rcvs, CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE);
        Layparam.setHeight(this.context, this.rcvu, CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE);
        Layparam.setHeight(this.context, this.lsize, 92);
        Layparam.setPadding(this.context, this.lsize, 30, 0, 23, 0);
        Layparam.setMargins(this.context, this.lsize, 0, 23, 0, 20);
        Layparam.setHeightAsBoth(this.context, this.ivsicon, 90);
        Layparam.setHeightAsBoth(this.context, this.ivdelete, 90);
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
                return ((float) VideoEffectEditor.this.mVideoInput.getMediaPlayer().getCurrentPosition()) / 1000.0f;
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
                VideoEffectEditor.this.mRenderPipeline = EZFilter.input(Uri.parse(VideoEffectEditor.this.inputpath)).setLoop(false).enableRecord(VideoEffectEditor.this.outfile.getAbsolutePath(), true, false).setPreparedListener(new IMediaPlayer.OnPreparedListener() {
                    public void onPrepared(IMediaPlayer fX_IMediaPlayer) {
                        VideoEffectEditor.this.mVideoInput.seekTo(1);
                        VideoEffectEditor.this.pauseVideo();
                    }
                }).setCompletionListener(new IMediaPlayer.OnCompletionListener() {
                    public void onCompletion(IMediaPlayer fX_IMediaPlayer) {
                        if (!VideoEffectEditor.this.mTouchingSeekBar && !VideoEffectEditor.this.mTouchingTextureView) {
                            VideoEffectEditor.this.mVideoInput.seekTo(0);
                            VideoEffectEditor.this.ivplay.setImageResource(R.drawable.play_edit);
                            VideoEffectEditor.this.runOnUiThread(new Runnable() {
                                public void run() {
                                    if (VideoEffectEditor.this.mSupportRecord.isRecording()) {
                                        Helper.showLog("SSS", "Saving Stopped");
                                        VideoEffectEditor.this.stopRecording();
                                    }
                                }
                            });
                        }
                    }
                }).setVideoEffectTimeBar(VideoEffectEditor.this.vseek).into(VideoEffectEditor.this.mRenderView);
                VideoEffectEditor.this.mVideoInput = (VideoInput) VideoEffectEditor.this.mRenderPipeline.getStartPointRender();
                for (GLRender next : VideoEffectEditor.this.mRenderPipeline.getEndPointRenders()) {
                    if (next instanceof ISupportRecord) {
                        VideoEffectEditor.this.mSupportRecord = (ISupportRecord) next;
                    }
                }
            }
        }.start();
    }

    private void setAdapter() {
        this.rcvs.setLayoutManager(new LinearLayoutManager(this.context, 0, false));
        this.rcvu.setLayoutManager(new LinearLayoutManager(this.context, 0, false));
        this.sadapter = new StickerAdapter(this.context, Constant.hslist);
        this.rcvs.setAdapter(this.sadapter);
        this.uadapter = new UsedAdapter(this.context, this.alused);
        this.rcvu.setAdapter(this.uadapter);
        this.rcvs.addOnItemTouchListener(new RecyclerTouchListener(this.context, this.rcvs, new RecyclerTouchListener.ClickListener() {
            public void onLongClick(View view, int i) {
            }

            public void onClick(View view, int i) {
                VideoEffectEditor.this.setSticker(i);
            }
        }));
        this.rcvu.addOnItemTouchListener(new RecyclerTouchListener(this.context, this.rcvu, new RecyclerTouchListener.ClickListener() {
            public void onLongClick(View view, int i) {
            }

            public void onClick(View view, int i) {
                VideoEffectEditor.this.setUsedSticker(i);
            }
        }));
    }

    @SuppressLint({"ClickableViewAccessibility"})
    private void setListeners() {
        this.mRenderView.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case 0:
                        VideoEffectEditor.this.mTouchingTextureView = true;
                        VideoEffectEditor.this.preTouchOperation();
                        VideoEffectEditor.this.mStickerRender = VideoEffectEditor.this.sHelper.getParticleRenderer(VideoEffectEditor.this.sticker);
                        VideoEffectEditor.this.startVideo();
                        VideoEffectEditor.this.mStickerRender.setEffectTimeBar(VideoEffectEditor.this.vseek);
                        VideoEffectEditor.this.mStickerRender.start();
                        VideoEffectEditor.this.alrender.add(VideoEffectEditor.this.mStickerRender);
                        VideoEffectEditor.this.mStickerRender.setPosition(Math.round(((motionEvent.getX() * ((float) VideoEffectEditor.this.mStickerRender.getWidth())) * 1.0f) / ((float) VideoEffectEditor.this.mRenderView.getWidth())), Math.round(((motionEvent.getY() * ((float) VideoEffectEditor.this.mStickerRender.getHeight())) * 1.0f) / ((float) VideoEffectEditor.this.mRenderView.getHeight())));
                        VideoEffectEditor.this.start = (long) VideoEffectEditor.this.mVideoInput.getMediaPlayer().getCurrentPosition();
                        VideoEffectEditor.this.vseek.setTouch(true, VideoEffectEditor.this.start);
                        if (!VideoEffectEditor.this.mRenderPipeline.getFilterRenders().contains(VideoEffectEditor.this.mStickerRender)) {
                            VideoEffectEditor.this.mRenderPipeline.addFilterRender(VideoEffectEditor.this.mStickerRender);
                            break;
                        }
                        break;
                    case 1:
                    case 3:
                        VideoEffectEditor.this.mTouchingTextureView = false;
                        if (VideoEffectEditor.this.mStickerRender != null) {
                            VideoEffectEditor.this.mStickerRender.setPosition(-2000, -2000);
                            new Handler().postDelayed(new Runnable() {
                                public void run() {
                                    VideoEffectEditor.this.mStickerRender.pause();
                                    VideoEffectEditor.this.mStickerRender = null;
                                }
                            }, 200);
                        }
                        VideoEffectEditor.this.end = (long) VideoEffectEditor.this.mVideoInput.getMediaPlayer().getCurrentPosition();
                        VideoEffectEditor.this.vseek.setTouch(false, VideoEffectEditor.this.end);
                        break;
                    case 2:
                        if (VideoEffectEditor.this.mStickerRender != null) {
                            VideoEffectEditor.this.mStickerRender.setPosition(Math.round(((motionEvent.getX() * ((float) VideoEffectEditor.this.mStickerRender.getWidth())) * 1.0f) / ((float) VideoEffectEditor.this.mRenderView.getWidth())), Math.round(((motionEvent.getY() * ((float) VideoEffectEditor.this.mStickerRender.getHeight())) * 1.0f) / ((float) VideoEffectEditor.this.mRenderView.getHeight())));
                            break;
                        }
                        break;
                }
                return true;
            }
        });
        this.vseek.setSeekBarChangeListener(new VideoEffectTimeBar.SeekBarChangeListener() {
            public void seekBarValueChanged(int i) {
                VideoEffectEditor.this.mVideoInput.getMediaPlayer().seekTo(i);
            }

            public void onSeeking(boolean z) {
                if (z) {
                    VideoEffectEditor.this.pauseVideo();
                } else {
                    VideoEffectEditor.this.startVideo();
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
                if (VideoEffectEditor.this.usedRender != null) {
                    VideoEffectEditor.this.usedRender.setSize(i2);
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
            this.ivedit.setImageResource(R.drawable.edit);
            this.lsize.setVisibility(View.INVISIBLE);
            this.rcvu.setVisibility(View.GONE);
            this.rcvs.setVisibility(View.VISIBLE);
            return;
        }
        this.ivedit.setImageResource(R.drawable.sticker);
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
                    VideoEffectEditor.this.mixAudio();
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
                VideoEffectEditor.this.resultReady(file);
            }

            public void onError(String str) {
                Helper.showLog("SSS", "Mixing Audio Failed : " + str);
                try {
                    Helper.copyFile(VideoEffectEditor.this.outfile.getAbsolutePath(), file.getAbsolutePath());
                    VideoEffectEditor.this.resultReady(file);
                } catch (Exception unused) {
                    Helper.show(VideoEffectEditor.this.context, "Error while saving video !!");
                    VideoEffectEditor.this.lpbar.setVisibility(View.GONE);
                    VideoEffectEditor.this.ivback.setVisibility(View.VISIBLE);
                    VideoEffectEditor.this.ivdone.setVisibility(View.VISIBLE);
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
                Helper.deleteFolder(new File(Helper.getTempFolder(VideoEffectEditor.this.context)));
                Helper.deleteFolder(VideoEffectEditor.this.outfile);
                VideoEffectEditor.this.runOnUiThread(new Runnable() {
                    public void run() {
                        VideoEffectEditor.this.lpbar.setVisibility(View.GONE);
                        VideoEffectEditor.this.ivback.setVisibility(View.VISIBLE);
                        VideoEffectEditor.this.ivdone.setVisibility(View.VISIBLE);
                        Intent intent = new Intent(VideoEffectEditor.this.context, VideoPreview.class);
                        intent.putExtra("vpath", file.getAbsolutePath());
                        intent.putExtra("fromedit", true);
                        VideoEffectEditor.this.startActivity(intent);
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
                    VideoEffectEditor.this.vseek.videoPlayingProgress(VideoEffectEditor.this.mVideoInput.getMediaPlayer().getCurrentPosition());
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
