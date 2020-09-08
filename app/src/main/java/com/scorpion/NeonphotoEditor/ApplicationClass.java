package com.scorpion.NeonphotoEditor;

import android.app.Application;

import io.github.inflationx.calligraphy3.CalligraphyConfig;
import io.github.inflationx.calligraphy3.CalligraphyInterceptor;
import io.github.inflationx.viewpump.ViewPump;

public class ApplicationClass extends Application
{
    public void onCreate() {
        super.onCreate();
        System.loadLibrary("NativeImageProcessor");
        ViewPump.init(ViewPump.builder().addInterceptor(new CalligraphyInterceptor(new CalligraphyConfig.Builder().setDefaultFontPath("arial.ttf").setFontAttrId(R.attr.fontPath).build())).build());
    }
}
