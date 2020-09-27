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
        context = this;
        civ = (CropImageView) findViewById(R.id.civ);
        header = (TextView) findViewById(R.id.my_header_text);
        ivdone = (ImageView) findViewById(R.id.ivoption);
        lbottom = (LinearLayout) findViewById(R.id.linearbottom);
        relpbar = (RelativeLayout) findViewById(R.id.relpbar);
        ivfh = (ImageView) findViewById(R.id.ivfh);
        ivfv = (ImageView) findViewById(R.id.ivfv);
        ivrl = (ImageView) findViewById(R.id.ivrl);
        ivrr = (ImageView) findViewById(R.id.ivrr);
        width = Helper.getWidth(context);
        height = Helper.getHeight(context);
        forUI();
        init();
    }

    private void forUI() {
        SetLayparam.setMargins(context, civ, 0, 50, 0, 50);
        SetLayparam.setHeight(context, lbottom, 250);

    }

    private void init() {

        ivdone.setVisibility(View.VISIBLE);
        header.setTypeface(Typeface.createFromAsset(getAssets(), "Poppins-Bold.ttf"));
        cropuri = Uri.parse(getIntent().getStringExtra("uri"));
        civ.setImageUriAsync(cropuri);
    }

    public void option(View view) {
        new SaveImage().execute(new Void[0]);
    }

    public void onSaveComplete(final String str) {
        Intent intent = new Intent(context, PhotoEffectEditor.class);
        intent.putExtra("crop", str);
        startActivity(intent);
        finish();
    }

    public void flipVertical(View view) {
        civ.flipImageVertically();
    }

    public void flipHorizontal(View view) {
        civ.flipImageHorizontally();
    }

    public void rotateRight(View view) {
        civ.rotateImage(90);
    }

    public void rotateLeft(View view) {
        civ.rotateImage(-90);
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
        finish();
    }

    public class SaveImage extends AsyncTask<Void, Void, Void> {
        Bitmap cropped;
        String path;

        public SaveImage() {
        }

        public void onPreExecute() {
            super.onPreExecute();
            relpbar.setVisibility(View.VISIBLE);
            cropped = civ.getCroppedImage();
        }

        public Void doInBackground(Void... voidArr) {
            cropped = Helper.getBitmapResize(context, cropped, 1080, 1080);
            path = Helper.saveCropBitmap(context, cropped);
            return null;
        }

        public void onPostExecute(Void voidR) {
            super.onPostExecute(voidR);
            relpbar.setVisibility(View.GONE);
            onSaveComplete(path);
        }
    }


}
