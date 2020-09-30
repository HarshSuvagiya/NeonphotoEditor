package com.scorpion.NeonphotoEditor;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.scorpion.NeonphotoEditor.Util.Helper;
import com.scorpion.NeonphotoEditor.Util.SetLayparam;

public class HomeActivity extends Activity {
    Context context;
    int height;
    ImageView ivmy;
    ImageView ivphoto, videoeffect;
    LinearLayout lmore;
    View vspace;
    int width;

    private void init() {
    }


    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_home);
        context = this;
        goToMain();
        ivphoto = (ImageView) findViewById(R.id.ivphoto);
        ivmy = (ImageView) findViewById(R.id.mygallery);
        videoeffect = findViewById(R.id.videoeffect);

        width = Helper.getWidth(context);
        height = Helper.getHeight(context);
        forUI();

        TextView textView =(TextView)findViewById(R.id.privacyPolicy);
        textView.setClickable(true);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
        String text = "<a href='https://sites.google.com/view/super-fx-neon/home'>Privacy Policy</a>";
        textView.setText(Html.fromHtml(text));
    }

    private void forUI() {

        SetLayparam.setHeightWidth(context, ivphoto, 760, 400);
        SetLayparam.setHeightWidth(context, ivmy, 760, 400);
        SetLayparam.setHeightWidth(context, videoeffect, 760, 400);

    }

    public void video(View view) {

        startActivity(new Intent(context, GalleryVideoList.class));

    }

    public void photo(View view) {

        Intent intent = new Intent("android.intent.action.PICK");
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(intent, 102);
    }

    public void creation(View view) {
        FBInterstitial.getInstance().displayFBInterstitial(HomeActivity.this, new FBInterstitial.FbCallback() {
            public void callbackCall() {
                startActivity(new Intent(context, MyCreation.class));
            }
        });

    }

    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i2 == -1) {
            Uri data = intent.getData();
            Intent intent2 = new Intent(context, CropActivity.class);
            intent2.putExtra("uri", data + "");
            startActivity(intent2);
        }
    }


    private void checkPermissions() {
        String[] strArr = {"android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.READ_EXTERNAL_STORAGE"};
        if (Build.VERSION.SDK_INT > 22) {
            requestPermissions(strArr, 101);
        } else {
            onSuccess();
        }
    }

    public void onRequestPermissionsResult(int i, @NonNull String[] strArr, @NonNull int[] iArr) {
        if (i != 101) {
            return;
        }
        if (iArr.length > 0) {
            for (int i2 : iArr) {
                if (i2 != 0) {
                    finish();
                    return;
                }
            }
            onSuccess();
            return;
        }
        finish();
    }

    public void onSuccess() {
        init();
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
        super.onBackPressed();
        finishAffinity();
    }

    public void goToMain() {
        checkPermissions();
    }


}
