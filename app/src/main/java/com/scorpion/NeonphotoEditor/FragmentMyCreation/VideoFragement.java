package com.scorpion.NeonphotoEditor.FragmentMyCreation;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.scorpion.NeonphotoEditor.NeonVideoPreview;
import com.scorpion.NeonphotoEditor.R;
import com.scorpion.NeonphotoEditor.Adapters.MyVideoAdapter;
import com.scorpion.NeonphotoEditor.Util.Helper;
import com.scorpion.NeonphotoEditor.Util.RecyclerTouchListener;
import com.scorpion.NeonphotoEditor.Util.Vdata;

import java.io.File;
import java.util.ArrayList;

public class VideoFragement extends Fragment {
    MyVideoAdapter adapter;
    ArrayList<String> alcheck = new ArrayList<>();
    ArrayList<Vdata> aldata = new ArrayList<>();
    Context context;
    int height;
    boolean isClicked = false;
    ProgressBar pbar;
    RecyclerView rcv;
    int width;

    private void forUI() {
    }

    @Nullable
    public View onCreateView(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.myvideo_fragement, viewGroup, false);
        this.context = getActivity();
        this.rcv = (RecyclerView) inflate.findViewById(R.id.rcvmy);
        this.pbar = (ProgressBar) inflate.findViewById(R.id.pbar);
        this.width = Helper.getWidth(this.context);
        this.height = Helper.getHeight(this.context);
        forUI();
        init();
        return inflate;
    }

    private void init() {
        this.rcv.setLayoutManager(new LinearLayoutManager(this.context));
        this.rcv.addOnItemTouchListener(new RecyclerTouchListener(this.context, this.rcv, new RecyclerTouchListener.ClickListener() {
            public void onLongClick(View view, int i) {
            }

            public void onClick(View view, int i) {
                if (!VideoFragement.this.isClicked) {
                    VideoFragement.this.isClicked = true;
                    Intent intent = new Intent(VideoFragement.this.context, NeonVideoPreview.class);
                    intent.putExtra("vpath", VideoFragement.this.aldata.get(i).getPath());
                    intent.putExtra("fromedit", false);
                    VideoFragement.this.startActivity(intent);
                }
            }
        }));
        new LoadTask().execute(new Void[0]);
    }

    public void loadData() {
        this.aldata.clear();
        this.alcheck.clear();
        String[] strArr = {"%" + getResources().getString(R.string.app_name) + "_%.mp4"};
        ContentResolver contentResolver = this.context.getContentResolver();
        Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        Cursor query = contentResolver.query(uri, new String[]{"_id", "_data", "title", "_display_name", "duration", "date_added", "datetaken"}, "_data" + " like ? ", strArr, "datetaken DESC");
        if (query != null) {
            while (query.moveToNext()) {
                query.getInt(query.getColumnIndex("_id"));
                query.getString(query.getColumnIndexOrThrow("datetaken"));
                String string = query.getString(query.getColumnIndex("_data"));
                File file = new File(string);
                if (!file.exists()) {
                    Helper.showLog("NNN", "Not Found : " + string);
                } else if (file.length() <= 0) {
                    Helper.showLog("NNN", "Size zero : " + string);
                } else if (!this.alcheck.contains(string)) {
                    this.alcheck.add(string);
                    Vdata fX_Vdata = new Vdata();
                    fX_Vdata.setID((long) query.getInt(query.getColumnIndexOrThrow("_id")));
                    fX_Vdata.setPath(query.getString(query.getColumnIndexOrThrow("_data")));
                    fX_Vdata.setTitle(query.getString(query.getColumnIndexOrThrow("title")));
                    fX_Vdata.setDate_taken(query.getString(query.getColumnIndexOrThrow("_display_name")));
                    fX_Vdata.setDate_taken(query.getString(query.getColumnIndexOrThrow("datetaken")));
                    fX_Vdata.setDate_added(query.getString(query.getColumnIndexOrThrow("date_added")));
                    fX_Vdata.setDuration(query.getString(query.getColumnIndexOrThrow("duration")));
                    this.aldata.add(fX_Vdata);
                }
            }
        }
    }

    public void onResume() {
        super.onResume();
        this.isClicked = false;
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle bundle) {
        super.onViewCreated(view, bundle);
        this.isClicked = false;
    }

    public int getWidth(int i) {
        return (this.width * i) / 1080;
    }

    public int getHeight(int i) {
        return (this.height * i) / 1920;
    }

    @SuppressLint({"StaticFieldLeak"})
    public class LoadTask extends AsyncTask<Void, Void, Void> {
        public LoadTask() {
        }

        public void onPreExecute() {
            super.onPreExecute();
            VideoFragement.this.rcv.setVisibility(View.GONE);
            VideoFragement.this.pbar.setVisibility(View.VISIBLE);
            VideoFragement.this.isClicked = false;
        }

        public Void doInBackground(Void... voidArr) {
            VideoFragement.this.loadData();
            return null;
        }


        public void onPostExecute(Void voidR) {
            super.onPostExecute(voidR);
            VideoFragement.this.adapter = new MyVideoAdapter(VideoFragement.this.context, VideoFragement.this.aldata);
            VideoFragement.this.rcv.setAdapter(VideoFragement.this.adapter);
            VideoFragement.this.rcv.setLayoutManager(new LinearLayoutManager(VideoFragement.this.context));
            VideoFragement.this.rcv.setVisibility(View.VISIBLE);
            VideoFragement.this.pbar.setVisibility(View.GONE);
        }
    }
}
