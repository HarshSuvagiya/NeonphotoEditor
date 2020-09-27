package com.scorpion.NeonphotoEditor.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
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
import java.util.ArrayList;

public class ExtraUsedAdapter extends RecyclerView.Adapter<ExtraUsedAdapter.MyViewHolder> {
    ArrayList<String> al;
    Context con;
    int height;
    int selected = -1;
    int width;

    public ExtraUsedAdapter(Context context, ArrayList<String> arrayList) {
        this.con = context;
        this.al = arrayList;
        this.width = Helper.getWidth(context);
        this.height = Helper.getHeight(context);
    }

    public void onBindViewHolder(MyViewHolder myViewHolder, int i) {
        Path path = Path.ASSETS;
        ((RequestBuilder) Glide.with(this.con).load(path.wrap(Constant.THUMB_FOLDER + "/" + this.al.get(i) + ".png"))
                .transform((Transformation<Bitmap>) new MultiTransformation((Transformation[])
                        new Transformation[]{new CenterCrop(), new RoundedCorners(getWidth(9))})))
                .transition(DrawableTransitionOptions.withCrossFade()).into(myViewHolder.iv);
        if (this.selected == i) {
            myViewHolder.relSelect.setVisibility(View.VISIBLE);
        } else {
            myViewHolder.relSelect.setVisibility(View.GONE);
        }
    }

    public void setSelected(int i) {
        this.selected = i;
        notifyDataSetChanged();
    }

    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new MyViewHolder(((LayoutInflater) this.con.getSystemService("layout_inflater")).inflate(R.layout.item_used, viewGroup, false));
    }

    public int getItemCount() {
        return this.al.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView iv;
        ImageView ivtick;
        RelativeLayout relSelect;
        RelativeLayout relmain;

        public MyViewHolder(View view) {
            super(view);
            this.iv = (ImageView) view.findViewById(R.id.ivparticle);
            this.ivtick = (ImageView) view.findViewById(R.id.ivtick);
            this.relmain = (RelativeLayout) view.findViewById(R.id.relpitem);
            this.relSelect = (RelativeLayout) view.findViewById(R.id.relselected);
            forUI();
        }

        private void forUI() {
            SetLayparam.setHeightWidth(ExtraUsedAdapter.this.con, this.relmain, 180, CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE);
            SetLayparam.setMarginLeft(ExtraUsedAdapter.this.con, this.relmain, 33);
            SetLayparam.setHeight(ExtraUsedAdapter.this.con, this.iv, 164);
            SetLayparam.setHeight(ExtraUsedAdapter.this.con, this.relSelect, 164);
            SetLayparam.setHeightAsBoth(ExtraUsedAdapter.this.con, this.ivtick, 90);
        }
    }

    public int getWidth(int i) {
        return (this.width * i) / 1080;
    }

    public int getHeight(int i) {
        return (this.height * i) / 1920;
    }
}
