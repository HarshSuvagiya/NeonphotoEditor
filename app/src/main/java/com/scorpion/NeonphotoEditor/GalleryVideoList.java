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
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.scorpion.NeonphotoEditor.adapter.VideoListAdapter;
import com.scorpion.NeonphotoEditor.util.Helper;
import com.scorpion.NeonphotoEditor.util.RVGridSpacing;
import com.scorpion.NeonphotoEditor.util.RecyclerTouchListener;
import com.scorpion.NeonphotoEditor.util.Vdata;

import io.github.inflationx.viewpump.ViewPumpContextWrapper;
import java.util.ArrayList;

public class GalleryVideoList extends Activity {
    private FrameLayout adContainerView;
    VideoListAdapter adapter;
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

    private void forUI() {
    }

    public void nothing(View view) {
    }

//    public void attachBaseContext(Context context2) {
//        super.attachBaseContext(ViewPumpContextWrapper.wrap(context2));
//    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_video_list);
        getWindow().setFlags(1024, 1024);
        this.context = this;

        this.header = (TextView) findViewById(R.id.my_header_text);
        this.ivmore = (ImageView) findViewById(R.id.ivoption);
        this.relpbar = (RelativeLayout) findViewById(R.id.relpbar);
        this.tvnothing = (TextView) findViewById(R.id.tvnothing);
        this.rcv = (RecyclerView) findViewById(R.id.rcvvideo);
        this.width = getResources().getDisplayMetrics().widthPixels;
        this.height = getResources().getDisplayMetrics().heightPixels;
        forUI();
        init();
    }

    private void init() {
        this.header.setText(R.string.app_name);
        this.ivmore.setVisibility(View.INVISIBLE);
        this.header.setTypeface(Typeface.createFromAsset(getAssets(), "Poppins-Bold.ttf"));
        this.task = new LoadTask();
        this.task.execute(new Void[0]);
        this.rcv.setLayoutManager(new GridLayoutManager(this.context, 2));
        this.rcv.addItemDecoration(new RVGridSpacing(2, getWidth(45), true));
        this.rcv.addOnItemTouchListener(new RecyclerTouchListener(this.context, this.rcv, new RecyclerTouchListener.ClickListener() {
            public void onLongClick(View view, int i) {
            }

            public void onClick(View view, int i) {
                if (!GalleryVideoList.this.isClicked) {
                    GalleryVideoList.this.isClicked = true;
                    String path = GalleryVideoList.this.vl.get(i).getPath();
                    String duration = GalleryVideoList.this.vl.get(i).getDuration();
                    Uri.parse(path);
                    Intent intent = new Intent(GalleryVideoList.this.context, VideoEffectEditor.class);
                    intent.putExtra("path", path + "");
                    intent.putExtra("duration", duration);
                    GalleryVideoList.this.startActivity(intent);
                }
            }
        }));
    }

    public void onResume() {
        super.onResume();
        this.isClicked = false;
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
        finish();
    }

    public class LoadTask extends AsyncTask<Void, Void, Void> {
        public LoadTask() {
        }

        public void onPreExecute() {
            super.onPreExecute();
            GalleryVideoList.this.vl.clear();
            GalleryVideoList.this.relpbar.setVisibility(View.VISIBLE);
            GalleryVideoList.this.tvnothing.setVisibility(View.GONE);
            GalleryVideoList.this.rcv.setVisibility(View.GONE);
        }

        public Void doInBackground(Void... voidArr) {
            GalleryVideoList.this.vl = Helper.getAllVideo(GalleryVideoList.this.context);
            return null;
        }

        public void onPostExecute(Void voidR) {
            super.onPostExecute(voidR);
            GalleryVideoList.this.relpbar.setVisibility(View.GONE);
            if (GalleryVideoList.this.vl.size() > 0) {
                GalleryVideoList.this.adapter = new VideoListAdapter(GalleryVideoList.this.context, GalleryVideoList.this.vl);
                GalleryVideoList.this.rcv.setAdapter(GalleryVideoList.this.adapter);
                GalleryVideoList.this.rcv.setVisibility(View.VISIBLE);
                return;
            }
            GalleryVideoList.this.tvnothing.setVisibility(View.VISIBLE);
        }
    }


}
