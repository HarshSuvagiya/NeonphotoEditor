package com.scorpion.NeonphotoEditor.Adapters;

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
import com.scorpion.NeonphotoEditor.Util.SetLayparam;

import java.util.ArrayList;

public class MyImageAdapter extends RecyclerView.Adapter<MyImageAdapter.MyViewHolder> {
    ArrayList<String> ad;
    Context context;
    int height;
    int width;

    public MyImageAdapter(Context context2, ArrayList<String> arrayList) {
        context = context2;
        ad = arrayList;
        width = context2.getResources().getDisplayMetrics().widthPixels;
        height = context2.getResources().getDisplayMetrics().heightPixels;
    }

    public void onBindViewHolder(MyViewHolder myViewHolder, int i) {
        ((RequestBuilder) Glide.with(context).load(ad.get(i)).transition(DrawableTransitionOptions.withCrossFade()).centerCrop()).into(myViewHolder.iv);
    }

    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new MyViewHolder(((LayoutInflater) context.getSystemService("layout_inflater")).inflate(R.layout.item_creation_image, viewGroup, false));
    }

    public int getItemCount() {
        return ad.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView iv;
        LinearLayout lmain;

        public MyViewHolder(View view) {
            super(view);
            iv = (ImageView) view.findViewById(R.id.ivcitem);
            lmain = (LinearLayout) view.findViewById(R.id.linearicreation);
            forUI();
        }

        private void forUI() {
            SetLayparam.setWidthAsBoth(context, lmain, 484);
            SetLayparam.setPadding(context, lmain, 4);
        }
    }
}
