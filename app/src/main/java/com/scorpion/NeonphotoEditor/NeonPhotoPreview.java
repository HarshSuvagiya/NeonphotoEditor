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
import com.facebook.ads.AdSize;
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
    private com.facebook.ads.AdView adViewfb;

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_preview);
        context = this;
        //banner ad
        adViewfb = new com.facebook.ads.AdView(NeonPhotoPreview.this, getString(R.string.banner_ad_unit_idfb), AdSize.BANNER_HEIGHT_50);

        // Find the Ad Container
        LinearLayout adContainer = (LinearLayout) findViewById(R.id.banner_container);
        adContainer.setVisibility(View.VISIBLE);
        // Add the ad view to your activity layout
        adContainer.addView(adViewfb);

        // Request an ad
        adViewfb.loadAd();

        header = (TextView) findViewById(R.id.my_header_text);
        iv = (ImageView) findViewById(R.id.ivpreview);
        ivshare = (ImageView) findViewById(R.id.ivshare);
        ivdelete = (ImageView) findViewById(R.id.ivdelete);
        lbottom = (LinearLayout) findViewById(R.id.linearbottom);
        width = Helper.getWidth(context);
        height = Helper.getHeight(context);
        forUI();
        init();
    }

    private void forUI() {
        SetLayparam.setMargins(context, iv, 60, 60, 60, 0);
        SetLayparam.setMargins(context, lbottom, 0, 85, 0, 80);
        SetLayparam.setHeightWidth(context, ivshare, 392, 156);
        SetLayparam.setHeightWidth(context, ivdelete, 392, 156);
    }

    private void init() {
        header.setText(R.string.preview);
        header.setTypeface(Typeface.createFromAsset(getAssets(), "Poppins-Bold.ttf"));
        from = getIntent().getIntExtra("from", 0);
        path = getIntent().getStringExtra("path");
        Glide.with(context).load(path).transition(DrawableTransitionOptions.withCrossFade()).into(iv);
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
            File file = new File(path);
            if (file.exists()) {
                file.delete();
            }
            MediaScannerConnection.scanFile(context, new String[]{path}, (String[]) null, new MediaScannerConnection.OnScanCompletedListener() {
                public void onScanCompleted(String str, Uri uri) {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            finish();
                        }
                    });
                }
            });
        } catch (Exception unused) {
        }
    }

    public void share(View view) {
        Helper.shareImage(context, path);
    }

    public void back(View view) {
        onBackPressed();
    }

    public void onBackPressed() {
         if (from == 0) {
            Intent intent = new Intent(context, HomeActivity.class);
            intent.setFlags(268435456);
            startActivity(intent);
        } else {
            super.onBackPressed();
            finish();
        }
    }


}
