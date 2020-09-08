package com.scorpion.NeonphotoEditor.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.scorpion.NeonphotoEditor.R;
import com.scorpion.NeonphotoEditor.util.Layparam;

import java.util.ArrayList;

public class ImageCreationAdapter extends RecyclerView.Adapter<ImageCreationAdapter.MyViewHolder> {
    ArrayList<String> ad;
    Context context;
    int height;
    int width;

    public ImageCreationAdapter(Context context2, ArrayList<String> arrayList) {
        this.context = context2;
        this.ad = arrayList;
        this.width = context2.getResources().getDisplayMetrics().widthPixels;
        this.height = context2.getResources().getDisplayMetrics().heightPixels;
    }

    public void onBindViewHolder(MyViewHolder myViewHolder, int i) {
        ((RequestBuilder) Glide.with(this.context).load(this.ad.get(i)).transition(DrawableTransitionOptions.withCrossFade()).centerCrop()).into(myViewHolder.iv);
    }

    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new MyViewHolder(((LayoutInflater) this.context.getSystemService("layout_inflater")).inflate(R.layout.item_creation_image, viewGroup, false));
    }

    public int getItemCount() {
        return this.ad.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView iv;
        LinearLayout lmain;

        public MyViewHolder(View view) {
            super(view);
            this.iv = (ImageView) view.findViewById(R.id.ivcitem);
            this.lmain = (LinearLayout) view.findViewById(R.id.linearicreation);
            forUI();
        }

        private void forUI() {
            Layparam.setWidthAsBoth(ImageCreationAdapter.this.context, this.lmain, 484);
            Layparam.setPadding(ImageCreationAdapter.this.context, this.lmain, 4);
        }
    }
}
