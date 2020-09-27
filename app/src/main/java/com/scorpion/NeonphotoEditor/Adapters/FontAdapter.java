package com.scorpion.NeonphotoEditor.Adapters;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.scorpion.NeonphotoEditor.R;
import com.scorpion.NeonphotoEditor.Util.SetLayparam;

public class FontAdapter extends BaseAdapter {
    String[] arr;
    Context con;
    int height;
    int width;

    public long getItemId(int i) {
        return 0;
    }

    public FontAdapter(Context context, String[] strArr) {
        con = context;
        arr = strArr;
        width = context.getResources().getDisplayMetrics().widthPixels;
        height = context.getResources().getDisplayMetrics().heightPixels;
    }

    public int getCount() {
        return arr.length;
    }

    public Object getItem(int i) {
        return arr[i];
    }

    public View getView(int i, View view, ViewGroup viewGroup) {
        View inflate = ((LayoutInflater) con.getSystemService("layout_inflater")).inflate(R.layout.item_font, viewGroup, false);
        TextView textView = (TextView) inflate.findViewById(R.id.tvitem);
        View findViewById = inflate.findViewById(R.id.vline);
        SetLayparam.setHeight(con, textView, 135);
        SetLayparam.setHeightWidth(con, findViewById, 900, 4);
        AssetManager assets = con.getAssets();
        textView.setTypeface(Typeface.createFromAsset(assets, "fonts/" + arr[i] + ".ttf"));
        textView.setText(R.string.app_name);
        return inflate;
    }
}
