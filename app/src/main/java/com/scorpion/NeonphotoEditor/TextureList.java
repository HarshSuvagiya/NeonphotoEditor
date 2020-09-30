package com.scorpion.NeonphotoEditor;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;

import com.facebook.ads.AdSize;
import com.scorpion.NeonphotoEditor.Adapters.TextureAdapter;
import com.scorpion.NeonphotoEditor.Util.GridRecyclerView;
import com.scorpion.NeonphotoEditor.Util.Helper;
import com.scorpion.NeonphotoEditor.Util.Path;
import com.scorpion.NeonphotoEditor.Util.RVGridSpacing;
import com.scorpion.NeonphotoEditor.Util.RecyclerTouchListener;

public class TextureList extends Activity {
    TextureAdapter adapter;
    Context context;
    TextView header;
    int height;
    GridRecyclerView rcv;
    int width;

    private void forUI() {
    }
    private com.facebook.ads.AdView adViewfb;
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_texture_list);
        getWindow().setFlags(1024, 1024);
        context = this;

        //banner ad
        adViewfb = new com.facebook.ads.AdView(TextureList.this, getString(R.string.banner_ad_unit_idfb), AdSize.BANNER_HEIGHT_50);

        // Find the Ad Container
        LinearLayout adContainer = (LinearLayout) findViewById(R.id.banner_container);
        adContainer.setVisibility(View.VISIBLE);
        // Add the ad view to your activity layout
        adContainer.addView(adViewfb);

        // Request an ad
        adViewfb.loadAd();
        header = (TextView) findViewById(R.id.my_header_text);
        rcv = (GridRecyclerView) findViewById(R.id.rcvtexture);
        width = Helper.getWidth(context);
        height = Helper.getHeight(context);
        forUI();
        init();
    }

    private void init() {
        header.setText(R.string.textures);
        header.setTypeface(Typeface.createFromAsset(getAssets(), "Poppins-Bold.ttf"));
        rcv.setLayoutManager(new GridLayoutManager(context, 2));
        rcv.addItemDecoration(new RVGridSpacing(2, getWidth(37), true));
        adapter = new TextureAdapter(context);
        rcv.setAdapter(adapter);
        rcv.addOnItemTouchListener(new RecyclerTouchListener(context, rcv, new RecyclerTouchListener.ClickListener() {
            public void onLongClick(View view, int i) {
            }

            public void onClick(View view, int i) {
                String texture = Path.TEXTURES.texture(i);
                Intent intent = new Intent();
                intent.putExtra("texture", texture);
                setResult(-1, intent);
                finish();
            }
        }));
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
        setResult(0);
        finish();
    }

}
