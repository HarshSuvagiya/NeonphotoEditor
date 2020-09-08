package com.scorpion.NeonphotoEditor;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;

import com.scorpion.NeonphotoEditor.adapter.TextureAdapter;
import com.scorpion.NeonphotoEditor.util.GridRecyclerView;
import com.scorpion.NeonphotoEditor.util.Helper;
import com.scorpion.NeonphotoEditor.util.Path;
import com.scorpion.NeonphotoEditor.util.RVGridSpacing;
import com.scorpion.NeonphotoEditor.util.RecyclerTouchListener;

import io.github.inflationx.viewpump.ViewPumpContextWrapper;

public class TextureList extends Activity {
    TextureAdapter adapter;
    Context context;
    TextView header;
    int height;
    GridRecyclerView rcv;
    int width;

    private void forUI() {
    }

//    public void attachBaseContext(Context context2) {
//        super.attachBaseContext(ViewPumpContextWrapper.wrap(context2));
//    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_texture_list);
        getWindow().setFlags(1024, 1024);
        this.context = this;


        this.header = (TextView) findViewById(R.id.my_header_text);
        this.rcv = (GridRecyclerView) findViewById(R.id.rcvtexture);
        this.width = Helper.getWidth(this.context);
        this.height = Helper.getHeight(this.context);
        forUI();
        init();
    }

    private void init() {
        this.header.setText(R.string.textures);
        this.header.setTypeface(Typeface.createFromAsset(getAssets(), "Poppins-Bold.ttf"));
        this.rcv.setLayoutManager(new GridLayoutManager(this.context, 2));
        this.rcv.addItemDecoration(new RVGridSpacing(2, getWidth(37), true));
        this.adapter = new TextureAdapter(this.context);
        this.rcv.setAdapter(this.adapter);
        this.rcv.addOnItemTouchListener(new RecyclerTouchListener(this.context, this.rcv, new RecyclerTouchListener.ClickListener() {
            public void onLongClick(View view, int i) {
            }

            public void onClick(View view, int i) {
                String texture = Path.TEXTURES.texture(i);
                Intent intent = new Intent();
                intent.putExtra("texture", texture);
                TextureList.this.setResult(-1, intent);
                TextureList.this.finish();
            }
        }));
    }

    public int getWidth(int i) {
        return (this.width * i) / 1080;
    }

    public int getHeight(int i) {
        return (this.height * i) / 1920;
    }

    public void back(View view) {
        onBackPressed();
    }

    public void onBackPressed() {
        setResult(0);
        finish();
    }

}
