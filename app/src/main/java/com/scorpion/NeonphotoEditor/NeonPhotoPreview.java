package com.scorpion.NeonphotoEditor;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.scorpion.NeonphotoEditor.Util.Helper;
import com.scorpion.NeonphotoEditor.Util.SetLayparam;

import java.io.File;

public class NeonPhotoPreview extends Activity {
    Context context;
    int from;
    TextView header;
    int height;
    ImageView iv;
    ImageView ivdelete;
    ImageView ivshare;
    LinearLayout lbottom;
    String path;
    int width;


    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_preview);
        getWindow().setFlags(1024, 1024);
        this.context = this;
        this.header = (TextView) findViewById(R.id.my_header_text);
        this.iv = (ImageView) findViewById(R.id.ivpreview);
        this.ivshare = (ImageView) findViewById(R.id.ivshare);
        this.ivdelete = (ImageView) findViewById(R.id.ivdelete);
        this.lbottom = (LinearLayout) findViewById(R.id.linearbottom);
        this.width = Helper.getWidth(this.context);
        this.height = Helper.getHeight(this.context);
        forUI();
        init();
    }

    private void forUI() {
        SetLayparam.setMargins(this.context, this.iv, 60, 60, 60, 0);
        SetLayparam.setMargins(this.context, this.lbottom, 0, 85, 0, 80);
        SetLayparam.setHeightWidth(this.context, this.ivshare, 392, 156);
        SetLayparam.setHeightWidth(this.context, this.ivdelete, 392, 156);
    }

    private void init() {
        this.header.setText(R.string.preview);
        this.header.setTypeface(Typeface.createFromAsset(getAssets(), "Poppins-Bold.ttf"));
        this.from = getIntent().getIntExtra("from", 0);
        this.path = getIntent().getStringExtra("path");
        Glide.with(this.context).load(this.path).transition(DrawableTransitionOptions.withCrossFade()).into(this.iv);
        if (Build.VERSION.SDK_INT >= 24) {
            try {
                StrictMode.class.getMethod("disableDeathOnFileUriExposure", new Class[0]).invoke((Object) null, new Object[0]);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void delete(View view) {
        try {
            File file = new File(this.path);
            if (file.exists()) {
                file.delete();
            }
            MediaScannerConnection.scanFile(this.context, new String[]{this.path}, (String[]) null, new MediaScannerConnection.OnScanCompletedListener() {
                public void onScanCompleted(String str, Uri uri) {
                    NeonPhotoPreview.this.runOnUiThread(new Runnable() {
                        public void run() {
                            NeonPhotoPreview.this.finish();
                        }
                    });
                }
            });
        } catch (Exception unused) {
        }
    }

    public void share(View view) {
        Helper.shareImage(this.context, this.path);
    }

    public void back(View view) {
        onBackPressed();
    }

    public void onBackPressed() {
         if (this.from == 0) {
            Intent intent = new Intent(this.context, HomeActivity.class);
            intent.setFlags(268435456);
            startActivity(intent);
        } else {
            super.onBackPressed();
            finish();
        }
    }


}
