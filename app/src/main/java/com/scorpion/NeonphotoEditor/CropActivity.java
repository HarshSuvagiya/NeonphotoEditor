package com.scorpion.NeonphotoEditor;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.scorpion.NeonphotoEditor.Util.Helper;
import com.scorpion.NeonphotoEditor.Util.SetLayparam;
import com.theartofdev.edmodo.cropper.CropImageView;

public class CropActivity extends Activity {
    CropImageView civ;
    Context context;
    Uri cropuri;
    TextView header;
    int height;
    ImageView ivdone;
    ImageView ivfh;
    ImageView ivfv;
    ImageView ivrl;
    ImageView ivrr;
    LinearLayout lbottom;
    RelativeLayout relpbar;
    int width;

    public void nothing(View view) {
    }


    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_crop);
        getWindow().setFlags(1024, 1024);
        this.context = this;
        this.civ = (CropImageView) findViewById(R.id.civ);
        this.header = (TextView) findViewById(R.id.my_header_text);
        this.ivdone = (ImageView) findViewById(R.id.ivoption);
        this.lbottom = (LinearLayout) findViewById(R.id.linearbottom);
        this.relpbar = (RelativeLayout) findViewById(R.id.relpbar);
        this.ivfh = (ImageView) findViewById(R.id.ivfh);
        this.ivfv = (ImageView) findViewById(R.id.ivfv);
        this.ivrl = (ImageView) findViewById(R.id.ivrl);
        this.ivrr = (ImageView) findViewById(R.id.ivrr);
        this.width = Helper.getWidth(this.context);
        this.height = Helper.getHeight(this.context);
        forUI();
        init();
    }

    private void forUI() {
        SetLayparam.setMargins(this.context, this.civ, 0, 50, 0, 50);
        SetLayparam.setHeight(this.context, this.lbottom, 250);

    }

    private void init() {

        this.ivdone.setVisibility(View.VISIBLE);
        this.header.setTypeface(Typeface.createFromAsset(getAssets(), "Poppins-Bold.ttf"));
        this.cropuri = Uri.parse(getIntent().getStringExtra("uri"));
        this.civ.setImageUriAsync(this.cropuri);
    }

    public void option(View view) {
        new SaveImage().execute(new Void[0]);
    }

    public void onSaveComplete(final String str) {
        Intent intent = new Intent(this.context, PhotoEffectEditor.class);
        intent.putExtra("crop", str);
        startActivity(intent);
        finish();
    }

    public void flipVertical(View view) {
        this.civ.flipImageVertically();
    }

    public void flipHorizontal(View view) {
        this.civ.flipImageHorizontally();
    }

    public void rotateRight(View view) {
        this.civ.rotateImage(90);
    }

    public void rotateLeft(View view) {
        this.civ.rotateImage(-90);
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
        finish();
    }

    public class SaveImage extends AsyncTask<Void, Void, Void> {
        Bitmap cropped;
        String path;

        public SaveImage() {
        }

        public void onPreExecute() {
            super.onPreExecute();
            CropActivity.this.relpbar.setVisibility(View.VISIBLE);
            this.cropped = CropActivity.this.civ.getCroppedImage();
        }

        public Void doInBackground(Void... voidArr) {
            this.cropped = Helper.getBitmapResize(CropActivity.this.context, this.cropped, 1080, 1080);
            this.path = Helper.saveCropBitmap(CropActivity.this.context, this.cropped);
            return null;
        }

        public void onPostExecute(Void voidR) {
            super.onPostExecute(voidR);
            CropActivity.this.relpbar.setVisibility(View.GONE);
            CropActivity.this.onSaveComplete(this.path);
        }
    }


}
