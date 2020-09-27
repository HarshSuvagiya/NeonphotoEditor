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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.scorpion.NeonphotoEditor.NeonPhotoPreview;
import com.scorpion.NeonphotoEditor.R;
import com.scorpion.NeonphotoEditor.Adapters.MyImageAdapter;
import com.scorpion.NeonphotoEditor.Util.Helper;
import com.scorpion.NeonphotoEditor.Util.RVGridSpacing;
import com.scorpion.NeonphotoEditor.Util.RecyclerTouchListener;

import java.io.File;
import java.util.ArrayList;

public class ImageFragement extends Fragment {
    MyImageAdapter adapter;
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
        View inflate = layoutInflater.inflate(R.layout.myimage_fragement, viewGroup, false);
        context = getActivity();
        rcv = (RecyclerView) inflate.findViewById(R.id.rcvicreation);
        pbar = (ProgressBar) inflate.findViewById(R.id.pbar);
        width = Helper.getWidth(context);
        height = Helper.getHeight(context);
        forUI();
        init();
        return inflate;
    }

    private void init() {
        rcv.setLayoutManager(new GridLayoutManager(context, 2));
        rcv.addItemDecoration(new RVGridSpacing(2, getWidth(37), true));
        rcv.addOnItemTouchListener(new RecyclerTouchListener(context, rcv, new RecyclerTouchListener.ClickListener() {
            public void onLongClick(View view, int i) {
            }

            public void onClick(View view, int i) {
                if (!isClicked) {
                    isClicked = true;
                    Intent intent = new Intent(context, NeonPhotoPreview.class);
                    intent.putExtra("path", aldata.get(i));
                    intent.putExtra("from", 1);
                    startActivity(intent);
                }
            }
        }));
        new LoadTask().execute(new Void[0]);
    }

    public void loadData() {
        aldata.clear();
        String[] strArr = {"%" + getResources().getString(R.string.app_name) + "_%.jpg"};
        ContentResolver contentResolver = context.getContentResolver();
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
                } else if (!aldata.contains(string)) {
                    aldata.add(string);
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
            adapter = new MyImageAdapter(context, aldata);
            rcv.setAdapter(adapter);
            rcv.setVisibility(View.VISIBLE);
            pbar.setVisibility(View.GONE);
        }
    }
}
