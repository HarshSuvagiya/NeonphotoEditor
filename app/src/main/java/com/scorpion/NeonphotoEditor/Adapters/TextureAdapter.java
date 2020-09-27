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
import com.scorpion.NeonphotoEditor.Util.Helper;
import com.scorpion.NeonphotoEditor.Util.Path;
import com.scorpion.NeonphotoEditor.Util.SetLayparam;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class TextureAdapter extends RecyclerView.Adapter<TextureAdapter.MyViewHolder> {
    Context context;
    String folder = "Textures";
    int height;
    String[] images;
    ArrayList<String> listImages;
    int width;

    public TextureAdapter(Context context2) {
        context = context2;
        try {
            images = context.getAssets().list(folder);
        } catch (IOException e) {
            e.printStackTrace();
        }
        listImages = new ArrayList<>(Arrays.asList(images));
        width = Helper.getWidth(context);
        height = Helper.getHeight(context);
    }

    public void onBindViewHolder(MyViewHolder myViewHolder, int i) {
        ((RequestBuilder) Glide.with(context).load(Path.TEXTURES.texture(i)).transition(DrawableTransitionOptions.withCrossFade()).centerCrop()).into(myViewHolder.iv);
    }

    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new MyViewHolder(((LayoutInflater) context.getSystemService("layout_inflater")).inflate(R.layout.item_texture, viewGroup, false));
    }

    public int getItemCount() {
        return listImages.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView iv;
        LinearLayout lmain;

        public MyViewHolder(View view) {
            super(view);
            iv = (ImageView) view.findViewById(R.id.ivtitem);
            lmain = (LinearLayout) view.findViewById(R.id.linearitexture);
            forUI();
        }

        private void forUI() {
            SetLayparam.setWidthAsBoth(context, lmain, 484);
            SetLayparam.setPadding(context, lmain, 4);
        }
    }
}
