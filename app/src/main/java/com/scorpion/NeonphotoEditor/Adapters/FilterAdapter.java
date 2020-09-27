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
import com.scorpion.NeonphotoEditor.Util.SetLayparam;

import java.util.ArrayList;

public class FilterAdapter extends RecyclerView.Adapter<FilterAdapter.MyViewHolder> {
    ArrayList<Bitmap> alimage;
    Context context;
    int height;
    OnFilterListener listener;
    int selected = -1;
    int width;

    public interface OnFilterListener {
        void onFilterClicked(int i);
    }

    public FilterAdapter(Context context2, ArrayList<Bitmap> arrayList, OnFilterListener onFilterListener) {
        this.context = context2;
        this.listener = onFilterListener;
        this.alimage = new ArrayList<>(arrayList);
        this.width = Helper.getWidth(this.context);
        this.height = Helper.getHeight(this.context);
    }

    public void onBindViewHolder(MyViewHolder myViewHolder, final int i) {
        ((RequestBuilder) Glide.with(this.context).load(this.alimage.get(i)).transition(DrawableTransitionOptions.withCrossFade()).transform((Transformation<Bitmap>) new MultiTransformation((Transformation[]) new Transformation[]{new CenterCrop(), new RoundedCorners(10)}))).into(myViewHolder.iv);
        myViewHolder.relselected.setBackgroundResource(this.selected == i ? R.drawable.effect_select : R.drawable.effect_unselect);
        myViewHolder.relselected.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                FilterAdapter.this.selected = i;
                FilterAdapter.this.notifyDataSetChanged();
                if (FilterAdapter.this.listener != null) {
                    FilterAdapter.this.listener.onFilterClicked(i);
                }
            }
        });
    }

    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new MyViewHolder(((LayoutInflater) this.context.getSystemService("layout_inflater")).inflate(R.layout.item_filter, viewGroup, false));
    }

    public int getItemCount() {
        return this.alimage.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView iv;
        RelativeLayout relmain;
        RelativeLayout relselected;

        public MyViewHolder(View view) {
            super(view);
            this.iv = (ImageView) view.findViewById(R.id.ivfitem);
            this.relmain = (RelativeLayout) view.findViewById(R.id.relfitem);
            this.relselected = (RelativeLayout) view.findViewById(R.id.relselected);
            forUI();
        }

        private void forUI() {
            SetLayparam.setHeightAsBoth(FilterAdapter.this.context, this.relmain, 160);
            SetLayparam.setMarginLeft(FilterAdapter.this.context, this.relmain, 20);
            SetLayparam.setMarginsAsHeight(FilterAdapter.this.context, this.iv, 10);
        }
    }
}
