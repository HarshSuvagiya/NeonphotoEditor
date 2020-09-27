package com.scorpion.NeonphotoEditor.Adapters;

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
import com.scorpion.NeonphotoEditor.Util.Helper;
import com.scorpion.NeonphotoEditor.Util.SetLayparam;
import com.scorpion.NeonphotoEditor.Util.Vdata;

import java.util.ArrayList;

public class MyVideoAdapter extends RecyclerView.Adapter<MyVideoAdapter.MyViewHolder> {
    ArrayList<Vdata> al;
    Context context;
    String dateFormat = "MMMM dd, hh:mm aaa";
    int height;
    int width;

    public MyVideoAdapter(Context context2, ArrayList<Vdata> arrayList) {
        context = context2;
        al = arrayList;
        width = Helper.getWidth(context2);
        height = Helper.getHeight(context2);
    }

    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        Vdata Vdata1 = al.get(i);
        String path = Vdata1.getPath();
        String date = Helper.getDate(Long.parseLong(Vdata1.getDate_taken()), dateFormat);
        TextView textView = myViewHolder.tvdate;
        textView.setText(date + "");
        String title = Vdata1.getTitle();
        TextView textView2 = myViewHolder.tvname;
        textView2.setText(title + "");
        String formatTime = Helper.getFormatTime(Long.parseLong(Vdata1.getDuration()));
        TextView textView3 = myViewHolder.tvtime;
        textView3.setText(formatTime + "");
        ((RequestBuilder) Glide.with(context).load(path).transform((Transformation<Bitmap>)
                new MultiTransformation((Transformation[]) new Transformation[]{new CenterCrop(), new RoundedCorners(getWidth(20))}))).into(myViewHolder.iv);
    }

    public int getItemCount() {
        return al.size();
    }

    @NonNull
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_creation_video, viewGroup, false));
    }

    public int getWidth(int i) {
        return (width * i) / 1080;
    }

    public int getHeight(int i) {
        return (height * i) / 1920;
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
            lmain = (LinearLayout) view.findViewById(R.id.linearvideolist);
            iv = (ImageView) view.findViewById(R.id.ivthumb);
            linside = (LinearLayout) view.findViewById(R.id.linearin);
            tvname = (TextView) view.findViewById(R.id.tvname);
            tvdate = (TextView) view.findViewById(R.id.tvdate);
            tvtime = (TextView) view.findViewById(R.id.tvtime);
            vline = view.findViewById(R.id.vline);
            forUI();
        }

        private void forUI() {
            SetLayparam.setPadding(context, linside, 60, 45, 60, 45);
            SetLayparam.setHeightWidth(context, iv, 280, 258);
            SetLayparam.setMarginRight(context, iv, 45);
            SetLayparam.setPadding(context, tvdate, 0, 20, 0, 20);
            SetLayparam.setPadding(context, tvtime, 10, 0, 10, 0);
            SetLayparam.setHeightWidth(context, vline, 1000, 4);
        }
    }
}
