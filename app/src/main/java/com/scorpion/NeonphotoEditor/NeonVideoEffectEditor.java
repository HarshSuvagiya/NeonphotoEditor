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
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.arthenica.mobileffmpeg.FFmpeg;
import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdIconView;
import com.facebook.ads.AdOptionsView;
import com.facebook.ads.NativeAd;
import com.facebook.ads.NativeAdLayout;
import com.facebook.ads.NativeAdListener;
import com.scorpion.NeonphotoEditor.Adapters.ExtraUsedAdapter;
import com.scorpion.NeonphotoEditor.Adapters.VideoStickerAdapter;
import com.scorpion.NeonphotoEditor.Util.Constant;
import com.scorpion.NeonphotoEditor.Util.Helper;
import com.scorpion.NeonphotoEditor.Util.RecyclerTouchListener;
import com.scorpion.NeonphotoEditor.Util.SetLayparam;
import com.scorpion.NeonphotoEditor.Util.VideoEffectTimeBar;
import com.scorpion.NeonphotoEditor.Videoneoneffect.EZFilter;
import com.scorpion.NeonphotoEditor.Videoneoneffect.GLRender;
import com.scorpion.NeonphotoEditor.Videoneoneffect.IMediaPlayer;
import com.scorpion.NeonphotoEditor.Videoneoneffect.RenderPipeline;
import com.scorpion.NeonphotoEditor.Videoneoneffect.Sticker.IStickerTimeController;
import com.scorpion.NeonphotoEditor.Videoneoneffect.Sticker.StickerHelper;
import com.scorpion.NeonphotoEditor.Videoneoneffect.TextureFitView;
import com.scorpion.NeonphotoEditor.Videoneoneffect.VideoInput;
import com.scorpion.NeonphotoEditor.Videoneoneffect.VideoRender.StickerRender;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import cn.ezandroid.ezfilter.media.record.ISupportRecord;

