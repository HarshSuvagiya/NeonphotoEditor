package com.scorpion.NeonphotoEditor;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;

import com.scorpion.NeonphotoEditor.Util.Helper;
import com.scorpion.NeonphotoEditor.Util.SetLayparam;

public class HomeActivity extends Activity {
    Context context;
    int height;
    ImageView ivmy;
    ImageView ivphoto,videoeffect;
    LinearLayout lmore;
    View vspace;
    int width;

    private void init() {
    }


    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_home);
        this.context = this;
            goToMain();
        this.ivphoto = (ImageView) findViewById(R.id.ivphoto);
        this.ivmy = (ImageView) findViewById(R.id.mygallery);
        videoeffect=findViewById(R.id.videoeffect);

        this.width = Helper.getWidth(this.context);
        this.height = Helper.getHeight(this.context);
        forUI();
    }

    private void forUI() {

        SetLayparam.setHeightWidth(this.context, this.ivphoto, 790, 430);
        SetLayparam.setHeightWidth(this.context, this.ivmy, 790, 430);
        SetLayparam.setHeightWidth(this.context, this.videoeffect, 790, 430);

    }

    public void video(View view) {

                    HomeActivity.this.startActivity(new Intent(HomeActivity.this.context, GalleryVideoList.class));

    }

    public void photo(View view) {

        Intent intent = new Intent("android.intent.action.PICK");
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(intent, 102);
    }

    public void creation(View view) {

        startActivity(new Intent(this.context, MyCreation.class));
    }

    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i2 == -1) {
            Uri data = intent.getData();
            Intent intent2 = new Intent(this.context, CropActivity.class);
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
        return (this.width * i) / 1080;
    }

    public int getHeight(int i) {
        return (this.height * i) / 1920;
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
