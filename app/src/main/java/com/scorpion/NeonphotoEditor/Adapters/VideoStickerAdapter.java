package com.scorpion.NeonphotoEditor.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import androidx.recyclerview.widget.RecyclerView;
import cn.ezandroid.ezfilter.core.util.Path;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.scorpion.NeonphotoEditor.R;
import com.scorpion.NeonphotoEditor.Util.Constant;
import com.scorpion.NeonphotoEditor.Util.Helper;
import com.scorpion.NeonphotoEditor.Util.SetLayparam;
import com.theartofdev.edmodo.cropper.CropImage;
import java.util.HashMap;

public class VideoStickerAdapter extends RecyclerView.Adapter<VideoStickerAdapter.MyViewHolder> {
    HashMap<Integer, String> al;
    Context con;
    int height;
    int selected = 0;
    int width;

    public VideoStickerAdapter(Context context, HashMap<Integer, String> hashMap) {
        this.con = context;
        this.al = hashMap;
        this.width = Helper.getWidth(context);
        this.height = Helper.getHeight(context);
    }

    public void onBindViewHolder(MyViewHolder myViewHolder, int i) {
        Path path = Path.ASSETS;
        ((RequestBuilder) Glide.with(this.con).load(path.wrap(Constant.THUMB_FOLDER + "/" + this.al.get(Integer.valueOf(i)) + ".png")).transform((Transformation<Bitmap>) new MultiTransformation((Transformation[]) new Transformation[]{new CenterCrop(), new RoundedCorners(getWidth(9))}))).transition(DrawableTransitionOptions.withCrossFade()).into(myViewHolder.iv);
        if (this.selected == i) {
            myViewHolder.vline.setVisibility(View.VISIBLE);
            myViewHolder.relborder.setBackgroundResource(R.drawable.border);
            return;
        }
        myViewHolder.vline.setVisibility(View.GONE);
        myViewHolder.relborder.setBackground((Drawable) null);
    }

    public void setSelected(int i) {
        this.selected = i;
        notifyDataSetChanged();
    }

    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new MyViewHolder(((LayoutInflater) this.con.getSystemService("layout_inflater")).inflate(R.layout.item_sticker, viewGroup, false));
    }

    public int getItemCount() {
        return this.al.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView iv;
        RelativeLayout relborder;
        RelativeLayout relmain;
        View vline;

        public MyViewHolder(View view) {
            super(view);
            this.iv = (ImageView) view.findViewById(R.id.ivparticle);
            this.vline = view.findViewById(R.id.vline);
            this.relmain = (RelativeLayout) view.findViewById(R.id.relpitem);
            this.relborder = (RelativeLayout) view.findViewById(R.id.relborder);
            forUI();
        }

        private void forUI() {
            SetLayparam.setHeight(VideoStickerAdapter.this.con, this.relmain, CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE);
            SetLayparam.setMarginLeft(VideoStickerAdapter.this.con, this.relmain, 33);
            SetLayparam.setHeightWidth(VideoStickerAdapter.this.con, this.relborder, 186, 170);
            SetLayparam.setHeightWidth(VideoStickerAdapter.this.con, this.iv, 180, 164);
            SetLayparam.setHeightWidth(VideoStickerAdapter.this.con, this.vline, 116, 6);
        }
    }

    public int getWidth(int i) {
        return (this.width * i) / 1080;
    }

    public int getHeight(int i) {
        return (this.height * i) / 1920;
    }
}