import static com.arthenica.mobileffmpeg.Config.RETURN_CODE_CANCEL;
import static com.arthenica.mobileffmpeg.Config.RETURN_CODE_SUCCESS;

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
    RelativeLayout hint;
    ImageView okbtn;
    public void nothing(View view) {
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_sticker_preview);
        getWindow().setFlags(1024, 1024);
        context = this;
        nativeAdLayout = (NativeAdLayout) findViewById(R.id.native_ad_container);
        nativeAdLayout.setVisibility(View.VISIBLE);
        hint = (RelativeLayout) findViewById(R.id.hint);
        okbtn = (ImageView) findViewById(R.id.btnok);
        okbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hint.setVisibility(View.GONE);
            }
        });
        //nativead
        loadNativeAd();
        header = (TextView) findViewById(R.id.my_header_text);
        ivdone = (ImageView) findViewById(R.id.ivoption);
        ivback = (ImageView) findViewById(R.id.ivback);
        ivplay = (ImageView) findViewById(R.id.ivplay);
        mRenderView = (TextureFitView) findViewById(R.id.render_view);
        lpbar = (LinearLayout) findViewById(R.id.lpbar);
        leffectbar = (LinearLayout) findViewById(R.id.leffectbar);
        vseek = (VideoEffectTimeBar) findViewById(R.id.vseek);
        rcvs = (RecyclerView) findViewById(R.id.rcvsticker);
        lsize = (LinearLayout) findViewById(R.id.linearSize);
        rcvu = (RecyclerView) findViewById(R.id.rcvused);
        sbsize = (SeekBar) findViewById(R.id.seeksize);
        lop = (LinearLayout) findViewById(R.id.linearop);
        ivedit = (ImageView) findViewById(R.id.ivedit);
        ivsicon = (ImageView) findViewById(R.id.ivsicon);
        ivdelete = (ImageView) findViewById(R.id.ivdelete);
        lav = (LottieAnimationView) findViewById(R.id.animfront);
        lcontainer = (LinearLayout) findViewById(R.id.lcontainer);
        width = Helper.getWidth(context);
        height = Helper.getHeight(context);
        forUI();
        init();
    }

    NativeAdLayout   nativeAdLayout;
    private void loadNativeAd() {
        final com.facebook.ads.NativeAd nativeAd = new NativeAd(getApplicationContext(), getString(R.string.native_ad_unit_Idfb));
        nativeAd.setAdListener(new NativeAdListener() {
            public void onAdClicked(Ad ad) {
            }

            public void onError(Ad ad, AdError adError) {
            }

            public void onLoggingImpression(Ad ad) {
            }

            public void onMediaDownloaded(Ad ad) {
            }

            public void onAdLoaded(Ad ad) {
                inflateAd(nativeAd);
            }
        });
        nativeAd.loadAd();
    }

    public void inflateAd(com.facebook.ads.NativeAd nativeAd2) {
        nativeAd2.unregisterView();
        int i = 0;
        LinearLayout adView = (LinearLayout) LayoutInflater.from(getApplicationContext()).inflate(R.layout.fbnative_ad, nativeAdLayout, false);
        nativeAdLayout.addView(adView);
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.ad_choices_container);
        AdOptionsView adOptionsView = new AdOptionsView(getApplicationContext(), nativeAd2, nativeAdLayout);
        linearLayout.removeAllViews();
        linearLayout.addView(adOptionsView, 0);
        AdIconView adIconView = (AdIconView) adView.findViewById(R.id.native_ad_icon);
        TextView textView = (TextView) adView.findViewById(R.id.native_ad_title);
        com.facebook.ads.MediaView mediaView = (com.facebook.ads.MediaView) adView.findViewById(R.id.native_ad_media);
        TextView textView2 = (TextView) adView.findViewById(R.id.native_ad_sponsored_label);
        Button button = (Button) adView.findViewById(R.id.native_ad_call_to_action);
        textView.setText(nativeAd2.getAdvertiserName());
        ((TextView) adView.findViewById(R.id.native_ad_body)).setText(nativeAd2.getAdBodyText());
        ((TextView) adView.findViewById(R.id.native_ad_social_context)).setText(nativeAd2.getAdSocialContext());
        if (!nativeAd2.hasCallToAction()) {
            i = 4;
        }
        button.setVisibility(i);
        button.setText(nativeAd2.getAdCallToAction());
        textView2.setText(nativeAd2.getSponsoredTranslation());
        ArrayList arrayList = new ArrayList();
        arrayList.add(textView);
        arrayList.add(button);
        nativeAd2.registerViewForInteraction((View) adView, mediaView, (com.facebook.ads.MediaView) adIconView, (List<View>) arrayList);
    }

    private void forUI() {
        SetLayparam.setMargins(context, lcontainer, 60, 60, 60, 0);
        SetLayparam.setMargins(context, lop, 60, 45, 60, 45);
        SetLayparam.setHeightAsBoth(context, ivplay, 120);
        SetLayparam.setHeightWidth(context, leffectbar, 808, 184);
        SetLayparam.setPadding(context, leffectbar, 20);
        SetLayparam.setHeightWidth(context, ivedit, 226, CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE);
        SetLayparam.setHeight(context, rcvs, CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE);
        SetLayparam.setHeight(context, rcvu, CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE);
        SetLayparam.setHeight(context, lsize, 92);
        SetLayparam.setPadding(context, lsize, 30, 0, 23, 0);
        SetLayparam.setMargins(context, lsize, 0, 23, 0, 20);
        SetLayparam.setHeightAsBoth(context, ivsicon, 90);
        SetLayparam.setHeightAsBoth(context, ivdelete, 90);
    }

    private void init() {
        header.setText(R.string.app_name);
        ivdone.setVisibility(View.VISIBLE);
        header.setTypeface(Typeface.createFromAsset(getAssets(), "Poppins-Bold.ttf"));
        alused.clear();
        setAdapter();
        setListeners();
        sTimeControl = new IStickerTimeController() {
            public float getCurrentTime() {
                return ((float) mVideoInput.getMediaPlayer().getCurrentPosition()) / 1000.0f;
            }
        };
        inputpath = getIntent().getStringExtra("path");
        duration = Long.parseLong(getIntent().getStringExtra("duration"));
        vseek.setMaxValue(duration);
        vseek.beginPreView(inputpath);
        vseek.setCanSeek(true);
        outfile = new File(Helper.getTempFolder(context), "woa.mp4");
        if (outfile.exists()) {
            outfile.delete();
        }
        new Thread() {
            public void run() {
                mRenderPipeline = EZFilter.input(Uri.parse(inputpath)).setLoop(false).enableRecord(outfile.getAbsolutePath(), true, false).setPreparedListener(new IMediaPlayer.OnPreparedListener() {
                    public void onPrepared(IMediaPlayer IMediaPlayer1) {
                        mVideoInput.seekTo(1);
                        pauseVideo();
                    }
                }).setCompletionListener(new IMediaPlayer.OnCompletionListener() {
                    public void onCompletion(IMediaPlayer IMediaPlayer1) {
                        if (!mTouchingSeekBar && !mTouchingTextureView) {
                            mVideoInput.seekTo(0);
                            ivplay.setImageResource(R.drawable.play_edit);
                            runOnUiThread(new Runnable() {
                                public void run() {
                                    if (mSupportRecord.isRecording()) {
                                        Helper.showLog("SSS", "Saving Stopped");
                                        stopRecording();
                                    }
                                }
                            });
                        }
                    }
                }).setVideoEffectTimeBar(vseek).into(mRenderView);
                mVideoInput = (VideoInput) mRenderPipeline.getStartPointRender();
                for (GLRender next : mRenderPipeline.getEndPointRenders()) {
                    if (next instanceof ISupportRecord) {
                        mSupportRecord = (ISupportRecord) next;
                    }
                }
            }
        }.start();
    }

    private void setAdapter() {
        rcvs.setLayoutManager(new LinearLayoutManager(context, 0, false));
        rcvu.setLayoutManager(new LinearLayoutManager(context, 0, false));
        sadapter = new VideoStickerAdapter(context, Constant.hslist);
        rcvs.setAdapter(sadapter);
        uadapter = new ExtraUsedAdapter(context, alused);
        rcvu.setAdapter(uadapter);
        rcvs.addOnItemTouchListener(new RecyclerTouchListener(context, rcvs, new RecyclerTouchListener.ClickListener() {
            public void onLongClick(View view, int i) {
            }

            public void onClick(View view, int i) {
                setSticker(i);
            }
        }));
        rcvu.addOnItemTouchListener(new RecyclerTouchListener(context, rcvu, new RecyclerTouchListener.ClickListener() {
            public void onLongClick(View view, int i) {
            }

            public void onClick(View view, int i) {
                setUsedSticker(i);
            }
        }));
    }

    @SuppressLint({"ClickableViewAccessibility"})
    private void setListeners() {
        mRenderView.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case 0:
                        mTouchingTextureView = true;
                        preTouchOperation();
                        mStickerRender = sHelper.getParticleRenderer(sticker);
                        startVideo();
                        mStickerRender.setEffectTimeBar(vseek);
                        mStickerRender.start();
                        alrender.add(mStickerRender);
                        mStickerRender.setPosition(Math.round(((motionEvent.getX() * ((float) mStickerRender.getWidth())) * 1.0f) / ((float) mRenderView.getWidth())), Math.round(((motionEvent.getY() * ((float) mStickerRender.getHeight())) * 1.0f) / ((float) mRenderView.getHeight())));
                        start = (long) mVideoInput.getMediaPlayer().getCurrentPosition();
                        vseek.setTouch(true, start);
                        if (!mRenderPipeline.getFilterRenders().contains(mStickerRender)) {
                            mRenderPipeline.addFilterRender(mStickerRender);
                            break;
                        }
                        break;
                    case 1:
                    case 3:
                        mTouchingTextureView = false;
                        if (mStickerRender != null) {
                            mStickerRender.setPosition(-2000, -2000);
                            new Handler().postDelayed(new Runnable() {
                                public void run() {
                                    mStickerRender.pause();
                                    mStickerRender = null;
                                }
                            }, 200);
                        }
                        end = (long) mVideoInput.getMediaPlayer().getCurrentPosition();
                        vseek.setTouch(false, end);
                        break;
                    case 2:
                        if (mStickerRender != null) {
                            mStickerRender.setPosition(Math.round(((motionEvent.getX() * ((float) mStickerRender.getWidth())) * 1.0f) / ((float) mRenderView.getWidth())), Math.round(((motionEvent.getY() * ((float) mStickerRender.getHeight())) * 1.0f) / ((float) mRenderView.getHeight())));
                            break;
                        }
                        break;
                }
                return true;
            }
        });
        vseek.setSeekBarChangeListener(new VideoEffectTimeBar.SeekBarChangeListener() {
            public void seekBarValueChanged(int i) {
                mVideoInput.getMediaPlayer().seekTo(i);
            }

            public void onSeeking(boolean z) {
                if (z) {
                    pauseVideo();
                } else {
                    startVideo();
                }
            }
        });
    }

    public void preTouchOperation() {
        if (sHelper == null) {
            sHelper = new StickerHelper(context, sTimeControl);
        }
        if (mStickerRender != null) {
            mStickerRender.pause();
        }
        int size = alused.size();
        alused.add(size, Constant.hslist.get(Integer.valueOf(sticker)));
        uadapter.notifyItemInserted(size);
    }

    public void setSticker(int i) {
        if (mStickerRender != null) {
            mStickerRender.pause();
        }
        Helper.freeMemory();
        sticker = i;
        sadapter.setSelected(sticker);
        sHelper = new StickerHelper(context, sTimeControl);
    }

    public void setUsedSticker(int i) {
        Helper.freeMemory();
        usticker = i;
        uadapter.setSelected(usticker);
        usedRender = null;
        usedRender = alrender.get(usticker);
        if (usedRender != null) {
            sbsize.setMax(usedRender.getOriginalSize());
        } else {
            sbsize.setMax(Constant.NORMAL_STICKER_SIZE);
        }
        sbsize.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                int i2 = i + 200;
                if (usedRender != null) {
                    usedRender.setSize(i2);
                }
            }
        });
        sbsize.setProgress(usedRender.getSize() - 200);
    }

    public void play(View view) {
        if (mVideoInput.isPlaying()) {
            pauseVideo();
        } else {
            startVideo();
        }
    }

    public void size(View view) {
        if (lsize.getVisibility() == View.VISIBLE) {
            ivedit.setImageResource(R.drawable.edit_unpress);
            lsize.setVisibility(View.INVISIBLE);
            rcvu.setVisibility(View.GONE);
            rcvs.setVisibility(View.VISIBLE);
            return;
        }
        ivedit.setImageResource(R.drawable.stickers_unpress);
        rcvs.setVisibility(View.GONE);
        lsize.setVisibility(View.VISIBLE);
        rcvu.setVisibility(View.VISIBLE);
    }

    public void delete(View view) {
        if (usticker >= 0) {
            mRenderPipeline.removeFilterRender(alrender.get(usticker));
            alrender.remove(usticker);
            vseek.removeEffectRange(usticker);
            alused.remove(usticker);
            uadapter.notifyItemRemoved(usticker);
            usticker = -1;
            usedRender = null;
        }
    }

    public void option(View view) {
         if (!mSupportRecord.isRecording()) {
            lpbar.setVisibility(View.VISIBLE);
            ivback.setVisibility(View.INVISIBLE);
            ivdone.setVisibility(View.INVISIBLE);
            if (mSupportRecord != null) {
                Helper.showLog("SSS", "Saving Start");
                mVideoInput.getMediaPlayer().setVolume(0.0f, 0.0f);
                stopVideo();
                startRecording();
            }
        }
    }

    public void startRecording() {
        Helper.freeMemory();
        Log.i("RRR", "startRecording:");
        if (mSupportRecord != null) {
            mSupportRecord.startRecording();
        }
    }

    public void stopRecording() {
        Helper.freeMemory();
        Log.i("RRR", "stopRecording: ");
        if (mSupportRecord != null) {
            mSupportRecord.stopRecording();
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    mixAudio();
                }
            }, 2000);
        }
    }

    public void mixAudio() {
        String outputFolder = Helper.getOutputFolder(context);
        final File file = new File(outputFolder, new Random().nextInt(1000) + "" + System.currentTimeMillis() + "_" + getResources().getString(R.string.app_name) + ".mp4");
        if (file.exists()) {
            file.delete();
        }

        int rc = FFmpeg.execute(new String[]{"-y", "-i", outfile.getAbsolutePath(), "-i", inputpath,
                "-c", "copy", "-map", "0:v:0", "-map", "1:a:0", "-shortest", file.getAbsolutePath()});
        if (rc == RETURN_CODE_SUCCESS) {
            Helper.showLog("SSS", "Mixing Audio Success");
            resultReady(file);
        } else if (rc == RETURN_CODE_CANCEL) {
            Helper.showLog("SSS", "Mixing Audio Stopped");
        } else {
            Helper.showLog("SSS", "Mixing Audio Failed : ");
            try {
                Helper.copyFile(outfile.getAbsolutePath(), file.getAbsolutePath());
                resultReady(file);
            } catch (Exception unused) {
                Helper.show(context, "Error while saving video !!");
                lpbar.setVisibility(View.GONE);
                ivback.setVisibility(View.VISIBLE);
                ivdone.setVisibility(View.VISIBLE);
            }
        }
//        GeneralUtils.deleteLicFile(context);
//        new CommandExecutor(context, new String[]{"ffmpeg", "-i", outfile.getAbsolutePath(), "-i", inputpath, "-c", "copy", "-map", "0:v:0", "-map", "1:a:0", "-shortest", file.getAbsolutePath()}, new FfResponse() {
//            public void onSuccess() {
//                Helper.showLog("SSS", "Mixing Audio Success");
//                resultReady(file);
//            }
//
//            public void onError(String str) {
//                Helper.showLog("SSS", "Mixing Audio Failed : " + str);
//                try {
//                    Helper.copyFile(outfile.getAbsolutePath(), file.getAbsolutePath());
//                    resultReady(file);
//                } catch (Exception unused) {
//                    Helper.show(context, "Error while saving video !!");
//                    lpbar.setVisibility(View.GONE);
//                    ivback.setVisibility(View.VISIBLE);
//                    ivdone.setVisibility(View.VISIBLE);
//                }
//            }
//
//            public void onStop() {
//                Helper.showLog("SSS", "Mixing Audio Stopped");
//            }
//
//            public void onStart() {
//                Helper.showLog("SSS", "Mixing Audio Start");
//            }
//        }).execute(new Void[0]);
    }

    public void resultReady(final File file) {
        MediaScannerConnection.scanFile(context, new String[]{file.getAbsolutePath()}, new String[]{"mp4"}, new MediaScannerConnection.OnScanCompletedListener() {
            public void onScanCompleted(String str, Uri uri) {
                Helper.deleteFolder(new File(Helper.getTempFolder(context)));
                Helper.deleteFolder(outfile);
                runOnUiThread(new Runnable() {
                    public void run() {
                        lpbar.setVisibility(View.GONE);
                        ivback.setVisibility(View.VISIBLE);
                        ivdone.setVisibility(View.VISIBLE);
                        FBInterstitial.getInstance().displayFBInterstitial(NeonVideoEffectEditor.this, new FBInterstitial.FbCallback() {
                            public void callbackCall() {
                                Intent intent = new Intent(context, NeonVideoPreview.class);
                                intent.putExtra("vpath", file.getAbsolutePath());
                                intent.putExtra("fromedit", true);
                                startActivity(intent);
                            }
                        });

                    }
                });
            }
        });
    }

    private void startTimer() {
        if (mTimer != null) {
            mTimer.cancel();
        }
        mTimer = new Timer();
        mTimer.schedule(new TimerTask() {
            public void run() {
                try {
                    vseek.videoPlayingProgress(mVideoInput.getMediaPlayer().getCurrentPosition());
                } catch (Exception unused) {
                }
            }
        }, 0, 50);
    }

    private void cancelTimer() {
        if (mTimer != null) {
            mTimer.cancel();
        }
        mTimer = null;
    }

    public void startVideo() {
        ivplay.setImageResource(R.drawable.pause_edit);
        mVideoInput.start();
        startTimer();
    }

    public void pauseVideo() {
        ivplay.setImageResource(R.drawable.play_edit);
        mVideoInput.pause();
        cancelTimer();
    }

    public void stopVideo() {
        pauseVideo();
        mVideoInput.resetCount();
        mVideoInput.seekTo(0);
        startVideo();
    }

    private void releaseVideo() {
        mVideoInput.release();
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
        return (width * i) / 1080;
    }

    public int getHeight(int i) {
        return (height * i) / 1920;
    }

    public void back(View view) {
        onBackPressed();
    }

    public void onBackPressed() {
        if (!mSupportRecord.isRecording()) {
            super.onBackPressed();
            finish();
        }
    }

}
