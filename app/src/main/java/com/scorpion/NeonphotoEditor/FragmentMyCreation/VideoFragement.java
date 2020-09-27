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
        context = getActivity();
        rcv = (RecyclerView) inflate.findViewById(R.id.rcvmy);
        pbar = (ProgressBar) inflate.findViewById(R.id.pbar);
        width = Helper.getWidth(context);
        height = Helper.getHeight(context);
        forUI();
        init();
        return inflate;
    }

    private void init() {
        rcv.setLayoutManager(new LinearLayoutManager(context));
        rcv.addOnItemTouchListener(new RecyclerTouchListener(context, rcv, new RecyclerTouchListener.ClickListener() {
            public void onLongClick(View view, int i) {
            }

            public void onClick(View view, int i) {
                if (!isClicked) {
                    isClicked = true;
                    Intent intent = new Intent(context, NeonVideoPreview.class);
                    intent.putExtra("vpath", aldata.get(i).getPath());
                    intent.putExtra("fromedit", false);
                    startActivity(intent);
                }
            }
        }));
        new LoadTask().execute(new Void[0]);
    }

    public void loadData() {
        aldata.clear();
        alcheck.clear();
        String[] strArr = {"%" + getResources().getString(R.string.app_name) + "_%.mp4"};
        ContentResolver contentResolver = context.getContentResolver();
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
                } else if (!alcheck.contains(string)) {
                    alcheck.add(string);
                    Vdata fX_Vdata = new Vdata();
                    fX_Vdata.setID((long) query.getInt(query.getColumnIndexOrThrow("_id")));
                    fX_Vdata.setPath(query.getString(query.getColumnIndexOrThrow("_data")));
                    fX_Vdata.setTitle(query.getString(query.getColumnIndexOrThrow("title")));
                    fX_Vdata.setDate_taken(query.getString(query.getColumnIndexOrThrow("_display_name")));
                    fX_Vdata.setDate_taken(query.getString(query.getColumnIndexOrThrow("datetaken")));
                    fX_Vdata.setDate_added(query.getString(query.getColumnIndexOrThrow("date_added")));
                    fX_Vdata.setDuration(query.getString(query.getColumnIndexOrThrow("duration")));
                    aldata.add(fX_Vdata);
                }
            }
        }
    }

    public void onResume() {
        super.onResume();
        isClicked = false;
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle bundle) {
        super.onViewCreated(view, bundle);
        isClicked = false;
    }

    public int getWidth(int i) {
        return (width * i) / 1080;
    }

    public int getHeight(int i) {
        return (height * i) / 1920;
    }

    @SuppressLint({"StaticFieldLeak"})
    public class LoadTask extends AsyncTask<Void, Void, Void> {
        public LoadTask() {
        }

        public void onPreExecute() {
            super.onPreExecute();
            rcv.setVisibility(View.GONE);
            pbar.setVisibility(View.VISIBLE);
            isClicked = false;
        }

        public Void doInBackground(Void... voidArr) {
            loadData();
            return null;
        }


        public void onPostExecute(Void voidR) {
            super.onPostExecute(voidR);
            adapter = new MyVideoAdapter(context, aldata);
            rcv.setAdapter(adapter);
            rcv.setLayoutManager(new LinearLayoutManager(context));
            rcv.setVisibility(View.VISIBLE);
            pbar.setVisibility(View.GONE);
        }
    }
}
