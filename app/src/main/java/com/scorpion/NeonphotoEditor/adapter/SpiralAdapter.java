package com.scorpion.NeonphotoEditor.adapter;

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
import com.scorpion.NeonphotoEditor.util.Helper;
import com.scorpion.NeonphotoEditor.util.Path;
import com.scorpion.NeonphotoEditor.util.Layparam;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class SpiralAdapter extends RecyclerView.Adapter<SpiralAdapter.MyViewHolder> {
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

    public SpiralAdapter(Context context2, OnSpiralListener onSpiralListener) {
        this.context = context2;
        this.listener = onSpiralListener;
        try {
            this.images = this.context.getAssets().list(this.folder);
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.listImages = new ArrayList<>(Arrays.asList(this.images));
        this.width = Helper.getWidth(this.context);
        this.height = Helper.getHeight(this.context);
    }

    public void onBindViewHolder(MyViewHolder myViewHolder, final int i) {
        ((RequestBuilder) Glide.with(this.context).load(Path.SPIRAL.thumb(i)).transition(DrawableTransitionOptions.withCrossFade()).transform((Transformation<Bitmap>) new MultiTransformation((Transformation[]) new Transformation[]{new CenterCrop(), new RoundedCorners(10)}))).into(myViewHolder.iv);
        myViewHolder.relselected.setBackgroundResource(this.selected == i ? R.drawable.effect_select : R.drawable.effect_unselect);
        myViewHolder.relselected.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                SpiralAdapter.this.selected = i;
                SpiralAdapter.this.notifyDataSetChanged();
                if (SpiralAdapter.this.listener != null) {
                    SpiralAdapter.this.listener.onSpiralClicked(i);
                }
            }
        });
    }

    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new MyViewHolder(((LayoutInflater) this.context.getSystemService("layout_inflater")).inflate(R.layout.item_spiral, viewGroup, false));
    }

    public int getItemCount() {
        return this.listImages.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView iv;
        RelativeLayout relmain;
        RelativeLayout relselected;

        public MyViewHolder(View view) {
            super(view);
            this.iv = (ImageView) view.findViewById(R.id.ivspiral);
            this.relmain = (RelativeLayout) view.findViewById(R.id.relsitem);
            this.relselected = (RelativeLayout) view.findViewById(R.id.relselected);
            forUI();
        }

        private void forUI() {
            Layparam.setHeightAsBoth(SpiralAdapter.this.context, this.relmain, 160);
            Layparam.setMarginLeft(SpiralAdapter.this.context, this.relmain, 38);
            Layparam.setMarginsAsHeight(SpiralAdapter.this.context, this.iv, 10);
        }
    }
}
