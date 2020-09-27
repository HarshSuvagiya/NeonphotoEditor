package com.scorpion.NeonphotoEditor.Adapters;

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
import com.scorpion.NeonphotoEditor.Util.Helper;
import com.scorpion.NeonphotoEditor.Util.SetLayparam;
import com.scorpion.NeonphotoEditor.Util.Vdata;

import java.util.ArrayList;

public class VideoGalleryAdapter extends RecyclerView.Adapter<VideoGalleryAdapter.MyViewHolder> {
    ArrayList<Vdata> al;
    Context context;
    int height;
    int width;

    public VideoGalleryAdapter(Context context2, ArrayList<Vdata> arrayList) {
        context = context2;
        al = arrayList;
        width = Helper.getWidth(context2);
        height = Helper.getHeight(context2);
    }

    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        Vdata Vdata1 = al.get(i);
        String path = Vdata1.getPath();
        if (!TextUtils.isEmpty(Vdata1.getDuration())) {
            myViewHolder.tvtime.setText(Helper.milliSecondsToTimer(Long.parseLong(Vdata1.getDuration())));
        } else {
            myViewHolder.tvtime.setText("00:15");
        }
        ((RequestBuilder) Glide.with(context).load(path).transform((Transformation<Bitmap>) new MultiTransformation((Transformation[]) new Transformation[]{new CenterCrop()}))).transition(DrawableTransitionOptions.withCrossFade()).into(myViewHolder.iv);
    }

    public int getItemCount() {
        return al.size();
    }

    @NonNull
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_video_list, viewGroup, false));
    }

    public int getWidth(int i) {
        return (width * i) / 1080;
    }

    public int getHeight(int i) {
        return (height * i) / 1920;
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
            relmain = (RelativeLayout) view.findViewById(R.id.relvideolist);
            iv = (ImageView) view.findViewById(R.id.ivthumb);
            ivi = (ImageView) view.findViewById(R.id.ivicon);
            ivplay = (ImageView) view.findViewById(R.id.ivplay);
            reltime = (RelativeLayout) view.findViewById(R.id.reltime);
            tvtime = (TextView) view.findViewById(R.id.tvtime);
            forUI();
        }

        private void forUI() {
            SetLayparam.setWidthAsHeight(context, relmain, 470, 484);
            SetLayparam.setWidthAsHeight(context, reltime, 144, 154);
            SetLayparam.setHeightAsBoth(context, ivi, 90);
            SetLayparam.setMarginLeft(context, ivi, 20);
            SetLayparam.setPadding(context, tvtime, 30, 70, 0, 0);
            SetLayparam.setHeightAsBoth(context, ivplay, 110);
        }
    }
}
