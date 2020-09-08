package com.scorpion.NeonphotoEditor.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.scorpion.NeonphotoEditor.R;
import com.scorpion.NeonphotoEditor.util.Helper;
import com.scorpion.NeonphotoEditor.util.Layparam;
import com.scorpion.NeonphotoEditor.util.Vdata;

import java.util.ArrayList;

public class VidCreationAdapter extends RecyclerView.Adapter<VidCreationAdapter.MyViewHolder> {
    ArrayList<Vdata> al;
    Context context;
    String dateFormat = "MMMM dd, hh:mm aaa";
    int height;
    int width;

    public VidCreationAdapter(Context context2, ArrayList<Vdata> arrayList) {
        this.context = context2;
        this.al = arrayList;
        this.width = Helper.getWidth(context2);
        this.height = Helper.getHeight(context2);
    }

    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        Vdata fX_Vdata = this.al.get(i);
        String path = fX_Vdata.getPath();
        String date = Helper.getDate(Long.parseLong(fX_Vdata.getDate_taken()), this.dateFormat);
        TextView textView = myViewHolder.tvdate;
        textView.setText(date + "");
        String title = fX_Vdata.getTitle();
        TextView textView2 = myViewHolder.tvname;
        textView2.setText(title + "");
        String formatTime = Helper.getFormatTime(Long.parseLong(fX_Vdata.getDuration()));
        TextView textView3 = myViewHolder.tvtime;
        textView3.setText(formatTime + "");
        ((RequestBuilder) Glide.with(this.context).load(path).transform((Transformation<Bitmap>)
                new MultiTransformation((Transformation[]) new Transformation[]{new CenterCrop(), new RoundedCorners(getWidth(20))}))).into(myViewHolder.iv);
    }

    public int getItemCount() {
        return this.al.size();
    }

    @NonNull
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new MyViewHolder(LayoutInflater.from(this.context).inflate(R.layout.item_creation_video, viewGroup, false));
    }

    public int getWidth(int i) {
        return (this.width * i) / 1080;
    }

    public int getHeight(int i) {
        return (this.height * i) / 1920;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView iv;
        LinearLayout linside;
        LinearLayout lmain;
        TextView tvdate;
        TextView tvname;
        TextView tvtime;
        View vline;

        public MyViewHolder(View view) {
            super(view);
            this.lmain = (LinearLayout) view.findViewById(R.id.linearvideolist);
            this.iv = (ImageView) view.findViewById(R.id.ivthumb);
            this.linside = (LinearLayout) view.findViewById(R.id.linearin);
            this.tvname = (TextView) view.findViewById(R.id.tvname);
            this.tvdate = (TextView) view.findViewById(R.id.tvdate);
            this.tvtime = (TextView) view.findViewById(R.id.tvtime);
            this.vline = view.findViewById(R.id.vline);
            forUI();
        }

        private void forUI() {
            Layparam.setPadding(VidCreationAdapter.this.context, this.linside, 60, 45, 60, 45);
            Layparam.setHeightWidth(VidCreationAdapter.this.context, this.iv, 280, 258);
            Layparam.setMarginRight(VidCreationAdapter.this.context, this.iv, 45);
            Layparam.setPadding(VidCreationAdapter.this.context, this.tvdate, 0, 20, 0, 20);
            Layparam.setPadding(VidCreationAdapter.this.context, this.tvtime, 10, 0, 10, 0);
            Layparam.setHeightWidth(VidCreationAdapter.this.context, this.vline, 1000, 4);
        }
    }
}
