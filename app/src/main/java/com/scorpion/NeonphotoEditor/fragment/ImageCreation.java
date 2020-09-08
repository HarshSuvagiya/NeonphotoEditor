package com.scorpion.NeonphotoEditor.fragment;

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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.scorpion.NeonphotoEditor.PhotoPreview;
import com.scorpion.NeonphotoEditor.R;
import com.scorpion.NeonphotoEditor.adapter.ImageCreationAdapter;
import com.scorpion.NeonphotoEditor.util.Helper;
import com.scorpion.NeonphotoEditor.util.RVGridSpacing;
import com.scorpion.NeonphotoEditor.util.RecyclerTouchListener;

import java.io.File;
import java.util.ArrayList;

public class ImageCreation extends Fragment {
    ImageCreationAdapter adapter;
    ArrayList<String> aldata = new ArrayList<>();
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
        View inflate = layoutInflater.inflate(R.layout.fragment_imagecreation, viewGroup, false);
        this.context = getActivity();
        this.rcv = (RecyclerView) inflate.findViewById(R.id.rcvicreation);
        this.pbar = (ProgressBar) inflate.findViewById(R.id.pbar);
        this.width = Helper.getWidth(this.context);
        this.height = Helper.getHeight(this.context);
        forUI();
        init();
        return inflate;
    }

    private void init() {
        this.rcv.setLayoutManager(new GridLayoutManager(this.context, 2));
        this.rcv.addItemDecoration(new RVGridSpacing(2, getWidth(37), true));
        this.rcv.addOnItemTouchListener(new RecyclerTouchListener(this.context, this.rcv, new RecyclerTouchListener.ClickListener() {
            public void onLongClick(View view, int i) {
            }

            public void onClick(View view, int i) {
                if (!ImageCreation.this.isClicked) {
                    ImageCreation.this.isClicked = true;
                    Intent intent = new Intent(ImageCreation.this.context, PhotoPreview.class);
                    intent.putExtra("path", ImageCreation.this.aldata.get(i));
                    intent.putExtra("from", 1);
                    ImageCreation.this.startActivity(intent);
                }
            }
        }));
        new LoadTask().execute(new Void[0]);
    }

    public void loadData() {
        this.aldata.clear();
        String[] strArr = {"%" + getResources().getString(R.string.app_name) + "_%.jpg"};
        ContentResolver contentResolver = this.context.getContentResolver();
        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        Cursor query = contentResolver.query(uri, new String[]{"_id", "_data", "_display_name", "_size", "date_added"}, "_data" + " like ? ", strArr, "date_added DESC");
        if (query != null) {
            while (query.moveToNext()) {
                query.getInt(query.getColumnIndex("_id"));
                String string = query.getString(query.getColumnIndex("_data"));
                File file = new File(string);
                if (!file.exists()) {
                    Helper.showLog("NNN", "Not Found : " + string);
                } else if (file.length() <= 0) {
                    Helper.showLog("NNN", "Size zero : " + string);
                } else if (!this.aldata.contains(string)) {
                    this.aldata.add(string);
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
            ImageCreation.this.rcv.setVisibility(View.GONE);
            ImageCreation.this.pbar.setVisibility(View.VISIBLE);
            ImageCreation.this.isClicked = false;
        }

        public Void doInBackground(Void... voidArr) {
            ImageCreation.this.loadData();
            return null;
        }

        public void onPostExecute(Void voidR) {
            super.onPostExecute(voidR);
            ImageCreation.this.adapter = new ImageCreationAdapter(ImageCreation.this.context, ImageCreation.this.aldata);
            ImageCreation.this.rcv.setAdapter(ImageCreation.this.adapter);
            ImageCreation.this.rcv.setVisibility(View.VISIBLE);
            ImageCreation.this.pbar.setVisibility(View.GONE);
        }
    }
}
