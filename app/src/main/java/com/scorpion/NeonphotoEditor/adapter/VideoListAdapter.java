package com.scorpion.NeonphotoEditor.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.scorpion.NeonphotoEditor.R;
import com.scorpion.NeonphotoEditor.util.Helper;
import com.scorpion.NeonphotoEditor.util.Layparam;
import com.scorpion.NeonphotoEditor.util.Vdata;

import java.util.ArrayList;

public class VideoListAdapter extends RecyclerView.Adapter<VideoListAdapter.MyViewHolder> {
    ArrayList<Vdata> al;
    Context context;
    int height;
    int width;

    public VideoListAdapter(Context context2, ArrayList<Vdata> arrayList) {
        this.context = context2;
        this.al = arrayList;
        this.width = Helper.getWidth(context2);
        this.height = Helper.getHeight(context2);
    }

    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        Vdata fX_Vdata = this.al.get(i);
        String path = fX_Vdata.getPath();
        if (!TextUtils.isEmpty(fX_Vdata.getDuration())) {
            myViewHolder.tvtime.setText(Helper.milliSecondsToTimer(Long.parseLong(fX_Vdata.getDuration())));
        } else {
            myViewHolder.tvtime.setText("00:15");
        }
        ((RequestBuilder) Glide.with(this.context).load(path).transform((Transformation<Bitmap>) new MultiTransformation((Transformation[]) new Transformation[]{new CenterCrop()}))).transition(DrawableTransitionOptions.withCrossFade()).into(myViewHolder.iv);
    }

    public int getItemCount() {
        return this.al.size();
    }

    @NonNull
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new MyViewHolder(LayoutInflater.from(this.context).inflate(R.layout.item_video_list, viewGroup, false));
    }

    public int getWidth(int i) {
        return (this.width * i) / 1080;
    }

    public int getHeight(int i) {
        return (this.height * i) / 1920;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView iv;
        ImageView ivi;
        ImageView ivplay;
        RelativeLayout relmain;
        RelativeLayout reltime;
        TextView tvtime;

        public MyViewHolder(View view) {
            super(view);
            this.relmain = (RelativeLayout) view.findViewById(R.id.relvideolist);
            this.iv = (ImageView) view.findViewById(R.id.ivthumb);
            this.ivi = (ImageView) view.findViewById(R.id.ivicon);
            this.ivplay = (ImageView) view.findViewById(R.id.ivplay);
            this.reltime = (RelativeLayout) view.findViewById(R.id.reltime);
            this.tvtime = (TextView) view.findViewById(R.id.tvtime);
            forUI();
        }

        private void forUI() {
            Layparam.setWidthAsHeight(VideoListAdapter.this.context, this.relmain, 470, 484);
            Layparam.setWidthAsHeight(VideoListAdapter.this.context, this.reltime, 144, 154);
            Layparam.setHeightAsBoth(VideoListAdapter.this.context, this.ivi, 90);
            Layparam.setMarginLeft(VideoListAdapter.this.context, this.ivi, 20);
            Layparam.setPadding(VideoListAdapter.this.context, this.tvtime, 30, 70, 0, 0);
            Layparam.setHeightAsBoth(VideoListAdapter.this.context, this.ivplay, 110);
        }
    }
}
