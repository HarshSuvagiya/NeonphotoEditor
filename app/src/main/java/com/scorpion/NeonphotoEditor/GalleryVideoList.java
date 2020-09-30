package com.scorpion.NeonphotoEditor;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.ads.AdSize;
import com.scorpion.NeonphotoEditor.Adapters.VideoGalleryAdapter;
import com.scorpion.NeonphotoEditor.Util.Helper;
import com.scorpion.NeonphotoEditor.Util.RVGridSpacing;
import com.scorpion.NeonphotoEditor.Util.RecyclerTouchListener;
import com.scorpion.NeonphotoEditor.Util.Vdata;

import java.util.ArrayList;

public class GalleryVideoList extends Activity {
    private FrameLayout adContainerView;
    VideoGalleryAdapter adapter;
    Context context;
    TextView header;
    int height;
    boolean isClicked = false;
    ImageView ivmore;
    RecyclerView rcv;
    RelativeLayout relpbar;
    LoadTask task;
    TextView tvnothing;
    ArrayList<Vdata> vl = new ArrayList<>();
    int width;
    private com.facebook.ads.AdView adViewfb;

    private void forUI() {
    }

    public void nothing(View view) {
    }


    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_video_list);
        getWindow().setFlags(1024, 1024);
        context = this;
        //banner ad
        adViewfb = new com.facebook.ads.AdView(GalleryVideoList.this, getString(R.string.banner_ad_unit_idfb), AdSize.BANNER_HEIGHT_50);

        // Find the Ad Container
        LinearLayout adContainer = (LinearLayout) findViewById(R.id.banner_container);
        adContainer.setVisibility(View.VISIBLE);
        // Add the ad view to your activity layout
        adContainer.addView(adViewfb);

        // Request an ad
        adViewfb.loadAd();
        header = (TextView) findViewById(R.id.my_header_text);
        ivmore = (ImageView) findViewById(R.id.ivoption);
        relpbar = (RelativeLayout) findViewById(R.id.relpbar);
        tvnothing = (TextView) findViewById(R.id.tvnothing);
        rcv = (RecyclerView) findViewById(R.id.rcvvideo);
        width = getResources().getDisplayMetrics().widthPixels;
        height = getResources().getDisplayMetrics().heightPixels;
        forUI();
        init();
    }

    private void init() {
        header.setText(R.string.app_name);
        ivmore.setVisibility(View.INVISIBLE);
        header.setTypeface(Typeface.createFromAsset(getAssets(), "Poppins-Bold.ttf"));
        task = new LoadTask();
        task.execute(new Void[0]);
        rcv.setLayoutManager(new GridLayoutManager(context, 2));
        rcv.addItemDecoration(new RVGridSpacing(2, getWidth(45), true));
        rcv.addOnItemTouchListener(new RecyclerTouchListener(context, rcv, new RecyclerTouchListener.ClickListener() {
            public void onLongClick(View view, int i) {
            }

            public void onClick(View view, int i) {
                if (!isClicked) {
                    isClicked = true;
                    String path = vl.get(i).getPath();
                    String duration = vl.get(i).getDuration();
                    Uri.parse(path);
                    FBInterstitial.getInstance().displayFBInterstitial(GalleryVideoList.this, new FBInterstitial.FbCallback() {
                        public void callbackCall() {
                            Intent intent = new Intent(context, NeonVideoEffectEditor.class);
                            intent.putExtra("path", path + "");
                            intent.putExtra("duration", duration);
                            startActivity(intent);
                        }
                    });


                }
            }
        }));
    }

    public void onResume() {
        super.onResume();
        isClicked = false;
    }

    public void back(View view) {
        onBackPressed();
    }

    public int getWidth(int i) {
        return (width * i) / 1080;
    }

    public int getHeight(int i) {
        return (height * i) / 1920;
    }

    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public class LoadTask extends AsyncTask<Void, Void, Void> {
        public LoadTask() {
        }

        public void onPreExecute() {
            super.onPreExecute();
            vl.clear();
            relpbar.setVisibility(View.VISIBLE);
            tvnothing.setVisibility(View.GONE);
            rcv.setVisibility(View.GONE);
        }

        public Void doInBackground(Void... voidArr) {
            vl = Helper.getAllVideo(context);
            return null;
        }

        public void onPostExecute(Void voidR) {
            super.onPostExecute(voidR);
            relpbar.setVisibility(View.GONE);
            if (vl.size() > 0) {
                adapter = new VideoGalleryAdapter(context, vl);
                rcv.setAdapter(adapter);
                rcv.setVisibility(View.VISIBLE);
                return;
            }
            tvnothing.setVisibility(View.VISIBLE);
        }
    }


}
