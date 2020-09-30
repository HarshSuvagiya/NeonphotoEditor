package com.scorpion.NeonphotoEditor;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.core.content.FileProvider;

import com.facebook.ads.AdSize;
import com.scorpion.NeonphotoEditor.Util.Helper;
import com.scorpion.NeonphotoEditor.Util.SetLayparam;

import java.io.File;
import java.net.URLConnection;

public class NeonVideoPreview extends Activity {
    Context context;
    boolean fromEdit;
    TextView header;
    int height;
    ImageView ivdelete;
    ImageView ivshare;
    LinearLayout lbottom;
    String vpath;
    VideoView vv;
    int width;
    private com.facebook.ads.AdView adViewfb;

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_play);
        getWindow().setFlags(1024, 1024);
        context = this;
        //banner ad
        adViewfb = new com.facebook.ads.AdView(NeonVideoPreview.this, getString(R.string.banner_ad_unit_idfb), AdSize.BANNER_HEIGHT_50);

        // Find the Ad Container
        LinearLayout adContainer = (LinearLayout) findViewById(R.id.banner_container);
        adContainer.setVisibility(View.VISIBLE);
        // Add the ad view to your activity layout
        adContainer.addView(adViewfb);

        // Request an ad
        adViewfb.loadAd();
        header = (TextView) findViewById(R.id.my_header_text);
        vv = (VideoView) findViewById(R.id.videoview);
        ivshare = (ImageView) findViewById(R.id.ivshare);
        ivdelete = (ImageView) findViewById(R.id.ivdelete);
        lbottom = (LinearLayout) findViewById(R.id.linearbottom);
        width = Helper.getWidth(context);
        height = Helper.getHeight(context);
        forUI();
        init();
    }

    private void forUI() {
        SetLayparam.setMargins(context, vv, 60, 60, 60, 0);
        SetLayparam.setMargins(context, lbottom, 0, 85, 0, 80);
        SetLayparam.setHeightWidth(context, ivshare, 392, 156);
        SetLayparam.setHeightWidth(context, ivdelete, 392, 156);
    }

    private void init() {
        header.setText(R.string.preview);
        header.setTypeface(Typeface.createFromAsset(getAssets(), "Poppins-Bold.ttf"));
        vpath = getIntent().getStringExtra("vpath");
        fromEdit = getIntent().getBooleanExtra("fromedit", false);
        vv.setVideoPath(vpath);
        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(vv);
        vv.setMediaController(mediaController);
        vv.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            public void onPrepared(MediaPlayer mediaPlayer) {
                mediaPlayer.start();
            }
        });
        vv.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer mediaPlayer) {
                mediaPlayer.start();
            }
        });
    }

    public void delete(View view) {
        if (vv.isPlaying()) {
            vv.stopPlayback();
        }
        Helper.deleteFolder(new File(vpath));
        MediaScannerConnection.scanFile(context, new String[]{vpath}, (String[]) null, new MediaScannerConnection.OnScanCompletedListener() {
            public void onScanCompleted(String str, Uri uri) {
                runOnUiThread(new Runnable() {
                    public void run() {
                        finish();
                    }
                });
            }
        });
    }

    public void share(View view) {
        if (vv.isPlaying()) {
            vv.pause();
        }
        File file = new File(vpath);
        Intent intent = new Intent("android.intent.action.SEND");
        Uri uriForFile = FileProvider.getUriForFile(this, context.getPackageName(), file);
        intent.addFlags(1);
        intent.setType(URLConnection.guessContentTypeFromName(file.getName()));
        intent.putExtra("android.intent.extra.STREAM", uriForFile);
        startActivity(Intent.createChooser(intent, "Share File"));
    }

    public void onPause() {
        if (vv != null && vv.isPlaying()) {
            vv.pause();
        }
        super.onPause();
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
        vv.pause();
         if (fromEdit) {
            Intent intent = new Intent(context, HomeActivity.class);
            intent.setFlags(268435456);
            startActivity(intent);
        } else {
            super.onBackPressed();
            finish();
        }
    }


}
