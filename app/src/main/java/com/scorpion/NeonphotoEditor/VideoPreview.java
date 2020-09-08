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

import com.scorpion.NeonphotoEditor.util.Helper;
import com.scorpion.NeonphotoEditor.util.Layparam;

import io.github.inflationx.viewpump.ViewPumpContextWrapper;
import java.io.File;
import java.net.URLConnection;

public class VideoPreview extends Activity {
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

//    public void attachBaseContext(Context context2) {
//        super.attachBaseContext(ViewPumpContextWrapper.wrap(context2));
//    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_play);
        getWindow().setFlags(1024, 1024);
        this.context = this;
        this.header = (TextView) findViewById(R.id.my_header_text);
        this.vv = (VideoView) findViewById(R.id.videoview);
        this.ivshare = (ImageView) findViewById(R.id.ivshare);
        this.ivdelete = (ImageView) findViewById(R.id.ivdelete);
        this.lbottom = (LinearLayout) findViewById(R.id.linearbottom);
        this.width = Helper.getWidth(this.context);
        this.height = Helper.getHeight(this.context);
        forUI();
        init();
    }

    private void forUI() {
        Layparam.setMargins(this.context, this.vv, 60, 60, 60, 0);
        Layparam.setMargins(this.context, this.lbottom, 0, 85, 0, 80);
        Layparam.setHeightWidth(this.context, this.ivshare, 392, 156);
        Layparam.setHeightWidth(this.context, this.ivdelete, 392, 156);
    }

    private void init() {
        this.header.setText(R.string.preview);
        this.header.setTypeface(Typeface.createFromAsset(getAssets(), "Poppins-Bold.ttf"));
        this.vpath = getIntent().getStringExtra("vpath");
        this.fromEdit = getIntent().getBooleanExtra("fromedit", false);
        this.vv.setVideoPath(this.vpath);
        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(this.vv);
        this.vv.setMediaController(mediaController);
        this.vv.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            public void onPrepared(MediaPlayer mediaPlayer) {
                mediaPlayer.start();
            }
        });
        this.vv.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer mediaPlayer) {
                mediaPlayer.start();
            }
        });
    }

    public void delete(View view) {
        if (this.vv.isPlaying()) {
            this.vv.stopPlayback();
        }
        Helper.deleteFolder(new File(this.vpath));
        MediaScannerConnection.scanFile(this.context, new String[]{this.vpath}, (String[]) null, new MediaScannerConnection.OnScanCompletedListener() {
            public void onScanCompleted(String str, Uri uri) {
                VideoPreview.this.runOnUiThread(new Runnable() {
                    public void run() {
                        VideoPreview.this.finish();
                    }
                });
            }
        });
    }

    public void share(View view) {
        if (this.vv.isPlaying()) {
            this.vv.pause();
        }
        File file = new File(this.vpath);
        Intent intent = new Intent("android.intent.action.SEND");
        Uri uriForFile = FileProvider.getUriForFile(this, this.context.getPackageName(), file);
        intent.addFlags(1);
        intent.setType(URLConnection.guessContentTypeFromName(file.getName()));
        intent.putExtra("android.intent.extra.STREAM", uriForFile);
        startActivity(Intent.createChooser(intent, "Share File"));
    }

    public void onPause() {
        if (this.vv != null && this.vv.isPlaying()) {
            this.vv.pause();
        }
        super.onPause();
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
        this.vv.pause();
         if (this.fromEdit) {
            Intent intent = new Intent(this.context, HomeActivity.class);
            intent.setFlags(268435456);
            startActivity(intent);
        } else {
            super.onBackPressed();
            finish();
        }
    }


}
