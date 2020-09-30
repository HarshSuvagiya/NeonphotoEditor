package com.scorpion.NeonphotoEditor;

import android.app.Application;

import com.facebook.ads.AdSettings;
import com.facebook.ads.AudienceNetworkAds;

import io.github.inflationx.calligraphy3.CalligraphyConfig;
import io.github.inflationx.calligraphy3.CalligraphyInterceptor;
import io.github.inflationx.viewpump.ViewPump;

public class ApplicationClass extends Application
{
    private static ApplicationClass instance;

    public void onCreate() {
        super.onCreate();
        System.loadLibrary("NativeImageProcessor");
        ViewPump.init(ViewPump.builder().addInterceptor(new CalligraphyInterceptor(new CalligraphyConfig.Builder().setDefaultFontPath("arial.ttf").setFontAttrId(R.attr.fontPath).build())).build());
        instance = this;
        AudienceNetworkAds.initialize(this);
        AdSettings.addTestDevice("1d75ff31-1330-4570-a8c7-9e616a85d1db");
        FBInterstitial.getInstance().loadFBInterstitial(this);

    }
}
