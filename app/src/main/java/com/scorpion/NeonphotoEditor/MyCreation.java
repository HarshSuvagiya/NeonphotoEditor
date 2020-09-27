package com.scorpion.NeonphotoEditor;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import com.scorpion.NeonphotoEditor.FragmentMyCreation.ImageFragement;
import com.scorpion.NeonphotoEditor.FragmentMyCreation.VideoFragement;
import com.scorpion.NeonphotoEditor.Util.Helper;
import com.scorpion.NeonphotoEditor.Util.SetLayparam;

public class MyCreation extends FragmentActivity {
    private FrameLayout adContainerView;
    Context context;
    int current;
    TextView header;
    int height;
    ImageFragement iCreationFrag;
    ImageView ivptab;
    ImageView ivvtab;
    LinearLayout lfrag;
    LinearLayout ltab;
    VideoFragement vCreationFrag;
    int width;


    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_my_creation);
        this.context = this;

        this.header = (TextView) findViewById(R.id.my_header_text);
        this.ltab = (LinearLayout) findViewById(R.id.lineartab);
        this.lfrag = (LinearLayout) findViewById(R.id.linearfrag);
        this.ivptab = (ImageView) findViewById(R.id.ivptab);
        this.ivvtab = (ImageView) findViewById(R.id.ivvtab);
        this.width = Helper.getWidth(this.context);
        this.height = Helper.getHeight(this.context);
        forUI();
        init();
    }

    public void forUI() {
        SetLayparam.setMarginTop(this.context, this.ltab, 35);
        SetLayparam.setMarginTop(this.context, this.lfrag, 20);
        SetLayparam.setHeightWidth(this.context, this.ivptab, 482, 100);
        SetLayparam.setHeightWidth(this.context, this.ivvtab, 482, 100);
    }

    public void init() {
        this.header.setText(R.string.app_name);
        this.header.setTypeface(Typeface.createFromAsset(getAssets(), "Poppins-Bold.ttf"));
    }

    public void imagetab(View view) {
        this.current = 1;
        this.ivptab.setImageResource(R.drawable.image_spiral_press);
        this.ivvtab.setImageResource(R.drawable.video_spiral_unpress);
        if (this.iCreationFrag == null) {
            this.iCreationFrag = new ImageFragement();
        }
        FragmentTransaction beginTransaction = getSupportFragmentManager().beginTransaction();
        beginTransaction.replace(this.lfrag.getId(), this.iCreationFrag);
        beginTransaction.commit();
    }

    public void videotab(View view) {
        this.current = 0;
        this.ivptab.setImageResource(R.drawable.image_spiral_unpress);
        this.ivvtab.setImageResource(R.drawable.video_spiral_press);
        if (this.vCreationFrag == null) {
            this.vCreationFrag = new VideoFragement();
        }
        FragmentTransaction beginTransaction = getSupportFragmentManager().beginTransaction();
        beginTransaction.replace(this.lfrag.getId(), this.vCreationFrag);
        beginTransaction.commit();
    }

    public void onResume() {
        super.onResume();
        switch (this.current) {
            case 0:
                this.vCreationFrag = null;
                videotab((View) null);
                return;
            case 1:
                this.iCreationFrag = null;
                imagetab((View) null);
                return;
            default:
                return;
        }
    }

    public void back(View view) {
        onBackPressed();
    }

    public int getWidth(int i) {
        return (this.width * i) / 1080;
    }

    public int getHeight(int i) {
        return (this.height * i) / 1920;
    }

    public void onBackPressed() {
        super.onBackPressed();
    }

}
