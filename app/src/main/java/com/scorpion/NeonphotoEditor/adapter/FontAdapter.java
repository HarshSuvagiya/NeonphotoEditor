package com.scorpion.NeonphotoEditor.adapter;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.scorpion.NeonphotoEditor.R;
import com.scorpion.NeonphotoEditor.util.Layparam;

public class FontAdapter extends BaseAdapter {
    String[] arr;
    Context con;
    int height;
    int width;

    public long getItemId(int i) {
        return 0;
    }

    public FontAdapter(Context context, String[] strArr) {
        this.con = context;
        this.arr = strArr;
        this.width = context.getResources().getDisplayMetrics().widthPixels;
        this.height = context.getResources().getDisplayMetrics().heightPixels;
    }

    public int getCount() {
        return this.arr.length;
    }

    public Object getItem(int i) {
        return this.arr[i];
    }

    public View getView(int i, View view, ViewGroup viewGroup) {
        View inflate = ((LayoutInflater) this.con.getSystemService("layout_inflater")).inflate(R.layout.item_font, viewGroup, false);
        TextView textView = (TextView) inflate.findViewById(R.id.tvitem);
        View findViewById = inflate.findViewById(R.id.vline);
        Layparam.setHeight(this.con, textView, 135);
        Layparam.setHeightWidth(this.con, findViewById, 900, 4);
        AssetManager assets = this.con.getAssets();
        textView.setTypeface(Typeface.createFromAsset(assets, "fonts/" + this.arr[i] + ".ttf"));
        textView.setText(R.string.app_name);
        return inflate;
    }
}
