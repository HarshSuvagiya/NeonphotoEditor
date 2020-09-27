package com.scorpion.NeonphotoEditor.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.scorpion.NeonphotoEditor.R;
import com.scorpion.NeonphotoEditor.Util.Helper;
import com.scorpion.NeonphotoEditor.Util.Path;
import com.scorpion.NeonphotoEditor.Util.SetLayparam;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class ImageSpiralAdapter extends RecyclerView.Adapter<ImageSpiralAdapter.MyViewHolder> {
    Context context;
    String folder = "Spiral";
    int height;
    String[] images;
    ArrayList<String> listImages;
    OnSpiralListener listener;
    int selected;
    int width;

    public interface OnSpiralListener {
        void onSpiralClicked(int i);
    }

    public ImageSpiralAdapter(Context context2, OnSpiralListener onSpiralListener) {
        context = context2;
        listener = onSpiralListener;
        try {
            images = context.getAssets().list(folder);
        } catch (IOException e) {
            e.printStackTrace();
        }
        listImages = new ArrayList<>(Arrays.asList(images));
        width = Helper.getWidth(context);
        height = Helper.getHeight(context);
    }

    public void onBindViewHolder(MyViewHolder myViewHolder, final int i) {
        ((RequestBuilder) Glide.with(context).load(Path.SPIRAL.thumb(i)).transition(DrawableTransitionOptions.withCrossFade()).transform((Transformation<Bitmap>) new MultiTransformation((Transformation[]) new Transformation[]{new CenterCrop(), new RoundedCorners(10)}))).into(myViewHolder.iv);
        myViewHolder.relselected.setBackgroundResource(selected == i ? R.drawable.effect_select : R.drawable.effect_unselect);
        myViewHolder.relselected.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                selected = i;
                notifyDataSetChanged();
                if (listener != null) {
                    listener.onSpiralClicked(i);
                }
            }
        });
    }

    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new MyViewHolder(((LayoutInflater) context.getSystemService("layout_inflater")).inflate(R.layout.item_spiral, viewGroup, false));
    }

    public int getItemCount() {
        return listImages.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView iv;
        RelativeLayout relmain;
        RelativeLayout relselected;

        public MyViewHolder(View view) {
            super(view);
            iv = (ImageView) view.findViewById(R.id.ivspiral);
            relmain = (RelativeLayout) view.findViewById(R.id.relsitem);
            relselected = (RelativeLayout) view.findViewById(R.id.relselected);
            forUI();
        }

        private void forUI() {
            SetLayparam.setHeightAsBoth(context, relmain, 160);
            SetLayparam.setMarginLeft(context, relmain, 38);
            SetLayparam.setMarginsAsHeight(context, iv, 10);
        }
    }
}
